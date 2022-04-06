package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.CacheLookup;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginPageV1 {
    @CacheLookup
    private final SelenideElement inputLogin = $("[data-test-id=login] input");
    @CacheLookup
    private final SelenideElement inputPassword = $("[data-test-id=password] input");
    @CacheLookup
    private final SelenideElement button = $("[data-test-id=action-login]");
    @CacheLookup
    private final SelenideElement error = $("[data-test-id=error-notification] .notification__content");


    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        inputLogin.append(info.getLogin());
        inputPassword.append(info.getPassword());
        button.click();
        return new VerificationPage();
    }

    public void invalidPassword(DataHelper.AuthInfo info) {
        inputLogin.append(info.getLogin());
        inputPassword.append(info.getPassword());
        button.click();
        error.shouldBe(visible, Duration.ofSeconds(15));
        assertEquals("Ошибка! Неверно указан логин или пароль", error.getText().trim());
    }
}
