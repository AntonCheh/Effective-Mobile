package cucumber.hooks;

import org.openqa.selenium.WebDriver;
import selenium.helpers.BaseTest;

public class SharedDriver extends BaseTest {

    private WebDriver driver;

    public SharedDriver() {
        // Инициализация драйвера
        setUp();
        this.driver = super.driver;  // Получаем driver из BaseTest
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
