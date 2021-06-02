package utils;

import java.util.Map;
import java.io.File;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileInputStream;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.TestSetup;

public class ExcelReader {

    public static Object[][] getTestData(String methodName) throws IOException {
        int index = 0, headerRowNum = 0;
        Boolean testData = false;
        File file = new File(System.getProperty("user.dir") + TestSetup.loadConfigProperties().getProperty("TestDataSheet"));
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = workbook.getSheet(TestSetup.loadConfigProperties().getProperty("workSheetName"));
        int lastRowNum = sheet.getLastRowNum();
        int countRowsInTestData = getRowCount(lastRowNum, sheet, methodName);
        Object[][] testDataObj = new Object[countRowsInTestData][1];
        retrieveTestCaseData(methodName, index, headerRowNum, sheet, lastRowNum, testDataObj, testData);
        return testDataObj;
    }

    public static void retrieveTestCaseData(String methodName, int index, int headerRowNum, XSSFSheet sheet, int lastRowNum, Object[][] testDataObj, Boolean testData) {
        int lastCellNum;
        for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
            if (sheet.getRow(rowNum) != null) {
                if (sheet.getRow(rowNum).getCell(0).getStringCellValue().equals(methodName)) {
                    testData = true;
                    //Headers will be fetched from next row once test name matches in spreadsheet
                    headerRowNum = rowNum + 1;
                    //Test data starts from (rowNum + 2) in each test case once test method name gets a match
                    rowNum = rowNum + 2;
                }

                if (testData && sheet.getRow(rowNum) != null) {
                    lastCellNum = sheet.getRow(rowNum).getLastCellNum();
                    Map<Object, Object> datamap = new HashMap<>();
                    for (int j = 0; j < lastCellNum; j++) {
                        datamap.put(sheet.getRow(headerRowNum).getCell(j).toString(), sheet.getRow(rowNum).getCell(j).toString());
                    }
                    testDataObj[index][0] = datamap;
                    index++;
                }
            } else {
                testData = false;
            }
        }
    }

    public static int getRowCount(int lastRowNum, XSSFSheet sheet, String methodName) {
        int count = 0;
        for (int rowNum = 0; rowNum < lastRowNum; rowNum++) {
            if (sheet.getRow(rowNum) != null) {
                if (sheet.getRow(rowNum).getCell(0).getStringCellValue().equals(methodName)) {
                    int startRowNum = rowNum;

                    for (int row = startRowNum; row < lastRowNum; row++) {
                        while (sheet.getRow(row + 2) != null) {
                            row++;
                            count++;
                        }
                        break;
                    }
                }
            }
        }
        return count;
    }
}