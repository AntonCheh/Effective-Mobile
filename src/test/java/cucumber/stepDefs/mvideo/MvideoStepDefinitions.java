package cucumber.stepDefs.mvideo;

import cucumber.hooks.Hooks;
import cucumber.hooks.SharedDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import selenium.assertions.AssertionsWeb;
import selenium.config.TestConfig;
import selenium.helpers.SoftAssertExecutor;
import selenium.pages.mvideo_purchases.MvideoPurchasesPage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MvideoStepDefinitions  {

    private MvideoPurchasesPage page;
    private SoftAssertExecutor softAssert;
    private final AssertionsWeb assertionsWeb;
    private final TestConfig config;

    private String currentFirstProductName;
    private List<String> productNames;
    private List<String> prices;
    private List<String> currentBrands;

    // Получаем driver из Hooks (статическое поле)
    private WebDriver driver;

    // Конструктор без параметров для Cucumber
    public MvideoStepDefinitions(SharedDriver sharedDriver) {
        this.driver = sharedDriver.getDriver();
        this.assertionsWeb = new AssertionsWeb();
        this.config = ConfigFactory.create(TestConfig.class);

        // Проверяем, что driver не null
        if (this.driver == null) {
            throw new RuntimeException("Driver is null in MvideoStepDefinitions constructor");
        }
        log.info("MvideoStepDefinitions initialized with driver: {}", this.driver);
    }

    @Given("I open Mvideo website")
    @Step("Открытие сайта М.Видео")
    public void iOpenMvideoWebsite() {

        page = new MvideoPurchasesPage(driver);
        softAssert = new SoftAssertExecutor(driver);

        page.open(config.mvideoUrl());
    }

    @And("I open catalog")
    @Step("Открытие каталога")
    public void iOpenCatalog() {
        page.openCatalog();
    }

    @And("I hover over electronics section")
    @Step("Наведение на раздел Электроника")
    public void iHoverOverElectronicsSection() {
        page.hoverOverElectronics();
    }

    @When("I navigate to {string} section")
    @Step("Переход в раздел {0}")
    public void iNavigateToSection(String sectionName) {
        log.info("Navigating to section: {}", sectionName);
        page.goToAllLaptops();
    }

    @And("I set price range from {string} to {string}")
    @Step("Установка диапазона цен от {0} до {1}")
    public void iSetPriceRange(String priceMin, String priceMax) {
        softAssert.executeStep(
                () -> page.setParameterPrice(priceMin, priceMax),
                "Установка цены: " + priceMin + " - " + priceMax
        );
    }

    @And("I select brands {string}")
    @Step("Выбор брендов: {0}")
    public void iSelectBrands(String brandsStr) {
        // Преобразуем строку с брендами в список
        currentBrands = Arrays.stream(brandsStr.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        softAssert.executeStep(
                () -> page.setParameterBrand(currentBrands, true),
                "Выбор производителей: " + currentBrands
        );
    }

    @Then("I should see at least {string} products")
    @Step("Проверка количества товаров (минимум {0})")
    public void iShouldSeeAtLeastProducts(String count) {
        Map<String, List<String>> data = page.getProductNamesAndPrices();
        productNames = data.get("names");
        prices = data.get("prices");

        softAssert.executeStep(
                () -> assertionsWeb.assertCountGreaterThan(Integer.parseInt(count), productNames.size()),
                "Проверка количества товаров. Ожидалось минимум: " + count +
                        ", фактически: " + productNames.size()
        );
    }

    @And("all products should contain selected brands")
    @Step("Проверка наличия выбранных брендов в названиях товаров")
    public void allProductsShouldContainSelectedBrands() {
        if (currentBrands != null && !currentBrands.isEmpty()) {
            softAssert.executeStep(
                    () -> assertionsWeb.assertProductNamesContainBrands(productNames, currentBrands),
                    "Проверка наличия брендов в названиях товаров"
            );
        }
    }

    @And("all prices should be within range {string} to {string}")
    @Step("Проверка диапазона цен от {0} до {1}")
    public void allPricesShouldBeWithinRange(String priceMin, String priceMax) {
        softAssert.executeStep(
                () -> assertionsWeb.assertPricesInRange(prices, priceMin, priceMax),
                "Проверка диапазона цен: " + priceMin + " - " + priceMax
        );
    }

    @When("I get first product name")
    @Step("Получение названия первого товара")
    public void iGetFirstProductName() {
        softAssert.executeStep(() -> {
            currentFirstProductName = page.getFirstProductName();
            log.info("First product name: {}", currentFirstProductName);
        }, "Получение названия первого товара");
    }

    @And("I search for this product")
    @Step("Поиск товара")
    public void iSearchForThisProduct() {
        softAssert.executeStep(
                () -> page.searchProduct(currentFirstProductName),
                "Поиск товара: " + currentFirstProductName
        );
    }

    @Then("search results should contain the product")
    @Step("Проверка наличия товара в результатах поиска")
    public void searchResultsShouldContainTheProduct() {
        softAssert.executeStep(
                () -> {
                    boolean found = page.verifySearchResultsContainProduct(currentFirstProductName);
                    Assertions.assertTrue(found, "Товар '" + currentFirstProductName + "' не найден в результатах поиска");
                },
                "Проверка наличия товара в результатах поиска"
        );
    }

    @And("I verify all assertions")
    @Step("Проверка всех утверждений")
    public void iVerifyAllAssertions() {
        softAssert.assertAll();
    }
}