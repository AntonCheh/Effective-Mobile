package selenium.pages.mvideo_purchases;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import selenium.base.BasePage;
import selenium.models.FilterSelectionResult;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class MvideoPurchasesPage extends BasePage {

    private static final By CATALOG_BUTTON = By.xpath("//button[@aria-label='Каталог']");
    private static final By ELECTRONICS_SECTION = By.xpath("//a[contains(@class, 'left-menu__item-text')][contains(@href, 'noutbuki')]");
    private static final By LAPTOPS_LINK = By.xpath("//a[text()='Ноутбуки' and contains(@href, 'catalog')]");
    private static final By PRODUCT_TITLES = By.xpath("//span[@class='name']");
    private static final By SEARCH_INPUT = By.xpath("//input[@type='search' and @placeholder='Поиск в М.Видео']");
    private static final By SEARCH_BUTTON = By.xpath("//button[@class = 'mui-button main-search__submit main-search__submit--desktop']");
    private static final String CATEGORY_DROPDOWN_TEMPLATE = "//a[text()='%s' and contains(@href, 'catalog')]";
    private static final By PRICE_TITLES = By.xpath("//span[@class='current-price']");
    private static final String SUBCATEGORY_TEMPLATE = "//span[contains(@class, 'last-level-category__name') and text()='%s']";
    private static final By PRICE = By.xpath("//button[contains(@class, 'mui-chip')][contains(text(), 'Цена')]");
    private static final By READY = By.xpath("//button[contains(@class, 'mui-button button') and contains(text(), 'Готово')]");
    private static final By PRICE_MIN_INPUT = By.xpath("//label[contains(text(), 'От')]/preceding-sibling::input");
    private static final By PRICE_MAX_INPUT = By.xpath("//label[contains(text(), 'До')]/preceding-sibling::input");
    private static final By BRAND_SECTION_HEADER = By.xpath("//button[contains(@class, 'mui-chip')][contains(text(), 'Бренд')]");
    private static final By SHOW_ALL_BRANDS_BUTTON = By.xpath("//button[contains(@class, 'mui-button show-all-link')][contains(text(), 'Показать всё')]");
    private static final By BRAND_SEARCH_INPUT = By.xpath("//input[@placeholder='Поиск по списку']");
    private static final By SHOW_EXTRA = By.xpath("//button[@class='mui-button load-button' and contains(text(), 'Показать ещё')]");


    public MvideoPurchasesPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть главную страницу Mvideo")
    public MvideoPurchasesPage open(String url) {
        log.info("Открытие страницы Mvideo: {}", url);
        driver.get(url);
        log.debug("URL загружен, ожидание появления значений страницы");
        waitActions.waitForElementVisible(CATALOG_BUTTON);

        popupHelper.smartClosePopups();
        log.info("Главная страница открыта: {}", url);

        // Повторно ждем кнопку каталога (могла перекрыться)
        waitActions.waitForElementClickable(CATALOG_BUTTON);

        log.info("Главная страница открыта: {}", url);
        return this;
    }


    @Step("Открыть каталог товаров")
    public MvideoPurchasesPage openCatalog() {
        log.info("Открытие каталога");
        click(CATALOG_BUTTON);
        log.info("Каталог открыт");
        return this;
    }

    @Step("Навести курсор на раздел 'Электроника'")
    public MvideoPurchasesPage hoverOverElectronics() {
        log.info("Наведение курсора на раздел 'Электроника'");
        hoverOver(ELECTRONICS_SECTION);

        // Ждем появления подменю
        waitActions.sleep(500); // Даем время на анимацию
        waitActions.waitForElementPresent(By.xpath("//a[contains(@href, 'catalog')]")); // Ждем появления любой категории

        log.debug("Курсор наведен, подменю появилось");
        return this;
    }

    /**
     * Кликнуть по подкатегории в меню
     *
     * @param subCategory название подкатегории (например, "Все ноутбуки")
     */
    @Step("Кликнуть по подкатегории '{subCategory}'")
    public MvideoPurchasesPage clickSubCategory(String subCategory) {
        log.info("Клик по подкатегории '{}'", subCategory);

        By subCategoryLink = By.xpath(String.format(SUBCATEGORY_TEMPLATE, subCategory));

        waitActions.waitForElementClickable(subCategoryLink);
        click(subCategoryLink);

        log.debug("Клик по подкатегории '{}' выполнен", subCategory);
        return this;
    }

    /**
     * Перейти в раздел "Все ноутбуки"
     */
    @Step("Перейти в раздел 'Все ноутбуки'")
    public MvideoPurchasesPage goToAllLaptops() {
        return clickSubCategory("Все ноутбуки");
    }

    @Step("Установить диапазон цен от {minPrice} до {maxPrice}")
    public MvideoPurchasesPage setParameterPrice(String minPrice, String maxPrice) {
        log.info("Установка диапазона цен: {} - {}", minPrice, maxPrice);

        // 1. Открываем фильтр цены
        waitActions.waitForElementClickable(PRICE);
        WebElement priceButton = driver.findElement(PRICE);
        priceButton.click();
        waitActions.sleep(300);  // Ждем появления полей ввода

        // 2. Ввод минимальной цены
        waitActions.waitForElementVisible(PRICE_MIN_INPUT);
        WebElement minInput = driver.findElement(PRICE_MIN_INPUT);
        minInput.clear();
        waitActions.sleep(100);
        assertionHelper.typeWithDelay(minInput, minPrice, 50);
//        waitActions.sleep(300);

        // 3. Ввод максимальной цены
        waitActions.waitForElementVisible(PRICE_MAX_INPUT);
        WebElement maxInput = driver.findElement(PRICE_MAX_INPUT);
        maxInput.clear();
        waitActions.sleep(100);
        assertionHelper.typeWithDelay(maxInput, maxPrice, 50);
//        waitActions.sleep(300);

        // 4. Применяем фильтр
        waitActions.waitForElementClickable(READY);
        WebElement readyButton = driver.findElement(READY);
        readyButton.click();

        // 5. Ждем обновления товаров
//        waitForLoaderDisappear();
//        sleep(2000);  // Даем странице время на обновление

        log.info("Диапазон цен установлен");
        return this;
    }

    @Step("Выбрать производителей: {brands}")
    public List<String> setParameterBrand(List<String> brands, boolean strict) {
        log.info("Выбор производителей: {}", brands);

        // 1. Открываем фильтр брендов
        waitActions.waitForElementClickable(BRAND_SECTION_HEADER);
        click(BRAND_SECTION_HEADER);
        waitActions.sleep(300);

        click(SHOW_ALL_BRANDS_BUTTON);
        waitActions.sleep(300);

        waitActions.waitForElementVisible(BRAND_SEARCH_INPUT);

        // 2. Вводим бренды по очереди и выбираем их
        List<String> selected = new ArrayList<>();
        List<String> notFound = new ArrayList<>();

        for (String brand : brands) {
            try {
                WebElement searchInput = driver.findElement(BRAND_SEARCH_INPUT);
                searchInput.clear();
                waitActions.sleep(100);

                // Вводим ОДИН бренд посимвольно
                assertionHelper.typeWithDelay(searchInput, brand, 50);  // ← Один бренд
                waitActions.sleep(1000);

                // Ищем чекбокс
//                String xpath = String.format(
//                        "//mui-checkbox[.//span[contains(@class, 'checkbox__content')][translate(normalize-space(text()), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '%s']]//input[@type='checkbox']",
//                        brand.toLowerCase()
//                );

                // Ищем LABEL (не input!), так как input disabled
                String xpath = String.format(
                        "(//mui-checkbox[.//span[contains(@class, 'checkbox__content')][translate(normalize-space(text()), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '%s']]//label[@class='mui-checkbox'])",
                        brand.toLowerCase()
                );

                List<WebElement> labels = driver.findElements(By.xpath(xpath));

                log.info("labels: {}", labels);

                if (labels.isEmpty()) {
                    log.warn("❌ Не найден: {}", brand);
                    notFound.add(brand);
                    continue;
                }

                // Кликаем по label
                WebElement firstLabel = labels.get(0);

                scrollActions.scrollToElement(firstLabel);
                waitActions.sleep(200);

                // Клик через Actions
                new Actions(driver)
                        .moveToElement(firstLabel)
                        .pause(200)
                        .click()
                        .perform();

                selected.add(brand);
                log.debug("✅ Выбран: {} (найдено элементов: {})", brand, labels.size());
                waitActions.sleep(500);

            } catch (Exception e) {
                log.warn("❌ Не найден: {}", brand);
                notFound.add(brand);
            }
        }

        waitActions.waitForElementClickable(READY);
        WebElement readyButton = driver.findElement(READY);
        readyButton.click();

        FilterSelectionResult result = new FilterSelectionResult(selected, notFound);

        // Логируем
        log.info(result.toString());
        attachBrandSelectionResult(result);

        // Проверки
        if (!result.hasSelected()) {
            throw new RuntimeException("Не удалось выбрать ни один бренд из: " + brands);
        }

        if (strict && result.hasNotFound()) {
            String errorMsg = String.format(
                    "❌ СТРОГИЙ РЕЖИМ: не все бренды выбраны!\n" +
                            "Ожидалось (%d): %s\n✅ Выбрано (%d): %s\n❌ Не найдено (%d): %s",
                    brands.size(), brands,
                    result.getSelected().size(), result.getSelected(),
                    result.getNotFound().size(), result.getNotFound()
            );
            log.error(errorMsg);
            throw new AssertionError(errorMsg);
        }

        log.info("Итог: {}", result.getSummary());
        return result.getSelected();
    }

    /**
     * Прикрепляет результаты выбора брендов к Allure отчету
     */
    @Attachment(value = "Результаты выбора брендов", type = "text/plain")
    private String attachBrandSelectionResult(FilterSelectionResult result) {
        return result.toString();
    }


    @Step("Получить список названий и цен товаров")
    public Map<String, List<String>> getProductNamesAndPrices() {
        log.info("Получение списка названий и цен товаров");

        Set<String> uniqueNames = new LinkedHashSet<>();
        Set<String> uniquePrices = new LinkedHashSet<>();
        int maxScrollAttempts = 50;

        for (int i = 0; i < maxScrollAttempts; i++) {
            // Собираем названия и цены
            collectCurrentProducts(uniqueNames, uniquePrices);

            log.debug("Прокрутка {}: названий={}, цен={}", i + 1, uniqueNames.size(), uniquePrices.size());

            // Нажимаем "Показать ещё" пока кнопка есть
            while (filterSeleniumActions.isShowMoreButtonPresent(SHOW_EXTRA)) {
                log.info("Найдена кнопка 'Показать ещё', кликаем...");
                filterSeleniumActions.clickShowMoreButton(SHOW_EXTRA);
                waitActions.sleep(2000);  // Ждем загрузку

                // Собираем новые товары после клика
                collectCurrentProducts(uniqueNames, uniquePrices);
            }

            // Проверяем конец страницы
            if (isPageEndReached()) {
                log.info("Достигнут конец страницы");
                sleep(1500);
                collectCurrentProducts(uniqueNames, uniquePrices);
                break;
            }

            scrollDown();
            sleep(500);
        }

        List<String> namesResult = new ArrayList<>(uniqueNames);
        List<String> pricesResult = new ArrayList<>(uniquePrices);

        log.info("Найдено: названий={}, цен={}", namesResult.size(), pricesResult.size());

        Map<String, List<String>> result = new LinkedHashMap<>();
        result.put("names", namesResult);
        result.put("prices", pricesResult);

        attachAllProductsAndPrices(namesResult, pricesResult);

        return result;
    }

    /**
     * Собирает текущие товары и цены со страницы
     */
    private void collectCurrentProducts(Set<String> names, Set<String> prices) {
        // Собираем названия
        List<WebElement> products = driver.findElements(PRODUCT_TITLES);
        for (WebElement product : products) {
            try {
                String text = product.getText();
                if (text != null && !text.isEmpty()) {
                    names.add(text);
                }
            } catch (StaleElementReferenceException e) {
                log.debug("Пропущен устаревший элемент названия");
            }
        }

        // Собираем цены
        List<WebElement> priceElements = driver.findElements(PRICE_TITLES);
        for (WebElement price : priceElements) {
            try {
                String text = price.getText();
                if (text != null && !text.isEmpty()) {
                    String cleanPrice = text.replaceAll("[^\\d]", "");
                    if (!cleanPrice.isEmpty()) {
                        prices.add(cleanPrice);
                    }
                }
            } catch (StaleElementReferenceException e) {
                log.debug("Пропущен устаревший элемент цены");
            }
        }
    }

    /**
     * Прикрепляет названия и цены к Allure отчету
     */
    @Attachment(value = "📋 Полный список товаров и цен", type = "text/plain")
    private String attachAllProductsAndPrices(List<String> names, List<String> prices) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ПОЛНЫЙ СПИСОК ТОВАРОВ И ЦЕН ===\n");
        sb.append(String.format("Всего найдено: %d товаров, %d цен\n", names.size(), prices.size()));
        sb.append("=====================================\n\n");

        if (names.isEmpty() && prices.isEmpty()) {
            sb.append("❌ ТОВАРЫ НЕ НАЙДЕНЫ!\n");
        } else {
            int limit = Math.min(names.size(), prices.size());

            // Выводим попарно
            for (int i = 0; i < limit; i++) {
                sb.append(String.format("%d. %s\n", i + 1, names.get(i)));
                sb.append(String.format("   💰 %s ₽\n\n", prices.get(i)));
            }

            // Если названий больше, чем цен
            if (names.size() > limit) {
                sb.append("⚠️ ТОВАРЫ БЕЗ ЦЕН:\n");
                for (int i = limit; i < names.size(); i++) {
                    sb.append(String.format("%d. %s\n", i + 1, names.get(i)));
                }
            }

            // Если цен больше, чем названий
            if (prices.size() > limit) {
                sb.append("⚠️ ЦЕНЫ БЕЗ ТОВАРОВ:\n");
                for (int i = limit; i < prices.size(); i++) {
                    sb.append(String.format("%d. %s ₽\n", i + 1, prices.get(i)));
                }
            }
        }

        sb.append("\n=====================================\n");
        sb.append(String.format("ИТОГО: %d товаров, %d цен\n", names.size(), prices.size()));

        return sb.toString();
    }

    /**
     * Прикрепляет ВСЕ товары к Allure отчету
     */
    @Attachment(value = "📋 Полный список товаров", type = "text/plain")
    private String attachAllProducts(List<String> products) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ПОЛНЫЙ СПИСОК ТОВАРОВ ===\n");
        sb.append(String.format("Всего найдено: %d товаров\n", products.size()));
        sb.append("===============================\n\n");

        if (products.isEmpty()) {
            sb.append("❌ ТОВАРЫ НЕ НАЙДЕНЫ!\n");
        } else {
            for (int i = 0; i < products.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, products.get(i)));
            }
        }

        sb.append("\n===============================\n");
        sb.append(String.format("ИТОГО: %d товаров\n", products.size()));

        return sb.toString();
    }


    @Step("Получить название первого товара")
    public String getFirstProductName() {
        log.info("Получение названия первого товара");

        // Прокручиваем в начало
        scrollActions.scrollToTop();
        sleep(800);

        // Используем универсальный метод
        String firstName = elementActions.getFirstElementText(PRODUCT_TITLES);

        log.info("Первый товар: {}", firstName);
        return firstName;
    }

    @Step("Выполнить поиск товара: {productName}")
    public MvideoPurchasesPage searchProduct(String productName) {
        log.info("Выполнение поиска товара: {}", productName);

        // Используем универсальный метод
        searchActions.performSearchAndWait(
                SEARCH_INPUT,
                SEARCH_BUTTON,
                PRODUCT_TITLES,
                productName
        );

        // Проверяем, что поиск выполнился
        if (!searchActions.isSearchExecuted("text=")) {
            log.warn("Поиск мог не выполниться корректно");
        }

        log.info("Поиск '{}' выполнен успешно", productName);
        return this;
    }

    @Step("Проверить наличие товара в результатах поиска")
    public boolean verifySearchResultsContainProduct(String productName) {
        log.info("Проверка наличия товара '{}' в результатах поиска", productName);

        // Ждем появления хотя бы одного товара (быстро)
        try {
            waitActions.waitForElementPresent(PRODUCT_TITLES);
        } catch (TimeoutException e) {
            log.warn("❌ Товары не найдены на странице");
            return false;
        }

        // Ищем только среди видимых товаров (быстро)
        List<WebElement> results = driver.findElements(PRODUCT_TITLES);

        String searchTerm = productName.toLowerCase().trim();

        boolean found = results.stream()
                .filter(WebElement::isDisplayed)  // Только видимые
                .map(WebElement::getText)
                .filter(text -> text != null && !text.isEmpty())
                .anyMatch(text -> text.toLowerCase().contains(searchTerm));

        if (found) {
            log.info("✅ Товар '{}' найден", productName);
        } else {
            log.warn("❌ Товар '{}' не найден среди {} видимых товаров", productName, results.size());

            // Для отладки - только если нужно (можно закомментировать)
            if (log.isDebugEnabled()) {
                List<String> visibleTexts = results.stream()
                        .filter(WebElement::isDisplayed)
                        .map(WebElement::getText)
                        .filter(text -> !text.isEmpty())
                        .limit(5)
                        .collect(Collectors.toList());
                log.debug("Примеры товаров: {}", visibleTexts);
            }
        }

        return found;
    }
}







