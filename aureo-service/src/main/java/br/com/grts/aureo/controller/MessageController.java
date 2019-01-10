package br.com.grts.aureo.controller;

import br.com.grts.aureo.domain.Message;
import br.com.grts.aureo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MessageController {

  private MessageService messageService;

  @Autowired
  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @GetMapping("/messages/{id}")
  public Message getMessageById(@PathVariable String id) {
    return messageService.findById(id);
  }

  @PostMapping("/messages")
  public Message createNewMessage(@Valid @RequestBody Message message) {
    return messageService.create(message);
  }

  @GetMapping("/messages")
  public List<Message> findByConversationId(@RequestParam(required = true) String conversationId) {
    return messageService.findByConversationId(conversationId);
  }
}
