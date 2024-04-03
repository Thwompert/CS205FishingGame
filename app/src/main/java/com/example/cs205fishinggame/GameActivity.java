package com.example.cs205fishinggame;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import com.airbnb.lottie.LottieAnimationView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
//        setContentView(R.layout.game_screen);
        // Set content view to game so objects in game class can be rendered to screen
        setContentView(new GameView(this));
        hideStatusBar();
        // Optionally configure LottieAnimationView
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottieAnimationView);



//
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game_screen), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController wic = getWindow().getInsetsController();
            if (wic != null) {
                wic.hide(WindowInsets.Type.statusBars());
            }
        }
    }
}
