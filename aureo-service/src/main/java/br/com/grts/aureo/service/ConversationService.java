package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Conversation;

public interface ConversationService {

  /**
   * Finds conversation by given id
   *
   * @param id
   * @return found Conversation
   */
  Conversation findById(String id);

  /**
   * @param conversation
   * @return created conversation
   */
  Conversation create(Conversation conversation);
}
