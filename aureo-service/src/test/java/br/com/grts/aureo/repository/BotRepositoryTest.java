package br.com.grts.aureo.repository;

import br.com.grts.aureo.domain.Bot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BotRepositoryTest {

  @Autowired
  private BotRepository repository;

  @Test
  public void shouldFindById() {
    Bot stub = getStubBot();
    repository.save(stub);

    Bot found = null;
    Optional<Bot> byId = repository.findById(stub.getId());
    if(byId.isPresent()) {
      found = byId.get();
      assertEquals(stub.getId(), found.getId());
    }
  }

  private Bot getStubBot() {
    Bot bot = new Bot();
    bot.setId("stubBot");
    bot.setName("I'm an awesome bot");

    return bot;
  }
}
