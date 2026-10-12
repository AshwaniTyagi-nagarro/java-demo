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

public class SearchResultsPage {

    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(SearchResultsPage.class);

    
    @FindBy(xpath = "//div[@data-component-type='s-search-result']//h2//span")
    private List<WebElement> productTitlesByH2;

    @FindBy(xpath = "//div[contains(@class,'s-result-item')]//h2/a/span")
    private List<WebElement> productTitlesByResultItem;

    @FindBy(xpath = "//div[@data-index]//h2//span")
    private List<WebElement> productTitlesByDataIndex;

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBoxOnResultsPage;


    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("SearchResultsPage initialized");
    }

    
    private List<WebElement> getLoadedProductTitles() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@data-component-type='s-search-result']")
            ));
            logger.info("Results container found via data-component-type");

            if (!productTitlesByH2.isEmpty()) {
                logger.info("Using locator 1 (H2 inside s-search-result): " 
                    + productTitlesByH2.size() + " results");
                return productTitlesByH2;
            }
        } catch (Exception e) {
            logger.warn("Locator 1 failed: " + e.getMessage());
        }

        try {
            if (!productTitlesByResultItem.isEmpty()) {
                logger.info("Using locator 2 (s-result-item): " 
                    + productTitlesByResultItem.size() + " results");
                return productTitlesByResultItem;
            }
        } catch (Exception e) {
            logger.warn("Locator 2 failed: " + e.getMessage());
        }

        try {
            if (!productTitlesByDataIndex.isEmpty()) {
                logger.info("Using locator 3 (data-index): " 
                    + productTitlesByDataIndex.size() + " results");
                return productTitlesByDataIndex;
            }
        } catch (Exception e) {
            logger.warn("Locator 3 failed: " + e.getMessage());
        }

        logger.error("ALL locators failed — page may not have loaded correctly");
        return new java.util.ArrayList<>();
    }


    public boolean areResultsDisplayed() {
        List<WebElement> titles = getLoadedProductTitles();
        boolean result = !titles.isEmpty();
        logger.info("areResultsDisplayed: " + result + " (" + titles.size() + " items)");
        return result;
    }

    public int getResultsCount() {
        return getLoadedProductTitles().size();
    }

    public ProductPage clickFirstProduct() {
        List<WebElement> titles = getLoadedProductTitles();
        logger.info("Clicking first product");
        titles.get(0).click();
        return new ProductPage(driver);
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            List<WebElement> noResult = driver.findElements(
                By.xpath("//*[contains(text(),'No results for') " +
                         "or contains(text(),'no results for') " +
                         "or contains(text(),'did not match any products')]")
            );
            return !noResult.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getResultsCountText() {
        String[] xpaths = {
            "//span[@data-component-type='s-result-count']",
            "//div[contains(@class,'s-breadcrumb')]//span",
            "//span[contains(text(),'results for')]",
            "//span[contains(text(),'result for')]"
        };

        for (String xpath : xpaths) {
            try {
                WebElement el = driver.findElement(By.xpath(xpath));
                if (el.isDisplayed() && !el.getText().isEmpty()) {
                    logger.info("Results count text found: " + el.getText());
                    return el.getText();
                }
            } catch (Exception ignored) {}
        }
        return "Count not found";
    }

    public String getPageTitle()    { return driver.getTitle(); }
    public String getCurrentUrl()   { return driver.getCurrentUrl(); }

    public String getSearchBoxValue() {
        try { return searchBoxOnResultsPage.getAttribute("value"); }
        catch (Exception e) { return ""; }
    }
}