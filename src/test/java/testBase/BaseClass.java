package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {

    public static WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass(alwaysRun = true)
    @Parameters({"os", "browser"})
    public void setup(String os, String br) throws IOException {

        // Load config.properties
        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());

        String env = p.getProperty("execution_env");

        if (env.equalsIgnoreCase("remote")) {

            DesiredCapabilities cap = new DesiredCapabilities();

            // OS
            if (os.equalsIgnoreCase("windows")) {
                cap.setPlatform(Platform.WIN11);
            } else if (os.equalsIgnoreCase("mac")) {
                cap.setPlatform(Platform.MAC);
            } else {
                throw new RuntimeException("Invalid OS");
            }

            // Browser
            switch (br.toLowerCase()) {
                case "chrome":
                    cap.setBrowserName("chrome");
                    break;
                case "edge":
                    cap.setBrowserName("MicrosoftEdge");
                    break;
                case "firefox":
                    cap.setBrowserName("firefox");
                    break;
                default:
                    throw new RuntimeException("Invalid Browser");
            }

            driver = new RemoteWebDriver(
                    new URL("http://localhost:4444/wd/hub"), cap);
        }

        else if (env.equalsIgnoreCase("local")) {

            switch (br.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                default:
                    throw new RuntimeException("Invalid Browser");
            }
        }

        else {
            throw new RuntimeException("Invalid execution_env in properties file");
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(p.getProperty("appurl"));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    // ================== Utilities ==================

    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphabetic(3) + "@"
                + RandomStringUtils.randomNumeric(3);
    }

    // ================== Screenshot (NULL SAFE) ==================

    public String captureScreen(String testName) {

        try {
            if (driver == null) {
                System.out.println("Driver is NULL. Screenshot skipped.");
                return null;
            }

            TakesScreenshot ts = (TakesScreenshot) driver;

            String timeStamp =
                    new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            File source = ts.getScreenshotAs(OutputType.FILE);

            String targetPath = System.getProperty("user.dir")
                    + "/screenshots/" + testName + "_" + timeStamp + ".png";

            File target = new File(targetPath);
            source.renameTo(target);

            return targetPath;

        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
            return null;
        }
    }
}
