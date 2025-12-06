package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider(name="LoginData") 
    public String[][] getLoginData() throws IOException { //
        
        String path = ".//testData//OpenCart_Login_Data.xlsx"; // r'eading xl file from TestData

        String logindata[][] = null; //

        ExcelUtility xlutil = new ExcelUtility(path); // creating an object for XlUtility
        
        int totalrows = xlutil.getRowCount("Sheet1"); //
        int totalcols = xlutil.getCellCount("Sheet1", 0); // created for two dimension array which can stor
        
        // created for two dimension array which can store
        logindata = new String[totalrows][totalcols]; //

        for (int i = 1; i <= totalrows; i++) { // i is rows j is col
            for (int j = 0; j < totalcols; j++) { // // read the data from xl string in two desimlonal array
                logindata[i - 1][j] = xlutil.getCellData("Sheet1", i, j); //
            }
        }
        
        // providers. Java let me explain so data
        return logindata; // returning two dimension array
        // providers. Java so data
    }
    // can keep adding more data provider methods
}