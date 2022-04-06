package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.CacheLookup;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class VerificationPage {
    @CacheLookup
    private final SelenideElement codeField = $("[data-test-id=code] input");
    @CacheLookup
    private final SelenideElement button = $("[data-test-id=action-verify]");
    @CacheLookup
    private final SelenideElement confirmationRequired = $("p");
    @CacheLookup
    private final SelenideElement error = $("[data-test-id=error-notification] .notification__content");


    public VerificationPage() {
        codeField.shouldBe(visible, Duration.ofSeconds(15));
        assertEquals("Необходимо подтверждение", confirmationRequired.getText().trim());
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeField.append(verificationCode.getCode());
        button.click();
        return new DashboardPage();
    }

    public void invalidVerify() {
        codeField.append(invalidCode(0, 9999));
        button.click();
        error.shouldBe(visible, Duration.ofSeconds(15));
        assertEquals("Ошибка! Неверно указан код! Попробуйте ещё раз.", error.getText().trim());
    }

    public void verifyBlock(DataHelper.VerificationCode verificationCode) {
        codeField.append(verificationCode.getCode());
        button.click();
        error.shouldBe(visible, Duration.ofSeconds(15));
        assertEquals("Ошибка! Превышено количество попыток ввода кода!", error.getText().trim());
    }
}
