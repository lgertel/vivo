package br.com.grts.aureo.repository;

import br.com.grts.aureo.domain.Conversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, String> {
}
