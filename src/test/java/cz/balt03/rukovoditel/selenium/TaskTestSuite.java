package cz.balt03.rukovoditel.selenium;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

public class TaskTestSuite {

    private ChromeDriver driver;
    private String baseURL = "https://digitalnizena.cz/rukovoditel/";

    @Before
    public void init() {
        ChromeOptions cho = new ChromeOptions();
        driver = new ChromeDriver(cho);
        driver.manage().window().maximize();
    }
    @After
    public void tearDown() {
        driver.close();
    }


    public void loginAndCreateProject() {
        String username = "rukovoditel";
        String password = "vse456ru";
        UUID uuid = UUID.randomUUID();
        String projectName = "balt03" + uuid;
        driver.get(baseURL);

        // WHEN user fill in credentials and click ok

        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys(username);
        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys(password);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login_form\"]/div[3]/button"));
        loginButton.click();

        // create project
        WebElement projects = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/ul/li[4]/a"));
        projects.click();
        //go to create project form
        driver.get("https://digitalnizena.cz/rukovoditel/index.php?module=items/items&path=21");

        // click create
        WebElement createProjectButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/button"));
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.visibilityOf(createProjectButton));
        createProjectButton.click();

        WebDriverWait wait2 = new WebDriverWait(driver, 5);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("fields_156")));

        Select priority = new Select(driver.findElement(By.id("fields_156")));
        priority.selectByVisibleText("Urgent");
        priority.selectByValue("35");

        Select status = new Select(driver.findElement(By.id("fields_157")));
        status.selectByVisibleText("New");
        status.selectByValue("37");

        WebElement projectNameInput = driver.findElement(By.cssSelector("#fields_158"));
        projectNameInput.sendKeys(projectName);

        Date cur_dt = new Date();
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String strTodaysDate = dateFormat.format(cur_dt);
        WebElement dateInput = driver.findElement(By.id("fields_159"));
        dateInput.sendKeys(strTodaysDate);

        // create project

        WebElement saveButton = driver.findElementByXPath("//*[@id=\"items_form\"]/div[2]/button[1]");
        saveButton.click();

        WebDriverWait projectNameWait = new WebDriverWait(driver, 1);
        projectNameWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[1]/div/ul/li[2]/a")));
    }

    @Test
    public void given_UserIsLoggedIn_and_projectIsCreated_when_taskIsCreatedWithSpecificParameters_then_taskParametersAreChecked_and_taskIsDeleted(){
        //GIVEN
        loginAndCreateProject();

        //THEN
        UUID uuid = UUID.randomUUID();
        String taskName = "Task name" + uuid;

        WebElement addTaskButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/button"));
        addTaskButton.click();
        WebDriverWait TaskFormWait = new WebDriverWait(driver, 5);
        TaskFormWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fields_167")));

        //fill data

        Select type = new Select(driver.findElement(By.id("fields_167")));
        type.selectByVisibleText("Task");
        type.selectByValue("42");

        WebElement TaskNameInput = driver.findElement(By.cssSelector("#fields_168"));
        TaskNameInput.sendKeys(taskName);

        Select statusTask = new Select(driver.findElement(By.id("fields_169")));
        statusTask.selectByVisibleText("New");
        statusTask.selectByValue("46");

        Select prio = new Select(driver.findElement(By.id("fields_170")));
        prio.selectByVisibleText("Medium");
        prio.selectByValue("55");

        driver.switchTo().frame(0);
        WebElement descriptionTask = driver.findElement(By.xpath("/html/body"));
        descriptionTask.sendKeys("Some description ...");
        driver.switchTo().defaultContent();

        WebElement saveButton = driver.findElementByXPath("/html/body/div[6]/div/div/form/div[2]/button[1]");
        saveButton.click();

        //THEN

        WebDriverWait taskNameWait = new WebDriverWait(driver, 1);
        taskNameWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table/tbody/tr/td[6]/a")));
        WebElement taskNameResult = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table/tbody/tr/td[6]/a"));
        Assert.assertEquals(taskName,taskNameResult.getText());

        WebElement taskDeleteButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table/tbody/tr/td[2]/a[1]/i"));
        taskDeleteButton.click();

        WebDriverWait taskDeleteWait = new WebDriverWait(driver, 2);
        taskDeleteWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete_item_form")));

        WebElement taskDeleteConfirmButton = driver.findElement(By.xpath("/html/body/div[6]/div/form/div[2]/button[1]"));
        taskDeleteConfirmButton.click();

        WebDriverWait waitForResults = new WebDriverWait(driver, 5);
        waitForResults.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table")));

        WebElement resultOfSearch = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table/tbody/tr/td"));

        Assert.assertEquals("No Records Found", resultOfSearch.getText());

    }



}
