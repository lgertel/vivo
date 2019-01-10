package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Message;
import br.com.grts.aureo.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessageServiceTest {

  @InjectMocks
  private MessageServiceImpl messageService;

  @Mock
  private MessageRepository repository;

  @Before
  public void setup() {
    initMocks(this);
  }

  @Test
  public void shouldFindById() {
    final Message message = new Message();
    message.setId("messageId");
    message.setConversationId("conversationId");
    message.setTimestamp(new Date());
    message.setFrom("botId");
    message.setTo("userId");
    message.setText("Olá, tudo bem?");

    when(repository.findById(message.getId())).thenReturn(java.util.Optional.of(message));
    Message found = messageService.findById(message.getId());

    assertEquals(message, found);
  }

  @Test
  public void shouldCreateMessage() {
    Message message = new Message();
    message.setId("messageId");
    message.setConversationId("conversationId");
    message.setTimestamp(new Date());
    message.setFrom("botId");
    message.setTo("userId");
    message.setText("Olá, tudo bem?");

    Message created = messageService.create(message);
    assertThat(created.getId())
        .isNotEmpty()
        .isNotNull();

    assertThat(created.getId())
      .isEqualTo(message.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailWhenNoConversationIdExisted() {
    final Message message = new Message();
    message.setId("messageId");
    message.setTimestamp(new Date());
    message.setFrom("botId");
    message.setTo("userId");
    message.setText("Olá, tudo bem?");

    when(repository.findById(message.getId())).thenReturn(null);
    messageService.create(message);
  }
}
