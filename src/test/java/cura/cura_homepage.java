package cura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class cura_homepage {
    WebDriver driver;

    @BeforeTest
    private void init() {

        // Initiate Browser
        System.setProperty("webdriver.chrome.driver", "C:/Chromedriver/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();

        // Go to CURA Home Page
        driver.navigate().to("https://katalon-demo-cura.herokuapp.com/");
        driver.manage().window().maximize();
    }

    @Test (priority = 0)
    private void check_element() {

        // Check H1 Element
        Assert.assertEquals(driver.findElement(By.cssSelector("header h1")).getText(), "CURA Healthcare Service");
        Assert.assertEquals(Color.fromString(driver.findElement(By.cssSelector("header h1")).getCssValue("color")).asHex(), "#ffffff");

        // Check h3 Element
        Assert.assertEquals(driver.findElement(By.cssSelector("header h3")).getText(), "We Care About Your Health");
        Assert.assertEquals(Color.fromString(driver.findElement(By.cssSelector("header h3")).getCssValue("color")).asHex(), "#4fb6e7");

        // Check button make appointment
        Assert.assertEquals(driver.findElement(By.id("btn-make-appointment")).getText(), "Make Appointment");
        Assert.assertEquals(driver.findElement(By.id("btn-make-appointment")).getCssValue("background-color"), "rgba(115, 112, 181, 0.8)");

        // Menu toggle
        Assert.assertEquals(driver.findElement(By.id("menu-toggle")).getCssValue("background-color"), "rgba(115, 112, 181, 0.8)");
    }

    @Test (priority = 1)
    // As a user, when I access CURA homepage and then I click on toggle menu, I should be able to see menu list
    public void click_toggle_menu() {
        driver.findElement(By.id("menu-toggle")).click();
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[1]")).getText(), "CURA Healthcare");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[2]")).getText(), "Home");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[3]")).getText(), "Login");
        driver.findElement(By.id("menu-close"));
        /*
        // User clicks Login button
        driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[3]/a")).click();
         */
    }

    @Test (priority = 2)
    public void click_make_appointment_button() {
        driver.findElement(By.id("btn-make-appointment")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/profile.php#login");
    }

    @AfterTest
    private void close_browser() {
        // Close the browser after test
        driver.quit();
    }
}
