package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);

    private static ExtentReports extent;

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    
    public static void initReport() {
        new File("reports").mkdirs();

        String reportPath = ConfigReader.getReportPath();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        sparkReporter.config().setDocumentTitle("Amazon Automation Test Report");
        sparkReporter.config().setReportName("Selenium Test Results");
        sparkReporter.config().setTheme(Theme.DARK); 

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Application", "Amazon.in");
        extent.setSystemInfo("Environment", "Production");
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());
        extent.setSystemInfo("Tester", "Automation QA");

        logger.info("ExtentReport initialized at: " + reportPath);
    }

    
    public static ExtentTest startTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test); 
        logger.info("Started test in report: " + testName);
        return test;
    }

    
    public static ExtentTest getTest() {
        return extentTest.get();
    }

   
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReport flushed/saved successfully");
        }
    }
}