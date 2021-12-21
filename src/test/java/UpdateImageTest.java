import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateImageTest extends BaseTest {

    String imageDeleteHash;

    @BeforeEach
    void setUp() {
        imageDeleteHash = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void UpdateImageTest() {
        given()
                .headers("Authorization", token)
                .param("title", "new name")
                .expect()
                .statusCode(200)
                .body("data", equalTo(true))
                .body("success", equalTo(true))
                .when()
                .post("https://api.imgur.com/3/image/{imageDeleteHash}", imageDeleteHash)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "entreri666", imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
