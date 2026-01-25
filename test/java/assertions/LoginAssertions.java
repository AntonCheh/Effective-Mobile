package assertions;

import constants.ErrorMessages;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import pages.LoginPage;
import pages.ProductsPage;

import static org.assertj.core.api.Assertions.assertThat;


public class LoginAssertions {

    @Step("Проверка успешного логина")
    public void assertLoginCorrect(WebDriver driver) {
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

    @Step("Проверка неверного логина")
    public void assertLoginWrong(WebDriver driver) {
        assertLoginError(driver, ErrorMessages.WRONG_PASSWORD);

    }

    @Step("Проверка заблокированного пользователя")
    public void assertBlockedUser(WebDriver driver) {
        assertLoginError(driver, ErrorMessages.LOCKED_OUT_USER);
    }

    @Step("Проверка пустых полей")
    public void assertEmptyStrings(WebDriver driver) {
        assertLoginError(driver, ErrorMessages.USERNAME_REQUIRED);
    }
}



