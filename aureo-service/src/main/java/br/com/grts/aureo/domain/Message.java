package br.com.grts.aureo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class Message {

  @Id
  private String id;

  @NotNull
  private String conversationId;

  @NotNull
  private String text;

  @NotNull
  private String from;

  @NotNull
  private String to;

  private Date timestamp;
}
