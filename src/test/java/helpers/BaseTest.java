package helpers;

import config.TestConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected TestConfig config;

    @BeforeEach
    public void setUp() {
        config = ConfigFactory.create(TestConfig.class);

        // Автоматически включаем headless, если мы в Docker
        boolean isDocker = System.getenv("DOCKER_ENV") != null;
        boolean headless = isDocker || config.headless();

        driver = createDriver(config.browser(), headless);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.timeout()));
    }

    private WebDriver createDriver(String browserType, boolean headless) {
        switch (browserType.toLowerCase()) {
            case "firefox":
                return createFirefoxDriver(headless);
            case "edge":
                return createEdgeDriver(headless);
            case "chrome":
            default:
                return createChromeDriver(headless);
        }
    }

    private WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless");
            // Важно для Linux-контейнеров:
            options.addArguments("--no-sandbox");          // Обязательно для Docker
            options.addArguments("--disable-dev-shm-usage"); // Для работы в ограниченной памяти
            options.addArguments("--disable-gpu");         // Для совместимости
        }

        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--start-maximized");

        return new FirefoxDriver(options);
    }

    private WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless");
        }

        options.addArguments("--start-maximized");

        return new EdgeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}