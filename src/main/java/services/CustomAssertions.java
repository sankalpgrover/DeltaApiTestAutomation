package services;

import org.testng.Assert;
import com.aventstack.extentreports.ExtentTest;

public class CustomAssertions {

    //Custome testng assertion method so as to push assertion results to Extent reports for better traceability
    public void assertEquals(String fieldName,String expectedString, String actualString, ExtentTest extentTest) {
        try {
            if (expectedString == null || actualString == null || expectedString.equals("") || actualString.equals("")) {
                TestSetup.setTestStatus("INFO",fieldName+ ":   Expected: "+expectedString+"   Actual:  "+actualString,extentTest);
                TestSetup.setTestStatus("FAIL", "Value is either BLANK or NULL",extentTest);
            }
            TestSetup.setTestStatus("INFO",fieldName+ ":   Expected: "+expectedString+"   Actual:  "+actualString,extentTest);
            Assert.assertTrue(expectedString.equals(actualString));
            TestSetup.setTestStatus("PASS", "",extentTest);
        } catch (AssertionError e) {
            TestSetup.setTestStatus("FAIL", e.getLocalizedMessage(),extentTest);
        }
    }
}
