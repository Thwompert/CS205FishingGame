package com.example.cs205fishinggame.FishGraphics;

public class FishAnimator {

    private FishSprite[] fishSpriteArray;
    //moving fish
    private int idxMovingFrame = 1;

    //dead fish
    private int idxDeadFrame = 2;
    public FishAnimator(FishSprite[] fishSpriteArray) {
        this.fishSpriteArray = fishSpriteArray;
    }

    private void toggleIdxMovingFrame() {
        if(idxMovingFrame == 1)
            idxMovingFrame = 2;
        else
            idxMovingFrame = 1;
    }
}
