package Fakerestapi.Authors;

import dto.AuthorsDTO;
import dto.AuthorsResponseDTO;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import services.AuthorsApi;


public class AuthorTests {

  @Test
  void createAuthorTest() {

    //Проверяем корректное создание автора

    AuthorsApi authorsApi = new AuthorsApi();

    AuthorsDTO authorDTO = AuthorsDTO.builder()
        .id(1)
        .idBook(1)
        .firstName("First Name 1")
        .lastName("Last Name 1")
        .build();

    AuthorsResponseDTO createAuthorResponse = authorsApi.createAuthor(authorDTO)
        .statusCode(HttpStatus.SC_OK)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/Author.json"))
        .extract().as(AuthorsResponseDTO.class);

    Assertions.assertAll( "Create author response",
        () -> Assertions.assertEquals(authorDTO.getId(), createAuthorResponse.getId(), "wrong id"),
        () -> Assertions.assertEquals(authorDTO.getIdBook(), createAuthorResponse.getIdBook(), "wrong idBook"),
        () -> Assertions.assertEquals(authorDTO.getFirstName(), createAuthorResponse.getFirstName(), "wrong firstName"),
        () -> Assertions.assertEquals(authorDTO.getLastName(), createAuthorResponse.getLastName(), "wrong lastName")
    );

    AuthorsResponseDTO getAuthorResponse = authorsApi.getAuthor(authorDTO.getId()).extract().as(AuthorsResponseDTO.class);

    Assertions.assertAll( "Get author response",
        () -> Assertions.assertEquals(authorDTO.getId(), getAuthorResponse.getId(), "wrong id"),
        () -> Assertions.assertEquals(authorDTO.getIdBook(), getAuthorResponse.getIdBook(), "wrong idBook"),
        () -> Assertions.assertEquals(authorDTO.getFirstName(), getAuthorResponse.getFirstName(), "wrong firstName"),
        () -> Assertions.assertEquals(authorDTO.getLastName(), getAuthorResponse.getLastName(), "wrong lastName")
    );
  }

  @Test
  void createAuthorWithoutIdBookTest() {

    //Проверяем необязательность поля IdBook при создании автора

    AuthorsApi authorsApi = new AuthorsApi();

    AuthorsDTO authorDTO = AuthorsDTO.builder()
        .id(1)
        .firstName("First Name 1")
        .lastName("Last Name 1")
        .build();

    AuthorsResponseDTO createAuthorResponse = authorsApi.createAuthor(authorDTO)
        .statusCode(HttpStatus.SC_OK)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/Author.json"))
        .extract().as(AuthorsResponseDTO.class);

    Assertions.assertAll( "Create author response",
        () -> Assertions.assertEquals(authorDTO.getId(), createAuthorResponse.getId(), "wrong id"),
        () -> Assertions.assertEquals(authorDTO.getFirstName(), createAuthorResponse.getFirstName(), "wrong firstName"),
        () -> Assertions.assertEquals(authorDTO.getLastName(), createAuthorResponse.getLastName(), "wrong lastName")
    );

    AuthorsResponseDTO getAuthorResponse = authorsApi.getAuthor(authorDTO.getId()).extract().as(AuthorsResponseDTO.class);

    Assertions.assertAll( "Get author response",
        () -> Assertions.assertEquals(authorDTO.getId(), getAuthorResponse.getId(), "wrong id"),
        () -> Assertions.assertEquals(authorDTO.getIdBook(), createAuthorResponse.getIdBook(), "wrong idBook"),
        () -> Assertions.assertEquals(authorDTO.getFirstName(), getAuthorResponse.getFirstName(), "wrong firstName"),
        () -> Assertions.assertEquals(authorDTO.getLastName(), getAuthorResponse.getLastName(), "wrong lastName")
    );
  }

  @Test
  void updateAuthorTest() {

    //Проверяем корректное обновление автора

    AuthorsApi authorsApi = new AuthorsApi();

    AuthorsDTO authorDTO = AuthorsDTO.builder()
        .id(1)
        .idBook(1)
        .firstName("First Name 1")
        .lastName("Last Name 1")
        .build();

    authorsApi.createAuthor(authorDTO).statusCode(HttpStatus.SC_OK);

    AuthorsDTO newAuthorDTO = AuthorsDTO.builder()
        .id(1)
        .idBook(2)
        .firstName("First Name 2")
        .lastName("Last Name 2")
        .build();

    AuthorsResponseDTO updateAuthorResponse = authorsApi.updateAuthor(newAuthorDTO, authorDTO.getId())
        .statusCode(HttpStatus.SC_OK)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/Author.json"))
        .extract().as(AuthorsResponseDTO.class);

    Assertions.assertAll( "Update author response",
        () -> Assertions.assertEquals(newAuthorDTO.getId(), updateAuthorResponse.getId(), "wrong id"),
        () -> Assertions.assertEquals(newAuthorDTO.getIdBook(), updateAuthorResponse.getIdBook(), "wrong idBook"),
        () -> Assertions.assertEquals(newAuthorDTO.getFirstName(), updateAuthorResponse.getFirstName(), "wrong firstName"),
        () -> Assertions.assertEquals(newAuthorDTO.getLastName(), updateAuthorResponse.getLastName(), "wrong lastName")
    );
  }

  @Test
  void updateAuthorTestWithoutFirstName() {

    //Проверяем обновление автора без передачи полей firstName и lastName

    AuthorsApi authorsApi = new AuthorsApi();

    AuthorsDTO authorDTO = AuthorsDTO.builder()
        .id(1)
        .idBook(1)
        .firstName("First Name 1")
        .lastName("Last Name 1")
        .build();

    authorsApi.createAuthor(authorDTO).statusCode(HttpStatus.SC_OK);

    AuthorsDTO newAuthorDTO = AuthorsDTO.builder()
        .id(1)
        .idBook(2)
        .build();

    AuthorsResponseDTO updateAuthorResponse = authorsApi.updateAuthor(newAuthorDTO, authorDTO.getId())
        .statusCode(HttpStatus.SC_OK)
        .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schema/Author.json"))
        .extract().as(AuthorsResponseDTO.class);

    Assertions.assertAll( "Update author response",
        () -> Assertions.assertEquals(newAuthorDTO.getId(), updateAuthorResponse.getId(), "wrong id"),
        () -> Assertions.assertEquals(newAuthorDTO.getIdBook(), updateAuthorResponse.getIdBook(), "wrong idBook"),
        () -> Assertions.assertEquals(newAuthorDTO.getFirstName(), updateAuthorResponse.getFirstName(), "wrong firstName"),
        () -> Assertions.assertEquals(newAuthorDTO.getLastName(), updateAuthorResponse.getLastName(), "wrong lastName")
    );
  }
}