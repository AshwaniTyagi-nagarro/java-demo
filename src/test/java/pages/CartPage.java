package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WaitUtil;


public class CartPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(CartPage.class);


  
    @FindBy(xpath = "//h1[contains(text(),'Shopping Cart')]")
    private WebElement cartHeading;

    @FindBy(xpath = "//h2[contains(text(),'Your Amazon Cart is empty')]")
    private WebElement emptyCartMessage;

    @FindBy(id = "sc-subtotal-amount-activecart")
    private WebElement subtotalAmount;

    @FindBy(xpath = "//input[@name='proceedToRetailCheckout']")
    private WebElement proceedToBuyButton;


    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("CartPage initialized");
    }


    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isOnCartPage() {
        try {
            WaitUtil.waitForVisibility(driver, cartHeading);
            return cartHeading.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCartEmpty() {
        try {
            return emptyCartMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}