package graphqll;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class UserResourceTest {

    public static final String GRAPHQL = "/graphql";
    public static final String CONTENT_TYPE = "application/json";
    public static final int STATUS_CODE_200 = 200;

    @Test
    void testQueryUserWithOrdersAndSettings() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"query\":\"{ user(id: \\\"123\\\") { name email } }\"}")
                .when()
                .post(GRAPHQL)
                .then()
                .statusCode(STATUS_CODE_200)
                .body("data.user.name", equalTo("Bob"));
    }

    @Test
    void testMutationUpdateEmail() {
        given()
                .contentType(CONTENT_TYPE)
                .body("{\"query\":\"mutation { updateUserEmail(id: \\\"123\\\", email: \\\"newalice@example.com\\\") { email } }\"}")
                .when()
                .post(GRAPHQL)
                .then()
                .statusCode(STATUS_CODE_200)
                .body("data.updateUserEmail.email", equalTo("newalice@example.com"));

    }

}