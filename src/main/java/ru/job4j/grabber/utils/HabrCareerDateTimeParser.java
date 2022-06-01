package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse(parse, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz"));
    }

    public static void main(String[] args) {
        HabrCareerDateTimeParser hb = new HabrCareerDateTimeParser();
        System.out.println(hb.parse("2022-05-31T16:57:31+03:00"));
    }
}