package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Bot;
import br.com.grts.aureo.repository.BotRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BotServiceTest {

  @InjectMocks
  private BotServiceImpl botService;

  @Mock
  private BotRepository repository;

  @Before
  public void setup() {
    initMocks(this);
  }

  @Test
  public void shouldFindById() {
    final Bot bot = new Bot();
    bot.setId("1azxd123a");
    bot.setName("SimpleBOT");

    when(botService.findById(bot.getId())).thenReturn(bot);
    Bot found = botService.findById(bot.getId());

    assertEquals(bot, found);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailWhenIdIsEmpty() {
    botService.findById("");
  }
}
