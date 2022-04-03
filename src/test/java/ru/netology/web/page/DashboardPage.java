package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.CacheLookup;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DashboardPage {
    @CacheLookup
    private final SelenideElement heading = $("[data-test-id=dashboard]");

    public DashboardPage() {
        heading.shouldBe(visible, Duration.ofSeconds(15));
        String expected = "Личный кабинет";
        String actual = heading.getText().trim();
        assertEquals(expected, actual);
    }

}
