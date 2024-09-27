import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class FavoriteImageTest extends BaseTest {

    String imageHash;

    @BeforeEach
    void setUp() {
        imageHash = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void FavoriteImageTest() {
        given()
                .headers("Authorization", token)
                .expect()
                .statusCode(200)
                .body("data", equalTo("favorited"))
                .body("success", equalTo(true))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite", imageHash)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{imageHash}", "entreri666", imageHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
