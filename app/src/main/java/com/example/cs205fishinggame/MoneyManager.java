package com.example.cs205fishinggame;

import android.widget.Toast;

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
}
