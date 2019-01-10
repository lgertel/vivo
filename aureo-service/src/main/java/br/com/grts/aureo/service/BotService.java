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

  /**
   * @param bot
   * @return created bot
   */
  Bot create(Bot bot);

  /**
   * @return all bots
   */
  Iterable<Bot> findAll();

  /**
   * Validates and applies incoming bot updates
   *
   * @param update
   */
  void saveChanges(Bot update);

  /**
   * Delete existing bot
   *
   * @param id
   */
  void delete(String id);
}
