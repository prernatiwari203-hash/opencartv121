package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

/*
 * Data Provider Legend:
 * email, pwd, exp (Expected Result: "Valid" or "Invalid")
 */

public class TC003_LoginDDT extends BaseClass {
	
	@Test(dataProvider = "LoginData" , dataProviderClass = DataProviders.class, groups ="Datadriven") //getting data provider from different class
	public void verify_loginDDT(String email , String pwd, String exp)
	{
		
		logger.info("**** starting test TC_003_LoginDDT_Test for data: " + email + " ****");
		
		try
		{ 
			// 1. Setup Page Objects and Navigation
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.linkLogin();
			
			// 2. Login Action
			LoginPage lp = new LoginPage(driver);
			lp.setEmail(email);
			lp.setPassword(pwd);
			lp.clickLogin();
			
			// 3. Check Result Page
			MyAccountPage macc = new MyAccountPage(driver);
			boolean targetPage = macc.isMyAccountPageExists(); // Corrected method name capitalization

			
			// 4. Assertion Logic based on Expected Result ("Valid" or "Invalid")
			
			if (exp.equalsIgnoreCase("Valid")) // EXPECTING SUCCESSFUL LOGIN
			{
				if (targetPage == true)
				{
					// Successful login: Pass the test and logout for the next iteration
					macc.clickLogout(); 
					Assert.assertTrue(true, "Login successful with valid data.");
				}
				else
				{
					// Expected success, but failed to reach MyAccount page
					Assert.assertTrue(false, "Login failed with valid data.");
				}
			}
			
			else if (exp.equalsIgnoreCase("Invalid")) // EXPECTING FAILED LOGIN
			{
				if (targetPage == true)
				{
					// Expected failure, but reached MyAccount page (e.g., bug)
					macc.clickLogout(); // Logout before assertion fails to clean up
					Assert.assertTrue(false, "Login succeeded unexpectedly with invalid data.");
				}
				else
				{
					// Expected failure, and MyAccount page was NOT reached
					Assert.assertTrue(true, "Login failed correctly with invalid data.");
				}
			}
			
		}
		catch(Exception e)
		{
			logger.error("Test failed due to exception: " + e.getMessage());
			Assert.fail("Test failed due to an exception during execution: " + e.getMessage());
		}
		finally
		{
			logger.info("**** Finished TC_003_LoginDDT_Test ****");
		}
	}
}
	
