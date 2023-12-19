package cura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;

public class cura_login_page {

    WebDriver driver;

    @BeforeTest
    private void init() {

        // Initiate browser
        System.setProperty("webdriver.chrome.driver", "C:/Chromedriver/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();

        // Go to CURA Login Page
        driver.navigate().to("https://katalon-demo-cura.herokuapp.com/profile.php#login");
        driver.manage().window().maximize();
    }

    @Test (priority = 0)
    private void check_element() {

        // Check element h2
        Assert.assertEquals(driver.findElement(By.cssSelector("h2")).getText(), "Login");

        // Check element p
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login\"]/div/div/div[1]/p")).getText(), "Please login to make appointment.");

        // Check label username & password
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login\"]/div/div/div[2]/form/div[2]/label")).getText(), "Username");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login\"]/div/div/div[2]/form/div[3]/label")).getText(), "Password");

        // Check placeholder username & password
        Assert.assertEquals(driver.findElement(By.id("txt-username")).getAttribute("placeholder"), "Username");
        Assert.assertEquals(driver.findElement(By.id("txt-password")).getAttribute("placeholder"), "Password");

        // Check login button
        Assert.assertEquals(driver.findElement(By.id("btn-login")).getText(), "Login");
        Assert.assertEquals(Color.fromString(driver.findElement(By.id("btn-login")).getCssValue("color")).asHex(), "#333333");
        Assert.assertEquals(Color.fromString(driver.findElement(By.id("btn-login")).getCssValue("background-color")).asHex(), "#ffffff");
    }

    @Test (priority = 1)
    private void login_with_null_values() {
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login\"]/div/div/div[1]/p[2]")).getText(), "Login failed! Please ensure the username and password are valid.");
    }

    // Verify the password field is required
    @Test (priority = 2)
    private void login_with_no_password() {
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login\"]/div/div/div[1]/p[2]")).getText(), "Login failed! Please ensure the username and password are valid.");
    }

    // Verify the username field is required
    @Test (priority = 3)
    private void login_with_no_username() {
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login\"]/div/div/div[1]/p[2]")).getText(), "Login failed! Please ensure the username and password are valid.");
    }

    @Test (priority = 4)
    private void login_with_wrong_password() {
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("Bambank");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login\"]/div/div/div[1]/p[2]")).getText(), "Login failed! Please ensure the username and password are valid.");
    }

    @Test (priority = 5)
    private void login_with_valid_account() {
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/#appointment");
    }

    @AfterTest
    private void close_browser() {
        // Close the browser after test
        driver.quit();
    }
}
