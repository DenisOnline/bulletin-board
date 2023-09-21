package ru.bulletin_board.bulletin_board.utils;

import ru.bulletin_board.bulletin_board.models.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DataFormatting {

    private final static DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", new Locale("ru"));

    public static String timeFormatting(LocalDateTime data) {
        return data.format(DATA_FORMAT);
    }

    public static Map<Long, String> timeFromPosts(List<Post> posts) {
        Map<Long, String> dataMap = new HashMap<>();
        posts.forEach(post -> dataMap.put(post.getId(), timeFormatting(post.getDateTime())));
        return dataMap;
    }

}
