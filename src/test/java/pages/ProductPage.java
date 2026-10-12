package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.ArrayList;

public class ProductPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(ProductPage.class);

    @FindBy(id = "productTitle")
    private WebElement productTitle;

    @FindBy(xpath = "//span[@class='a-price-whole']")
    private WebElement productPrice;

    @FindBy(id = "add-to-cart-button")
    private WebElement addToCartButton;

    @FindBy(id = "buy-now-button")
    private WebElement buyNowButton;

    @FindBy(id = "availability")
    private WebElement availabilityStatus;

    public ProductPage(WebDriver driver) {
        this.driver = driver;

        switchToNewTabIfOpened();

        PageFactory.initElements(driver, this);
        logger.info("ProductPage initialized on URL: " + driver.getCurrentUrl());
    }

   
    private void switchToNewTabIfOpened() {
        try {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            if (tabs.size() > 1) {
                logger.info("New tab detected — switching from tab 0 to tab 1");
                driver.switchTo().window(tabs.get(1));
            } else {
                logger.info("Single tab — no switch needed");
            }
        } catch (Exception e) {
            logger.warn("Tab switch check failed: " + e.getMessage());
        }
    }

  
    public String getProductTitle() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        try {
            WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("productTitle"))
            );
            String title = el.getText().trim();
            logger.info("Product title found (id): " + title);
            return title;
        } catch (Exception e) {
            logger.warn("productTitle by id not found, trying fallback");
        }

        try {
            WebElement el = driver.findElement(
                By.xpath("//h1[@id='title']//span")
            );
            String title = el.getText().trim();
            logger.info("Product title found (h1#title): " + title);
            return title;
        } catch (Exception e) {
            logger.warn("Fallback title locator 2 failed");
        }

        try {
            WebElement el = driver.findElement(By.xpath("//h1//span[@class]"));
            String title = el.getText().trim();
            logger.info("Product title found (h1 span): " + title);
            return title;
        } catch (Exception e) {
            logger.warn("All product title locators failed");
        }

        return "";
    }

    public String getProductPrice() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[@class='a-price-whole']")
                )
            );
            return el.getText().trim();
        } catch (Exception e) {
            logger.warn("Price not found: " + e.getMessage());
            return "Price not available";
        }
    }

    public boolean isAddToCartDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-button"))
            );
            return el.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddToCart() {
        logger.info("Clicking Add to Cart");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement btn = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button"))
        );
        btn.click();
    }

    public String getAvailabilityText() {
        try {
            return availabilityStatus.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getPageTitle() { return driver.getTitle(); }

    public String getRating() {
        try {
            WebElement el = driver.findElement(
                By.xpath("//span[@data-hook='rating-out-of-text']")
            );
            return el.getText();
        } catch (Exception e) {
            return "Rating not found";
        }
    }
}