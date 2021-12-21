import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccountTest extends BaseTest {

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .contentType("application/json")
                .body("data.url", equalTo(username))
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();

    }
}
