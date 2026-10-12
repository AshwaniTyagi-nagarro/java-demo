package tests;

import base.BaseClass;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.ExtentReportManager;


public class UIValidationTests extends BaseClass {



  
    @Test(description = "Verify the Amazon home page title contains expected text")
    public void TC08_VerifyHomePageTitle() {
        ExtentReportManager.getTest().info("TC08: Verifying Amazon home page title");

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));

        String actualTitle = "";

        try {
            wait.until(ExpectedConditions.titleContains("Amazon"));
            actualTitle = getDriver().getTitle();
        } catch (Exception e) {
            actualTitle = getDriver().getTitle();
        }

        System.out.println("=== ACTUAL TITLE: [" + actualTitle + "] ===");
        ExtentReportManager.getTest().info("Actual page title: " + actualTitle);

        Assert.assertTrue(
            actualTitle.contains("Amazon"),
            "TC08 FAILED: Title does not contain 'Amazon'. Actual: [" + actualTitle + "]"
        );

        ExtentReportManager.getTest().pass("TC08 PASSED: Title = " + actualTitle);
    }



    @Test(description = "Verify clicking Sign In navigates to the login page")
    public void TC09_VerifySignInNavigation() {
        ExtentReportManager.getTest().info("TC09: Verifying Sign In navigation");

        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.clickSignIn();

        ExtentReportManager.getTest().info("Clicked Sign In, checking login page");

        boolean onLoginPage = loginPage.isOnSignInPage();
        String loginTitle = loginPage.getPageTitle();

        ExtentReportManager.getTest().info("On login page: " + onLoginPage);
        ExtentReportManager.getTest().info("Login page title: " + loginTitle);

        Assert.assertTrue(
            loginTitle.contains("Amazon Sign-In") || loginTitle.contains("Sign in") || onLoginPage,
            "TC09 FAILED: Did not navigate to Sign In page. Title was: " + loginTitle
        );

        ExtentReportManager.getTest().pass("TC09 PASSED: Successfully navigated to Sign In page");
    }

   

    @Test(description = "Verify invalid email shows an error on login page")
    public void TC10_InvalidEmailOnLoginPage() {
        ExtentReportManager.getTest().info("TC10: Testing invalid email on login page");

        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.clickSignIn();

        String invalidEmail = ConfigReader.getTestData("login.invalid.email");
        ExtentReportManager.getTest().info("Entering invalid email: " + invalidEmail);

        loginPage.enterEmailAndContinue(invalidEmail);

        boolean errorDisplayed = loginPage.isErrorDisplayed();
        String errorText = loginPage.getErrorMessageText();

        ExtentReportManager.getTest().info("Error displayed: " + errorDisplayed);
        ExtentReportManager.getTest().info("Error message: " + errorText);

        Assert.assertTrue(
            errorDisplayed,
            "TC10 FAILED: No error shown for invalid email: " + invalidEmail
        );

        ExtentReportManager.getTest().pass("TC10 PASSED: Error shown for invalid email");
    }



    @Test(description = "Verify clicking cart icon navigates to shopping cart page")
    public void TC11_CartPageNavigation() {
        ExtentReportManager.getTest().info("TC11: Testing cart page navigation");

        HomePage homePage = new HomePage(getDriver());
        CartPage cartPage = homePage.clickCart();

        String cartUrl = cartPage.getCurrentUrl();
        ExtentReportManager.getTest().info("Cart URL: " + cartUrl);

        Assert.assertTrue(
            cartUrl.contains("cart") || cartUrl.contains("Cart"),
            "TC11 FAILED: Not on cart page. URL: " + cartUrl
        );

        ExtentReportManager.getTest().pass("TC11 PASSED: Navigated to cart page. URL: " + cartUrl);
    }

    

    @Test(description = "INTENTIONAL FAIL: Verify page title equals completely wrong value")
    public void TC12_FAIL_WrongPageTitle() {
        ExtentReportManager.getTest().info("TC12 (INTENTIONAL FAIL): Asserting wrong page title");

        HomePage homePage = new HomePage(getDriver());
        String actualTitle = homePage.getPageTitle();

        ExtentReportManager.getTest().info("Actual title: " + actualTitle);

        Assert.assertEquals(
            actualTitle,
            "Google",
            "TC12 INTENTIONAL FAIL: Expected 'Google' but page title is: " + actualTitle
        );
    }
}