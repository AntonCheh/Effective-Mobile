package helpers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.AfterSearch;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class BaseTest {


    protected WebDriver chromeDriver;
    protected AfterSearch afterSearch;
    //    protected WebDriver fireFoxDriver = new FirefoxDriver();
//    protected WebDriver edgeDriver = new EdgeDriver();
    @BeforeEach
    public void before() {
        System.setProperty("webdriver.chrome.driver", "C:\\tmp\\chromedriver.exe");

        chromeDriver = new ChromeDriver();
        chromeDriver.manage().window().maximize();
        chromeDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        chromeDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        afterSearch = new AfterSearch(chromeDriver);
    }


    @AfterEach
    public void after() {
        chromeDriver.quit();
    }
}
