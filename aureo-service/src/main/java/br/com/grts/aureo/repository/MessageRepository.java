package br.com.grts.aureo.repository;

import br.com.grts.aureo.domain.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, String> {
}
