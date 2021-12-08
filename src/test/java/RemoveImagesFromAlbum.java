import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class RemoveImagesFromAlbum extends BaseTest {

    String albumHash;
    String image;

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

    @BeforeEach
    void AddImagesToAlbum() {
        image = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/album/{albumDeleteHash}/add", albumHash)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void RemoveImagesFromAlbumTest() {
        given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_IMAGE))
                .expect()
                .statusCode(200)
                .body("data", is(notNullValue()))
                .body("success", equalTo(true))
                .when()
                .post("https://api.imgur.com/3/album/{albumDeleteHash}/remove_images", albumHash)
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

