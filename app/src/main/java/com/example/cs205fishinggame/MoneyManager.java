package com.example.cs205fishinggame;

import android.content.Context;
import android.content.SharedPreferences;

public class MoneyManager {
    private int money;

    public MoneyManager() {
        this.money = 0; // Setting the initial amount of money by the player to 0
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void subtractMoney(int amount) {
        if (amount <= this.money) {
            this.money -= amount;
        } else {
            // Handle the case where there is not enough money
        }
    }

    public int getMoney() {
        return this.money;
    }

    public void saveMoney(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Money", this.money);
        editor.apply();
    }

    public void loadMoney(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        this.money = prefs.getInt("Money", 0); // Default to 0 if not found
    }
}
