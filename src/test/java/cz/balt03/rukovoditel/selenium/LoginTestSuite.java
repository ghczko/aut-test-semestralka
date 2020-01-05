package cz.balt03.rukovoditel.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import cz.balt03.rukovoditel.selenium.BaseTestSuite;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;


public class LoginTestSuite {
    private ChromeDriver driver;
    BaseTestSuite baseTestSuite = new BaseTestSuite();
    private String baseURL = baseTestSuite.getBaseURL();

    @Before
    public void init() {
        ChromeOptions cho = new ChromeOptions();
        driver = new ChromeDriver(cho);
        driver.manage().window().maximize();
        driver.get(baseURL);
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void given_userHasValidCredentials_when_userFillInCredentials_then_UserIsLoggedIn() throws InterruptedException {
        // GIVEN user has valid credentials

        String username = "rukovoditel";
        String password = "vse456ru";

        // WHEN user fill in credentials and click ok

       baseTestSuite.login(username,password);

        // THEN newly added deposit should be shown in deposits table grid

        WebElement profile = driver.findElement(By.xpath("/html/body/div[1]/div/ul/li[2]/a/span"));
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOf(profile));

        Assert.assertTrue(profile.getText().contains("System Administrator"));
    }

    @Test
    public void given_userHasInvalidCredentials_when_userFillInCredentials_then_UserIsNotLoggedIn() throws InterruptedException {
        // GIVEN user has valid credentials

        String username = "rukovoditell";
        String password = "vse456ru!";

        // WHEN user fill in credentials and click ok

        baseTestSuite.login(username,password);

        // THEN newly added deposit should be shown in deposits table grid

        WebElement profile = driver.findElement(By.xpath("/html/body/div[3]/div"));
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOf(profile));

        Assert.assertTrue(profile != null);

    }
    @Test
    public void given_userWasloggedIn_when_userClicksOnLogOut_then_UserIsLoggedOut() throws InterruptedException {
        // GIVEN user has valid credentials

        String username = "rukovoditel";
        String password = "vse456ru";

        baseTestSuite.login(username,password);

        // WHEN User clicks on log out
        driver.get("https://digitalnizena.cz/rukovoditel/index.php?module=users/login&action=logoff");

        // THEN User is logged out
        Assert.assertEquals(driver.getCurrentUrl(), "https://digitalnizena.cz/rukovoditel/index.php?module=users/login");
    }
}
