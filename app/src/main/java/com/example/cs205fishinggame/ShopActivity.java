package com.example.cs205fishinggame;

import android.app.Activity;
import android.content.Intent;
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

public class ShopActivity extends Activity {
    private MoneyManager moneyManager;
    private Bitmap coinBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("hi lol");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        moneyManager = new MoneyManager();
        moneyManager.loadMoney(this.getApplicationContext());

        coinBitmap = BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.coin_bg_removed);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, (int) (coinBitmap.getWidth() * Constants.COINICON_SCALE), (int) (coinBitmap.getHeight() * Constants.COINICON_SCALE), true);
        moneyManager.draw(new Canvas(), coinBitmap);

    }

    // Method called when the 'back to main' button is clicked
    public void onBackButtonClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBuyOxygenButtonClicked(View view) {

        Constants.maxOxygen += 10;
        moneyManager.subtractMoney(150);
        moneyManager.saveMoney(this.getApplicationContext());
    }

    public void onBuyFishButtonClicked(View view) {
        Constants.maxFishCount += 3;
        moneyManager.subtractMoney(200);
        moneyManager.saveMoney(this.getApplicationContext());
    }
}
