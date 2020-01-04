package cz.balt03.rukovoditel.selenium;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.UUID;

import org.openqa.selenium.support.ui.WebDriverWait;

public class ProjectTestSuite extends BaseTestSuite {


    @Test
    public void givenUserIsLoggedIn_when_userWantsToCreateProjectWithOutName_then_projectIsNotCreated() throws InterruptedException {
        //GIVEN
            // in the BaseTestSuite
        //WHEN

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

        //GIVEN
            // in the BaseTestSuite
        //WHEN
            //create project

            createProject("35", "37");

        //THEN

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