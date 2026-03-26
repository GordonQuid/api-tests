package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorsResponseDTO {

  private int id;
  private int idBook;
  private String firstName;
  private String lastName;
}
