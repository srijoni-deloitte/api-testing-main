package Operation;

import Helper.User;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class UserSignupController {
    ExtentTest test;
    Logger log;
    String baseUrl;
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    public UserSignupController(ExtentTest test, Logger log, String baseUrl) {
        RestAssured.useRelaxedHTTPSValidation();
        this.test = test;
        this.log = log;
        this.baseUrl = baseUrl;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(baseUrl).addHeader("Content-Type", "application/json");
        requestSpecification = RestAssured.with().spec(reqBuilder.build());

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
    }

    public boolean registerUser(User user) {
        log.debug(user.getFullUserJson());
        test.info("User Registration test");
        test.debug(user.getFullUserJson().toString());

        Response response = given().spec(requestSpecification)
                .body(user.getFullUserJson().toString()).post("/user/register").then().
                spec(responseSpecification).extract().response();

        if (response.statusCode() == 400) {
            String msg = "User already present with same email i.e. " + user.email;
            log.warn(msg);
            test.warning(msg);
            return false;
        }
        log.info("User successfully registered");
        test.log(Status.PASS, "User successfully registered");
        return true;
     }
}
