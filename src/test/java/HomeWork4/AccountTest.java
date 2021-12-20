package HomeWork4;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AccountTest extends BaseTest {

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .get("/account/{username}", username);

    }
}
