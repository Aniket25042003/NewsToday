package com.example.newstoday;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private List<NewsItem> bookmarkedArticles;
    private Context context;

    public BookmarkAdapter(Context context, List<NewsItem> bookmarkedArticles) {
        this.context = context;
        this.bookmarkedArticles = bookmarkedArticles;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_recycler, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        NewsItem newsItem = bookmarkedArticles.get(position);

        holder.newsTitle.setText(newsItem.getTitle());
        holder.newsDescription.setText(newsItem.getDescription());
        Picasso.get()
                .load(newsItem.getImageUrl())
                .placeholder(R.drawable.news_loading)
                .error(R.drawable.general_news)
                .into(holder.newsImage);

        holder.bookmarkIcon.setOnClickListener(v -> {
            removeBookmark(newsItem, position);
        });

        holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_outline);
    }

    @Override
    public int getItemCount() {
        return bookmarkedArticles.size();
    }

    private void removeBookmark(NewsItem newsItem, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("BookmarkedArticles", null);
        Type type = new TypeToken<ArrayList<NewsItem>>() {}.getType();
        List<NewsItem> bookmarkedArticles = gson.fromJson(json, type);

        if (bookmarkedArticles != null && bookmarkedArticles.contains(newsItem)) {
            bookmarkedArticles.remove(newsItem);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("BookmarkedArticles", gson.toJson(bookmarkedArticles));
            editor.apply();

            this.bookmarkedArticles.remove(position);
            notifyItemRemoved(position);

            Toast.makeText(context, "Bookmark removed", Toast.LENGTH_SHORT).show();
        }
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsDescription;
        ImageView newsImage, bookmarkIcon;

        public BookmarkViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsDescription = itemView.findViewById(R.id.news_description);
            newsImage = itemView.findViewById(R.id.news_image);
            bookmarkIcon = itemView.findViewById(R.id.bookmark_icon);
        }
    }
}
