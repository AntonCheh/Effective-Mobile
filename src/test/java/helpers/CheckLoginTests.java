package helpers;

import assertions.LoginAssertions;

import constants.Messages;
import pages.LoginPage;
import sources.DataForLoginTests;
import sources.LoginTestsSource;


public class CheckLoginTests extends BaseTest implements DataForLoginTests {

    private final LoginAssertions loginAssertions;


    public CheckLoginTests(LoginAssertions loginAssertions) {
        this.loginAssertions = loginAssertions;
    }

    public CheckLoginTests() {
        this(new LoginAssertions());
    }

    public void commonMethod (LoginTestsSource testsSource) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testsSource.login(), testsSource.password());
    }

    /**
     * 1. Успешный логин
     */
    public void execute(LoginTestsSource testsSource) {
        commonMethod(testsSource);
        loginAssertions.assertLoginCorrect(driver, testsSource.login(), testsSource.password());
    }

    /**
     * 2. Логин с неверным паролем
     */
    public void executeLoginIncorrect(LoginTestsSource testsSource) {
        commonMethod(testsSource);
        loginAssertions.assertLoginWrong(driver, Messages.WRONG_PASSWORD);
    }

    /**
     * 3. Логин заблокированного пользователя
     */
    public void executeBlockedUser(LoginTestsSource testsSource) {
        commonMethod(testsSource);
        loginAssertions.assertBlockedUser(driver, testsSource.login(), Messages.LOCKED_OUT_USER);
    }

    /**
     * 4. Логин с пустыми полями
     */
    public void executeEmpty(LoginTestsSource testsSource) {
        commonMethod(testsSource);
        loginAssertions.assertEmptyStrings(driver, Messages.USERNAME_REQUIRED);
    }

    /**
     * 5. Логин performance_glitch_user (с замером времени)
     */
    public long executePerformanceGlitch(LoginTestsSource testsSource) {
        LoginPage loginPage = new LoginPage(driver);

        long startTime = System.currentTimeMillis();
        loginPage.login(testsSource.login(), testsSource.password());
        long endTime = System.currentTimeMillis();

        loginAssertions.assertLoginCorrect(driver, testsSource.login(), testsSource.password());
        return endTime - startTime;
    }
}
