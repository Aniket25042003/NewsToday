package com.example.newstoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        // Initialize RecyclerView
        RecyclerView bookmarkRecyclerView = findViewById(R.id.bookmark_recycler_view);
        bookmarkRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load bookmarked articles
        List<NewsItem> bookmarkedArticles = loadBookmarkedArticles(); // Changed to directly load NewsItems

        // Set up adapter with the list of bookmarked articles
        BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(this, bookmarkedArticles);
        bookmarkRecyclerView.setAdapter(bookmarkAdapter);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        ImageButton aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(v -> showAboutDialog());
    }

    /**
     * Loads bookmarked articles from SharedPreferences.
     * @return List of bookmarked NewsItem objects.
     */
    private List<NewsItem> loadBookmarkedArticles() {
        SharedPreferences sharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE);
        String json = sharedPreferences.getString("BookmarkedArticles", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<NewsItem>>() {}.getType();  // Changed to NewsItem
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Displays the About dialog with creator details.
     */
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About")
                .setMessage("This app was created by Aniket Patel. \n\nYou can contact Aniket at: apatel12@ashland.edu")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}