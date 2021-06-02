
import com.aventstack.extentreports.ExtentTest;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import models.responsemodels.users.Datum;
import models.responsemodels.users.Users;
import services.CustomAssertions;
import services.EndPoints;
import utils.ExcelReader;
import services.RequestMethods;
import services.TestSetup;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DemoTest extends TestSetup {

    Gson gson = new Gson();
    Response response;
    RequestMethods requestMethods = new RequestMethods();
    CustomAssertions customAssertion = new CustomAssertions();

    //Test method name has to same in excel data sheet
    //HashMap<String, String> data parameter provides test data for every execution from data provider
    @Test(dataProvider = "data-provider")
    public void getDemographicDataFromApi(HashMap<String, String> data, Method method) {

        //Start monitoring of test case in extent report
        ExtentTest extentTest = TestSetup.extent.createTest(method.getName(), "Validate demographic data of a User");
        try {
            // Start the test using the ExtentTest class object.
            response = requestMethods.getJsonResponse(EndPoints.getUsersEndPoint(), extentTest);
            Users jsonPojoGoRest = gson.fromJson(response.getBody().asString(), Users.class);
            Optional<Datum> dataId = jsonPojoGoRest.getData().stream().filter(entity -> Integer.valueOf(entity.getId()) == Integer.valueOf(data.get("Id"))).findFirst();
            customAssertion.assertEquals("Email", dataId.get().getEmail(), data.get("email"), extentTest);
            customAssertion.assertEquals("Gender", dataId.get().getGender(), data.get("gender"), extentTest);
            customAssertion.assertEquals("Name", dataId.get().getName(), data.get("name"), extentTest);
            customAssertion.assertEquals("status", dataId.get().getStatus(), data.get("status"), extentTest);

        } catch (Exception ex) {
            TestSetup.setTestStatus("FAIL", ex.getMessage(), extentTest);
        }
    }

    //Test method name has to same in excel data sheet
    //HashMap<String, String> data parameter provides test data for every execution from data provider
    @Test(dataProvider = "data-provider")
    public void getActiveUsers(HashMap<String, String> data, Method method) {
        //Start monitoring of test case in extent report
        ExtentTest extentTest = TestSetup.extent.createTest(method.getName(), "Count Active Users");
        try {
            // Start the test using the ExtentTest class object.
            response = requestMethods.getJsonResponse(EndPoints.getUsersEndPoint(), extentTest);
            Users jsonPojoGoRest = gson.fromJson(response.getBody().asString(), Users.class);
            List<Datum> activeUsersList = jsonPojoGoRest.getData().stream().filter(entity -> entity.getStatus().equals("Active")).collect(Collectors.toList());
            List<Datum> maleUsersList = jsonPojoGoRest.getData().stream().filter(entity -> entity.getStatus().equals("Active")&&entity.getGender().equals("Female")).collect(Collectors.toList());
            List<Datum> femaleUsersList = jsonPojoGoRest.getData().stream().filter(entity -> entity.getStatus().equals("Active")&&entity.getGender().equals("Male")).collect(Collectors.toList());

            if (activeUsersList.size() == Integer.valueOf(data.get("totalCount"))) {
                TestSetup.setTestStatus("PASS", "Expected Actual Users: " + data.get("totalCount") + " Actual Users: " + activeUsersList.size(), extentTest);
            } else {
                TestSetup.setTestStatus("FAIL", "Expected Actual Users: " + data.get("totalCount") + " Actual Users: " + activeUsersList.size(), extentTest);
            }

            TestSetup.setTestStatus("INFO"," Count of Active Male users: "+maleUsersList.size(),extentTest);
            TestSetup.setTestStatus("INFO"," Count of Active Female users: "+femaleUsersList.size(),extentTest);

        } catch (Exception ex) {
            TestSetup.setTestStatus("FAIL", ex.getMessage(), extentTest);
        }
    }

    @DataProvider(name = "data-provider")
    public static Object[][] getData(Method method) throws IOException {
        return ExcelReader.getTestData(method.getName());
    }
}
