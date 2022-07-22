package ru.job4j.grabber;

import ru.job4j.grabber.models.Post;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.List;

public interface Parse {

    List<Post> list(String link);
}