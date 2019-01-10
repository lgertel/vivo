package br.com.grts.aureo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bots")
public class Bot {

  @Id
  private String id;

  @Length(min = 3, max=50)
  private String name;
}
