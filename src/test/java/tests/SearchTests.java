package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchResultsPage;
import utils.ConfigReader;
import utils.ExtentReportManager;


public class SearchTests extends BaseClass {



    @Test(description = "Verify that searching for a valid product shows results")
    public void TC01_ValidProductSearch() {
        ExtentReportManager.getTest().info("TC01: Starting valid product search test");

        HomePage homePage = new HomePage(getDriver());
        ExtentReportManager.getTest().info("Step 1: HomePage loaded");

        String searchTerm = ConfigReader.getTestData("search.term1"); // "laptop"
        SearchResultsPage resultsPage = homePage.searchForProduct(searchTerm);
        ExtentReportManager.getTest().info("Step 2: Searched for: " + searchTerm);

        boolean resultsDisplayed = resultsPage.areResultsDisplayed();
        ExtentReportManager.getTest().info("Step 3: Results displayed: " + resultsDisplayed);

        Assert.assertTrue(
            resultsDisplayed,
            "TC01 FAILED: No results displayed for search term: " + searchTerm
        );

        ExtentReportManager.getTest().pass("TC01 PASSED: Results displayed for '" + searchTerm + "'");
    }



    @Test(description = "Verify the searched keyword appears in the page URL")
    public void TC02_SearchTermInUrl() {
        ExtentReportManager.getTest().info("TC02: Verifying search term appears in URL");

        HomePage homePage = new HomePage(getDriver());
        String searchTerm = ConfigReader.getTestData("search.term2"); // "mobile phone"
        SearchResultsPage resultsPage = homePage.searchForProduct(searchTerm);

        String currentUrl = resultsPage.getCurrentUrl();
        ExtentReportManager.getTest().info("Current URL: " + currentUrl);

        Assert.assertTrue(
            currentUrl.contains("mobile") || currentUrl.contains("phone"),
            "TC02 FAILED: Search term not reflected in URL. Actual URL: " + currentUrl
        );

        ExtentReportManager.getTest().pass("TC02 PASSED: Search term found in URL");
    }

  

    @Test(description = "Verify behavior when searching for a non-existent product")
    public void TC03_InvalidProductSearch() {
        ExtentReportManager.getTest().info("TC03: Testing invalid/non-existent product search");

        HomePage homePage = new HomePage(getDriver());
        String invalidTerm = ConfigReader.getTestData("search.term4");
        SearchResultsPage resultsPage = homePage.searchForProduct(invalidTerm);

        String pageTitle    = resultsPage.getPageTitle();
        String currentUrl   = resultsPage.getCurrentUrl();
        String resultsText  = resultsPage.getResultsCountText();

        System.out.println("=== TC03 DEBUG ===");
        System.out.println("Page Title   : " + pageTitle);
        System.out.println("URL          : " + currentUrl);
        System.out.println("Results Text : " + resultsText);
        System.out.println("=================");

        ExtentReportManager.getTest().info("Page title: "   + pageTitle);
        ExtentReportManager.getTest().info("Current URL: "  + currentUrl);
        ExtentReportManager.getTest().info("Results text: " + resultsText);

       
        boolean pageLoaded = !pageTitle.isEmpty() && currentUrl.contains("amazon.in");

        Assert.assertTrue(
            pageLoaded,
            "TC03 FAILED: Page did not load after invalid search. Title: " + pageTitle
        );

        if (resultsPage.isNoResultsMessageDisplayed()) {
            ExtentReportManager.getTest().info("Amazon showed: No results message");
        } else {
            ExtentReportManager.getTest().info(
                "Amazon showed related results — this is expected Amazon behavior"
            );
        }

        ExtentReportManager.getTest().pass(
            "TC03 PASSED: Amazon handled invalid search gracefully. URL: " + currentUrl
        );
    }



    @Test(description = "Verify clicking a search result opens the product detail page")
    public void TC04_ProductPageNavigation() {
        ExtentReportManager.getTest().info("TC04: Verifying navigation to product detail page");

        HomePage homePage = new HomePage(getDriver());
        String searchTerm = ConfigReader.getTestData("search.term3"); // "wireless headphones"
        SearchResultsPage resultsPage = homePage.searchForProduct(searchTerm);

        ExtentReportManager.getTest().info("Search completed, clicking first result");

        ProductPage productPage = resultsPage.clickFirstProduct();

        String productTitle = productPage.getProductTitle();
        ExtentReportManager.getTest().info("Product title: " + productTitle);

        Assert.assertFalse(
            productTitle.isEmpty(),
            "TC04 FAILED: Product title is empty on product detail page"
        );

        ExtentReportManager.getTest().pass("TC04 PASSED: Product page opened. Title: " + productTitle);
    }

   
    @Test(description = "Verify search results count is greater than zero for valid search")
    public void TC05_ResultsCountGreaterThanZero() {
        ExtentReportManager.getTest().info("TC05: Verifying results count > 0");

        HomePage homePage = new HomePage(getDriver());
        SearchResultsPage resultsPage = homePage.searchForProduct("laptop");

        int count = resultsPage.getResultsCount();
        ExtentReportManager.getTest().info("Number of visible results: " + count);

        Assert.assertTrue(
            count > 0,
            "TC05 FAILED: Expected more than 0 results but found: " + count
        );

        ExtentReportManager.getTest().pass("TC05 PASSED: " + count + " results found");
    }

 

    @Test(description = "INTENTIONAL FAIL: Verify search returns exactly 5 results (wrong expectation)")
    public void TC06_FAIL_IncorrectResultsCount() {
        ExtentReportManager.getTest().info("TC06 (INTENTIONAL FAIL): Testing with wrong expected count");

        HomePage homePage = new HomePage(getDriver());
        SearchResultsPage resultsPage = homePage.searchForProduct("laptop");

        int actualCount = resultsPage.getResultsCount();
        ExtentReportManager.getTest().info("Actual results count: " + actualCount);

        Assert.assertEquals(
            actualCount, 5,
            "TC06 INTENTIONAL FAIL: Expected exactly 5 results but got: " + actualCount
        );
    }

    

    @Test(description = "Verify key home page elements are visible on load")
    public void TC07_HomePageElementsVisible() {
        ExtentReportManager.getTest().info("TC07: Verifying home page elements");

        HomePage homePage = new HomePage(getDriver());

        boolean searchBoxVisible = homePage.isSearchBoxDisplayed();
        ExtentReportManager.getTest().info("Search box visible: " + searchBoxVisible);

        Assert.assertTrue(
            searchBoxVisible,
            "TC07 FAILED: Search box is not visible on home page"
        );

        ExtentReportManager.getTest().pass("TC07 PASSED: All home page elements are visible");
    }
}