package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AfterSearch  {

    WebDriverWait wait;

    public AfterSearch(WebDriver chromeDriver) {
        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
    }

    public WebDriverWait getWait() {
        return wait;
    }
}
