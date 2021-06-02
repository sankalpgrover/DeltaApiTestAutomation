package services;

import java.io.File;
import java.io.IOException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import com.aventstack.extentreports.ExtentTest;


public class RequestMethods {

    public static int getStatusCode(String status) throws IOException {
        return Integer.parseInt(TestSetup.loadConfigProperties().getProperty(status));
    }

    public static Response postJsonPayload(Object payload, String endPoint,ExtentTest extentTest) throws IOException {
        RestAssured.baseURI = TestSetup.loadConfigProperties().getProperty("api.uri");
        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(payload)
                        .post(endPoint)
                        .then().log().all()
                        .statusCode(getStatusCode("StatusCode_OK"))
                        .extract()
                        .response();
        TestSetup.setTestStatus("INFO", "Api End Point: " + RestAssured.baseURI + File.separatorChar + endPoint, extentTest);
        TestSetup.setTestStatus("INFO", "Api response code: " + response.getStatusCode(), extentTest);
        return response;
    }

    public Response getJsonResponse(String endPoint, ExtentTest extentTest) throws IOException {
        RestAssured.baseURI = TestSetup.loadConfigProperties().getProperty("api.uri");
        Response response =
                given()
                        .header("Authorization", "Bearer " + TestSetup.loadConfigProperties().get("bearer_token"))
                        .get(endPoint)
                        .then().log().headers()
                        .statusCode(getStatusCode("StatusCode_OK"))
                        .extract()
                        .response();
        TestSetup.setTestStatus("INFO", "Api End Point: " + RestAssured.baseURI + File.separatorChar + endPoint, extentTest);
        TestSetup.setTestStatus("INFO", "Api response code: " + response.getStatusCode(), extentTest);
        return response;
    }
}
