package com.example.cs205fishinggame.FishGraphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.example.cs205fishinggame.R;

public class FishSpriteSheet {
    private Bitmap bitmap;

    //how many pixels is the sprite (including empty spaces)
    private final int SPRITE_WIDTH = 150;
    private final int SPRITE_HEIGHT= 300;
    //width of empty space
    private final int WIDTH_OFFSET = 30;

    private final int HEIGHT_OFFSET = 90;
    public FishSpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fish_spritesheet, bitmapOptions);
        //resize bitmap to 3 * original size, new size will be 900 x 600
        bitmap = getResizedBitmap(bitmap, bitmap.getWidth() * 3, bitmap.getHeight() * 3);
    }

    public FishSprite getRedFishSprite() {
        //add 300 to go up/down, add 150 to go left/right
        return new FishSprite(this, new Rect(WIDTH_OFFSET, HEIGHT_OFFSET, SPRITE_WIDTH, SPRITE_HEIGHT));
    }

    public FishSprite[] getRedFishSpriteArray() {
        FishSprite[] spriteArray = new FishSprite[4];
        spriteArray[0] = new FishSprite(this, new Rect(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT));
        spriteArray[1] = new FishSprite(this, new Rect(SPRITE_WIDTH, 0, 2*SPRITE_WIDTH, SPRITE_HEIGHT));
        spriteArray[2] = new FishSprite(this, new Rect(2*SPRITE_WIDTH, 0, 3*SPRITE_WIDTH, SPRITE_HEIGHT));
        spriteArray[3] = new FishSprite(this, new Rect(3*SPRITE_WIDTH, 0, 4*SPRITE_WIDTH, SPRITE_HEIGHT));
        return spriteArray;
    }

    public FishSprite getYellowFishSprite() {
        //add 300 to go up/down, add 150 to go left/right
        return new FishSprite(this, new Rect(WIDTH_OFFSET,SPRITE_HEIGHT + HEIGHT_OFFSET, SPRITE_WIDTH,SPRITE_HEIGHT * 2));
    }

    public FishSprite getGreenFishSprite() {
        //add 300 to go up/down, add 150 to go left/right
        return new FishSprite(this, new Rect(WIDTH_OFFSET,SPRITE_HEIGHT * 2 + HEIGHT_OFFSET, SPRITE_WIDTH,SPRITE_HEIGHT * 3));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create matrix for image transformation
        Matrix matrix = new Matrix();
        // resize bitmap using matrix
        matrix.postScale(scaleWidth, scaleHeight);
        //create the resized bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        //avoid memory leaks
        bitmap.recycle();
        return resizedBitmap;
    }


}
