package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    private static Properties configProperties;
    private static Properties testDataProperties;

    static {
        loadConfigProperties();
        loadTestDataProperties();
    }

    
    private static void loadConfigProperties() {
        try {
            // FileInputStream opens the file at the given path
            FileInputStream fis = new FileInputStream(
                "src/test/resources/config.properties"
            );
            configProperties = new Properties();
            configProperties.load(fis); // Reads all key=value pairs
            fis.close();
            logger.info("config.properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load config.properties: " + e.getMessage());
            throw new RuntimeException("config.properties not found!", e);
        }
    }

   
    private static void loadTestDataProperties() {
        try {
            FileInputStream fis = new FileInputStream(
                "src/test/resources/testdata.properties"
            );
            testDataProperties = new Properties();
            testDataProperties.load(fis);
            fis.close();
            logger.info("testdata.properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load testdata.properties: " + e.getMessage());
            throw new RuntimeException("testdata.properties not found!", e);
        }
    }


    public static String getAppUrl() {
        return configProperties.getProperty("app.url");
    }

    public static String getBrowser() {
        return configProperties.getProperty("browser", "chrome");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(
            configProperties.getProperty("implicit.wait", "10")
        );
    }

    public static int getExplicitWait() {
        return Integer.parseInt(
            configProperties.getProperty("explicit.wait", "15")
        );
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(
            configProperties.getProperty("headless", "false")
        );
    }

    public static String getReportPath() {
        return configProperties.getProperty("report.path", "reports/ExtentReport.html");
    }

    public static String getScreenshotPath() {
        return configProperties.getProperty("screenshot.path", "screenshots/");
    }


    public static String getTestData(String key) {
        String value = testDataProperties.getProperty(key);
        if (value == null) {
            logger.warn("Test data key not found: " + key);
        }
        return value;
    }
}