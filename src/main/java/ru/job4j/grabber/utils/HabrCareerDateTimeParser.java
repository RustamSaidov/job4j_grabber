package ru.job4j.grabber.utils;

import java.time.*;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        ZonedDateTime zdt = ZonedDateTime.parse(parse);
        ZoneId zId = ZoneId.of("Europe/Moscow");
        return LocalDateTime.ofInstant(zdt.toInstant(), zId);
    }
}