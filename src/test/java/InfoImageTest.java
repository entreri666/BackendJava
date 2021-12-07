import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class InfoImageTest extends BaseTest {
    String imageHash;
    Response image;

    @BeforeEach
    void setUp() {
            imageHash = given()
                    .headers("Authorization", token)
                    .multiPart("image", new File(PATH_TO_IMAGE))
                    .expect()
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
        void imageTest () {
            image = given()
                    .headers("Authorization", token)
                    .expect()
                    .statusCode(200)
                    .body("data.type", equalTo("image/jpeg"))
                    .body("data.title", equalTo(null))
                    .when()
                    .get("https://api.imgur.com/3/image/{imageHash}", imageHash)
                    .prettyPeek();
        }
    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "entreri666", image)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
    }
