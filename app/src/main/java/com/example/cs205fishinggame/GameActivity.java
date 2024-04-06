package com.example.cs205fishinggame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import androidx.appcompat.view.menu.MenuView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends Activity {

    private GameView gameView;
    private PopupWindow pauseMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);

        // Set content view to game so objects in game class can be rendered to screen
        FrameLayout rootLayout = new FrameLayout(this);
        setContentView(rootLayout);

        // Set content view to game so objects in game class can be rendered to screen
        gameView = new GameView(this);
        rootLayout.addView(gameView); // Add GameView to the root layout
        // Create a smaller button as the menu overlay
        Button pauseButton = new Button(this);
        // Set the button text (optional)
        pauseButton.setText("||");
        pauseButton.setTextSize(20);

        // Set layout parameters to make the button smaller and position it at the top right corner
        FrameLayout.LayoutParams buttonParams1 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT  // Height
        );
        buttonParams1.gravity = Gravity.TOP | Gravity.END; // Position at top right corner
        buttonParams1.setMargins(0, 20, 20, 0); // Add margin to top and right
        pauseButton.setLayoutParams(buttonParams1);

        // Set background color with transparency
        pauseButton.setBackgroundColor(Color.parseColor("#80FFFFFF")); // Set transparency to 50%

        // Add the button to the root layout
        rootLayout.addView(pauseButton);

        // Inflate the overlay XML layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pause_menu, null);

        // Create the PopupWindow
        pauseMenu = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Set an onClickListener for the menu button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle menu button click
                // Add your menu logic here
                gameView.pause();

                // Show the PopupWindow at the center of the screen
                pauseMenu.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        hideStatusBar();


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

    public void resume(View view) {
        // Add your menu logic here
        gameView.resume();
        // Dismiss the PopupWindow
        if (pauseMenu != null && pauseMenu.isShowing()) {
            pauseMenu.dismiss();
        }
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Call a method on GameView to handle saving the money
//        if (gameView != null) {
//            gameView.saveMoneyState();
//        }
    }
}
