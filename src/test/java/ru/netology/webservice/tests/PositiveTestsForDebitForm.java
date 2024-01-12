package ru.netology.webservice.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.webservice.data.DatabaseHelper;
import ru.netology.webservice.data.DataHelper;
import ru.netology.webservice.page.DebitCardForm;
import ru.netology.webservice.page.MainPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;
public class PositiveTestsForDebitForm {
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
        debitCardForm.checkVisibleHeadingDebitCard();
    }

    @Test
    void shouldBuyDebitWithApprovedCard() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getApprovedCardNumber();
        var month = DataHelper.getMonth(1);
        var year = DataHelper.getYear(1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.successNotificationForm();
        assertTrue(DatabaseHelper.tablePaymentHasRows());
        assertEquals("APPROVED", DatabaseHelper.getTransactionPaymentStatus());
    }

    @Test
    void shouldNotBuyDebitWithDeclinedCard() {
        Configuration.holdBrowserOpen = true;
        var cardNumber = DataHelper.getDeclinedCardNumber();
        var month = DataHelper.getMonth(1);
        var year = DataHelper.getYear(1);
        var owner = DataHelper.getOwner("en");
        var code = DataHelper.getValidCode();
        debitCardForm.fillOutFields(cardNumber, month, year, owner, code);
        debitCardForm.errorNotificationForm();
        assertTrue(DatabaseHelper.tablePaymentHasRows());
        assertEquals("DECLINED", DatabaseHelper.getTransactionPaymentStatus());
    }
}
