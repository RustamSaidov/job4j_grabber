package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 5; i++) {
            Connection connection = Jsoup.connect(PAGE_LINK + "?page=" + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String description = retrieveDescription(link);
                Element dateElement = row.select(".vacancy-card__date").first();
                Element timeElement = dateElement.child(0);
                String datetime = String.format(timeElement.attr("datetime"));
                HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.printf("%s %s %s %s%n ",
                        timeParser.parse(datetime).format(formatter),
                        vacancyName,
                        description,
                        link
                );
            });
        }
    }

    private static String retrieveDescription(String link) {
        Connection innerConnection = Jsoup.connect(link);
        Document innerDocument = null;
        try {
            innerDocument = innerConnection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return innerDocument.select(".style-ugc").text();
    }
}