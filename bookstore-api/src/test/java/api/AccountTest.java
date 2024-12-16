package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AccountTest {
//    todo After impl DEMOQA-004, move it to config variable
    private static final String BASE_ACCOUNT_URI = "https://demoqa.com/Account/v1";
    private static final String LOGIN_BODY_TEMPLATE = "{\"userName\": \"%s\", \"password\": \"%s\"}";
    private static final String BEARER_TEMPLATE = "Bearer %s";
    private static final String USER_TEMPLATE = BASE_ACCOUNT_URI + "/User/";

    @Test (dataProvider = "provideUserData")
    public void getAccountBooksTest(String userName, String password, String expectedIsbn) {
        ResponseBody userBody = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType("application/json")
                .body(LOGIN_BODY_TEMPLATE.formatted(userName, password))
                .post(BASE_ACCOUNT_URI + "/Login")
                .body();

        String userId = userBody.jsonPath().get("userId");
        String token = userBody.jsonPath().get("token");

        RestAssured.given()
                .when()
                .header("Authorization", BEARER_TEMPLATE.formatted(token))
                .get(USER_TEMPLATE + userId)
                .then()
                .statusCode(200)
                .body("books[0].isbn", Matchers.equalTo(expectedIsbn));
    }

    @DataProvider
    public Object[][] provideUserData() {
        return new Object[][] {{"userName", "Password123!", "9781449365035"}};
    }
}
