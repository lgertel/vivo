package br.com.grts.aureo.repository;

import br.com.grts.aureo.domain.Conversation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataMongoTest
public class ConversationRepositoryTest {

  @Autowired
  private ConversationRepository repository;

  @Test
  public void shouldFindById() {
    Conversation stub = getSubConversation();
    repository.save(stub);

    Conversation found = null;
    Optional<Conversation> byId = repository.findById(stub.getId());
    if(byId.isPresent()) {
      found = byId.get();

      assertEquals(stub.getId(), found.getId());
    }
  }

  private Conversation getSubConversation() {
    Conversation conversation = new Conversation();
    conversation.setId("cr4zyConvers4ti0n");
    conversation.setBotId("imabotid");

    return conversation;
  }
}
