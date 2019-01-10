package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Message;

import java.util.List;

public interface MessageService {

  /**
   * Finds message by given id
   *
   * @param id
   * @return found Message
   */
  Message findById(String id);

  /**
   * @param message
   * @return created message
   */
  Message create(Message message);

  /**
   * @param id
   * @return list of messages
   */
  List<Message> findByConversationId(String id);
}
