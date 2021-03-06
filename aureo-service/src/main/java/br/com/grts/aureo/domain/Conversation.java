package br.com.grts.aureo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "conversations")
public class Conversation {

  @Id
  private String id;

  @NotNull
  private String botId;

  private Date timestamp;

  @Valid
  private List<Message> messages;
}
