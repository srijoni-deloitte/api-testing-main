package Operation;

import Helper.User;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class LoginController {
    public String token, baseUrl;
    public ExtentTest test;
    public Logger log;
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;

    public LoginController(String baseUrl, ExtentTest test, Logger log) {
        RestAssured.useRelaxedHTTPSValidation();
        this.baseUrl = baseUrl;
        this.test = test;
        this.log = log;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setBaseUri(baseUrl).addHeader("Content-Type", "application/json");
        requestSpecification = RestAssured.with().spec(reqBuilder.build());

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
    }

    public void login(User user) {
        // logging in
        log.info("Login Test");
        test.info("Login Test");
        log.debug("user login json data = " + user.getLoginJson());
        test.debug("user login json data = " + user.getLoginJson());

        Response response = given().spec(requestSpecification).body(user.getLoginJson().toString())
                .post("/user/login").then().spec(responseSpecification)
                .extract().response();
        log.debug("Login status code = " + response.statusCode());
        //as given there statuscode=200
        assert response.statusCode() == 200;

        // validating user credentials
        JSONObject obj = new JSONObject(response.asString());
        log.debug("response object json data = " + obj);
        token = obj.getString("token");
        obj = obj.getJSONObject("user");
        assert obj.getString("name").equals(user.name) && obj.getString("email").equals(user.email.toLowerCase());

        test.info("Session token = " + token);
        test.pass("Login successful, user name = " + user.name);
    }

    public void deleteUser(User user) {
        log.info("Deleting user = " + user);
        test.info("Deleting user = " + user);

        Response response = given().spec(requestSpecification)
                .header("Authorization", "Bearer " + token)
                .when().delete("/user/me").then().extract().response();
        log.debug(response.statusCode());
    }
}
