package br.com.grts.aureo.repository;

import br.com.grts.aureo.domain.Bot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BotRepositoryTest {

  @Autowired
  private BotRepository repository;

  @Test
  public void shouldFindById() {
    Bot stub = getStubBot();
    Bot bot = repository.save(stub);

    assertEquals(stub.getName(), bot.getName());
  }

  private Bot getStubBot() {
    Bot bot = new Bot();
    bot.setName("I'm an awesome bot");

    return bot;
  }
}
