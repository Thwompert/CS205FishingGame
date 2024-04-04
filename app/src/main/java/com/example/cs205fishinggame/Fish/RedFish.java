package com.example.cs205fishinggame.Fish;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.DrawableRes;

import com.example.cs205fishinggame.R;

public class RedFish extends Fish{
    private int id;
//    private float posX;
//    private float posY;
    private float radius;
    private String type;
    private int monetaryValue;

    @DrawableRes
    private int image;

    public RedFish(Context context, int id, int radius) {
        super(context, id, radius);
        this.type = "redfish";
        this.monetaryValue = 130;
        this.image = R.drawable.test_rickroll;
    }


}
