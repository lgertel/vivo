package br.com.grts.aureo.controller;

import br.com.grts.aureo.domain.Bot;
import br.com.grts.aureo.service.BotService;
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

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BotControllerTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  @InjectMocks
  private BotController botController;

  @Mock
  private BotService botService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(botController).build();
  }

  @Test
  public void shouldGetBotById() throws Exception {
    final Bot bot = new Bot();
    bot.setId("th1s1sth3b0t1d");
    bot.setName("BotController");

    when(botService.findById(bot.getId())).thenReturn(bot);

    mockMvc.perform(get("/bots/" + bot.getId()))
        .andExpect(jsonPath("$.id").value(bot.getId()))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldRegisterNewBot() throws Exception {
    final Bot bot = new Bot();
    bot.setId("th1s1sth3b0t1d");
    bot.setName("BotController");

    String json = mapper.writeValueAsString(bot);
    MockHttpServletRequestBuilder requestBuilder = post("/bots")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mockMvc.perform(requestBuilder)
        .andExpect(status().isOk());
  }

  @Test
  public void shouldFailValidationTryingToRegisterNewBot() throws Exception {
    final Bot bot = new Bot();
    bot.setName("B");

    String json = mapper.writeValueAsString(bot);
    mockMvc.perform(post("/bots").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldSaveCurrentBot() throws Exception{
    Bot bot = new Bot();
    bot.setId("id");
    bot.setName("Original name");

    String json = mapper.writeValueAsString(bot);
    mockMvc.perform(put("/bots/" + bot.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());
  }

  @Test
  public void shouldFailOnValidationTryingToSaveCurrentBot() throws Exception {
    final Bot bot = new Bot();
    bot.setId("id");
    bot.setName("d");

    String json = mapper.writeValueAsString(bot);
    mockMvc.perform(put("/bots/" + bot.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());
  }
}
