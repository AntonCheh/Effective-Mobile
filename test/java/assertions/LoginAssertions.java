package assertions;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginAssertions {

    @Step("Проверка успешного логина")
    public void assertLoginCorrect(WebDriverWait wait) {

        WebElement pageTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("title"))
        );
        String titleText = pageTitle.getText();
        assertTrue(titleText.equals("Products"),
                "Заголовок должен быть 'Products', текущий: " + titleText);
    }

    @Step("Проверка неверного логина")
    public void assertLoginWrong(WebDriver driver) {
        String errorMessage = driver.findElement(
                By.xpath("//h3[@data-test='error']")).getText();

        assertTrue(errorMessage.contains("Username and password do not match"),
                "Ожидалась ошибка о неверном пароле, получено: " + errorMessage);
    }

    @Step("Проверка заблокированного пользователя")
    public void assertBlockedUser(WebDriver driver) {
        String errorMessage = driver.findElement(By.xpath("//h3[@data-test='error']")).getText();

        assertTrue(errorMessage.contains("Epic sadface: Sorry, this user has been locked out."),
                "Ожидалась ошибка о блокировке пользователя, получено: " + errorMessage);
    }

    @Step("Проверка путых строк")
    public void assertEmptyStrings(WebDriver driver) {
        // Проверяем ошибку
        String errorMessage = driver.findElement(
                By.xpath("//h3[@data-test='error']")
        ).getText();

        assertTrue(errorMessage.contains("Username is required"),
                "Ожидалась ошибка о необходимости имени пользователя, получено: " + errorMessage);
    }
}


