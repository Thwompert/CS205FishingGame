package com.example.cs205fishinggame.Diver;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Diver {
    private Bitmap spriteSheet;
    private int frameWidth;
    private int frameHeight;
    private int frameCount;
    private int currentFrame;
    private long lastFrameChangeTime;
    private int frameLengthInMilliseconds;

    public Diver(Bitmap spriteSheet, int frameCount, int frameWidth, int frameHeight, int frameLengthInMilliseconds) {
        this.spriteSheet = spriteSheet;
        this.frameCount = frameCount;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.frameLengthInMilliseconds = frameLengthInMilliseconds;
        currentFrame = 0;
        lastFrameChangeTime = System.currentTimeMillis();
    }

    public void update(long gameTime) {
        if (gameTime - lastFrameChangeTime > frameLengthInMilliseconds) {
            lastFrameChangeTime = gameTime;
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }

    public void draw(Canvas canvas, int x, int y) {
        int column = currentFrame % frameCount;
        Bitmap frameToDraw = Bitmap.createBitmap(spriteSheet, column * frameWidth, 0, frameWidth, frameHeight);
        canvas.drawBitmap(frameToDraw, x, y, null);
    }
}
