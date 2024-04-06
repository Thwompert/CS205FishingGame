package com.example.cs205fishinggame;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        TextView backstoryTextView = findViewById(R.id.backstoryTextView);
        String backstory = "In the year 2100, the vibrant city-state of Singapore, " +
                "once a bustling metropolis, now lies beneath the waves. " +
                "Climate change and rising sea levels have irreversibly transformed " +
                "the landscape, submerging the once-thriving nation under the ocean's embrace. " +
                "The iconic Marina Bay Sands and the majestic Merlion are now part of an underwater realm, " +
                "creating a surreal fusion of the past and the marine world.";
        backstoryTextView.setText(backstory);
    }

    // This method is called when the back button is clicked
    public void onBackButtonClicked(View view) {
        finish(); // Closes this activity and returns to the previous one
    }
}