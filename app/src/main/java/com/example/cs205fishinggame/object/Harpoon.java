package com.example.cs205fishinggame.object;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.cs205fishinggame.Constants;
import com.example.cs205fishinggame.Fish;
import com.example.cs205fishinggame.GameObject;
import com.example.cs205fishinggame.Player;

import java.util.ArrayList;
import java.util.List;

public class Harpoon extends GameObject {
    private RectF hitbox;
    private Paint ropePaint;
    private Player player;
    private Paint tipPaint;
    private double dirX;
    private double dirY;

    private static double harpoonSpeed = -1;
    private static double dampingFactor = -1;
    private boolean retracting = false;
    private List<Fish> fishList;

    public Harpoon(Player player, double strengthX, double strengthY, double upgradeStrength) {
        super(player.getPositionX(), player.getPositionY());

        // Initialize harpoon speed
        if (harpoonSpeed == -1) {
            harpoonSpeed = Constants.HARPOON_SPEED + upgradeStrength;
        }

        if (dampingFactor == -1) {
            dampingFactor = Constants.HARPOON_DAMPING_FACTOR;
        }

        tipPaint = new Paint();
        tipPaint.setColor(Color.WHITE);
        velocityX = strengthX * harpoonSpeed;
        velocityY = strengthY * harpoonSpeed;
        double distance = getDistanceBetweenPoints(0, 0, velocityX, velocityY);
        dirX = velocityX / distance;
        dirY = velocityY / distance;


        this.player = player;
        ropePaint = new Paint();
        ropePaint.setAntiAlias(true);
        ropePaint.setStrokeWidth(20);
        ropePaint.setColor(Color.rgb(88, 57, 39));
        ropePaint.setStyle(Paint.Style.STROKE);

        hitbox = new RectF((float) (player.getPositionX() - 12.5), (float) (player.getPositionY() - 12.5), (float) (player.getPositionX() + 12.5), (float) (player.getPositionY() + 12.5));
        fishList = new ArrayList<Fish>();
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawRect(hitbox, ropePaint);
        canvas.drawLine(
                (float) (player.getPositionX()),
                (float) (player.getPositionY()),
                (float) (positionX),
                (float) (positionY),
                ropePaint
        );
        canvas.drawCircle((float) positionX, (float) positionY, 25, tipPaint);
//        }
    }

    public boolean hasCollided(RectF fish) {
        if (hitbox.intersect(fish)) {
            return true;
        }
        return false;
    }

    public boolean isRetracting() {
        return retracting;
    }

    public void addFish(Fish fish) {
        fishList.add(fish);
        velocityX *= dampingFactor;
        velocityY *= dampingFactor;
    }

    public List<Fish> getFishList() {
        return fishList;
    }

    @Override
    public void update(float deltaTime) {
        positionX += velocityX * deltaTime;
        positionY += velocityY * deltaTime;
        // Update hitbox to move with tip
        hitbox.set((int) (positionX - 12.5), (int) (positionY - 12.5), (int) (positionX + 12.5), (int) (positionY + 12.5));

        if (!retracting) {
            // slow down velocityx and y
            // Apply damping to slow down velocity
            //float dampingFactor = 0.92f; // Adjust this value as needed

            float dampingFactor = (float) Math.pow(Constants.HARPOON_DAMPING_FACTOR, deltaTime);

            velocityX *= dampingFactor;
            velocityY *= dampingFactor;

            // If velocity is close to zero, set it to zero to prevent infinite small updates
            if (getSpeed() < Constants.HARPOON_RETURN_THRESH) { //1) {
                velocityX = 0;
                velocityY = 0;
            }
            if (velocityX == 0 && velocityY == 0) {
                retracting = true;
                velocityX = -dirX * Constants.RETRACT_SPEED;
                velocityY = -dirY * Constants.RETRACT_SPEED;
            }
        }
    }

    public double getSpeed() {
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }
}
