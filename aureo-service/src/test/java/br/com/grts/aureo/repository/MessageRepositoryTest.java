package br.com.grts.aureo.repository;

import br.com.grts.aureo.domain.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MessageRepositoryTest {

  @Autowired
  private MessageRepository repository;

  @Test
  public void shouldFindById() {
    Message stub = getStubMessage();
    repository.save(stub);

    Message found = null;
    Optional<Message> byId = repository.findById(stub.getId());
    if(byId.isPresent()) {
      found = byId.get();
      assertEquals(stub.getId(), found.getId());
    }
  }

  private Message getStubMessage() {
    Message message = new Message();
    message.setId("messageId");
    message.setConversationId("conversationId");
    message.setText("Olá, tudo bem?");
    message.setFrom("fromRobot");
    message.setTo("toUser");
    message.setTimestamp(new Date());

    return message;
  }
}
