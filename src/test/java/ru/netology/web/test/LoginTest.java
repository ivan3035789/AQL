package ru.netology.web.test;

import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.data.DataHelper.authInfo;
import static ru.netology.web.data.DataHelper.getVerificationCodeFor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

LoginPageV1 loginPage = new LoginPageV1();
DataHelper.AuthInfo authInfo = authInfo();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterEach
    void close() {
        clearBrowserCookies();
        closeWebDriver();
    }

    @Order(1)
    @RepeatedTest(3)
    @DisplayName("must log in to your personal account")
    void mustLogInToYourPersonalAccount() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Order(2)
    @RepeatedTest(3)
    @DisplayName("a warning message should appear when entering an incorrect code")
    void warningMessageShouldAppearWhenEnteringAnIncorrectCode() {
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerify();

    }

    @Order(3)
    @RepeatedTest(2)
    @DisplayName("a warning message should appear when entering the wrong code three times, the system is blocked")
    void warningMessageShouldAppearWhenEnteringWrongCodeThreeTimesSystemIsBlocked() {
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerifyBlock();
    }

    @Order(4)
    @RepeatedTest(2)
    @DisplayName("must not log into your personal account")
    void mustNotLogIntoYourPersonalAccount() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.verifyBlock(verificationCode);
    }

    @AfterAll
    static void shouldTruncateTables() {
        DataHelper.truncateTables();

    }
}


