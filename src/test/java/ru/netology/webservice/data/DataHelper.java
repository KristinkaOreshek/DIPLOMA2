package ru.netology.webservice.data;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getMonth(int month) {
        return YearMonth.now().plusMonths(month).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getInvalidMonth() {
        return "13";
    }

    public static String getYear(int year) {
        return Year.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getValidCode() {
        return RandomStringUtils.randomNumeric(3);
    }

    public static String getInvalidFieldFormat
            (int countDigit, int countSpecialSymbol, int countCyrillicLetters, int countDigitZero, int countLatinLetter) {
        return RandomStringUtils.randomNumeric(countDigit) + RandomStringUtils.randomAlphabetic(countLatinLetter) + RandomStringUtils.random(countDigitZero, '0')
                + RandomStringUtils.random(countSpecialSymbol, '№', '@', '&', '$', '%') + RandomStringUtils.random(countCyrillicLetters, 'а', 'б', 'в', 'Г', 'Д', 'Е');
    }
}
