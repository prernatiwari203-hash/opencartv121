package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC002_LoginTest extends BaseClass {

    @Test(groups = {"Sanity", "Master"})
    public void verify_login() {

        logger.info("***** TC002_LoginTest STARTED *****");

        try {

            // Home Page
            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();
            logger.info("Clicked on MyAccount");

            hp.linkLogin();
            logger.info("Clicked on Login link");

            // Login Page
            LoginPage lp = new LoginPage(driver);
            lp.setEmail(p.getProperty("email"));
            lp.setPassword(p.getProperty("password"));
            lp.clickLogin();

            logger.info("Login submitted");

            // My Account Page
            MyAccountPage macc = new MyAccountPage(driver);
            boolean isMyAccountDisplayed = macc.isMyAccountPageExists();

            Assert.assertTrue(
                    isMyAccountDisplayed,
                    "Login failed - My Account page not displayed"
            );

        } catch (Exception e) {
            logger.error("❌ Login Test Failed", e);
            Assert.fail(e.getMessage());
        }

        logger.info("***** TC002_LoginTest FINISHED *****");
    }
}
