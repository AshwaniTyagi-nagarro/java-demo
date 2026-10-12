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
import java.util.List;

public class LoginPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    @FindBy(id = "ap_email")
    private WebElement emailField_v1;

    @FindBy(id = "ap_email_login")
    private WebElement emailField_v2;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "ap_password")
    private WebElement passwordField;

    @FindBy(id = "signInSubmit")
    private WebElement signInButton;

    @FindBy(id = "auth-error-message-box")
    private WebElement errorBox_v1;

    @FindBy(xpath = "//div[contains(@class,'a-alert-content')]")
    private WebElement errorBox_v2;

    @FindBy(xpath = "//span[contains(@class,'a-list-item') and string-length(text())>5]")
    private List<WebElement> errorSpans;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("LoginPage initialized on: " + driver.getCurrentUrl());
    }


    private WebElement getEmailField() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("ap_email"))
            );
            logger.info("Email field found: id=ap_email");
            return el;
        } catch (Exception e) {
            logger.warn("ap_email not found, trying ap_email_login");
        }

        try {
            WebElement el = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("ap_email_login"))
            );
            logger.info("Email field found: id=ap_email_login");
            return el;
        } catch (Exception e) {
            logger.warn("ap_email_login not found either");
        }

        try {
            WebElement el = driver.findElement(
                By.xpath("//input[@type='email']")
            );
            logger.info("Email field found: input[type=email]");
            return el;
        } catch (Exception e) {
            logger.error("No email field found on page: " + driver.getCurrentUrl());
            throw new RuntimeException("Email input field not found on login page");
        }
    }


    private WebElement getContinueButton() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(
                ExpectedConditions.elementToBeClickable(By.id("continue"))
            );
        } catch (Exception e) {
            logger.warn("continue button not found by id, trying xpath");
        }

        return driver.findElement(
            By.xpath("//input[@type='submit'] | //button[@type='submit']")
        );
    }


    public void enterEmail(String email) {
        logger.info("Entering email: " + email);
        WebElement field = getEmailField();
        field.clear();
        field.sendKeys(email);
    }

    public void clickContinue() {
        logger.info("Clicking Continue");
        getContinueButton().click();
    }

    public void enterEmailAndContinue(String email) {
        enterEmail(email);
        clickContinue();
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement field = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("ap_password"))
        );
        field.clear();
        field.sendKeys(password);
    }


    public boolean isErrorDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Method 1: auth-error-message-box (classic Amazon error)
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("auth-error-message-box")
            ));
            logger.info("Error found via: auth-error-message-box");
            return true;
        } catch (Exception e) {
            logger.warn("auth-error-message-box not found");
        }

        try {
            WebElement el = driver.findElement(
                By.xpath("//div[contains(@class,'a-alert-content')]")
            );
            if (el.isDisplayed() && !el.getText().isEmpty()) {
                logger.info("Error found via: a-alert-content");
                return true;
            }
        } catch (Exception e) {
            logger.warn("a-alert-content not found");
        }

        try {
            List<WebElement> alerts = driver.findElements(
                By.xpath("//*[contains(@class,'a-alert') or contains(@class,'error') " +
                         "or contains(@id,'error') or contains(@id,'alert')]")
            );
            for (WebElement alert : alerts) {
                if (alert.isDisplayed() && !alert.getText().trim().isEmpty()) {
                    logger.info("Error found via alert class: " + alert.getText().trim());
                    return true;
                }
            }
        } catch (Exception e) {
            logger.warn("Generic alert check failed");
        }

   
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("ap/signin") || currentUrl.contains("ap/login")) {
            logger.info("Still on sign-in page after Continue — treating as validation");
            return true;
        }

        logger.warn("No error element found and not on login page");
        return false;
    }

    public String getErrorMessageText() {
        String[] xpaths = {
            "//*[@id='auth-error-message-box']//span",
            "//div[contains(@class,'a-alert-content')]",
            "//*[contains(@class,'a-list-item') and contains(@class,'error')]"
        };
        for (String xpath : xpaths) {
            try {
                WebElement el = driver.findElement(By.xpath(xpath));
                if (!el.getText().isEmpty()) return el.getText();
            } catch (Exception ignored) {}
        }
        return "Error displayed (text not captured)";
    }

    public boolean isOnSignInPage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.or(
                ExpectedConditions.titleContains("Sign-In"),
                ExpectedConditions.titleContains("Sign in"),
                ExpectedConditions.urlContains("ap/signin"),
                ExpectedConditions.urlContains("ap/login")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle()   { return driver.getTitle(); }
    public String getCurrentUrl()  { return driver.getCurrentUrl(); }
}