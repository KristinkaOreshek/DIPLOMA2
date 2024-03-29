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
import ru.netology.webservice.page.CreditCardForm;
import ru.netology.webservice.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class NegativeTestsForCreditForm {
    CreditCardForm creditCardForm;

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
        creditCardForm = mainPage.buyWithCredit();
        creditCardForm.checkVisibleHeadingCreditCard("Кредит по данным карты");
    }

    // Отправка пустой формы
    @Test
    void shouldNotBuyCreditWithEmptyForm() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var month = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var year = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var owner = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var code = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        creditCardForm.errorMessageInvalidMonthField("Неверный формат");
        creditCardForm.errorMessageInvalidYearField("Неверный формат");
        creditCardForm.errorMessageInvalidOwnerField("Поле обязательно для заполнения");
        creditCardForm.errorMessageInvalidCodeField("Неверный формат");
        creditCardForm.errorMessageInvalidCardNumberField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Месяц"

    @Test
    void shouldNotBuyCreditWithCardMonthZeroes() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidFieldFormat(0, 0, 0, 2, 0);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidMonthField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithCardMonthLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 2);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidMonthField("Неверный формат");
        assertFalse(DatabaseHelper.tableCreditRequestHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    @Test
    void shouldNotBuyCreditWithCardExpiredMonth() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(-1);
        var year = DataHelper.getYear(0);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidMonthField("Неверно указан срок действия карты");
        assertFalse(DatabaseHelper.tableCreditRequestHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithCardInvalidMonthLongerLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidFieldFormat(3, 0, 0, 0, 0);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidMonthField("Неверный формат");
        assertFalse(DatabaseHelper.tableCreditRequestHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithCardInvalidMonthLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidFieldFormat(1, 0, 0, 0, 0);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidMonthField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithCardNonexistentMonth() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getInvalidMonth();
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidMonthField("Неверно указан срок действия карты");
        assertFalse(DatabaseHelper.tableCreditRequestHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Год"

    @Test
    void shouldNotBuyCreditWithYearFieldZeroes() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getInvalidFieldFormat(0, 0, 0, 2, 0);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidYearField("Истёк срок действия карты");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithYearFieldLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getInvalidFieldFormat(0, 0, 1, 0, 1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidYearField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    @Test
    void shouldNotBuyCreditWithCardExpiredYear() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(1);
        var year = DataHelper.getYear(-1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidYearField("Истёк срок действия карты");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithYearInvalidLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getInvalidFieldFormat(1, 0, 0, 0, 0);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidYearField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Владелец"
    @Test
    void shouldNotBuyCreditWithEmptyOwnerField() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidOwnerField("Поле обязательно для заполнения");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithOwnerFieldCyrillicLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("ru");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidOwnerField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithOwnerFieldDigitsAndSymbols() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(3);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getInvalidFieldFormat(5, 5, 0, 0, 0);
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidOwnerField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithOwnerFieldTooLong() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 50);
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidOwnerField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithOwnerFieldOneLetter() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 1);
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidOwnerField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Код"
    @Test
    void shouldNotBuyCreditWithEmptyCodeField() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(3);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidCodeField("Поле обязательно для заполнения");
        creditCardForm.errorMessageOwnerFieldEmptyWhenCVCTest();
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithCodeLetters() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(1);
        var year = DataHelper.getYear(1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getInvalidFieldFormat(0, 0, 1, 0, 2);
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidCodeField("Неверный формат");
        creditCardForm.errorMessageOwnerFieldEmptyWhenCVCTest();
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithCodeSymbols() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getInvalidFieldFormat(0, 3, 0, 0, 0);
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidCodeField("Неверный формат");
        creditCardForm.errorMessageOwnerFieldEmptyWhenCVCTest();
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithCodeInvalidLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(3);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getInvalidFieldFormat(2, 0, 0, 0, 0);
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidCodeField("Неверный формат");
        creditCardForm.errorMessageOwnerFieldEmptyWhenCVCTest();
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }

    //  Тестирование поля "Номер карты"
    @Test
    void shouldNotBuyCreditWithEmptyCardNumberField() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(0, 0, 0, 0, 0);
        var month = DataHelper.getMonth(1);
        var year = DataHelper.getYear(3);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidCardNumberField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
    @Test
    void shouldNotBuyCreditWithInvalidCardNumberZeroes() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(0, 0, 0, 16, 0);
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidCardNumberField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }


    @Test
    void shouldNotBuyCreditWithInvalidCardNumberLength() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getInvalidFieldFormat(12, 0, 0, 0, 0);
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorMessageInvalidCardNumberField("Неверный формат");
        assertFalse(DatabaseHelper.tablePaymentHasRows());
        assertFalse(DatabaseHelper.tableOrderHasRows());
    }
}