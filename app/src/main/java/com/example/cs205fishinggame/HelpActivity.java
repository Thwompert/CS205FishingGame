package com.example.cs205fishinggame;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help); // Make sure this matches your XML layout file name
    }

    // This method is called when the back button is clicked
    public void onBackButtonClicked(View view) {
        finish(); // Closes this activity and returns to the previous one
    }
}