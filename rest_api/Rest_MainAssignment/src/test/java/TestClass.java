import Helper.ExtractData;
import Helper.User;
import Operation.LoginController;
import Operation.UserSignupController;
import Operation.TaskOperation;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(Listener.class)
//listener command is added to configure with listener class
public class TestClass {
    Logger log = extentController.log;
    ExtentReports extent = extentController.extent;
    @Test (priority = 1)
    //first task to register all the user
    void register() {
        ExtentTest regUserTest = extent.createTest("Register UserTest");
        //how to register using extenttest data
        UserSignupController op = new UserSignupController(regUserTest, extentController.log, extentController.baseUrl);
        //from extentController class take the baseurl
        for (User user: ExtractData.getAllUsers()) {

            log.debug(user);
            op.registerUser(user);
        }
    }
    @Test (priority = 2)
    void login1() {
        ExtentTest loginTest = extent.createTest("User_session");

        TaskOperation taskOperation = new TaskOperation(extentController.baseUrl, loginTest, extentController.log);
        taskOperation.login(ExtractData.getSingleRandomUser());
        taskOperation.init();
        taskOperation.addTasks();
        //as told there need to be done by page limit 2,5 and 10
        taskOperation.getTask(2);
    }

    @Test (priority = 3)
    void registerSingleUser_negativeTest() {
        ExtentTest negRegisterTest = extent.createTest("Registering_with_already_registered_user");
        //fr negative test case this is done
        negRegisterTest.info("Testing negativeTest");
        UserSignupController neg = new UserSignupController(negRegisterTest, extentController.log, extentController.baseUrl);
        assert neg.registerUser(ExtractData.getFirstOrLast(true));
    }

    @Test (priority = 4)
    void neg_login_Test() {
        ExtentTest negLoginTest = extent.createTest("Negative Login test");
        LoginController loginController = new LoginController(extentController.baseUrl, negLoginTest, extentController.log);
        negLoginTest.info("Testing_for_neg_Login");
        loginController.login(ExtractData.getFirstOrLast(false));

    }

    @Test (priority = 5)
    void negAddTask() {
        ExtentTest negAddTaskTest = extent.createTest("Negative Add Task test");
        TaskOperation operation = new TaskOperation(extentController.baseUrl, negAddTaskTest, extentController.log);
        operation.login(ExtractData.getFirstOrLast(true));
        JSONObject badObjectType = new JSONObject();
        //creating jsonobject
        badObjectType.put("desc", "add_task");
        operation.init();
        operation.addTask(badObjectType);
    }

    @Test (priority = 6)
    void deleteAllUsers() {
        ExtentTest delTest = extent.createTest("Delete_All_user");
        LoginController session = new LoginController(extentController.baseUrl, delTest, extentController.log);
        //delete all users

        for (User user: ExtractData.getAllUsers()) {
            session.login(user);
            session.deleteUser(user);
        }
    }
}
