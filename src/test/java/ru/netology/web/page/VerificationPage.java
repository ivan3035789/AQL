package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.CacheLookup;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.invalidCode;

public class VerificationPage {
    @CacheLookup
    private final SelenideElement codeField = $("[data-test-id=code] input");
    @CacheLookup
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");
    @CacheLookup
    private final SelenideElement ConfirmationRequired = $("p");
//    @CacheLookup
//    private static final SelenideElement mistake = $("div.notification__content");
    @CacheLookup
    private static final SelenideElement mistake = $(".notification__title+.notification__content");
    @CacheLookup
    private final SelenideElement error = $("[data-test-id=error-notification] .notification__content");

    public VerificationPage() {
        codeField.shouldBe(visible, Duration.ofSeconds(5));
        String expected = "Необходимо подтверждение";
        String actual = ConfirmationRequired.getText().trim();
        assertEquals(expected, actual);
    }

    public DashboardPage validVerify(DataHelper.VerificationCode verificationCode) {
        codeField.append(verificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }

    public void invalidVerify() {
        codeField.append(invalidCode(0, 999999));
        verifyButton.click();
        mistake.shouldBe(visible, Duration.ofSeconds(5));
        String expected = "Ошибка! Неверно указан код! Попробуйте ещё раз.";
        String actual = mistake.getText().trim();
        assertEquals(expected, actual);
    }

    public void verifyBlock(DataHelper.VerificationCode verificationCode) {
        codeField.append(verificationCode.getCode());
        verifyButton.click();
        error.shouldBe(visible, Duration.ofSeconds(5));
        String expected = "Ошибка! Превышено количество попыток ввода кода!";
        String actual = error.getText().trim();
        assertEquals(expected, actual);
    }

    public void invalidVerifyBlock() {
        codeField.append(invalidCode(0, 999999));
        verifyButton.click();
        error.shouldBe(visible, Duration.ofSeconds(5));
        String expected = "Ошибка! Превышено количество попыток ввода кода!";
        String actual = error.getText().trim();
        assertEquals(expected, actual);
    }
}
