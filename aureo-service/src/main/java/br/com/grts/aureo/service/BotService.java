package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Bot;

public interface BotService {

  /**
   * Finds bot by given id
   *
   * @param id
   * @return found Bot
   */
  Bot findById(String id);
}
