package com.example.cs205fishinggame;

import android.content.Context;
import android.content.SharedPreferences;
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
    final int MAX_FISH_COUNT = 10;
    int fishId = 0;
    boolean isPaused = false;

    FishThread[] fishThreads = new FishThread[MAX_FISH_COUNT];
    private boolean drawUPSText;
    private boolean drawFPSText;

    public void loadPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        this.drawUPSText = prefs.getBoolean("drawUPS", false); // Default to 0 if not found
        this.drawFPSText = prefs.getBoolean("drawFPS", false); // Default to 0 if not found
    }

    public GameView(Context context) {
        super(context);
        this.context = context;
        // Get surface holder and callback
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        loadPreferences(context);

        gameThread = new GameThread(this, surfaceHolder);

        // Initialise fish sprite sheet
        FishSpriteSheet fishSpriteSheet = new FishSpriteSheet(context);
        // Initialise fish thread
        while (fishCount < MAX_FISH_COUNT) {
            switch (fishId % 3) {
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
        oxygenManager = new OxygenManager(context);

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
        float coinScale = 0.5f;

        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_bg_removed);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, (int) (coinBitmap.getWidth() * coinScale), (int) (coinBitmap.getHeight() * coinScale), true);
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
                    Player player = new Player(275, 800);
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

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        }

        if (drawUPSText) {
            drawUPS(canvas);
        }
        if (drawFPSText) {
            drawFPS(canvas);
        }
        drawMoney(canvas);

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
        for (FishThread fishThread : fishThreads) {
            fishThread.draw(canvas);
        }
        oxygenManager.draw(canvas);

    }


    public void drawUPS(Canvas canvas) {
        String averageUPS = Integer.toString((int) gameThread.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 80, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageUPS = Integer.toString((int) gameThread.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageUPS, 100, 150, paint);
    }

    private void drawMoney(Canvas canvas) {
        int coinBarXPos = canvas.getWidth() - 500;
        int coinBarYPos = 60;
        int coinBarLength = 400;
        int coinBarHeight = 100;
        int coinXPos = coinBarXPos - 10;
        int coinYPos = coinBarYPos - 8;
        int coinValXPos = coinBarXPos + (coinBarLength / 2);
        int coinValYPos = coinYPos;
        float cornerRadius = 60;

        // Draw coin bar
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK); // Set color of the bar
        paint.setStrokeWidth(5);

        RectF rect = new RectF(coinBarXPos, coinBarYPos, coinBarXPos + coinBarLength, coinBarYPos + coinBarHeight);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        // Draw coin
        Rect coinDst = new Rect(coinXPos, coinYPos, coinXPos + coinBitmap.getWidth(), coinYPos + coinBitmap.getHeight());
        canvas.drawBitmap(coinBitmap, null, coinDst, null);

        // Draw money value
        String moneyText = "" + moneyManager.getMoney();
        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.black);
        paint.setColor(color);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText(moneyText, coinValXPos, coinValYPos + coinBitmap.getHeight() + paint.ascent(), paint); // Can change the coordinates for the text as needed
    }

    public void update() {
        // Update oxygen
        oxygenManager.update();

        // Update game state
        harpoonLauncher.update();

        //Update state of each harpoon projectile
        for (Harpoon harpoon : harpoonList) {
            harpoon.update();
        }

        // Iterate through fishlist and projectilelist to check for collisions
        Iterator<Harpoon> iteratorProjectile = harpoonList.iterator();
        while (iteratorProjectile.hasNext()) {
            iteratorProjectile.next();
////            if (GameObject.isColliding(iteratorProjectile.next(), fish)) {
////                // Stop fish movement, stop harpoonmovement, start harpoon reeling back
//////                iteratorProjectile.remove();
////            }
            // Update fish movement
            for (FishThread fishThread : fishThreads) {
                fishThread.update();
            }
        }

//    public void saveMoneyState() {
//        moneyManager.saveMoney(getContext());
//    }
    }

    public void resume() {
        isPaused = false;
    }

    public void pause() {
        isPaused = true;
    }
}