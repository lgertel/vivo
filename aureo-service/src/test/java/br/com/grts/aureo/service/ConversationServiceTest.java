package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Conversation;
import br.com.grts.aureo.repository.ConversationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConversationServiceTest {

  @InjectMocks
  private ConversationServiceImpl conversationService;

  @Mock
  private ConversationRepository repository;

  @Before
  public void setup() {
    initMocks(this);
  }

  @Test
  public void shouldFindById(){
    final Conversation conversation = new Conversation();
    conversation.setId("conversationId");
    conversation.setBotId("botId");
    conversation.setTimestamp(new Date());

    when(repository.findById(conversation.getId())).thenReturn(java.util.Optional.of(conversation));
    Conversation found = conversationService.findById(conversation.getId());

    assertEquals(conversation, found);
  }

  @Test
  public void shouldCreateConversation() {
    Conversation conversation = new Conversation();
    conversation.setBotId("botId");
    conversation.setTimestamp(new Date());

    Conversation created = conversationService.create(conversation);
    assertThat(conversation.getBotId())
        .isEqualTo(created.getBotId());

    assertThat(created.getBotId())
        .isNotEmpty()
        .isNotNull();
  }
}
