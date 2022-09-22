package ru.netology.test;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(registeredUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $("#root").shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(180));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(notRegisteredUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(notRegisteredUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(180)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(blockedUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(blockedUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(180)).shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(wrongLogin);
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(registeredUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(180)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(wrongPassword);
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(180)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}