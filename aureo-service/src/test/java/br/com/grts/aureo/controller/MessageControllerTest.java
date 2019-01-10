package br.com.grts.aureo.controller;

import br.com.grts.aureo.domain.Conversation;
import br.com.grts.aureo.domain.Message;
import br.com.grts.aureo.service.ConversationService;
import br.com.grts.aureo.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageControllerTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  @InjectMocks
  private MessageController messageController;

  @Mock
  private MessageService messageService;

  @Mock
  private ConversationService conversationService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
  }

  @Test
  public void shouldGetMessageById() throws Exception{
    final Message message = new Message();
    message.setId("messageId");

    when(messageService.findById(message.getId())).thenReturn(message);

    mockMvc.perform(get("/messages/" + message.getId()))
        .andExpect(jsonPath("$.id").value(message.getId()))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldRegisterNewMessage() throws Exception {
    final Message message = new Message();
    message.setId("messageId");
    message.setConversationId("conversationId");
    message.setText("Olá mundo!");
    message.setFrom("botId");
    message.setTo("userId");

    String json = mapper.writeValueAsString(message);
    MockHttpServletRequestBuilder requestBuilder = post("/messages")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(requestBuilder)
        .andExpect(status().isOk());
  }

  @Test
  public void shouldGetMessagesByConversationId() throws Exception {
    Conversation conversation = new Conversation();
    conversation.setId("conversationId");

    Message firstMessage = new Message();
    firstMessage.setId("firstMessageId");
    firstMessage.setFrom("botId");
    firstMessage.setTo("userId");
    firstMessage.setConversationId(conversation.getId());
    firstMessage.setText("Olá, bem vindo!");

    Message secondMessage = new Message();
    secondMessage.setId("secondMessageId");
    secondMessage.setFrom("userId");
    secondMessage.setTo("botId");
    secondMessage.setConversationId(conversation.getId());
    secondMessage.setText("Oi, o que vc sabe fazer?");

    conversation.setMessages(Arrays.asList(firstMessage, secondMessage));

    when(messageService.findByConversationId(conversation.getId())).thenReturn(Arrays.asList(firstMessage, secondMessage));

    mockMvc.perform(get("/messages?conversationId=" + conversation.getId()))
        .andExpect(status().isOk());
  }
}
