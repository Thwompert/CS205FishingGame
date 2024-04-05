package com.example.cs205fishinggame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.cs205fishinggame.FishGraphics.FishSprite;
import com.example.cs205fishinggame.object.Harpoon;

import java.util.Random;

public class Fish {
    private Context context;
    private int id;
    private float posX;
    private float posY;

    //used to determine the direction of the fish's movement. -1 for left, 1 for right.
    private int direction = 1;

    private final float speed;


    //width and height of current device
    private final int LANDSCAPE_WIDTH;
    private final int LANDSCAPE_HEIGHT;

    //fish should not be too close to the player
    //left border stores the x position where the fish cannot cross
    private final int LEFT_BORDER = 500;

    private FishSprite fishSprite;

    private boolean isCaught = false;

    private Harpoon caughtBy;

    public Fish(Context context, int id, FishSprite fishSprite) {
        this.context = context;
        this.id = id;
        Random rand = new Random();

        //set width and height of current device in landscape mode
        LANDSCAPE_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        LANDSCAPE_WIDTH = context.getResources().getDisplayMetrics().widthPixels;

        posX = LEFT_BORDER + rand.nextInt(LANDSCAPE_WIDTH - LEFT_BORDER);
        posY = rand.nextInt(LANDSCAPE_HEIGHT - fishSprite.getHeight());

        speed = rand.nextFloat() + 0.5f;
        //paint = new Paint();
//        paint.setColor(Color.rgb(255, 0, 0));

        this.fishSprite = fishSprite;
    }

    //draws fish on the screen
    public void draw(Canvas canvas) {
        //canvas.drawCircle(posX, posY, radius, paint);
        //fishSprite.draw(canvas, (int) posX, (int) posY);
        if (direction == 1) {
            fishSprite.drawFlipped(canvas, (int) posX, (int) posY);
        } else {
            fishSprite.draw(canvas, (int) posX, (int) posY);
        }
    }

    //moves fish in the x axis
    public void move() {
        if (!isCaught) {
            posX += ((float) fishSprite.getWidth() / 6) * direction * speed;

            //switches the direction of fish
            if (posX < LEFT_BORDER || posX > LANDSCAPE_WIDTH) {
                direction *= -1;
            }
        } else {
            // make it follow harpoon
        }
    }

    public Rect getRect() {
        return fishSprite.getRect();
    }

    public void caught(Harpoon harpoon) {
        caughtBy = harpoon;
        isCaught = true;
    }


}
