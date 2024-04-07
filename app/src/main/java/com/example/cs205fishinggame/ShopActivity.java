package com.example.cs205fishinggame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {
    private MoneyManager moneyManager;
    private SharedPreferences prefs;
    private int currentMoney;

    private SharedPreferences.Editor editor;
    // private Bitmap coinBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.println("hi lol");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize SharedPrefs
        prefs = getApplicationContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();

        // Init current money
        currentMoney = prefs.getInt("Money", 0);

        updateMoneyDisplay();


//        moneyManager = new MoneyManager();
//        moneyManager.loadMoney(this.getApplicationContext());

//        coinBitmap = BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.coin_bg_removed);
//        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, (int) (coinBitmap.getWidth() * Constants.COINICON_SCALE), (int) (coinBitmap.getHeight() * Constants.COINICON_SCALE), true);
//        new MoneyManager().draw(new Canvas(), coinBitmap);

    }

    public void updateMoneyDisplay() {
        // Update moneyAmt value
        TextView moneyAmt = findViewById(R.id.moneyAmt);
        moneyAmt.setText("" + currentMoney);
    }

    // Method called when the 'back to main' button is clicked
    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBuyOxygenButtonClicked(View view) {
        int currentMaxOxygen = prefs.getInt("MaxOxygen", Constants.maxOxygen);

        // Subtract money
        if (currentMoney >= Constants.PRICE_UPGRADE_OXYGEN) {
            int newMoney = currentMoney - Constants.PRICE_UPGRADE_OXYGEN;
            editor.putInt("Money", newMoney);

            // Increase max oxygen
            editor.putInt("MaxOxygen", currentMaxOxygen + Constants.UPGRADE_OXYGEN);
            currentMoney = newMoney;
            editor.apply();
            updateMoneyDisplay();
        }
        else {

        }
    }

    public void onBuyFishButtonClicked(View view) {
        int currentMaxFishCount = prefs.getInt("MaxFishCount", Constants.maxFishCount);

        // Subtract money
        if (currentMoney >= Constants.PRICE_UPGRADE_FISHES) {
            int newMoney = currentMoney - Constants.PRICE_UPGRADE_FISHES;
            editor.putInt("Money", newMoney);

            // Increase max fishes
            editor.putInt("MaxFishCount", currentMaxFishCount + Constants.UPGRADE_FISHES);
            currentMoney = newMoney;
            editor.apply();
            updateMoneyDisplay();
        } else {
        }

    }
}
