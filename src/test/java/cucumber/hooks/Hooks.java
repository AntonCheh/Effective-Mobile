package cucumber.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

@Slf4j
public class Hooks {

    private final SharedDriver sharedDriver;

    public Hooks(SharedDriver sharedDriver) {
        this.sharedDriver = sharedDriver;
    }

    @Before(order = 0)
    public void setUpDriver(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
        // Driver уже инициализирован в SharedDriver
        WebDriver driver = sharedDriver.getDriver();
        if (driver == null) {
            throw new RuntimeException("Driver not initialized in SharedDriver");
        }
        log.info("Driver initialized successfully: {}", driver);
    }

    @After(order = 0)
    public void tearDownDriver(Scenario scenario) {
        WebDriver driver = sharedDriver.getDriver();
        if (scenario.isFailed() && driver != null) {
            takeScreenshot(driver, scenario.getName());
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "Screenshot - {scenarioName}", type = "image/png")
    public byte[] takeScreenshot(WebDriver driver, String scenarioName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}