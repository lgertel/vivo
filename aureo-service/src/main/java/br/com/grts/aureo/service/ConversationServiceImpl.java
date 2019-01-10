package br.com.grts.aureo.service;

import br.com.grts.aureo.domain.Conversation;
import br.com.grts.aureo.repository.BotRepository;
import br.com.grts.aureo.repository.ConversationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Slf4j
@Service
public class ConversationServiceImpl implements ConversationService {

  private ConversationRepository repository;

  public ConversationServiceImpl(ConversationRepository repository) {
    this.repository = repository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Conversation findById(String id) {
    Assert.hasLength(id, "id must not be null and must be not empty");
    Optional<Conversation> byId = repository.findById(id);
    Conversation found = null;
    if (byId.isPresent()) {
      found = byId.get();
    }

    return found;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Conversation create(Conversation conversation) {
    repository.save(conversation);
    log.info("new conversation has been created for bot " + conversation.getBotId() + ":" + conversation.getId());

    return conversation;
  }
}
