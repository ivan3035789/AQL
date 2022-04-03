package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.CacheLookup;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPageV1 {
    @CacheLookup
    private final SelenideElement inputLogin = $("[data-test-id=login] input");
    @CacheLookup
    private final SelenideElement inputPassword = $("[data-test-id=password] input");
    @CacheLookup
    private final SelenideElement button = $("[data-test-id=action-login]");


    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        inputLogin.append(info.getLogin());
        inputPassword.append(info.getPassword());
        button.click();
        return new VerificationPage();
    }
}
