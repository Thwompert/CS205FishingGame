package com.example.cs205fishinggame.object;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.cs205fishinggame.GameObject;
import com.example.cs205fishinggame.Player;

public class Harpoon extends GameObject {
    private Rect hitbox;
    private Paint ropePaint;
    private Player player;
    private Paint tipPaint;
    private double dirX;
    private double dirY;
    private boolean retracting = false;

    public Harpoon(Player player, double strengthX, double strengthY) {
        super(275, 800);
        tipPaint = new Paint();
        tipPaint.setColor(Color.WHITE);
        velocityX = strengthX * 60;
        velocityY = strengthY * 60;
        double distance = getDistanceBetweenPoints(0, 0, strengthX, strengthY);
        dirX = velocityX / distance;
        dirY = velocityY / distance;

        this.player = player;
        ropePaint = new Paint();
        ropePaint.setAntiAlias(true);
        ropePaint.setStrokeWidth(20);
        ropePaint.setColor(Color.rgb(88, 57, 39));
        ropePaint.setStyle(Paint.Style.STROKE);

        hitbox = new Rect((int) (player.getPositionX() - 12.5), (int) (player.getPositionY() - 12.5), (int) (player.getPositionX() + 12.5), (int) (player.getPositionY() + 12.5));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(hitbox, ropePaint);
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

    public boolean hasCollided(Rect fish) {
        if (hitbox.intersect(fish)) {
            return true;
        }
        return false;
    }

    public boolean isRetracting() {
        return retracting;
    }

    @Override
    public void update() {
        positionX += velocityX;
        positionY += velocityY;

        // Update hitbox to move with tip
        hitbox.set((int) (positionX - 12.5), (int) (positionY - 12.5), (int) (positionX + 12.5), (int) (positionY + 12.5));

        if (!retracting) {
            // slow down velocityx and y
            // Apply damping to slow down velocity
            float dampingFactor = 0.95f; // Adjust this value as needed
            velocityX *= dampingFactor;
            velocityY *= dampingFactor;

            // If velocity is close to zero, set it to zero to prevent infinite small updates
            if (Math.abs(velocityX) < 1) {
                velocityX = 0;
            }
            if (Math.abs(velocityY) < 1) {
                velocityY = 0;
            }
            if (velocityX == 0 && velocityY == 0) {
                retracting = true;
            }
        } else {
            velocityX = -dirX * 0.5;
            velocityY = -dirY * 0.5;
        }
    }
}
