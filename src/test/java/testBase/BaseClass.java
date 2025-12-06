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
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {

    public static WebDriver driver;
    public Logger logger;
    public Properties p;

    @BeforeClass
    @Parameters({"os", "browser"})
    public void setup(
            @Optional("windows") String os,
            @Optional("chrome") String browser) throws IOException {

        // load config
        FileReader file = new FileReader("./src/test/resources/config.properties");
        p = new Properties();
        p.load(file);

        logger = LogManager.getLogger(this.getClass());

        // ===== REMOTE EXECUTION =====
        if (p.getProperty("execution_env").equalsIgnoreCase("remote")) {

            DesiredCapabilities caps = new DesiredCapabilities();

            if (os.equalsIgnoreCase("windows")) {
                caps.setPlatform(Platform.WIN11);
            } else if (os.equalsIgnoreCase("mac")) {
                caps.setPlatform(Platform.MAC);
            } else {
                throw new RuntimeException("Invalid OS");
            }

            switch (browser.toLowerCase()) {
                case "chrome":
                    caps.setBrowserName("chrome");
                    break;
                case "edge":
                    caps.setBrowserName("MicrosoftEdge");
                    break;
                case "firefox":
                    caps.setBrowserName("firefox");
                    break;
                default:
                    throw new RuntimeException("Invalid Browser");
            }

            driver = new RemoteWebDriver(
                    new URL("http://localhost:4444/wd/hub"), caps);
        }

        // ===== LOCAL EXECUTION =====
        else {
            switch (browser.toLowerCase()) {
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

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get(p.getProperty("appurl"));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ===== Utilities =====
    public String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNumber() {
        return RandomStringUtils.randomNumeric(10);
    }

    public String randomAlphaNumeric() {
        return RandomStringUtils.randomAlphabetic(3) + "@" +
               RandomStringUtils.randomNumeric(3);
    }

    public String captureScreen(String tname) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") +
                "\\screenshots\\" + tname + "_" + timeStamp + ".png";
        File dest = new File(path);
        src.renameTo(dest);
        return path;
    }
}

