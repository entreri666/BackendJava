import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class FavoriteAlbumTest extends BaseTest {

    String albumHash;

    @BeforeEach
    void AlbumCreation() {
        albumHash = given()
                .headers("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/album")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void FavoriteAlbumTest() {
        given()
                .headers("Authorization", token)
                .expect()
                .statusCode(200)
                .body("data", is(notNullValue()))
                .body("success", equalTo(true))
                .when()
                .post("https://api.imgur.com/3/album/{albumDeleteHash}/favorite", albumHash)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/album/{albumDeleteHash}", albumHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
