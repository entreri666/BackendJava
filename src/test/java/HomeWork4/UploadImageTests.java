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

public class UploadImageTests extends BaseTest {

    static String encodedFile;
    String uploadedImageId;
    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification multiPartSpecWithFile;
    static RequestSpecification requestSpecificationWithAuthWithBase64;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;

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
                    .addFormParam("title", "Picture")
                    .addMultiPart(base64MultiPartSpec)
                    .build();

        }

        @Test
        void uploadBase64Test() {
            uploadedImageId = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                    .post("/upload")
                    .prettyPeek()
                    .then()
                    .extract()
                    .body()
                    .as(PostImageResponse.class)
                    .getData().getDeletehash();
        }

        @Test
        void uploadWithMultiPart() {
            uploadedImageId = given(requestSpecificationWithAuthAndMultipartImage, positiveResponseSpecification)
                    .post("/upload")
                    .prettyPeek()
                    .then()
                    .extract()
                    .body()
                    .as(PostImageResponse.class)
                    .getData().getDeletehash();
        }



        @AfterEach
        void tearDown() {
            given(requestSpecificationWithAuth, positiveResponseSpecification)
                    .delete("/account/{username}/image/{deleteHash}", username, uploadedImageId)
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
