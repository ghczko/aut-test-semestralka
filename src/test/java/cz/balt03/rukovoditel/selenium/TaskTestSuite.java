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

import java.lang.reflect.Array;
import java.util.*;


public class TaskTestSuite extends BaseTestSuite {

    public void createTask(String taskNameValue, String typeValue, String statusValue, String priorityValue, String descriptionValue){
        WebElement addTaskButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/button"));
        addTaskButton.click();
        WebDriverWait TaskFormWait = new WebDriverWait(driver, 5);
        TaskFormWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fields_167")));

        Select type = new Select(driver.findElement(By.id("fields_167")));
        type.selectByVisibleText("Task");
        type.selectByValue(typeValue);

        WebElement TaskNameInput = driver.findElement(By.cssSelector("#fields_168"));
        TaskNameInput.sendKeys(taskNameValue);

        Select statusTask = new Select(driver.findElement(By.id("fields_169")));
        statusTask.selectByVisibleText("New");
        statusTask.selectByValue(statusValue);

        Select priority = new Select(driver.findElement(By.id("fields_170")));
        priority.selectByVisibleText("Medium");
        priority.selectByValue(priorityValue);

        driver.switchTo().frame(0);
        WebElement descriptionTask = driver.findElement(By.xpath("/html/body"));
        descriptionTask.sendKeys(descriptionValue);
        driver.switchTo().defaultContent();

        WebElement saveButton = driver.findElementByXPath("/html/body/div[6]/div/div/form/div[2]/button[1]");
        saveButton.click();
        WebDriverWait taskNameWait = new WebDriverWait(driver, 1);
        taskNameWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table/tbody/tr/td[6]/a")));

    }

    public void filtrationResultTest(List<String> statusForCompare,List<String> statusForCheck) {
        WebDriverWait tableResultWait = new WebDriverWait(driver, 1);
        tableResultWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"slimScroll\"]/table/tbody/tr")));
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id=\"slimScroll\"]/table/tbody/tr"));

        System.out.println(rows.size());
        for (int i=1; i<=rows.size();i++) {
            WebElement statusValue = driver.findElement(By.xpath("//*[@id=\"slimScroll\"]/table/tbody/tr[" + i + "]/td[7]/div"));
            String statusText = statusValue.getText();
            System.out.println(statusText);
            statusForCheck.add(statusText);
        }
        ////*[@id=\"slimScroll\"]/table/tbody/tr[" + i + "]/td[7]/div
        ///html/body/div[3]/div[2]/div/div/div[2]/div/div[6]/div/div/div[1]/div/table/tbody/tr[1]/td[7]/div
        System.out.println(statusForCheck);
        System.out.println(statusForCompare);
        Assert.assertEquals(statusForCheck,statusForCompare);
    }

    @Test
    public void given_UserIsLoggedIn_and_projectIsCreated_when_taskIsCreatedWithSpecificParameters_then_taskParametersAreChecked_and_taskIsDeleted(){

        //GIVEN
        createProject("35", "37");

        //WHEN
        String taskName = "Task name" + uuid;
        createTask(taskName,"42","46", "55", "Some description ...");

        //THEN

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

    @Test
    public void giver_UserIsLoggedIn_and_projectExists_when_userCreateSevenDifferentTasks_then_onlyTaskWhichShouldBeShownAreShown() {
        //GIVEN
        createProject("35", "37");

        //WHEN
            //create tasks
                String[] statusValues = {"46","47","48","49","50","51","52"};
                String taskName = "Task name" + uuid;

                for (int i = 0; i < statusValues.length; i++){
                    createTask(taskName, "42", statusValues[i], "55", "Some description ...");
                    taskName = taskName + "1";
                }
            //verify filters

                List<String> statusForCompare1 = new ArrayList<String>(Arrays.asList("New", "Open", "Waiting"));
                List<String> statusForCheck1 = new ArrayList<String>();

                filtrationResultTest(statusForCompare1, statusForCheck1);

            // change filter only for New and Waiting

                WebElement openFiltration = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[2]/div[2]/ul/li[2]/div"));
                openFiltration.click();

                WebDriverWait waitForFiltration = new WebDriverWait(driver, 5);
                waitForFiltration.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[6]/div/div/h4")));

                WebElement openLabelCross = driver.findElement(By.xpath("/html/body/div[6]/div/form/div[1]/div/div[3]/div[2]/div/div/ul/li[2]/a"));
                openLabelCross.click();

                WebElement saveFiltrationButton = driver.findElement(By.xpath("/html/body/div[6]/div/form/div[2]/button[1]"));
                saveFiltrationButton.click();

                WebDriverWait waitForResults = new WebDriverWait(driver, 5);
                waitForResults.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table")));

            // verify filters
                List<String> statusForCompare2 = new ArrayList<String>(Arrays.asList("New", "Waiting"));
                List<String> statusForCheck2 = new ArrayList<String>();
                filtrationResultTest(statusForCompare2, statusForCheck2);

            //change filter for all
                WebElement cancelFiltration = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[2]/div[2]/ul/li[1]/a[1]"));
                cancelFiltration.click();
                WebDriverWait waitForResults2 = new WebDriverWait(driver, 5);
                waitForResults2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[1]")));

            //verify filter

                List<String> statusForCompare3 = new ArrayList<String>(Arrays.asList("New","Open","Waiting","Done","Closed","Paid","Canceled"));
                List<String> statusForCheck3 = new ArrayList<String>();
                filtrationResultTest(statusForCompare3, statusForCheck3);

            // delete tasks

                WebElement selectAllTasks = driver.findElement(By.id("uniform-select_all_items"));
                selectAllTasks.click();

                //nefunguje
                WebElement taskOptions = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/div/ul/li[1]/a"));
                taskOptions.click();

                WebElement deleteTasksOptions = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[4]/div[1]/div/div/ul/li[2]/a"));
                deleteTasksOptions.click();

                WebDriverWait waitForDeleteModal = new WebDriverWait(driver, 5);
                waitForDeleteModal.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[6]/div/div/h4")));
                WebElement confirmDeleteTasks = driver.findElement(By.xpath("/html/body/div[6]/div/form/div[2]/button[1]"));
                confirmDeleteTasks.click();


                WebDriverWait waitForResults3 = new WebDriverWait(driver, 5);
                waitForResults3.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[1]")));
                WebElement resultOfSearch = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div/div[2]/div/div[5]/div/div/div[1]/div/table/tbody/tr/td"));
                Assert.assertEquals("No Records Found", resultOfSearch.getText());

    }
}
