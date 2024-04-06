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
    private Player player;
    private Bitmap backgroundBitmap;
    private Bitmap coinBitmap;
    private Bitmap oxygenBitmap;
    private MoneyManager moneyManager;
    //how many fishes are currently on screen
    int fishCount = 0;
    final int MAX_FISH_COUNT = 10;
    int fishId = 0;
    boolean isPaused = false;

    private boolean isGameOver = false;
    private long gameOverStartTime = -1;

    private final FishSpriteSheet fishSpriteSheet;

    List<Fish> fishes = new ArrayList<Fish>();
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
        fishSpriteSheet = new FishSpriteSheet(context);

        // Initialise fish
        while (fishCount < Constants.MAX_FISH_COUNT) {
            spawnFish();

            // FishThread fishThread = new FishThread(context, fishId, fishSpriteSheet.getRedFishSprite());
            //fishThreads[fishCount] = fishThread;
            fishId++;
            fishCount++;
        }

        // Initialise game objects
        this.player = new Player(Constants.PLAYER_X, Constants.PLAYER_Y);
        harpoonLauncher = new HarpoonLauncher(Constants.JOYSTICK_X, Constants.JOYSTICK_Y, Constants.JOYSTICK_OUTER_RADIUS , Constants.JOYSTICK_INNER_RADIUS, player);
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
        float coinScale = 0.5f;

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
        if (isGameOver) {
            return false;
        }
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
//                Player player = new Player(275,800);
//                System.out.println(player.getPositionX());
//                System.out.println("ID2" + Thread.currentThread().getId());
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

        // Draw background
        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        }


//        // Draw harpoons launched
//        harpoonLauncher.draw(canvas);

        if (drawUPSText) {
            drawUPS(canvas);
        }
        if (drawFPSText) {
            drawFPS(canvas);
        }

        // Draw money bar
        moneyManager.draw(canvas, coinBitmap);

        // Draw oxygen bar
        oxygenManager.draw(canvas);

        for (Harpoon harpoon : harpoonList) {
            harpoon.draw(canvas);
        }
        harpoonLauncher.draw(canvas);

        if (lottieDrawable != null) {
            int animationWidth = 600; // Adjust as needed
            int animationHeight = 600; // Adjust as needed
            int startX = 0; // Move 100 pixels to the right
            int startY = 0; // Move 50 pixels down

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


        // Draw fishes
        for (Fish fish : fishes) {
            fish.draw(canvas);
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

    public void drawFPS(Canvas canvas) {
        String averageUPS = Integer.toString((int) gameThread.getAverageFPS());
        Paint paint = new Paint();
        paint.setColor(Constants.UPS_FPS_COLOR);
        paint.setTextSize(Constants.UPS_FPS_TEXT_SIZE);
        canvas.drawText("FPS: " +  averageUPS, 100, 150, paint);
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
        // Update oxygen, check game over
        oxygenManager.update();
        isGameOver = oxygenManager.getGameOver();

        // Update money
        moneyManager.update();

        // Update harpoon game state
        harpoonLauncher.update();

        // Update fish movement
        for (Fish fish : fishes) {
            fish.move();

            if (!fish.isCaught()) {
                for (Harpoon harpoon : harpoonList) {
                    if (!harpoon.isRetracting() && harpoon.hasCollided(fish.getRect()) && harpoon.getSpeed() > Constants.CATCH_SPEED) {
                        fish.caught(harpoon);
                        harpoon.addFish(fish);
                    }
                }
            }
        }

        // Iterate through fishlist and harpoonlist to check for collisions
        Iterator<Harpoon> iteratorHarpoon = harpoonList.iterator();
        while (iteratorHarpoon.hasNext()) {
            Harpoon harpoon = iteratorHarpoon.next();
            harpoon.update();

            if (harpoon.isRetracting()) {
                // Handle when harpoon retracting
                if (GameObject.getDistanceBetweenGameObjects(harpoon, player) <= 100) {
                    for (Fish fish : harpoon.getFishList()) {
                        fishes.remove(fish);
                        spawnFish();

                        // Add money here
                        moneyManager.addMoney(10);
                    }
                    iteratorHarpoon.remove();
                }
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


    public void resume() {
        isPaused = false;
    }

    public void pause() {
        isPaused = true;



    }

    public void saveMoneyState() {
        moneyManager.saveMoney(getContext());
    }

    //helper function to spawn new fish
    public void spawnFish() {
        switch (fishId % 3) {
            case 0:
                fishes.add(new Fish(context, fishSpriteSheet.getRedFishSprite()));
                break;
            case 1:
                fishes.add(new Fish(context, fishSpriteSheet.getYellowFishSprite()));
//                    fishes[fishCount] = new Fish(context, fishId, fishSpriteSheet.getYellowFishSprite());
                break;
            case 2:
                fishes.add(new Fish(context, fishSpriteSheet.getGreenFishSprite()));
//                    fishes[fishCount] = new Fish(context, fishId, fishSpriteSheet.getGreenFishSprite());
                break;
        }
        fishId++;
    }
}