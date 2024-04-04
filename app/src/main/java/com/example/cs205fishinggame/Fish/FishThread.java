package com.example.cs205fishinggame.Fish;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import com.example.cs205fishinggame.FishGraphics.FishSprite;
import com.example.cs205fishinggame.FishGraphics.FishSpriteSheet;

import java.util.Random;

public class FishThread extends Thread{
    //spawns new fish to the screen
    private int id;
    private Fish fish;
    private Context context;



    public FishThread(Context context, int id, FishSprite fishSprite) {
        this.context = context;
        this.id = id;

        //create new fish
        fish = new Fish(context, id, fishSprite);



    }
    public Fish spawnFish(Canvas canvas) {
        fish.draw(canvas);
        return fish;
    }

    public void update() {

        fish.move();
//        try {
//            //to be removed later on
//            int time = 0;
//            while(time < 100) {
//                fish.move();
//                time++;
//            }
//        } catch (IllegalArgumentException e){
//            e.printStackTrace();
//        }


    }


}
