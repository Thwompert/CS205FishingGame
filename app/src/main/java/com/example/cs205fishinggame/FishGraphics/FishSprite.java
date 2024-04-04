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
}
