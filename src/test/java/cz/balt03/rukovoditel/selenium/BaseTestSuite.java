package cz.balt03.rukovoditel.selenium;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTestSuite {

    protected ChromeDriver driver;
    private String baseURL = "https://digitalnizena.cz/rukovoditel/";

    @Before
    public void init() {
        ChromeOptions cho = new ChromeOptions();
        driver = new ChromeDriver(cho);
        driver.manage().window().maximize();
        login();
    }

    @After
    public void tearDown() {
        driver.close();
    }


    public void login() {
        String username = "rukovoditel";
        String password = "vse456ru";
        driver.get(baseURL);

        // WHEN user fill in credentials and click ok

        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys(username);
        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys(password);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login_form\"]/div[3]/button"));
        loginButton.click();
    }
}
