package HomeWork4;

import HomeWork4.dto.PostImageResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AlbumUpdate extends BaseTest {

    String albumDeleteHash;

    @BeforeEach

    void AlbumCreation() {
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
    void AlbumUpdateTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .put("https://api.imgur.com/3/album/{albumHash}", albumDeleteHash)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete("/album/{albumHash}", albumDeleteHash)
                .prettyPeek();
    }

}
