package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AuthorsDTO {

  private int id;
  private int idBook;
  private String firstName;
  private String lastName;
}
