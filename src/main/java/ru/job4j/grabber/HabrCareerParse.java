package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> rsl = new ArrayList<>();
        try {
            Connection connection = Jsoup.connect(link);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element dataElement = row.select(".vacancy-card__date").first();
                String vacancyData = dataElement.child(0).attr("datetime");
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String linkEl = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String desc = "";
                try {
                    desc = retrieveDescription(linkEl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Post post = new Post(
                        vacancyName,
                        linkEl,
                        desc,
                        dateTimeParser.parse(vacancyData)
                );
                rsl.add(post);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element descriptionElement = document
                .select(".job_show_description__vacancy_description")
                .first();
        return descriptionElement.text();
    }

    public static void main(String[] args) {
        HabrCareerParse hcp = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> list = new ArrayList<>();
                hcp.list(PAGE_LINK);
        for (int i = 1; i <= 5; i++) {
            list.addAll(hcp.list(String.format("%s%s", PAGE_LINK, i)));
        }
        list.forEach(System.out::println);
    }
}