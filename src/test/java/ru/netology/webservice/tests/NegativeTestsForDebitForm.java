package ru.netology.webservice.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.webservice.data.DataHelper;
import ru.netology.webservice.data.DatabaseHelper;
import ru.netology.webservice.page.DebitCardForm;
import ru.netology.webservice.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class NegativeTestsForDebitForm {
    DebitCardForm debitCardForm;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        DatabaseHelper.clearDB();
        open("http://localhost:8080");
        MainPage mainPage = new MainPage();
        debitCardForm = mainPage.buyWithCard();
        debitCardForm.checkVisibleHeadingDebitCard("Оплата по карте");
    }

    // Отправка пустой формы
    @Test
    void shouldNotBuyDebitWithEmptyForm() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var month = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var year = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var owner = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var code = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        debitCardForm.errorMessageInvalidCardNumberField("Поле обязательно для заполнения");
        debitCardForm.errorMessageInvalidMonthField("Поле обязательно для заполнения");
        debitCardForm.errorMessageInvalidYearField("Поле обязательно для заполнения");
        debitCardForm.errorMessageInvalidOwnerField("Поле обязательно для заполнения");
        debitCardForm.errorMessageInvalidCodeField("Поле обязательно для заполнения");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Месяц"

    @Test
    void shouldNotBuyDebitWithCardMonthZeroes() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidFieldFormat(0, 0, 0, 2, 0);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidMonthField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithCardMonthLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidMonthField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    @Test
    void shouldNotBuyDebitWithCardExpiredMonth() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(-1);
        var year = DataHelper.getYear(0);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidMonthField("Неверно указан срок действия карты");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithCardInvalidMonthLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidFieldFormat(1, 0, 0, 0, 0);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidMonthField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithCardNonexistentMonth() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidMonth();
        var year = DataHelper.getYear(1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidMonthField("Неверно указан срок действия карты");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Год"

    @Test
    void shouldNotBuyDebitWithYearFieldZeroes() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getInvalidFieldFormat(0, 0, 0, 2, 0);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidYearField("Истёк срок действия карты");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithYearFieldLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getInvalidFieldFormat(0, 0, 1, 0, 1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidYearField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    @Test
    void shouldNotBuyDebitWithCardExpiredYear() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(1);
        var year = DataHelper.getYear(-1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidYearField("Истёк срок действия карты");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithYearInvalidLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getInvalidFieldFormat(1, 0, 0, 0, 0);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidYearField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Владелец"
    @Test
    void shouldNotBuyDebitWithEmptyOwnerField() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidOwnerField("Поле обязательно для заполнения");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithOwnerFieldCyrillicLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("ru");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidOwnerField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithOwnerFieldDigitsAndSymbols() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getInvalidFieldFormat(5, 5, 0, 0, 0);
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidOwnerField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    @Test
    void shouldNotBuyDebitWithOwnerFieldOneLetter() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 1);
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidOwnerField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Код"


    @Test
    void shouldNotBuyDebitWithCodeSymbols() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(3);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getInvalidFieldFormat(0, 3, 0, 0, 0);
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidCodeField("Неверный формат");
        debitCardForm.errorMessageOwnerFieldEmptyWhenCVCTest();
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithCodeInvalidLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(3);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getInvalidFieldFormat(2, 0, 0, 0, 0);
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidCodeField("Неверный формат");
        debitCardForm.errorMessageOwnerFieldEmptyWhenCVCTest();
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Номер карты"

    @Test
    void shouldNotBuyDebitWithInvalidCardNumberZeroes() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(0, 0, 0, 16, 0);
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidCardNumberField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyDebitWithInvalidCardNumberLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(0, 0, 8, 0, 8);
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidCardNumberField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    @Test
    void shouldNotBuyDebitWithInvalidCardNumberLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(12, 0, 0, 0, 0);
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorMessageInvalidCardNumberField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
}