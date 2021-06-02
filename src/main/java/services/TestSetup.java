package services;

import org.testng.Assert;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;
import org.testng.annotations.*;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TestSetup {
    public static ExtentReports extent;
    public static ExtentSparkReporter extentReportFile;

    //Read config.properties before every test/suite file for any configurations
    @BeforeTest
    public static Properties loadConfigProperties() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config.properties"));
        return props;
    }

    //Read message.properties before every test/suite file for any configurations
    @BeforeTest
    public static Properties loadMessageProperties() throws IOException {
        Properties props = new Properties();
        props.load(new FileReader(System.getProperty("user.dir") + "/src/test/resources/message.properties"));
        return props;
    }

    //Set up Extent Reports 5
    @BeforeTest
    public void setup() {
        extentReportFile = new ExtentSparkReporter(System.getProperty("user.dir") + "/extentReportFile.html");
        extent = new ExtentReports();
        extent.attachReporter(extentReportFile);
    }

    //Push status and comments to Extent reports based on different logging levels
    public static void setTestStatus(String status, String msg, ExtentTest extentTest) {
        try {
            switch (status) {
                case "PASS":
                    extentTest.log(Status.PASS, loadMessageProperties().getProperty("testStatus_PASS") + " " + msg);
                    break;
                case "FAIL":
                    extentTest.log(Status.FAIL, loadMessageProperties().getProperty("testStatus_FAIL") + " " + msg);
                    break;
                case "WARNING":
                    extentTest.log(Status.WARNING, loadMessageProperties().getProperty("testStatus_WARNING") + " " + msg);
                    break;
                case "INFO":
                    extentTest.log(Status.INFO, loadMessageProperties().getProperty("testStatus_INFO") + " " + msg);
                    break;
                case "SKIP":
                    extentTest.log(Status.SKIP, loadMessageProperties().getProperty("testStatus_SKIP") + " " + msg);
                    break;
                default:
                    extentTest.log(Status.FAIL, loadMessageProperties().getProperty("testStatus_FAIL") + " " + msg);
                    break;
            }
        } catch (Exception e) {
            setTestStatus("FAIL", e.getLocalizedMessage(), extentTest);
            Assert.fail("Logging Failed: " + e.getLocalizedMessage());
        }
    }



    @AfterTest
    public void tearDown() {
        extent.flush();
    }
}
