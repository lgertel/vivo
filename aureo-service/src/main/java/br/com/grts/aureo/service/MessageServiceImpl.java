package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Conversation;
import br.com.grts.aureo.domain.Message;
import br.com.grts.aureo.repository.ConversationRepository;
import br.com.grts.aureo.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

  private MessageRepository repository;
  private ConversationRepository conversationRepository;

  @Autowired
  public MessageServiceImpl(MessageRepository repository, ConversationRepository conversationRepository) {
    this.repository = repository;
    this.conversationRepository = conversationRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Message findById(String id) {
    Assert.hasLength(id, "id must not be null and must be not empty");
    Optional<Message> byId = repository.findById(id);
    Message found = null;
    if (byId.isPresent()) {
      found = byId.get();
    }

    return found;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Message create(Message message) {
    Assert.notNull(message.getConversationId(), "can't save a message without a specific conversationId defined");

    Optional<Conversation> existingConversation = conversationRepository.findById(message.getConversationId());
    Conversation conversation = null;
    if(existingConversation.isPresent()) {
      conversation = existingConversation.get();
    } else {
      conversation = new Conversation();
      conversation.setId(message.getConversationId());
      conversation.setTimestamp(new Date());
      conversation.setMessages(new ArrayList<Message>());

      conversationRepository.save(conversation);
    }

    conversation.getMessages().add(message);
    conversationRepository.save(conversation);

    repository.save(message);
    log.info("new message has been created for conversation " + message.getConversationId() + ":" + message.getId());

    return message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Message> findByConversationId(String id) {
    Assert.notNull(id, "can't retrieve messages by conversation without a conversation id");

    Optional<Conversation> byId = conversationRepository.findById(id);
    Conversation found = null;
    if (byId.isPresent()) {
      found = byId.get();
    }
    return found.getMessages();
  }
}
