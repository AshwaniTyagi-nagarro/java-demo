package utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScreenshotUtil {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtil.class);

  
    public static String captureScreenshot(WebDriver driver, String testCaseName, String errorDesc) {

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String fileName = testCaseName + "_" + errorDesc + "_" + timestamp + ".png";

        String screenshotPath = ConfigReader.getScreenshotPath() + fileName;

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;

            File srcFile = ts.getScreenshotAs(OutputType.FILE);

            File destFile = new File(screenshotPath);

            destFile.getParentFile().mkdirs();

            FileUtils.copyFile(srcFile, destFile);

            logger.info("Screenshot saved: " + screenshotPath);
            return screenshotPath;

        } catch (IOException e) {
            logger.error("Screenshot capture failed: " + e.getMessage());
            return null;
        }
    }
}