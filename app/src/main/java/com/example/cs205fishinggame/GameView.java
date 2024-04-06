package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
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
import com.example.cs205fishinggame.object.Harpoon;

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
    private List<Harpoon> harpoonList = new ArrayList<Harpoon>();
    private OxygenManager oxygenManager;
    private Bitmap backgroundBitmap;
    private Bitmap coinBitmap;
    private Bitmap oxygenBitmap;
    private MoneyManager moneyManager;
    //how many fishes are currently on screen
    int fishCount = 0;
    int fishId = 0;

    private boolean isGameOver = false;
    private long gameOverStartTime = -1;

    FishThread[] fishThreads = new FishThread[Constants.MAX_FISH_COUNT];

    public GameView(Context context) {
        super(context);
        this.context = context;

        // Add callback to surface
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        // Create main thread
        gameThread = new GameThread(this, surfaceHolder);

        // Initialise fish sprite sheet
        FishSpriteSheet fishSpriteSheet = new FishSpriteSheet(context);
        // Initialise fish threads
        while (fishCount < Constants.MAX_FISH_COUNT) {
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

        // Initialise other game objects
        harpoonLauncher = new HarpoonLauncher(275, 800, 140, 80);
        oxygenManager = new OxygenManager();

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

    private void initOxygenAndCoin() {
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_bg_removed);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, (int) (coinBitmap.getWidth() * Constants.COINICON_SCALE), (int) (coinBitmap.getHeight() * Constants.COINICON_SCALE), true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        initBackground();
        initOxygenAndCoin();
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
        if (!isGameOver) {
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
                        Player player = new Player(275,800);
                        System.out.println(player.getPositionX());
                        System.out.println("ID2" + Thread.currentThread().getId());
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
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draw background
        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        }

        // Draw ups / fps
        drawUPS(canvas);
        drawFPS(canvas);

        // Draw money bar
        moneyManager.draw(canvas, coinBitmap);

        // Draw oxygen bar
        oxygenManager.draw(canvas);

        // Draw harpoons launched
        harpoonLauncher.draw(canvas);
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

        // Draw fishes
        for (FishThread fishThread : fishThreads) {
            fishThread.draw(canvas);
        }

        // Check game over
        if (isGameOver) {
            if (gameOverStartTime == -1) {
                gameOverStartTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - gameOverStartTime <= Constants.GAMEOVER_DURATION * 1000) {
                Paint p = new Paint();
                p.setTypeface(Constants.GAMEOVER_TYPEFACE);
                p.setTextSize(Constants.GAMEOVER_TEXT_SIZE);
                p.setColor(Constants.GAMEOVER_TEXT_COLOR);

                // Render game over text
                drawCenterText(canvas, p, Constants.GAMEOVER_TEXT);
            }
        }



    }


    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameThread.getAverageUPS());
        Paint paint = new Paint();
        paint.setColor(Constants.UPS_FPS_COLOR);
        paint.setTextSize(Constants.UPS_FPS_TEXT_SIZE);
        canvas.drawText("UPS: " +  averageUPS, 100, 80, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageUPS = Double.toString(gameThread.getAverageFPS());
        Paint paint = new Paint();
        paint.setColor(Constants.UPS_FPS_COLOR);
        paint.setTextSize(Constants.UPS_FPS_TEXT_SIZE);
        canvas.drawText("FPS: " +  averageUPS, 100, 150, paint);
    }


    public void update() {
        // Update oxygen, check game over
        oxygenManager.update();
        isGameOver = oxygenManager.getGameOver();

        // Update money
        moneyManager.update();

        // Update harpoon game state
        harpoonLauncher.update();

        //Update state of each harpoon projectile
        for (Harpoon harpoon : harpoonList) {
            harpoon.update();
        }

        // Iterate through fishlist and projectilelist to check for collisions
        Iterator<Harpoon> iteratorProjectile = harpoonList.iterator();
        while (iteratorProjectile.hasNext()) {
            iteratorProjectile.next();
//            if (GameObject.isColliding(iteratorProjectile.next(), fish)) {
//                // Stop fish movement, stop harpoon movement, start harpoon reeling back
////                iteratorProjectile.remove();
//            }
            //Update fish movement
            for (FishThread fishThread : fishThreads) {
                fishThread.update();
            }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        Rect r = new Rect();
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}