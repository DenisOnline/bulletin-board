package ru.bulletin_board.bulletin_board.utils;

import ru.bulletin_board.bulletin_board.models.Post;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceFormatting {

    private final static DecimalFormat PRICE_FORMATTER = new DecimalFormat("#,###");

    public static String priceFormatting(int price) {
        return PRICE_FORMATTER.format(price);
    }

    public static Map<Long, String> priceFromPosts(List<Post> posts) {
        Map<Long, String> priceMap = new HashMap<>();
        posts.forEach(post -> priceMap.put(post.getId(), priceFormatting(post.getPrice())));
        return priceMap;
    }
}
