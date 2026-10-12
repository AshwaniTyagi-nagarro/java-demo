package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.WaitUtil;


public class HomePage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(HomePage.class);


    @FindBy(id = "nav-logo-sprites")
    private WebElement amazonLogo;

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(id = "nav-search-submit-button")
    private WebElement searchButton;

    @FindBy(id = "nav-link-accountList")
    private WebElement signInLink;

    @FindBy(id = "nav-cart")
    private WebElement cartIcon;

    @FindBy(xpath = "//a[contains(text(),\"Today's Deals\")]")
    private WebElement todayDealsLink;

    @FindBy(id = "nav-orders")
    private WebElement returnsOrdersLink;


  
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("HomePage initialized");
    }


    public SearchResultsPage searchForProduct(String productName) {
        logger.info("Searching for: " + productName);
        WaitUtil.waitForVisibility(driver, searchBox);
        searchBox.clear();
        searchBox.sendKeys(productName);
        WaitUtil.waitForClickability(driver, searchButton);
        searchButton.click();
        logger.info("Search submitted for: " + productName);
        return new SearchResultsPage(driver); // Page transition
    }

    public LoginPage clickSignIn() {
        logger.info("Clicking Sign In link");
        WaitUtil.waitForClickability(driver, signInLink);
        signInLink.click();
        return new LoginPage(driver);
    }

    public CartPage clickCart() {
        logger.info("Clicking Cart icon");
        WaitUtil.waitForClickability(driver, cartIcon);
        cartIcon.click();
        return new CartPage(driver);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isLogoDisplayed() {
        try {
            return amazonLogo.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSearchBoxDisplayed() {
        try {
            WaitUtil.waitForVisibility(driver, searchBox);
            return searchBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getSearchBoxPlaceholder() {
        return searchBox.getAttribute("placeholder");
    }
}