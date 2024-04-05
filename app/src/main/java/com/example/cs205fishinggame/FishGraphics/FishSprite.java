package com.example.cs205fishinggame.FishGraphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

public class FishSprite {

    private final FishSpriteSheet fishSpriteSheet;
    private final Rect rect;
    FishSprite(FishSpriteSheet fishSpriteSheet, Rect rect) {
        this.fishSpriteSheet = fishSpriteSheet;
        this.rect = rect;
    }

    public void draw(Canvas canvas, int posX, int posY) {
//        Matrix flipHorizontalMatrix = new Matrix();
//        flipHorizontalMatrix.setScale(-1,1);
//        flipHorizontalMatrix.postTranslate(fishSpriteSheet.getBitmap().getWidth(),0);
//flipHorizontalMatrix.mapRect(rect, new Rect(posX, posY, posX + 150, posY + 300))
//        canvas.drawBitmap(fishSpriteSheet.getBitmap(), flipHorizontalMatrix, null);
        canvas.drawBitmap(fishSpriteSheet.getBitmap(), rect, new Rect(posX, posY, posX + 150, posY + 300), null);
    }

    public int getWidth() {
        return rect.width();
    }

    public int getHeight() {
        return rect.height();
    }

    public void flip(Canvas canvas) {
        canvas.save(); // first save the state of the canvas
        canvas.scale(-1f, 1f, rect.width()/2, rect.height()/2);
        canvas.restore(); // restore previous state (rotate it back)

    }
}
