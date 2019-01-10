package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Bot;
import br.com.grts.aureo.repository.BotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

@Slf4j
public class BotServiceImpl implements BotService {

  private BotRepository botRepository;

  @Autowired
  public BotServiceImpl(BotRepository botRepository) {
    this.botRepository = botRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Bot findById(String id) {
    Assert.hasLength(id, "id must not be null and must not be empty");
    return botRepository.findById(id);
  }
}
