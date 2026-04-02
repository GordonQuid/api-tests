package services;

import static io.restassured.RestAssured.given;

import dto.AuthorsDTO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class AuthorsApi {
  private static final String API_PATH = "/api/v1";
  private static final String AUTHORS = "/Authors";
  private static final String ID = "/{id}";
  private RequestSpecification spec;

  public AuthorsApi() {
    String baseUrl = System.getProperty("base.url");
    spec = new RequestSpecBuilder()
        .setBaseUri(baseUrl)
        .setBasePath(API_PATH)
        .setContentType(ContentType.JSON)
        .log(io.restassured.filter.log.LogDetail.ALL)
        .build();
  }

  public ValidatableResponse createAuthor(AuthorsDTO author) {
    return given(spec)
        .body(author)
        .when()
        .post(AUTHORS)
        .then()
        .log().all();
  }

  public ValidatableResponse getAuthor(int id) {
    return given(spec)
        .pathParam("id", id)
        .when()
        .get(AUTHORS + ID)
        .then()
        .log().all();
  }

  public ValidatableResponse updateAuthor(AuthorsDTO author, int id) {
    return given(spec)
        .pathParam("id", id)
        .body(author)
        .when()
        .put(AUTHORS + ID)
        .then()
        .log().all();
  }

  public ValidatableResponse deleteAuthor(int id) {
    return given(spec)
        .pathParam("id", id)
        .when()
        .delete(AUTHORS + ID)
        .then()
        .log().all();
  }
}
