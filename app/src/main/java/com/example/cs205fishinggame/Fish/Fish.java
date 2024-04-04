package com.example.cs205fishinggame.Fish;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Fish {
    private Context context;
    private int id;
    private float posX;
    private float posY;
    private int radius;

    //used to determine the direction of the fish's movement. -1 for left, 1 for right.
    private int direction = 1;

    private Paint paint;

    //width and height of current device
    private final int LANDSCAPE_WIDTH;
    private final int LANDSCAPE_HEIGHT;

    //fish should not be too close to the player
    //left border stores the x position where the fish cannot cross
    private final int LEFT_BORDER = 500;


    public Fish(Context context, int id, int radius) {
        this.context = context;
        this.id = id;
        this.radius = radius;
        Random rand = new Random();

        //set width and height of current device in landscape mode
        LANDSCAPE_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        LANDSCAPE_WIDTH = context.getResources().getDisplayMetrics().widthPixels;

        posX = LEFT_BORDER + rand.nextInt(LANDSCAPE_WIDTH - LEFT_BORDER);
        posY = radius + rand.nextInt(LANDSCAPE_HEIGHT - radius);

        paint = new Paint();
        paint.setColor(Color.rgb(255, 0, 0));
    }

    //draws Fish on the screen
    public void draw(Canvas canvas) {
        canvas.drawCircle(posX, posY, radius, paint);
    }

    //moves fish in the x axis
    public void move() {
        posX += ((float) radius / 2) * direction;

        //switches the direction of fish
        if (posX < LEFT_BORDER || posX > LANDSCAPE_WIDTH) {
            direction *= -1;
        }
    }
}
