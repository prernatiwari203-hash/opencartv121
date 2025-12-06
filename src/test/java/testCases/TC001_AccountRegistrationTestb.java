package testCases;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTestb extends BaseClass  {
	
	@Test(groups ={"Regression" ,"Master"})
	public void verify_Account_Registration()
	{
		
		logger.info("**** TC001_AccountRegistrationTestb ****");
		
		try {
		
			
		HomePage hp =new HomePage(driver);
		hp.clickMyAccount();
		logger.info("Clicked on MyAccount link");
		
		hp.clickRegister();
		logger.info("Clicked on Register link");
		
		
		AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
		
		logger.info("Providing customer details....");

		regpage.setFirstName(randomString().toUpperCase());
		regpage.setLastName(randomString().toUpperCase());
		regpage.setEmail(randomString()+"@gmail.com");
		regpage.setTelephone(randomNumber());
		
		
		String password = randomAlphaNumeric();
		
		regpage.setPassword(password);
		regpage.setConfirmPassword(password);
		
		regpage.setprivacypolicy();
		regpage.clickContinue();
		
		logger.info("Validating expected message...");
		
		String confmsg = regpage.getConfirmationMsg();
		if(confmsg.equals("Your Account Has Been Created!"))
		{
			Assert.assertTrue(true);
		}
		else
		{
			logger.error("Test Failed");
			logger.debug("Debug logs... ");
			Assert.assertTrue(false);
		}
		// Assert.assertEquals(confmsg, "Your Account Has Been Creatyted!");
		
		}
		catch(Exception e)
		{
			Assert.fail();
			
		}
		logger.info(" *****Finished TC001_AccountRegistrationTestb ***** ");
	
		}
	
		
	}
	
	
	
	
	
	
	


