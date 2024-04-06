package com.example.cs205fishinggame.FishGraphics;

import android.graphics.Canvas;

public class FishAnimator {

    private final FishSprite[] fishSpriteArray;
    //moving fish
    private int idxMovingFrame = 0;


    //Which frame number is the last frame
    private final int FINAL_FRAME = 3;


    public FishAnimator(FishSprite[] fishSpriteArray) {
        this.fishSpriteArray = fishSpriteArray;
    }

    //go to next frame of animation
    private void toggleIdxMovingFrame() {
        if(idxMovingFrame == FINAL_FRAME)
            idxMovingFrame = 1;
        else
            idxMovingFrame++;
    }

    public void draw(Canvas canvas, int posX, int posY) {
        fishSpriteArray[idxMovingFrame].draw(canvas, posX, posY);
        toggleIdxMovingFrame();
    }

    public void drawFlipped(Canvas canvas, int posX, int posY) {
        fishSpriteArray[idxMovingFrame].draw(canvas, posX, posY);
        toggleIdxMovingFrame();
    }


}
