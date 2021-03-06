package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Bot;
import br.com.grts.aureo.repository.BotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Slf4j
@Service
public class BotServiceImpl implements BotService {

  private BotRepository repository;

  @Autowired
  public BotServiceImpl(BotRepository repository) {
    this.repository = repository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Bot findById(String id) {
    Assert.hasLength(id, "id must not be null and must not be empty");

    Optional<Bot> byId = repository.findById(id);
    Bot bot = null;
    if (byId.isPresent()) {
      bot = byId.get();
    }

    return bot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Bot create(Bot bot) {
    repository.save(bot);
    log.info("new bot has been created:" + bot.getName());

    return bot;
  }

  @Override
  public Iterable<Bot> findAll() {
    return repository.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveChanges(Bot update) {
    Bot bot = findById(update.getId());
    Assert.notNull(bot, "can't find bot with id: " + update.getId());

    bot.setName(update.getName());
    repository.save(bot);
  }

  @Override
  public void delete(String id) {
    Bot found = findById(id);
    Assert.notNull(found, "can't find bot with id: " + id);

    repository.delete(found);
  }
}
