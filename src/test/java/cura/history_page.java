package cura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class history_page {

    private make_appointment make_appointment = new make_appointment();

    WebDriver driver;

    @BeforeTest
    private void init(){

        // Initiate browser
        System.setProperty("webdriver.chrome.driver", "C:/Chromedriver/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();

        // Go to CURA homepage
        driver.navigate().to("https://katalon-demo-cura.herokuapp.com/profile.php#login");
        driver.manage().window().maximize();
    }

    @Test (priority = 0)
    private void login_with_valid_account() {
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/#appointment");
    }

    @Test (priority = 1, dependsOnMethods = "login_with_valid_account")
    private void click_toggle_menu() {
        driver.findElement(By.id("menu-toggle")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[2]/a")).getText(), "Home");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[3]/a")).getText(), "History");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[4]/a")).getText(), "Profile");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[5]/a")).getText(), "Logout");
    }

    @Test (priority = 2, dependsOnMethods = "click_toggle_menu")
    private void click_history_menu() {

        // User clicks history menu on the side bar menu
        driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[3]/a")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/history.php#history");
    }

    // Verify the history menu is empty
    @Test (priority = 3, dependsOnMethods = "click_history_menu")
    private void check_element_no_appointment() {

        // Check h2 element
        Assert.assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "History");

        // If the user doesn't have appointment
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"history\"]/div/div[1]/div/p")).getText(), "No appointment.");

        // Then Go to homepage
        driver.findElement(By.className("btn-default")).click();
        Assert.assertEquals(driver.getCurrentUrl(),"https://katalon-demo-cura.herokuapp.com/");
    }

    @Test (priority = 4, dependsOnMethods = "check_element_no_appointment")
    private void create_appointment() {

        // make_appointment.create_appointment();

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

    @Test (priority = 5, dependsOnMethods = "create_appointment")
    private void check_appointment_summary() {

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


    @Test (priority = 6, dependsOnMethods = "check_appointment_summary")
    private void check_element_history_page() {

        // Click toggle menu (call another method in the same class)
        click_toggle_menu();

        // Click history menu (call another method in the same class)
        click_history_menu();

        // Check h2 element
        Assert.assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "History");

        // Check visited date
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"history\"]/div/div[2]/div/div/div[1]")).getText(), "19/12/2023");

        // Check Facility (label and value)
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"history\"]/div/div[2]/div/div/div[2]/div[1]/label")).getText(), "Facility");
        Assert.assertEquals(driver.findElement(By.id("facility")).getText(), "Seoul CURA Healthcare Center");

        // Check Apply for hospital readmission
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"history\"]/div/div[2]/div/div/div[2]/div[4]/label")).getText(), "Apply for hospital readmission");
        Assert.assertEquals(driver.findElement(By.id("hospital_readmission")).getText(), "Yes");

        // Check Healthcare program
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"history\"]/div/div[2]/div/div/div[2]/div[7]/label")).getText(), "Healthcare Program");
        Assert.assertEquals(driver.findElement(By.id("program")).getText(), "Medicaid");

        // Check Comment
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"history\"]/div/div[2]/div/div/div[2]/div[10]/label")).getText(), "Comment");
        Assert.assertEquals(driver.findElement(By.id("comment")).getText(), "Test Comment 1234 ### AAA");

        // Then Go to homepage
        driver.findElement(By.className("btn-default")).click();
        Assert.assertEquals(driver.getCurrentUrl(),"https://katalon-demo-cura.herokuapp.com/");
    }


    @AfterTest
    private void close_browser() {
        // Close the browser after test
        driver.quit();
    }

}
