package ru.bulletin_board.bulletin_board.utils;

import ru.bulletin_board.bulletin_board.models.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PhoneNumberFormatting {

    public static String  formatPhoneNumber(String phoneNumber) {
        String cleanedNumber = phoneNumber.replaceAll("[^0-9]", "");

        /**
         * Используем регулярное выражение для форматирования номера
         * (\d{1,3}) - захватывает первую, вторую и третью цифры (код страны)
         * (\d{3}) - захватывает следующие три цифры (код города/региона)
         * (\d{2}) - захватывает следующие две цифры
         * (\d{2}) - захватывает последние две цифры
         */
        String formattedNumber = cleanedNumber.replaceAll("(\\d{1})(\\d{3})(\\d{3})(\\d{2})(\\d{2})", "+$1 ($2) $3-$4-$5");
        return formattedNumber;
    }
}
