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
import java.util.UUID;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
//import LoginTestSuite.*;

public class ProjectTestSuite {
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


    public void goToProjectForm() {

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

    }

    @Test
    public void givenUserIsLoggedIn_when_userWantsToCreateProjectWithOutName_then_projectIsNotCreated() throws InterruptedException {
        //GIVEN
        login();
        //WHEN
        //go to projects
        WebElement projects = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/ul/li[4]/a"));
        projects.click();
        //go to create project form
//        driver.get("https://digitalnizena.cz/rukovoditel/index.php?module=items/items&path=21");
//
//        // click create
//        WebElement createProjectButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/button"));
//        WebDriverWait wait = new WebDriverWait(driver, 1);
//        wait.until(ExpectedConditions.visibilityOf(createProjectButton));
//        createProjectButton.click();
//
//        WebDriverWait wait2 = new WebDriverWait(driver, 5);
//        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("fields_156")));
        goToProjectForm();

        WebElement saveButton = driver.findElementByXPath("//*[@id=\"items_form\"]/div[2]/button[1]");
        saveButton.click();

        //THEN

        WebDriverWait errorLabel = new WebDriverWait(driver, 1);
        errorLabel.until(ExpectedConditions.visibilityOfElementLocated(By.id("form-error-container")));
        //WebElement errorLabel = driver.findElement(By.id("form-error-container"));
        System.out.println(errorLabel);
        //workaround

        WebElement errorLabelResult = driver.findElement(By.xpath("/html/body/div[6]/div/div/form/div[2]/div[1]/div"));


        Assert.assertEquals("Some fields are required. They have been highlighted above.", errorLabelResult.getText());
        Assert.assertEquals(driver.getCurrentUrl(), "https://digitalnizena.cz/rukovoditel/index.php?module=items/items&path=21");
    }


    @Test
    public void givenUserIsLoggedIn_when_userWantsToCreateProjectWithSpecificParameters_then_projectIsCreated_and_deleted() throws InterruptedException {

        UUID uuid = UUID.randomUUID();
        String projectName = "balt03" + uuid;

        //GIVEN
        login();
        //WHEN
            //go to projects
            WebElement projects = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/div/ul/li[4]/a"));
            projects.click();

            //go to create project form
            goToProjectForm();
            // fill data

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


        //THEN

            WebDriverWait projectNameWait = new WebDriverWait(driver, 1);
            projectNameWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[1]/div/ul/li[2]/a")));

            WebElement projectNameResult = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[1]/div/ul/li[2]/a"));
            Assert.assertEquals(projectName, projectNameResult.getText());

            //deleting ...

            WebElement projectInfo = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[1]/div/div/div[1]/a"));
            projectInfo.click();

            WebDriverWait waitForDropdown = new WebDriverWait(driver, 5);
            waitForDropdown.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[2]/div[1]/div[1]/div[2]/div[1]/ul/li[2]/div")));

            WebElement clickOnDropdown = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[2]/div[1]/div[1]/div[2]/div[1]/ul/li[2]/div"));
            clickOnDropdown.click();

            WebElement clickOnDelete = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[2]/div[1]/div[1]/div[2]/div[1]/ul/li[2]/div/ul/li[2]/a"));
            clickOnDelete.click();

            WebDriverWait waitForModal = new WebDriverWait(driver, 2);
            waitForModal.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete_item_form")));

            WebElement clickOnConfirmDelete = driver.findElement(By.id("delete_confirm"));
            clickOnConfirmDelete.click();

            WebElement clickOnDeleteFinal = driver.findElement(By.xpath("/html/body/div[6]/div/form/div[2]/button[1]"));
            clickOnDeleteFinal.click();

            // check if project is deleted
            WebElement clickOnSearchField = driver.findElement(By.id("entity_items_listing66_21_search_keywords"));
            clickOnSearchField.sendKeys(projectName);
            WebElement clickOnSearchButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[3]/div/form/div/span/button/i"));
            clickOnSearchButton.click();


                //wait for result
                WebDriverWait waitForResults = new WebDriverWait(driver, 2);
                waitForResults.until(ExpectedConditions.visibilityOfElementLocated(By.id("entity_items_listing66_21")));

                WebElement resultOfSearch = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[2]/div/table/tbody/tr/td"));

           Assert.assertEquals("No Records Found", resultOfSearch.getText());






    }

}