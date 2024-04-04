package com.example.cs205fishinggame.object;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.cs205fishinggame.GameObject;

public class Projectile extends GameObject {
    private RectF hitbox;

    private Paint paint;
    public Projectile(double strengthX, double strengthY) {
        super(275, 800);
        paint = new Paint();
        velocityX = strengthX * 60;
        velocityY = strengthY * 60;
//        this.hitbox = new RectF(pos.x,pos.y,pos.x+width,pos.y+height);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float)positionY, 20, paint);
    }

    @Override
    public void update() {
        positionX += velocityX;
        positionY += velocityY;
        // slow down velocityx and y
    }
}
