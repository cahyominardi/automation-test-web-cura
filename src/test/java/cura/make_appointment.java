package cura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;

public class make_appointment {

    WebDriver driver;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/mm/yyyy");
    LocalDateTime now = LocalDateTime.now();

    @BeforeTest
    private void init() {

        // Initiate browser
        System.setProperty("webdriver.chrome.driver", "C:/Chromedriver/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();

        // Go to CURA login page
        driver.navigate().to("https://katalon-demo-cura.herokuapp.com/profile.php#login");
        driver.manage().window().maximize();

        /*
        // Login to CURA web
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/#appointment");
         */
    }

    @Test (priority = 0)
    private void login_with_valid_account() {
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/#appointment");
    }

    @Test (priority = 1, dependsOnMethods = "login_with_valid_account")
    private void check_element() {

        // Check element h2
        Assert.assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "Make Appointment");

        // Check Facility field (dropdown field)
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"appointment\"]/div/div/form/div[1]/label")).getText(), "Facility");
        Select dropdown_facility = new Select(driver.findElement(By.id("combo_facility")));
        List<WebElement> dropdown_options = dropdown_facility.getOptions();
        Assert.assertEquals(dropdown_options.get(0).getAttribute("value"), "Tokyo CURA Healthcare Center");
        Assert.assertEquals(dropdown_options.get(1).getAttribute("value"), "Hongkong CURA Healthcare Center");
        Assert.assertEquals(dropdown_options.get(2).getAttribute("value"), "Seoul CURA Healthcare Center");

        // Check label for apply for hospital readmission checkbox field
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"appointment\"]/div/div/form/div[2]/div/label")).getText(), "Apply for hospital readmission");

        // Check Healthcare Program field label
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"appointment\"]/div/div/form/div[3]/label")).getText(), "Healthcare Program");

        // Check Visited Date field label
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"appointment\"]/div/div/form/div[4]/label")).getText(), "Visit Date (Required)");
        Assert.assertEquals(driver.findElement(By.id("txt_visit_date")).getAttribute("placeholder"), "dd/mm/yyyy");

        // Check Comment field
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"appointment\"]/div/div/form/div[5]/label")).getText(), "Comment");
        Assert.assertEquals(driver.findElement(By.id("txt_comment")).getAttribute("placeholder"), "Comment");
    }

    @Test (priority = 2, dependsOnMethods = "login_with_valid_account")
    private void create_appointment_with_nuil_values() {

        // Click Book Appointment
        driver.findElement(By.id("btn-book-appointment")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/#appointment");
        driver.findElement(By.id("txt_comment")).click();
    }

    @Test (priority = 3, dependsOnMethods = "login_with_valid_account")
    public void create_appointment() {

        // System.out.println(dtf.format(now));

        // input Facility
        Select dropdown_facility = new Select(driver.findElement(By.id("combo_facility")));
        dropdown_facility.selectByIndex(2);

        // Checkbox input (Apply for hospital readmission)
        driver.findElement(By.xpath("//*[@id=\"chk_hospotal_readmission\"]")).click();

        // Input Healthcare program
        driver.findElement(By.id("radio_program_medicaid")).click();

        // input Visited Date
        driver.findElement(By.xpath("//*[@id=\"appointment\"]/div/div/form/div[4]/div/div/div/span")).click();
        driver.findElement(By.xpath("/html/body/div/div[1]/table/tbody/tr[4]/td[3]")).click();

        // Input comment
        driver.findElement(By.id("txt_comment")).sendKeys("Test Comment 1234 ### AAA");

        // Click Book Appointment button
        driver.findElement(By.id("btn-book-appointment")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/appointment.php#summary");
    }

    @Test (priority = 4, dependsOnMethods = "create_appointment")
    public void check_appointment_summary() {

        // Check h2 element
        Assert.assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "Appointment Confirmation");

        // Check p element
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"summary\"]/div/div/div[1]/p")).getText(), "Please be informed that your appointment has been booked as following:");

        // Check Facility
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"summary\"]/div/div/div[2]/div[1]/label")).getText(), "Facility");
        Assert.assertEquals(driver.findElement(By.id("facility")).getText(), "Seoul CURA Healthcare Center");

        // Check Apply for hospital readmission
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"summary\"]/div/div/div[3]/div[1]/label")).getText(), "Apply for hospital readmission");
        Assert.assertEquals(driver.findElement(By.id("hospital_readmission")).getText(), "Yes");

        // Check Healthcare program
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"summary\"]/div/div/div[4]/div[1]/label")).getText(), "Healthcare Program");
        Assert.assertEquals(driver.findElement(By.id("program")).getText(), "Medicaid");

        // Check Visited Date
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"summary\"]/div/div/div[5]/div[1]/label")).getText(), "Visit Date");
        Assert.assertEquals(driver.findElement(By.id("visit_date")).getText(), "19/12/2023");

        // Check comment
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"summary\"]/div/div/div[6]/div[1]/label")).getText(), "Comment");
        Assert.assertEquals(driver.findElement(By.id("comment")).getText(), "Test Comment 1234 ### AAA");

        // User clicks Go to Homepage
        driver.findElement(By.className("btn-default")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/");
    }

    @AfterTest
    private void close_browser() {
        // Close the browser after test
        driver.quit();
    }
}
