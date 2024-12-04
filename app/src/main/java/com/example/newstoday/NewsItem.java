package com.example.newstoday;

import java.util.Objects;

public class NewsItem {
    private String title;
    private String description;
    private String url;
    private String imageUrl;

    // Constructor
    public NewsItem(String title, String description, String url, String urlToImage) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = urlToImage;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    // Override equals and hashCode based on the URL to uniquely identify an article
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsItem newsItem = (NewsItem) o;
        return url.equals(newsItem.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
