package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import com.example.cs205fishinggame.object.Projectile;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.cs205fishinggame.Fish.FishThread;
import com.example.cs205fishinggame.FishGraphics.FishSpriteSheet;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final HarpoonLauncher harpoonLauncher;
    private GameThread gameThread;
    private Context context;
    private LottieDrawable lottieDrawable;
    private List<Projectile> projectileList = new ArrayList<Projectile>();

    private Bitmap backgroundBitmap;
    private MoneyManager moneyManager;
    //how many fishes are currently on screen
    int fishCount = 0;
    final int MAX_FISH_COUNT = 10;
    int fishId = 0;

    FishThread[] fishThreads = new FishThread[MAX_FISH_COUNT];

    public GameView(Context context) {
        super(context);
        this.context = context;
        // Get surface holder and callback
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);


        gameThread = new GameThread(this, surfaceHolder);

        // Initialise fish sprite sheet
        FishSpriteSheet fishSpriteSheet = new FishSpriteSheet(context);
        // Initialise fish thread
        while (fishCount < MAX_FISH_COUNT) {
            switch(fishId % 3){
                case 0:
                    fishThreads[fishCount] = new FishThread(context, fishId, fishSpriteSheet.getRedFishSprite());
                    break;
                case 1:
                    fishThreads[fishCount] = new FishThread(context, fishId, fishSpriteSheet.getYellowFishSprite());
                    break;
                case 2:
                    fishThreads[fishCount] = new FishThread(context, fishId, fishSpriteSheet.getGreenFishSprite());
                    break;
            }

            // FishThread fishThread = new FishThread(context, fishId, fishSpriteSheet.getRedFishSprite());
            //fishThreads[fishCount] = fishThread;
            fishId++;
            fishCount++;
        }

        // Initialise game objects
        harpoonLauncher = new HarpoonLauncher(275, 800, 140, 80);

        // Initialising money manager -> to keep track of the money that the player currently has
        moneyManager = new MoneyManager();
        moneyManager.loadMoney(context);
        setFocusable(true);
        initLottieAnimation();
    }
    private void initBackground() {
        // Load the original bitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background2);

        // Create a new bitmap scaled to the size of the canvas
        backgroundBitmap = Bitmap.createScaledBitmap(originalBitmap, getWidth(), getHeight(), true);

        // If the original bitmap won't be used again, you can recycle it to free up memory
        originalBitmap.recycle();
    }

    private void initLottieAnimation() {
        lottieDrawable = new LottieDrawable();
        // Load your animation with the LottieDrawable here
        LottieCompositionFactory.fromRawRes(context, R.raw.diver).addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition composition) {
                lottieDrawable.setComposition(composition);
                lottieDrawable.setRepeatCount(LottieDrawable.INFINITE);


                lottieDrawable.playAnimation();
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        initBackground();
        gameThread.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (harpoonLauncher.isPressed((double) event.getX(), (double)event.getY())){
                    harpoonLauncher.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (harpoonLauncher.getIsPressed()) {
                    harpoonLauncher.setActuator((double) event.getX(), (double)event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                // Spawn harpoon
                if (harpoonLauncher.getActuatorX() != 0 || harpoonLauncher.getActuatorY() != 0){
                    projectileList.add(new Projectile(-harpoonLauncher.getActuatorX(), -harpoonLauncher.getActuatorY()));
                }

//                // Check if a certain game condition is met to reward money
//                if (harpoonLauncher.successfulHit()) {
//                    moneyManager.addMoney(10); // Reward the player with a certain number of coins based on the fish that is caught
//                }
                harpoonLauncher.setIsPressed(false);
                harpoonLauncher.resetActuator();
                return true;


        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        }
        drawUPS(canvas);
        drawFPS(canvas);
        drawMoney(canvas);

        harpoonLauncher.draw(canvas);
        for (Projectile projectile : projectileList) {
            projectile.draw(canvas);
        }

        if (lottieDrawable != null) {
            int animationWidth = 800; // Adjust as needed
            int animationHeight = 800; // Adjust as needed
            int startX = 150; // Move 100 pixels to the right
            int startY = 50; // Move 50 pixels down

            // Correctly set the bounds for the lottieDrawable
            lottieDrawable.setBounds(startX, startY, startX + animationWidth, startY + animationHeight +200);

            // Save the current state of the canvas
            int saveCount = canvas.save();

            // Flip the animation horizontally around its center
            canvas.scale(-1f, 1f, startX + (animationWidth / 2f), startY + (animationHeight / 2f));

            // Draw the diver
            lottieDrawable.draw(canvas);

            // Restore the canvas to its previous state
            canvas.restoreToCount(saveCount);
        }
        for (FishThread fishThread : fishThreads) {
            fishThread.draw(canvas);
        }
    }



    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameThread.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " +  averageUPS, 100, 80, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageUPS = Double.toString(gameThread.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " +  averageUPS, 100, 150, paint);
    }

    private void drawMoney(Canvas canvas) {
        String moneyText = "Money: " + moneyManager.getMoney();
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText(moneyText, 100, 220, paint); // Can change the coordinates for the text as needed
    }

    public void update() {
        // Update game state
        harpoonLauncher.update();

        //Update state of each harpoon projectile
        for (Projectile projectile : projectileList) {
            projectile.update();
        }

        // Update fish movement
        for (FishThread fishThread : fishThreads) {
            fishThread.update();
        }

        // Iterate through fishlist and projectilelist to check for collisions
        Iterator<Projectile> iteratorProjectile = projectileList.iterator();
        while (iteratorProjectile.hasNext()) {
            iteratorProjectile.next();
////            if (GameObject.isColliding(iteratorProjectile.next(), fish)) {
////                // Stop fish movement, stop harpoonmovement, start harpoon reeling back
//////                iteratorProjectile.remove();
////            }

    }

//    public void saveMoneyState() {
//        moneyManager.saveMoney(getContext());
//    }
}
}