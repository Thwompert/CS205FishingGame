package com.example.cs205fishinggame.object;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.cs205fishinggame.GameObject;
import com.example.cs205fishinggame.Player;

public class Projectile extends GameObject {
    private RectF hitbox;
    private Paint ropePaint1;
    private Paint ropePaint2;
    private Player player;

    private Paint paint;
    public Projectile(Player player, double strengthX, double strengthY) {
        super(275, 800);
        paint = new Paint();
        velocityX = strengthX * 60;
        velocityY = strengthY * 60;
        this.player = player;
        ropePaint1 = new Paint();
//        this.hitbox = new RectF(pos.x,pos.y,pos.x+width,pos.y+height);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float)positionY, 20, paint);
//        for (int i = 0; i < 10; i++) {
//            double startX = actuatorX * (harpoonIndicatorBlockSize + harpoonIndicatorBlockInterval) * i;
//            double startY = actuatorY * (harpoonIndicatorBlockSize + harpoonIndicatorBlockInterval) * i;
//            double endX =  startX + actuatorX * (harpoonIndicatorBlockSize);
//            double endY = startY + actuatorY * (harpoonIndicatorBlockSize);

            System.out.println("ID1" + Thread.currentThread().getId());
            canvas.drawLine(
                    (float) (player.getPositionX()),
                    (float) (player.getPositionY()),
                    (float) (positionX),
                    (float) (positionY),
                    ropePaint1
            );
//        }
    }

    @Override
    public void update() {
        positionX += velocityX;
        positionY += velocityY;
        // slow down velocityx and y
    }
}
