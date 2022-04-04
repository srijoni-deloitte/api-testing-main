package Operation;

import Helper.ExtractData;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TaskOperation extends LoginController {
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;
    private List<String> tasks;
    private JSONArray tasksJsonArray;

    public TaskOperation(String baseUrl, ExtentTest test, Logger log) {
        super(baseUrl, test, log);
    }

    public void init() {
        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        //taken from video
        reqBuilder.setBaseUri(baseUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token);
        requestSpecification = RestAssured.with().spec(reqBuilder.build());

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectContentType(ContentType.JSON);
        responseSpecification = resBuilder.build();
    }

    private void getAllTasks() {
        tasks = ExtractData.getAllTasks();
    }

    public void addTasks() {
        test.info("Adding all tasks");
        getAllTasks();
        for (String task: tasks)
            addTask((new JSONObject()).put("description", task));
        test.pass("All tasks added");
    }

    public void addTask(JSONObject task) {
        test.info("Adding task => " + task);
        log.info("Adding task =>" + task);
//        log.debug(baseUrl);
//        log.debug("Bearer " + token);

        Response response = given().spec(requestSpecification).body(task.toString())
                .when().post("/task").then().spec(responseSpecification)
                .extract().response();
        assert (response.statusCode() == 201);
        test.pass("Task added");
    }

    public void getTask(int pageLimit) {
        test.info("Getting task data by Page Limit, " + pageLimit);
        log.info("Getting task data by Page limit, " + pageLimit);

        Response response = given().spec(requestSpecification).param("limit", pageLimit)
                .when().get("/task").then().spec(responseSpecification)
                .log().all().extract().response();

        JSONObject obj = new JSONObject(response.asString());
        tasksJsonArray = new JSONArray(obj.getJSONArray("data"));

        assert response.statusCode() == 200 && validateGetResponse(pageLimit);

        log.debug("Get response");
        log.debug(response.asString());
        test.pass("Successfully fetch " + pageLimit + " no of data");
    }

    private boolean validateGetResponse(int limit) {
        log.info("Validating get response data");
        test.info("Validating test response data");
        if (tasksJsonArray.length() != limit) return false;
        boolean ok = true;
        String owner = tasksJsonArray.getJSONObject(0).getString("owner");
        for (int i = 0; i < limit && ok; i++) {
            JSONObject obj = tasksJsonArray.getJSONObject(i);
            ok = tasks.get(i).equals(obj.getString("description"))
                && owner.equals(obj.getString("owner"));
        }
        return ok;
    }

}
