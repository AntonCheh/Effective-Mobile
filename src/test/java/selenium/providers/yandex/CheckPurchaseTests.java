package selenium.providers.yandex;

import selenium.assertions.AssertionsWeb;
import selenium.config.TestConfig;
import selenium.helpers.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.ConfigFactory;
import selenium.helpers.SoftAssertExecutor;
import selenium.pages.mvideo_purchases.MvideoPurchasesPage;
import selenium.pages.yandex_purchases.YandexPurchasesPage;
import selenium.sources.mvideoTest.DataForMvideoPurchases;
import selenium.sources.yandexTest.DataForYandexPurchases;
import selenium.sources.yandexTest.PurchasesSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class CheckPurchaseTests extends BaseTest implements DataForYandexPurchases, DataForMvideoPurchases {

    private final AssertionsWeb assertionsWeb;
    private final TestConfig config;


    public CheckPurchaseTests(AssertionsWeb assertionsWeb) {

        this.assertionsWeb = assertionsWeb;
        this.config = ConfigFactory.create(TestConfig.class); // Создаём конфиг
    }

    public CheckPurchaseTests() {
        this(new AssertionsWeb());
    }


    public void executeWithSoftAssert(PurchasesSource testsSource) {
        SoftAssertExecutor softAssert = new SoftAssertExecutor(driver);

        YandexPurchasesPage page = new YandexPurchasesPage(driver);
        List<String> selectedBrands = new ArrayList<>();

        // 1. Открытие страницы и навигация
        softAssert.executeStep(() -> page.open(config.yandexUrl()), "Открытие страницы");
        softAssert.executeStep(page::openCatalog, "Открытие каталога");
        softAssert.executeStep(page::hoverOverElectronics, "Наведение на Электронику");
        softAssert.executeStep(() ->
                page.clickLaptopsInDropdown(testsSource.laptopTitles()), "Переход в " + testsSource.laptopTitles());

        // 2. Проверка заголовка
//        softAssert.executeStep(
//                () -> assertionsWeb.assertPageExist(testsSource.laptopTitles()),
//                "Проверка заголовка");

        // 3. Установка фильтров
        softAssert.executeStep(
                () -> page.setParameterPrice(testsSource.priceMin(), testsSource.priceMax()),
                "Установка цены"
        );

        // 4. Выбор брендов
        softAssert.executeStep(
                () -> selectedBrands.addAll(page.setParameterBrand(testsSource.producer(), true)),
                "Выбор производителей"
        );


        Map<String, List<String>> data = page.getProductNamesAndPrices();
        List<String> productNames = data.get("names");
        List<String> prices = data.get("prices");

        softAssert.executeStep(
                () -> assertionsWeb.assertCountGreaterThan(12, productNames.size()),
                "Проверка количества товаров"
        );

        softAssert.executeStep(
                () -> assertionsWeb.assertProductNamesContainBrands(productNames, testsSource.producer()),
                "Проверка количества товаров"
        );

        softAssert.executeStep(
                () -> assertionsWeb.assertPricesInRange(prices, testsSource.priceMin(), testsSource.priceMax()),
                "Проверка диапазона цен"
        );

        // 6. Поиск первого товара
        String firstElement = page.getFirstProductName();

        softAssert.executeStep(
                () -> page.searchProduct(firstElement),
                "Поиск первого товара"
        );

        softAssert.executeStep(
                () -> page.verifySearchResultsContainProduct(firstElement),
                "Проверка наличия товара в результатах"
        );

        // 7. Проверка всех шагов (упадет, если были ошибки)
        softAssert.assertAll();
    }

    public void executeWithSoftAssertMvideo(PurchasesSource testsSource) {
        SoftAssertExecutor softAssert = new SoftAssertExecutor(driver);

        MvideoPurchasesPage page = new MvideoPurchasesPage(driver);
        List<String> selectedBrands = new ArrayList<>();

        // 1. Открытие страницы и навигация
        softAssert.executeStep(() -> page.open(config.mvideoUrl()), "Открытие страницы");
        softAssert.executeStep(page::openCatalog, "Открытие каталога");
        softAssert.executeStep(page::hoverOverElectronics, "Наведение на Электронику");
        softAssert.executeStep(() ->
                page.goToAllLaptops(), "Переход в " + testsSource.laptopTitles());

        // 2. Проверка заголовка
//        softAssert.executeStep(
//                () -> assertionsWeb.assertPageExist(testsSource.laptopTitles()),
//                "Проверка заголовка");

        // 3. Установка фильтров
        softAssert.executeStep(
                () -> page.setParameterPrice(testsSource.priceMin(), testsSource.priceMax()),
                "Установка цены"
        );

        // 4. Выбор брендов
        softAssert.executeStep(
                () -> selectedBrands.addAll(page.setParameterBrand(testsSource.producer(), true)),
                "Выбор производителей"
        );


        Map<String, List<String>> data = page.getProductNamesAndPrices();
        List<String> productNames = data.get("names");
        List<String> prices = data.get("prices");

        softAssert.executeStep(
                () -> assertionsWeb.assertCountGreaterThan(12, productNames.size()),
                "Проверка количества товаров"
        );

        softAssert.executeStep(
                () -> assertionsWeb.assertProductNamesContainBrands(productNames, testsSource.producer()),
                "Проверка количества товаров"
        );

        softAssert.executeStep(
                () -> assertionsWeb.assertPricesInRange(prices, testsSource.priceMin(), testsSource.priceMax()),
                "Проверка диапазона цен"
        );

        // 6. Поиск первого товара
        String firstElement = page.getFirstProductName();

        softAssert.executeStep(
                () -> page.searchProduct(firstElement),
                "Поиск первого товара"
        );

        softAssert.executeStep(
                () -> page.verifySearchResultsContainProduct(firstElement),
                "Проверка наличия товара в результатах"
        );

        // 7. Проверка всех шагов (упадет, если были ошибки)
        softAssert.assertAll();
    }
}




