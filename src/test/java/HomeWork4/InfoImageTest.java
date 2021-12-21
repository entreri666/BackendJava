package HomeWork4;

import HomeWork4.dto.PostImageResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class InfoImageTest extends BaseTest {

    String uploadedImageId;
    MultiPartSpecification multiPartSpecWithFile;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;

    @BeforeEach
    void setUp() {
        multiPartSpecWithFile = new MultiPartSpecBuilder(new File(PATH_TO_IMAGE))
                .controlName("image")
                .build();

        requestSpecificationWithAuthAndMultipartImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "gif")
                .addMultiPart(multiPartSpecWithFile)
                .build();

        uploadedImageId = given(requestSpecificationWithAuthAndMultipartImage, positiveResponseSpecification)
                .post("/upload")
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getId();
    }

    @Test
    void imageTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .get("/image/{imageHash}", uploadedImageId)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete("/image/{imageHash}", uploadedImageId)
                .prettyPeek();
    }
}
