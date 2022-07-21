package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.models.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer", SOURCE_LINK);
    public static final int PAGE_QUANTITY = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private static String retrieveDescription(String link) {
        Connection innerConnection = Jsoup.connect(link);
        Document innerDocument;
        try {
            innerDocument = innerConnection.get();
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
        return innerDocument.select(".style-ugc").text();
    }


    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> list = new ArrayList<>();
        for (int i = 0; i <= PAGE_QUANTITY; i++) {
            Connection connection = Jsoup.connect(link + i);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(getElementConsumer(list));
        }
        return list;
    }

    private Consumer<Element> getElementConsumer(List<Post> list) {
        return row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = titleElement.child(0);
            String vacancyName = titleElement.text();
            String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            String description = retrieveDescription(vacancyLink);
            Element dateElement = row.select(".vacancy-card__date").first();
            Element timeElement = dateElement.child(0);
            String datetime = String.format(timeElement.attr("datetime"));
            Post post = new Post(vacancyName, vacancyLink, description, dateTimeParser.parse(datetime));
            list.add(post);
        };
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> list = habrCareerParse.list(PAGE_LINK + "?page=");
    }
}