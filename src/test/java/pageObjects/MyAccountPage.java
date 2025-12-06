package pageObjects; //

import org.openqa.selenium.WebDriver; //
import org.openqa.selenium.WebElement; //
import org.openqa.selenium.support.FindBy; //
import org.openqa.selenium.support.PageFactory; //

public class MyAccountPage extends BasePage { //

    public MyAccountPage(WebDriver driver) { //
        super(driver); //
        PageFactory.initElements(driver, this); //
    }

    @FindBy(xpath = "//h2[text()='My Account']") // MyAccount Page Heading
    WebElement msgHeading; //

    @FindBy(xpath = "//div[@class='list-group']/a[text()='Logout']") //
    WebElement lnkLogout; //

    public boolean isMyAccountPageExists() { //
        try { //
            return msgHeading.isDisplayed(); //
        } catch (Exception e) { //
            return false; //
        }
    }
    
    public void clickLogout() { // Inferred method
        lnkLogout.click();
    }
}
	


