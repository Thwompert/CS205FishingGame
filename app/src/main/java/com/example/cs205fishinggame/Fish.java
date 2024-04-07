package com.example.cs205fishinggame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.cs205fishinggame.FishGraphics.FishSprite;
import com.example.cs205fishinggame.object.Harpoon;

import java.util.Random;

public class Fish {
    //used to determine the current position of fish
    private float posX;
    private float posY;

    //used to determine the direction of the fish's movement.
    private float directionX;
    private float directionY;

    //how fast the fish will move
    private float speed;


    //width and height of current device
    private final int LANDSCAPE_WIDTH;
    private final int LANDSCAPE_HEIGHT;
    private final FishSprite fishSprite;

    private boolean isCaught = false;

    private Harpoon caughtBy;

    private float targetX;
    private float targetY;

    //Fish Constructor
    public Fish(Context context, FishSprite fishSprite) {
        //Random object created to randomize speed and initial position
        Random rand = new Random();

        //set width and height of current device in landscape mode
        LANDSCAPE_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        LANDSCAPE_WIDTH = context.getResources().getDisplayMetrics().widthPixels;

        posX = LANDSCAPE_WIDTH + rand.nextInt(200);
        posY = rand.nextInt(LANDSCAPE_HEIGHT + 200) - 100;

        //randomize speed
        speed = Constants.MIN_FISH_SPEED + rand.nextFloat() * (Constants.MAX_FISH_SPEED - Constants.MIN_FISH_SPEED);

        //initialize fish sprite
        this.fishSprite = fishSprite;

        // Initialise target x and y coordinate
        targetX = rand.nextFloat() * LANDSCAPE_WIDTH;
        targetY = rand.nextFloat() * LANDSCAPE_HEIGHT;
    }

    //draws fish on the screen
    public void draw(Canvas canvas) {
        if (directionX > 0) {

            fishSprite.drawFlipped(canvas, (int) posX, (int) posY);
        } else {
            fishSprite.draw(canvas, (int) posX, (int) posY);
        }
    }

    //moves fish in the x axis
    public void move() {
        if (!isCaught) {
            // Calculate direction towards the target point
            float differenceX = targetX - posX;
            float differenceY = targetY - posY;
            float distance = (float) GameObject.getDistanceBetweenPoints(targetX, targetY, posX, posY);

            // Normalize direction vector
            directionX = differenceX / distance;
            directionY = differenceY / distance;

            // Update fish position
            posX += directionX * speed;
            posY += directionY * speed;

            // Check if fish reaches the target point
            if (Math.abs(posX - targetX) < speed && Math.abs(posY - targetY) < speed) {
                // Fish reached the target point, generate a new target point
                Random random = new Random();
                targetX = random.nextFloat() * LANDSCAPE_WIDTH;
                targetY = random.nextFloat() * LANDSCAPE_HEIGHT;
                speed = Constants.MIN_FISH_SPEED + random.nextFloat() * (Constants.MAX_FISH_SPEED - Constants.MIN_FISH_SPEED);
            }
        } else {
            // make it follow harpoon
            posX = (float) caughtBy.positionX - 50;
            posY = (float) caughtBy.positionY - 50;
        }
    }

    public Rect getRect() {
        return fishSprite.getRect();
    }

    public void caught(Harpoon harpoon) {
        caughtBy = harpoon;
        isCaught = true;
    }

    public boolean isCaught(){
        return isCaught;
    }
}
