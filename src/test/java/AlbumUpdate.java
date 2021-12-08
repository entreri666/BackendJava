import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class AlbumUpdate extends BaseTest {

    String albumDeleteHash;
    String albumHash;

    @BeforeEach
    void AlbumCreationTest() {
        albumHash = given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/album")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void AlbumUpdateTest() {
        albumDeleteHash = given()
                .headers("Authorization", token)
                .param("title", "new album")
                .expect()
                .statusCode(200)
                .body("data", equalTo(true))
                .body("success", equalTo(true))
                .when()
                .put("https://api.imgur.com/3/album/{albumHash}", albumHash)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/album/{albumDeleteHash}", albumDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
