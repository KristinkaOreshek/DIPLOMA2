package ru.netology.webservice.page;

import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
public class CreditCardForm {
    private SelenideElement headingCardType = $("[class='heading heading_size_m heading_theme_alfa-on-white']");
    private SelenideElement cardNumberField =$(byText("Номер карты")).parent().$(".input__control");
    private SelenideElement monthField = $(byText("Месяц")).parent().$(".input__control");
    private SelenideElement yearField = $(byText("Год")).parent().$(".input__control");
    private SelenideElement ownerField = $(byText("Владелец")).parent().$(".input__control");
    private SelenideElement codeField = $(byText("CVC/CVV")).parent().$(".input__control");
    private SelenideElement buttonContinue = $(byText("Продолжить"));
    private SelenideElement successNotification = $$(".notification").first();
    private SelenideElement errorNotification = $$(".notification").last();
    private SelenideElement closeButtonErrorNotification = $$(".notification").last().$$("button[class*=notification__closer]").get(0);
    private SelenideElement errorMessageCardNumberField = $(byText("Номер карты")).parent().$(".input__sub");
    private SelenideElement errorMessageMonthField = $(byText("Месяц")).parent().$(".input__sub");
    private SelenideElement errorMessageYearField = $(byText("Год")).parent().$(".input__sub");
    private SelenideElement errorMessageOwnerField = $(byText("Владелец")).parent().$(".input__sub");
    private SelenideElement errorMessageCodeField = $(byText("CVC/CVV")).parent().$(".input__sub");

    public void checkVisibleHeadingCreditCard(String headingCard) {
        headingCardType.shouldBe(visible).shouldHave(text(headingCard));
    }

    //Сообщения от сервиса

    public void successNotificationForm(String successNote) {
        successNotification.shouldBe(visible).shouldHave(text(successNote));
    }

    public void errorNotificationForm(String errorNote) {
        errorNotification.shouldBe(visible).shouldHave(text(errorNote));
    }

    //Сообщения при заполнении поля "Номер карты"

    public void errorMessageInvalidCardNumberField(String errorMessage) {
        errorMessageCardNumberField.shouldBe(visible).shouldHave(text(errorMessage));
    }


    //Сообщения при заполнении поля "Месяц"

    public void errorMessageInvalidMonthField(String errorNote) {
        errorMessageMonthField.shouldBe(visible).shouldHave(text(errorNote));
    }

    //Сообщения при заполнении поля "Год"

    public void errorMessageInvalidYearField(String errorNote) {
        errorMessageYearField.shouldBe(visible).shouldHave(text(errorNote));
    }

    //Сообщения при заполнении поля "Владелец"

    public void errorMessageInvalidOwnerField(String errorNote) {
        errorMessageOwnerField.shouldBe(visible).shouldHave(text(errorNote));
    }

    public void errorMessageOwnerFieldEmptyWhenCVCTest() {
        errorMessageOwnerField.shouldNotBe(visible);
    }

    //поле CVC

    public void errorMessageInvalidCodeField(String errorNote) {
        errorMessageCodeField.shouldBe(visible).shouldHave(text(errorNote));
    }

    public void fillOutFields(String cardNumber, String month, String year, String owner, String code) {
        cardNumberField.setValue(cardNumber);
        monthField.setValue(month);
        yearField.setValue(year);
        ownerField.setValue(owner);
        codeField.setValue(code);
        buttonContinue.click();
    }

    public void closeErrorNotification() {
        closeButtonErrorNotification.click();
        successNotification.shouldNotBe(visible);
    }
}