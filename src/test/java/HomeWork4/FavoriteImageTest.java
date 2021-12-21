package HomeWork4;

import HomeWork4.dto.PostImageResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;

public class FavoriteImageTest extends BaseTest {

    String uploadedImageId;
    static String encodedFile;
    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification multiPartSpecWithFile;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;

    @BeforeEach
    void beforeTest() {


        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        multiPartSpecWithFile = new MultiPartSpecBuilder(new File(PATH_TO_IMAGE))
                .controlName("image")
                .build();

        requestSpecificationWithAuthAndMultipartImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "gif")
                .addMultiPart(multiPartSpecWithFile)
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
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
    void FavoriteImageTest() {
        given(requestSpecificationWithAuth)
                .post("/image/{imageHash}/favorite", uploadedImageId)
                .prettyPeek();
    }

    @AfterEach
    void tearDown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete("/account/{username}/image/{imageHash}", username, uploadedImageId)
                .prettyPeek();
    }

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
