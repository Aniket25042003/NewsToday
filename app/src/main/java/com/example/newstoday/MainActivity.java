package com.example.newstoday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsAdapter newsAdapter;
    LinearProgressIndicator progressIndicator;
    LinearLayout category1Layout, category2Layout, category3Layout, category4Layout, category5Layout, category6Layout, category7Layout;
    SearchView searchView;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ImageButton menuButton = findViewById(R.id.menu_button);
        if (menuButton != null) {
            menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }
        else{
            Log.e("MenuButton", "menuButton not found");
        }
        ImageButton aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(v -> {
            showAboutDialog();
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else if (id == R.id.menu_bookmarks) {
                    startActivity(new Intent(MainActivity.this, BookmarkActivity.class));
                }

                return true; // Ensure the menu item selection is handled
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        progressIndicator = findViewById(R.id.progress_bar);
        category1Layout = findViewById(R.id.category1_layout);
        category2Layout = findViewById(R.id.category2_layout);
        category3Layout = findViewById(R.id.category3_layout);
        category4Layout = findViewById(R.id.category4_layout);
        category5Layout = findViewById(R.id.category5_layout);
        category6Layout = findViewById(R.id.category6_layout);
        category7Layout = findViewById(R.id.category7_layout);
        searchView = findViewById(R.id.search_view);
        category1Layout.setOnClickListener(this);
        category2Layout.setOnClickListener(this);
        category3Layout.setOnClickListener(this);
        category4Layout.setOnClickListener(this);
        category5Layout.setOnClickListener(this);
        category6Layout.setOnClickListener(this);
        category7Layout.setOnClickListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Progress(true);
                getNews("GENERAL", s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        setRecyclerView();
        getNews("GENERAL", null);
    }

    void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, articleList);
        recyclerView.setAdapter(newsAdapter);
    }

    void Progress(boolean visible){
        if(visible){
            progressIndicator.setVisibility(View.VISIBLE);
        }
        else{
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    void getNews(String category, String query) {
        Progress(true);
        NewsApiClient newsApiClient = new NewsApiClient("API_KEY");

        // Build the request
        TopHeadlinesRequest.Builder requestBuilder = new TopHeadlinesRequest.Builder()
                .language("en")
                .country("us");  // Add country parameter

        if (query != null && !query.isEmpty()) {
            requestBuilder.q(query);
        } else if (category != null) {
            requestBuilder.category(category);
        }

        newsApiClient.getTopHeadlines(
                requestBuilder.build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse articlesResponse) {
                        runOnUiThread(() -> {
                            Progress(false);
                            articleList = articlesResponse.getArticles();
                            newsAdapter.updateRecyclerView(articleList);
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        runOnUiThread(() -> {
                            Progress(false);
                            Log.e("API ERROR", "Error fetching news", throwable);
                            if (throwable.getMessage() != null) {
                                Log.e("API ERROR DETAILS", throwable.getMessage());
                            }
                            // Show error message to user
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Failed to load news. Please check your internet connection and try again.")
                                    .setPositiveButton("Retry", (dialog, which) -> getNews(category, query))
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        });
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        Progress(true);
        if(view.getId() == R.id.category7_layout){
            getNews("GENERAL", null);
        }
        else if(view.getId() == R.id.category1_layout){
            getNews("BUSINESS", null);
        }
        else if(view.getId() == R.id.category2_layout){
            getNews("ENTERTAINMENT", null);
        }
        else if(view.getId() == R.id.category3_layout){
            getNews("HEALTH", null);
        }
        else if(view.getId() == R.id.category4_layout){
            getNews("SCIENCE", null);
        }
        else if(view.getId() == R.id.category5_layout){
            getNews("SPORTS", null);
        }
        else if(view.getId() == R.id.category6_layout){
            getNews("TECHNOLOGY", null);
        }
        else{
            getNews("GENERAL", null);
        }
    }
    /**
     * Displays the About dialog with creator details.
     */
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About")
                .setMessage("This app was created by Aniket Patel. \n\nYou can contact Aniket at: apatel12@ashland.edu")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())  // Close dialog on OK
                .create()
                .show();
    }
}