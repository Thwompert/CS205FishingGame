package com.example.cs205fishinggame.FishGraphics;


import android.graphics.Canvas;
import android.graphics.Rect;

public class FishSprite {

    private final FishSpriteSheet fishSpriteSheet;
    private final Rect rect;
    FishSprite(FishSpriteSheet fishSpriteSheet, Rect rect) {
        this.fishSpriteSheet = fishSpriteSheet;
        this.rect = rect;
    }

    public void draw(Canvas canvas, int posX, int posY) {
        canvas.drawBitmap(fishSpriteSheet.getBitmap(), rect, new Rect(posX, posY, posX + 150, posY + 300), null);
    }

    public int getWidth() {
        return rect.width();
    }

    public int getHeight() {
        return rect.height();
    }

    public void drawFlipped(Canvas canvas, int posX, int posY) {
        int canvasWidth = canvas.getWidth();
        Rect destRect = new Rect(canvasWidth - posX, posY, canvasWidth - posX + 150, posY + 300);
        canvas.save();
        canvas.scale(-1, 1, (float) rect.width() / 2, (float) rect.height() / 2);
        canvas.translate(-canvas.getWidth(), 0);
        canvas.drawBitmap(fishSpriteSheet.getBitmap(), rect, destRect , null);
//        canvas.setMatrix(null);
        canvas.restore();
    }




}


