package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV1;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;
import static ru.netology.web.data.DataHelper.authInfoOnlyVasya;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    LoginPageV1 loginPage = new LoginPageV1();
    DataHelper.AuthInfo authInfoRandomLogin = authInfo();
    String status = userStatus();
    DataHelper.AuthInfo authInfoOnlyVasya = authInfoOnlyVasya();


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
        val verificationPage = loginPage.validLogin(authInfoRandomLogin);
        val verificationCode = getVerificationCodeFor(authInfoRandomLogin);

        verificationPage.validVerify(verificationCode);
        assertEquals("active", status);
        DataHelper.auth_codes();
        clearBrowserCookies();
    }

    @Order(2)
    @RepeatedTest(3)
    @DisplayName("a warning message should appear when entering an incorrect code")
    void warningMessageShouldAppearWhenEnteringAnIncorrectCode() {
        val verificationPage = loginPage.validLogin(authInfoOnlyVasya);

        verificationPage.invalidVerify();
        assertEquals("active", status);
    }

    @Order(3)
    @Test
    @DisplayName("a warning message should appear about exceeding the number of incorrect code entries")
    void aWarningMessageShouldAppearAboutExceedingTheNumberOfIncorrectCodeEntries() {
        val verificationPage = loginPage.validLogin(authInfoOnlyVasya);
        val verificationCode = getVerificationCodeFor(authInfoOnlyVasya);

        verificationPage.verifyBlock(verificationCode);
        assertEquals("active", status);
    }

    @Order(4)
    @RepeatedTest(3)
    @DisplayName("A warning message should appear if the password is entered incorrectly")
    void aWarningMessageShouldAppearIfThePasswordIsEnteredIncorrectly() {
        loginPage.invalidPassword(invalidPasswordAuthInfo());
        assertEquals("active", status);
    }

    @Order(5)
    @Test
    @DisplayName("The user must be blocked")
    void theUserMustBeBlocked() {
        val verificationPage = loginPage.validLogin(authInfoRandomLogin);
        val verificationCode = getVerificationCodeFor(authInfoRandomLogin);

        verificationPage.verifyBlock(verificationCode);
        assertEquals("block", status);
    }
}


