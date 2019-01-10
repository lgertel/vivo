package br.com.grts.aureo.controller;

import br.com.grts.aureo.domain.Bot;
import br.com.grts.aureo.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class BotController {

  private BotService botService;

  @Autowired
  public BotController(BotService botService) {
    this.botService = botService;
  }

  @GetMapping
  public Iterable<Bot> listAllBots() {
    return botService.findAll();
  }

  @GetMapping("/bots/{id}")
  public Bot getBotById(@PathVariable String id) {
    return botService.findById(id);
  }

  @PostMapping("/bots")
  public Bot createNewBot(@Valid @RequestBody Bot bot) {
    return botService.create(bot);
  }

  @PutMapping("/bots/{id}")
  public void saveCurrentBot(@Valid @RequestBody Bot bot) {
    botService.saveChanges(bot);
  }
}
