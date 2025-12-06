package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class TC001_AccountRegistrationTestb extends BaseClass {

    @Test(groups = {"Regression", "Master"})
    public void verify_Account_Registration() {

        logger.info("***** TC001_AccountRegistrationTestb STARTED *****");

        try {

            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();
            logger.info("Clicked on MyAccount link");

            hp.clickRegister();
            logger.info("Clicked on Register link");

            AccountRegistrationPage regpage =
                    new AccountRegistrationPage(driver);

            logger.info("Providing customer details");

            regpage.setFirstName(randomString().toUpperCase());
            regpage.setLastName(randomString().toUpperCase());
            regpage.setEmail(randomString() + "@gmail.com");
            regpage.setTelephone(randomNumber());

            String password = randomAlphaNumeric();
            regpage.setPassword(password);
            regpage.setConfirmPassword(password);

            regpage.setprivacypolicy();
            regpage.clickContinue();

            logger.info("Validating confirmation message");

            String confmsg = regpage.getConfirmationMsg();

            Assert.assertEquals(
                    confmsg,
                    "Your Account Has Been Created!",
                    "Account creation failed"
            );

        } catch (Exception e) {
            logger.error("Test failed due to exception", e);
            Assert.fail(e.getMessage());
        }

        logger.info("***** TC001_AccountRegistrationTestb FINISHED *****");
    }
}

	
	
	
	
	
	
	


