package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseBody;
import model.AuthorizedUser;
import model.User;
import org.hamcrest.Matchers;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AccountTest {
//    todo After impl DEMOQA-004, move it to config variable
    private static final String BASE_ACCOUNT_URI = "https://demoqa.com/Account/v1";
    private static final String BEARER_TEMPLATE = "Bearer %s";
    private static final String USER_TEMPLATE = BASE_ACCOUNT_URI + "/User/";

    @Test (dataProvider = "provideUserData")
    public void getAccountBooksTest(User user, String expectedIsbn) throws JsonProcessingException {

//        todo Move it to Config provider
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String userSerialized = mapper.writeValueAsString(user);

        ResponseBody userBody = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType("application/json")
                .body(userSerialized)
                .post(BASE_ACCOUNT_URI + "/Login")
                .body();

        AuthorizedUser authorizedUser = mapper.readValue(userBody.asString(), AuthorizedUser.class);

        RestAssured.given()
                .when()
                .header("Authorization", BEARER_TEMPLATE.formatted(authorizedUser.getToken()))
                .get(USER_TEMPLATE + authorizedUser.getId().toString())
                .then()
                .statusCode(200)
                .body("books[0].isbn", Matchers.equalTo(expectedIsbn));
    }

    @DataProvider
    public Object[][] provideUserData() {
        return new Object[][] {{new User("userName", "Password123!"), "9781449365035"}};
    }
}
