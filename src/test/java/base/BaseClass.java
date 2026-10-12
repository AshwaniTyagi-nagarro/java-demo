package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.ExtentReportManager;
import utils.ScreenshotUtil;

import java.time.Duration;


public class BaseClass {

    
    protected static final Logger logger = LogManager.getLogger(BaseClass.class);

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    
    public static WebDriver getDriver() {
        return driver.get();
    }

  
    @BeforeSuite
    public void suiteSetup() {
        logger.info("===== TEST SUITE STARTED =====");
        ExtentReportManager.initReport();
    }

   
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browserName, ITestResult result) {

        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        logger.info("Setting up browser: " + browserName + " for test: " + testName);

        ExtentReportManager.startTest(testName, description);

        WebDriver webDriver = launchBrowser(browserName);

        driver.set(webDriver);

        getDriver().manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));

        getDriver().manage().window().maximize();

        String url = ConfigReader.getAppUrl();
        getDriver().get(url);
        logger.info("Opened URL: " + url);

        ExtentReportManager.getTest().info("Browser launched: " + browserName);
        ExtentReportManager.getTest().info("Navigated to: " + url);
    }

   
    private WebDriver launchBrowser(String browserName) {
        boolean isHeadless = ConfigReader.isHeadless();
        WebDriver webDriver;

        switch (browserName.toLowerCase().trim()) {

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                if (isHeadless) {
                    ffOptions.addArguments("--headless");
                    logger.info("Firefox running in HEADLESS mode");
                }
                webDriver = new FirefoxDriver(ffOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    edgeOptions.addArguments("--headless");
                    logger.info("Edge running in HEADLESS mode");
                }
                webDriver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new"); // New headless for Chrome
                    chromeOptions.addArguments("--window-size=1920,1080");
                    logger.info("Chrome running in HEADLESS mode");
                }
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                webDriver = new ChromeDriver(chromeOptions);
                break;
        }

        logger.info(browserName + " browser launched successfully");
        return webDriver;
    }

    
    @AfterMethod
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("TEST FAILED: " + testName);

            String errorMessage = result.getThrowable().getMessage();
            String safeError = errorMessage != null
                ? errorMessage.replaceAll("[^a-zA-Z0-9]", "_").substring(0, Math.min(30, errorMessage.length()))
                : "Unknown_Error";

            String screenshotPath = ScreenshotUtil.captureScreenshot(
                getDriver(), testName, safeError
            );

            ExtentReportManager.getTest().fail("TEST FAILED: " + errorMessage);
            if (screenshotPath != null) {
                try {
                    ExtentReportManager.getTest()
                        .addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                } catch (Exception e) {
                    logger.error("Could not attach screenshot to report: " + e.getMessage());
                }
            }

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("TEST PASSED: " + testName);
            ExtentReportManager.getTest().pass("TEST PASSED ✓");

        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.warn("TEST SKIPPED: " + testName);
            ExtentReportManager.getTest().skip("TEST SKIPPED");
        }

        if (getDriver() != null) {
            getDriver().quit();
            driver.remove(); 
            logger.info("Browser closed for test: " + testName);
        }
    }

   
    @AfterSuite
    public void suiteTearDown() {
        ExtentReportManager.flushReport();
        logger.info("===== TEST SUITE COMPLETED =====");
    }
}
