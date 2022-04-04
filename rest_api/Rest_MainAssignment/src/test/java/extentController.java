import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class extentController {
    public static Logger log;
    public static ExtentReports extent;
    public static ExtentTest test;
    public static String baseUrl = "https://api-nodejs-todolist.herokuapp.com";
    static {
        log = LogManager.getLogger(extentController.class.getName());
        extent = new ExtentReports();
        extent.attachReporter(new ExtentHtmlReporter("extent.html"));
        test = extent.createTest("Main Assignment", "Test Description");
    }
}
