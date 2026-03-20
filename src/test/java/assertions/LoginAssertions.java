package assertions;

import constants.Messages;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import pages.LoginPage;
import pages.ProductsPage;

import static org.assertj.core.api.Assertions.assertThat;


public class LoginAssertions {

    @Step("Проверка регистрации при вводе корректных данных {user} : {password}")
    public void assertLoginCorrect(WebDriver driver, String user, String password) {
        ProductsPage productsPage = new ProductsPage(driver);
        assertThat(productsPage.isPageOpened())
                .withFailMessage("Логин не удался - страница продуктов не открылась")
                .isTrue();
    }

    @Step("Проверка ошибки: {expectedError}")
    public void assertLoginError(WebDriver driver, String expectedError) {
        LoginPage loginPage = new LoginPage(driver);
        assertThat(loginPage.getErrorMessageText())
                .withFailMessage("Ожидалась ошибка: " + expectedError)
                .contains(expectedError);
    }

    @Step("Проверка логина с неверным паролем, ожидаем ошибку - {message}")
    public void assertLoginWrong(WebDriver driver, String message) {
        assertLoginError(driver, message);

    }

    @Step("Проверка заблокированного пользователя - {user}")
    public void assertBlockedUser(WebDriver driver, String user, String message) {
        assertLoginError(driver, message);
    }

    @Step("Проверка регистрации при оставлении пустых полей, ожидаем ошибку - {message} ")
    public void assertEmptyStrings(WebDriver driver, String message) {
        assertLoginError(driver, message);
    }
}



