package ru.job4j.grabber.utils;

import java.time.*;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        return ZonedDateTime.parse(parse).toLocalDateTime();
    }
}