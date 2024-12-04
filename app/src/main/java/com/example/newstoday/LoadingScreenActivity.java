package com.example.newstoday;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoadingScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView loadingImage = findViewById(R.id.loading_image);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(loadingImage, "alpha", 0f, 1f);
        fadeIn.setDuration(1500);
        fadeIn.start();


        new Handler().postDelayed(new Runnable(){
            @Override
                    public void run() {
                        startActivity(new Intent(LoadingScreenActivity.this, MainActivity.class));
                        finish();
            }
        }, 2000);
    }
}