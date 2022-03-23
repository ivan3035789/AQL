package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.data.DataHelper.authInfo;
import static ru.netology.web.data.DataHelper.getVerificationCodeFor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterEach
    void close() {
        closeWebDriver();
    }

    @Order(3)
    @Test
    @DisplayName("a warning message should appear when entering an incorrect code")
    void warningMessageShouldAppearWhenEnteringAnIncorrectCode() {
        var loginPage = new LoginPageV1();
        var verificationPage = loginPage.validLogin(authInfo());
        verificationPage.invalidVerifyBlock();

    }

    @Order(1)
    @RepeatedTest(2)
    @DisplayName("must log in to your personal account")
    void mustLogInToYourPersonalAccount() {
        var loginPage = new LoginPageV1();
        var authInfo = authInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);

    }

    @Order(2)
    @RepeatedTest(2)
    @DisplayName("a warning message should appear when entering the wrong code three times, the system is blocked")
    void warningMessageShouldAppearWhenEnteringWrongCodeThreeTimesSystemIsBlocked() {
        var loginPage = new LoginPageV1();
        var verificationPage = loginPage.validLogin(authInfo());
        verificationPage.invalidVerify();
    }

    @AfterAll
    static void shouldTruncateTables() {
        clearBrowserCookies();
        DataHelper.truncateTables();

    }
}
