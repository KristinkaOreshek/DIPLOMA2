package ru.netology.webservice.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.webservice.data.DataHelper;
import ru.netology.webservice.data.DatabaseHelper;
import ru.netology.webservice.page.CreditCardForm;
import ru.netology.webservice.page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class PositiveTestsForCreditForm {
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
        creditCardForm.checkVisibleHeadingCreditCard();
    }

    @Test
    void shouldBuyCreditWithApprovedCard() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(0);
        var year = DataHelper.getYear(0);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.successNotificationForm();
        assertTrue(DatabaseHelper.tableCreditRequestHasRows());
        assertEquals("APPROVED", DatabaseHelper.getTransactionCreditRequestStatus());
    }

    @Test
    void shouldNotBuyCreditWithDeclinedCard() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getDeclinedCardNumber();
        var month = DataHelper.getMonth(2);
        var year = DataHelper.getYear(2);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        creditCardForm.fillOutFields(cardNumber, month, year, owner, code);
        creditCardForm.errorNotificationForm();
        assertTrue(DatabaseHelper.tableCreditRequestHasRows());
        assertEquals("DECLINED", DatabaseHelper.getTransactionCreditRequestStatus());
    }
}
