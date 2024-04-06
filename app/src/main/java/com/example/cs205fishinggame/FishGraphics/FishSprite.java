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
        //int canvasWidth = canvas.getWidth();
        positionRect =  new Rect(posX, posY, posX + (spriteRect.width() * 2), posY + (spriteRect.height() * 2));
        canvas.drawBitmap(fishSpriteSheet.getBitmap(), spriteRect, positionRect, null);
        //positionRect = new Rect(canvasWidth - posX, posY, canvasWidth - posX + 150, posY + 300);

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
        int spriteWidth = spriteRect.width();
        int spriteHeight = spriteRect.height();

        positionRect = new Rect(posX, posY, posX + (spriteWidth * 2), posY + (spriteHeight * 2));
        //where to draw the fish in the flipped canvas
        Rect destRect = new Rect(canvasWidth - posX, posY, canvasWidth - posX + (spriteWidth * 2), posY + (spriteHeight * 2));

        //flip canvas, then draw fish inside the flipped canvas
        canvas.save();
        canvas.scale(-1, 1, (float) spriteWidth / 2, (float) spriteHeight / 2);
        canvas.translate(- canvasWidth - spriteWidth, 0);
        canvas.drawBitmap(fishSpriteSheet.getBitmap(), spriteRect, destRect, null);
//        canvas.drawRect(positionRect, new Paint());
        canvas.restore();
//        canvas.drawRect(positionRect, new Paint());
    }

    public Rect getRect(){
        return positionRect;
    }


}


