package com.example.newstoday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<Article> articleList;
    private Context context;

    NewsAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.newsTitle.setText(article.getTitle());
        holder.newsDescription.setText(article.getDescription());
        Picasso.get().load(article.getUrlToImage()).error(R.drawable.news_loading).placeholder(R.drawable.news_loading).into(holder.newsImage);

        NewsItem newsItem = new NewsItem(article.getTitle(), article.getDescription(), article.getUrl(), article.getUrlToImage());
        boolean isBookmarked = isArticleBookmarked(newsItem);
        updateBookmarkIcon(holder.bookmarkIcon, isBookmarked);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NewsPage.class);
            intent.putExtra("url", article.getUrl());
            v.getContext().startActivity(intent);
        });

        holder.bookmarkIcon.setOnClickListener(v -> {
            toggleBookmark(newsItem);
            updateBookmarkIcon(holder.bookmarkIcon, !isBookmarked);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    private void toggleBookmark(NewsItem newsItem) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("BookmarkedArticles", null);
        Type type = new TypeToken<ArrayList<NewsItem>>() {}.getType();
        List<NewsItem> bookmarkedArticles = gson.fromJson(json, type);

        if (bookmarkedArticles == null) {
            bookmarkedArticles = new ArrayList<>();
        }

        if (bookmarkedArticles.contains(newsItem)) {
            bookmarkedArticles.remove(newsItem);
        } else {
            bookmarkedArticles.add(newsItem);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BookmarkedArticles", gson.toJson(bookmarkedArticles));
        editor.apply();
    }

    private boolean isArticleBookmarked(NewsItem newsItem) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("BookmarkedArticles", null);
        Type type = new TypeToken<ArrayList<NewsItem>>() {}.getType();
        List<NewsItem> bookmarkedArticles = gson.fromJson(json, type);

        return bookmarkedArticles != null && bookmarkedArticles.contains(newsItem);
    }

    private void updateBookmarkIcon(ImageView bookmarkIcon, boolean isBookmarked) {
        bookmarkIcon.setImageResource(isBookmarked ? R.drawable.ic_bookmark_outline : R.drawable.ic_bookmark);
    }

    public void updateRecyclerView(List<Article> newArticles) {
        // Replace the existing dataset with the new list
        this.articleList.clear(); // Assuming `articleList` is your adapter's dataset
        this.articleList.addAll(newArticles);
        notifyDataSetChanged(); // Notify adapter to refresh the RecyclerView
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsTitle;
        TextView newsDescription;
        ImageView bookmarkIcon;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            bookmarkIcon = itemView.findViewById(R.id.bookmark_icon);
        }
    }
}
