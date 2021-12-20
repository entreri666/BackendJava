package HomeWork4;

import HomeWork4.dto.PostImageResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class SetAlbumImagesTest extends BaseTest {

    String albumDeleteHash;

    @BeforeEach
    void AlbumCreationTest() {
        albumDeleteHash = given(requestSpecificationWithAuth, positiveResponseSpecification)
                .post("/album")
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @Test
    void SetAlbumImagesTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .post("/album/{albumDeleteHash}", albumDeleteHash)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete("/album/{albumDeleteHash}", albumDeleteHash)
                .prettyPeek();
    }
}
