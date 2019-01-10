package br.com.grts.aureo.repository;

import br.com.grts.aureo.domain.Bot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotRepository extends CrudRepository<Bot, Long> {
}
