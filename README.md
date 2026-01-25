# Effective-Mobile
Тестирование авторизации SauceDemo
Проект автоматизированного тестирования функционала авторизации на сайте SauceDemo.

Структура проекта
LoginTests.java - тестовый класс с параметризованными тестами

CheckLoginTests.java - базовый класс с методами выполнения тестов

BaseTest.java - базовый класс для настройки WebDriver

LoginAssertions.java - класс для проверок

Конфигурационные файлы в resources/

Предварительные требования
Java 11+

Maven 3.6+

Установленный браузер (Chrome, Firefox или Edge)

Доступ к интернету

Установка зависимостей
bash
mvn clean install
Запуск тестов
Запуск всех тестов
bash
mvn test
Запуск тестов с определенными тегами
bash
# Только позитивные тесты
mvn test -Dgroups="Positive"

# Только smoke-тесты
mvn test -Dgroups="Smoke"

# Негативные тесты
mvn test -Dgroups="Negative"
Запуск с определенным браузером
bash
# Chrome (по умолчанию)
mvn test -Dbrowser=chrome

# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge
Запуск в headless режиме
bash
mvn test -Dheadless=true
Параллельный запуск тестов
bash
mvn test -Dparallel=true
Генерация отчетов
Allure отчеты
Установите Allure командной строки:

bash
mvn clean test
Сгенерируйте и откройте отчет:

bash
allure serve target/allure-results
Для генерации статического отчета:

bash
allure generate target/allure-results -o target/allure-report --clean
# Откройте в браузере
open target/allure-report/index.html
JUnit отчеты
JUnit отчеты автоматически генерируются в:

text
target/surefire-reports/
Генерация отчетов с помощью Maven
bash
# Генерация Allure отчетов после тестов
mvn allure:report

# Открытие Allure отчетов
mvn allure:serve
Конфигурация
Основные параметры конфигурации можно задать через системные свойства или в файле src/test/resources/config.properties:

properties
# Браузер для запуска тестов (chrome, firefox, edge)
browser=chrome

# Таймаут неявных ожиданий (секунды)
timeout=10

# Режим headless (true/false)
headless=false

# Базовый URL тестируемого приложения
base.url=https://www.saucedemo.com/
Типы тестов
Проект содержит следующие типы тестов:

Позитивные тесты (Positive)
Успешный логин с валидными данными

Логин пользователя performance_glitch_user

Негативные тесты (Negative)
Логин с неверным паролем

Логин заблокированного пользователя

Логин с пустыми полями

Smoke-тесты
Все тесты помечены как Smoke-тесты и могут быть запущены отдельно для быстрой проверки основного функционала.

Структура тестового класса
Тесты используют параметризованный подход с источниками данных:

dataSuccessful() - данные для успешного логина

dataWrongPassword() - данные с неверным паролем

dataBlockedUser() - данные заблокированного пользователя

dataEmpty() - данные с пустыми полями

dataWithTimeOut() - данные пользователя с замедленным ответом