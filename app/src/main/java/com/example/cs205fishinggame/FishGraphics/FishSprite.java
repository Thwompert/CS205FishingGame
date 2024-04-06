package com.example.cs205fishinggame.FishGraphics;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FishSprite {

    private final FishSpriteSheet fishSpriteSheet;
    private final Rect spriteRect;
    private Rect positionRect;
    FishSprite(FishSpriteSheet fishSpriteSheet, Rect spriteRect) {
        this.fishSpriteSheet = fishSpriteSheet;
        this.spriteRect = spriteRect;
    }

    public void draw(Canvas canvas, int posX, int posY) {
        int canvasWidth = canvas.getWidth();
        canvas.drawBitmap(fishSpriteSheet.getBitmap(), spriteRect, new Rect(posX, posY, posX + 150, posY + 300), null);
        positionRect = new Rect(canvasWidth - posX, posY, canvasWidth - posX + 150, posY + 300);

//        canvas.drawRect(positionRect, new Paint());
    }

    public int getWidth() {
        return spriteRect.width();
    }

    public int getHeight() {
        return spriteRect.height();
    }

    public void drawFlipped(Canvas canvas, int posX, int posY) {
        int canvasWidth = canvas.getWidth();
        positionRect = new Rect(canvasWidth - posX, posY, canvasWidth - posX + 150, posY + 300);
        canvas.save();
        canvas.scale(-1, 1, (float) spriteRect.width() / 2, (float) spriteRect.height() / 2);
        canvas.translate(-canvas.getWidth(), 0);
        canvas.drawBitmap(fishSpriteSheet.getBitmap(), spriteRect, positionRect , null);
//        canvas.setMatrix(null);
//        canvas.drawRect(positionRect, new Paint());
        canvas.restore();
    }

    public Rect getRect(){
        return positionRect;
    }


}


