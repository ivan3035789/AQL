package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.data.DataHelper.*;

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
        closeWebDriver();
    }

    @AfterAll
    static void shouldTruncateTables() {
        DataHelper.truncateTables();
        clearBrowserCookies();
    }

    @Order(1)
    @RepeatedTest(3)
    @DisplayName("must log in to your personal account")
    void mustLogInToYourPersonalAccount() {
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Order(2)
    @RepeatedTest(3)
    @DisplayName("a warning message should appear when entering an incorrect code")
    void warningMessageShouldAppearWhenEnteringAnIncorrectCode() {
        val verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerify();
    }

    @Order(3)
    @RepeatedTest(3)
    @DisplayName("a warning message should appear when entering the wrong code three times, the system is blocked")
    void warningMessageShouldAppearWhenEnteringWrongCodeThreeTimesSystemIsBlocked() {
        val verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerifyBlock();
    }

    @Order(4)
    @RepeatedTest(3)
    @DisplayName("must not log into your personal valid code account")
    void mustNotLogIntoYourPersonalValidCodeAccount() {
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = getVerificationCodeFor(authInfo);
        verificationPage.verifyBlock(verificationCode);
    }

    @Order(5)
    @Test
    @DisplayName("must not log into your personal account invalid code")
    void mustNotLogIntoYourPersonalAccountInvalidCode() {
        val verificationPage = loginPage.validLogin(authInfo);
        verificationPage.invalidVerifyBlock();
    }
}


