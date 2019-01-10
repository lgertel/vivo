package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Bot;
import br.com.grts.aureo.repository.BotRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
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

    when(repository.findById(bot.getId())).thenReturn(java.util.Optional.of(bot));
    Bot found = botService.findById(bot.getId());

    assertEquals(bot, found);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldFailWhenIdIsEmpty() {
    botService.findById("");
  }

  @Test
  public void shouldCreateBot() {
    Bot bot = new Bot();
    bot.setName("SavedBOT");

    Bot created = botService.create(bot);
    assertEquals(bot.getName(), created.getName());

    verify(repository, times(1)).save(bot);
  }

  @Test
  public void shouldSaveChangesWhenUpdatedBotGiven() {
    Bot original = new Bot();
    original.setId("botId");
    original.setName("coolB");

    final Bot update = new Bot();
    update.setId("botId");
    update.setName("coolBOT");

    when(botService.findById(original.getId())).thenReturn(original);
    botService.saveChanges(update);

    assertEquals(original.getName(), update.getName());
  }
}
