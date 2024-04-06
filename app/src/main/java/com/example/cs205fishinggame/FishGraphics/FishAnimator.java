package com.example.cs205fishinggame.FishGraphics;

public class FishAnimator {

    private FishSprite[] fishSpriteArray;
    //moving fish
    private int idxMovingFrame = 1;


    //Which frame number is the last frame
    private final int FINAL_FRAME = 4;


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


}
