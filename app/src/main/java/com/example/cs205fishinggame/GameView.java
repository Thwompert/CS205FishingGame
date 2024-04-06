package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import com.example.cs205fishinggame.object.Harpoon;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.cs205fishinggame.FishGraphics.FishSpriteSheet;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final HarpoonLauncher harpoonLauncher;
    private GameThread gameThread;
    private Context context;
    private LottieDrawable lottieDrawable;
    private List<Harpoon> harpoonList = new ArrayList<Harpoon>();
    private Player player;
    private Bitmap backgroundBitmap;
    private MoneyManager moneyManager;
    //how many fishes are currently on screen
    int fishCount = 0;
    final int MAX_FISH_COUNT = 10;
    int fishId = 0;

    private final FishSpriteSheet fishSpriteSheet;

    List<Fish> fishes = new ArrayList<Fish>();

    public GameView(Context context) {
        super(context);
        this.context = context;
        // Get surface holder and callback
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);


        gameThread = new GameThread(this, surfaceHolder);

        // Initialise fish sprite sheet
        fishSpriteSheet = new FishSpriteSheet(context);

        // Initialise fish
        while (fishCount < MAX_FISH_COUNT) {
            spawnFish();

            // FishThread fishThread = new FishThread(context, fishId, fishSpriteSheet.getRedFishSprite());
            //fishThreads[fishCount] = fishThread;
            fishCount++;
        }
        this.player = new Player(275, 800);
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
                if (harpoonLauncher.isPressed((double) event.getX(), (double) event.getY())) {
                    harpoonLauncher.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (harpoonLauncher.getIsPressed()) {
                    harpoonLauncher.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                // Spawn harpoon
                if (harpoonLauncher.getActuatorX() != 0 || harpoonLauncher.getActuatorY() != 0) {
                    harpoonList.add(new Harpoon(player, -harpoonLauncher.getActuatorX(), -harpoonLauncher.getActuatorY()));
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
        for (Harpoon harpoon : harpoonList) {
            harpoon.draw(canvas);
        }
        harpoonLauncher.draw(canvas);

        if (lottieDrawable != null) {
            int animationWidth = 800; // Adjust as needed
            int animationHeight = 800; // Adjust as needed
            int startX = 150; // Move 100 pixels to the right
            int startY = 50; // Move 50 pixels down

            // Correctly set the bounds for the lottieDrawable
            lottieDrawable.setBounds(startX, startY, startX + animationWidth, startY + animationHeight + 200);

            // Save the current state of the canvas
            int saveCount = canvas.save();

            // Flip the animation horizontally around its center
            canvas.scale(-1f, 1f, startX + (animationWidth / 2f), startY + (animationHeight / 2f));

            // Draw the diver
            lottieDrawable.draw(canvas);

            // Restore the canvas to its previous state
            canvas.restoreToCount(saveCount);
        }
        for (Fish fish : fishes) {
            fish.draw(canvas);
        }
    }


    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameThread.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 80, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageUPS = Double.toString(gameThread.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageUPS, 100, 150, paint);
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

        // Update fish movement
        for (Fish fish : fishes) {
            fish.move();
        }

        //Update state of each harpoon projectile
        for (Harpoon harpoon : harpoonList) {
            harpoon.update();
            for (Fish fish : fishes) {
                if (!harpoon.isRetracting() && harpoon.hasCollided(fish.getRect())) {
                    fish.caught(harpoon);
                    harpoon.addFish(fish);
                }
            }
        }


        // Iterate through fishlist and harpoonlist to check for collisions
        Iterator<Harpoon> iteratorHarpoon = harpoonList.iterator();
        while (iteratorHarpoon.hasNext()) {
            Harpoon harpoon = iteratorHarpoon.next();
            // Handle when harpoon retract fully
            if (harpoon.isRetracting() && GameObject.getDistanceBetweenGameObjects(harpoon, player) <= 100) {

                Iterator<Fish> iteratorFish = fishes.iterator();
                //how many fishes have been removed from the arraylist
                int fishRemoved = 0;

                while (iteratorFish.hasNext()) {
                    if (harpoon.getFishList().contains(iteratorFish.next())) {
                        System.out.println("TEST");
                        iteratorFish.remove();
                        fishRemoved++;
                        // Add money here


                    }
                }

                // Spawns new fish after fish gets removed
                for (int i = 0; i < fishRemoved; i++) {
                    spawnFish();
                }

                iteratorHarpoon.remove();
            }
//            iteratorHarpoon.next();
////            if (GameObject.isColliding(iteratorProjectile.next(), fish)) {
////                // Stop fish movement, stop harpoonmovement, start harpoon reeling back
//////                iteratorProjectile.remove();
////            }

        }



    }

    public void saveMoneyState() {
        moneyManager.saveMoney(getContext());
    }

    //helper function to spawn new fish
    public void spawnFish() {
        switch (fishId % 3) {
            case 0:
                fishes.add(new Fish(context, fishId, fishSpriteSheet.getRedFishSprite()));
                break;
            case 1:
                fishes.add(new Fish(context, fishId, fishSpriteSheet.getYellowFishSprite()));
//                    fishes[fishCount] = new Fish(context, fishId, fishSpriteSheet.getYellowFishSprite());
                break;
            case 2:
                fishes.add(new Fish(context, fishId, fishSpriteSheet.getGreenFishSprite()));
//                    fishes[fishCount] = new Fish(context, fishId, fishSpriteSheet.getGreenFishSprite());
                break;
        }
        fishId++;
    }

}