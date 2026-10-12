package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Duration;


public class WaitUtil {

    private static final Logger logger = LogManager.getLogger(WaitUtil.class);

   
    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        logger.info("Waiting for element visibility: " + element);
        WebDriverWait wait = new WebDriverWait(
            driver, 
            Duration.ofSeconds(ConfigReader.getExplicitWait())
        );
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

   
    public static WebElement waitForClickability(WebDriver driver, WebElement element) {
        logger.info("Waiting for element to be clickable");
        WebDriverWait wait = new WebDriverWait(
            driver, 
            Duration.ofSeconds(ConfigReader.getExplicitWait())
        );
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    
    public static boolean waitForTitleContains(WebDriver driver, String titleText) {
        logger.info("Waiting for title to contain: " + titleText);
        WebDriverWait wait = new WebDriverWait(
            driver, 
            Duration.ofSeconds(ConfigReader.getExplicitWait())
        );
        return wait.until(ExpectedConditions.titleContains(titleText));
    }

    public static boolean waitForTextPresent(WebDriver driver, WebElement element, String text) {
        logger.info("Waiting for text: " + text);
        WebDriverWait wait = new WebDriverWait(
            driver, 
            Duration.ofSeconds(ConfigReader.getExplicitWait())
        );
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }
}