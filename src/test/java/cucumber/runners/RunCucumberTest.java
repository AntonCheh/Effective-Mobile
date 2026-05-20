package cucumber.runners;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * ============================================================================
 * CUCUMBER TEST RUNNER CONFIGURATION
 * ============================================================================

 * Этот класс является точкой входа для запуска Cucumber тестов с JUnit 5.
 * Все настройки выполняются через аннотации.
 */

@Suite
/**
 * ПОМЕЧАЕТ КЛАСС КАК НАБОР ТЕСТОВ (СЬЮТ)
 * - Заменяет старый @RunWith(Cucumber.class) из JUnit 4
 * - Говорит JUnit Platform, что это контейнер для запуска тестов
 */

@SuiteDisplayName("Cucumber Tests")
/**
 * ЗАДАЕТ ЧЕЛОВЕКО-ЧИТАЕМОЕ ИМЯ ДЛЯ СЬЮТА
 * - В отчетах вместо имени класса будет "Cucumber Tests"
 * - Улучшает читаемость в отчетах и IDE
 */
@IncludeEngines("cucumber")
/**
 * УКАЗЫВАЕТ ДВИЖОК ДЛЯ ЗАПУСКА ТЕСТОВ
 * - Использует Cucumber JUnit Platform Engine
 * - Говорит "используй Cucumber для выполнения этих тестов"
 */
@SelectClasspathResource("feature")
/**
 * УКАЗЫВАЕТ ПАПКУ С .FEATURE ФАЙЛАМИ
 * - Ищет сценарии в: src/test/resources/feature/
 * - Все .feature файлы в этой папке будут выполнены
 */
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, " +
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm, " +
                "json:target/cucumber-report/report.json, " +
                "html:target/cucumber-report/report.html")
/**
 * НАСТРАИВАЕТ ПЛАГИНЫ ДЛЯ ОТЧЕТОВ:
 * - pretty: красивый вывод в консоль
 * - AllureCucumber7Jvm: интеграция с Allure
 * - json: отчет в JSON формате (target/cucumber-report/report.json)
 * - html: HTML отчет (target/cucumber-report/report.html)
 */

@ConfigurationParameter(key = GLUE_PROPERTY_NAME,
        value = "ru.sberbank.sdct.autotest.stepdefs, " +
                "ru.sberbank.sdct.autotest.hooks")
/**
 * УКАЗЫВАЕТ ПАКЕТЫ ДЛЯ ПОИСКА STEP DEFINITIONS И HOOKS:
 * - Step definitions: классы с @Given, @When, @Then
 * - Hooks: классы с @Before, @After
 * - Cucumber ищет реализации шагов в этих пакетах
 */
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @excluded")
/**
 * ФИЛЬТРУЕТ ТЕСТЫ ПО ТЕГАМ
 * - "not @excluded": запускает все сценарии, КРОМЕ помеченных @excluded
 * - Позволяет пропускать определенные тесты
 */
@ConfigurationParameter(key = ANSI_COLORS_DISABLED_PROPERTY_NAME, value = "false")
/**
 * ВКЛЮЧАЕТ ЦВЕТНОЙ ВЫВОД В КОНСОЛИ
 * - false: цвета ВКЛЮЧЕНЫ
 * - true: цвета ВЫКЛЮЧЕНЫ
 * - Улучшает читаемость вывода в терминале
 */
@ConfigurationParameter(key = EXECUTION_MODE_FEATURE_PROPERTY_NAME, value = "concurrent")
/**
 * НАСТРАИВАЕТ РЕЖИМ ВЫПОЛНЕНИЯ
 * - concurrent: параллельное выполнение сценариев
 * - sequential: последовательное выполнение
 * - Ускоряет выполнение тестов
 */
public class RunCucumberTest {
    // Пустой класс - вся конфигурация через аннотации
    // Не нужно методов с @Test - Cucumber сам найдет сценарии
}
