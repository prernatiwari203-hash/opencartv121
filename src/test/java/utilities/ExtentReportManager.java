package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

// Email imports
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

public class ExtentReportManager implements ITestListener {

    public ExtentSparkReporter sparkReporter;
    public ExtentReports extent;
    public ExtentTest test;

    String repName;

    @Override
    public void onStart(ITestContext testContext) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        repName = "Test-Report-" + timeStamp + ".html";

        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "\\reports\\" + repName);

        sparkReporter.config().setDocumentTitle("opencart Automation Report");
        sparkReporter.config().setReportName("opencart Functional Testing");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Application", "opencart");
        extent.setSystemInfo("Module", "Admin");
        extent.setSystemInfo("Sub Module", "Customers");

        String os = testContext.getCurrentXmlTest().getParameter("os");
        extent.setSystemInfo("Operating System", os);

        String browser = testContext.getCurrentXmlTest().getParameter("browser");
        extent.setSystemInfo("Browser", browser);

        List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
        if (!includedGroups.isEmpty()) {
            extent.setSystemInfo("Groups", includedGroups.toString());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.PASS, result.getMethod().getMethodName() + " got successfully executed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        test = extent.createTest(result.getMethod().getMethodName());
        test.assignCategory(result.getMethod().getGroups());
        test.log(Status.FAIL, result.getMethod().getMethodName() + " got failed");
        test.log(Status.INFO, result.getThrowable());

        try {
            String path = new BaseClass().captureScreen(result.getName());
            test.addScreenCaptureFromPath(path);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test = extent.createTest(result.getName());
        test.log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext testContext) {

        extent.flush();

        String pathOfReport = System.getProperty("user.dir") + "\\reports\\" + repName;
        File reportFile = new File(pathOfReport);

        if (reportFile.exists()) {

            // Open report automatically
            try {
                Desktop.getDesktop().browse(reportFile.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // -------- EMAIL SENDING CODE (COMPLETE) ----------
         /*   try {

                URL url = new URL("file:///" + System.getProperty("user.dir") + "\\reports\\" + repName);

                ImageHtmlEmail email = new ImageHtmlEmail();
                email.setDataSourceResolver(new DataSourceUrlResolver(url));
                email.setHostName("smtp.googlemail.com");
                email.setSmtpPort(465);
                email.setAuthenticator(new DefaultAuthenticator("prernatiwari203@gmail.com", "password"));
                email.setSSLOnConnect(true);

                email.setFrom("pavanoltraining@gmail.com");
                email.setSubject("Test Results");
                email.setMsg("Please find attached report...");

                email.addTo("pavankumar.busya@gmail.com");

                email.attach(url, "Extent Report", "Please check the test report.");

                email.send();
            }
            catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }
}
