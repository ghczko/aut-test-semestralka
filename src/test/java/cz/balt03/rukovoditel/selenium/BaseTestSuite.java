package cz.balt03.rukovoditel.selenium;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BaseTestSuite {

    protected ChromeDriver driver;
    private String baseURL = "https://digitalnizena.cz/rukovoditel/";
    UUID uuid = UUID.randomUUID();
    String projectName = "balt03" + uuid;

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
        WebDriverWait wait1 = new WebDriverWait(driver, 5);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/h3")));
    }

    public void goToProjectForm() {

        WebElement projects = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/ul/li[4]/a/span"));
        projects.click();
        WebDriverWait wait1 = new WebDriverWait(driver, 5);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/button")));
        //go to create project form
        //driver.get("https://digitalnizena.cz/rukovoditel/index.php?module=items/items&path=21");

        // click create
        WebElement createProjectButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/button"));
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOf(createProjectButton));
        createProjectButton.click();

        WebDriverWait wait2 = new WebDriverWait(driver, 5);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("fields_156")));

    }
    public void createProject(String priorityValue, String statusValue){
        goToProjectForm();

        //Priority
        Select priority = new Select(driver.findElement(By.id("fields_156")));
        priority.selectByVisibleText("Urgent");
        priority.selectByValue(priorityValue);

        //Status
        Select status = new Select(driver.findElement(By.id("fields_157")));
        status.selectByVisibleText("New");
        status.selectByValue(statusValue);

        //Project name
        WebElement projectNameInput = driver.findElement(By.cssSelector("#fields_158"));
        projectNameInput.sendKeys(projectName);

        //Date
        Date cur_dt = new Date();
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String strTodaysDate = dateFormat.format(cur_dt);
        WebElement dateInput = driver.findElement(By.id("fields_159"));
        dateInput.sendKeys(strTodaysDate);

        WebElement saveButton = driver.findElementByXPath("//*[@id=\"items_form\"]/div[2]/button[1]");
        saveButton.click();

        WebDriverWait projectNameWait = new WebDriverWait(driver, 1);
        projectNameWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[1]/div/ul/li[2]/a")));

    }
}
