package ru.netology.webservice.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private SelenideElement buttonBuy = $$("[class='button__content']").first();
    private SelenideElement buttonCreditBuy = $$("[class='button__content']").last();


    public DebitCardForm buyWithCard() {
        buttonBuy.click();
        return new DebitCardForm();
    }

    public CreditCardForm buyWithCredit() {
        buttonCreditBuy.click();
        return new CreditCardForm();
    }
}
