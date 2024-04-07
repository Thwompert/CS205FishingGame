package com.example.cs205fishinggame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

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
    private GameActivity activity;
    private LottieDrawable lottieDrawable;
    private List<Harpoon> harpoonList = new ArrayList<Harpoon>();
    private OxygenManager oxygenManager;
    private Player player;
    private Bitmap backgroundBitmap;
    private Bitmap coinBitmap;
    private Bitmap oxygenBitmap;
    private Bitmap merlionBitmap;
    private MoneyManager moneyManager;
    //how many fishes are currently on screen
    int fishCount = 0;
    int fishId = 0;
    boolean isPaused = false;

    private boolean isGameOver = false;
    private long gameOverStartTime = -1;

    private final FishSpriteSheet fishSpriteSheet;

    List<Fish> fishes = new ArrayList<Fish>();
    private boolean drawUPSText;
    private boolean drawFPSText;

    private Typeface chikiBubblesFont;

    int fishesCaught = 0;

    public void loadPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        this.drawUPSText = prefs.getBoolean("drawUPS", false); // Default to 0 if not found
        this.drawFPSText = prefs.getBoolean("drawFPS", false); // Default to 0 if not found
    }
    public GameView(GameActivity activity) {
        super((Context) activity);
        this.activity = activity;
        context = (Context) activity;

        // Add callback to surface
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        loadPreferences(context);
        gameThread = new GameThread(this, surfaceHolder);

        // Initialise fish sprite sheet
        fishSpriteSheet = new FishSpriteSheet(context);

        // Initialise fish
        while (fishCount < Constants.maxFishCount) {
            spawnFish();
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
        initOxygenAndCoin();
        initFonts();
    }

    private void initBackground() {
        // Load the original bitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background2);

        // Create a new bitmap scaled to the size of the canvas
        backgroundBitmap = Bitmap.createScaledBitmap(originalBitmap, getWidth(), getHeight(), true);

        // If the original bitmap won't be used again, you can recycle it to free up memory
        originalBitmap.recycle();

        // Create scaled merlion bitmap
        Bitmap originalMerlionBm = BitmapFactory.decodeResource(getResources(), R.drawable.merlion);
        merlionBitmap = Bitmap.createScaledBitmap(originalMerlionBm, (int) (originalMerlionBm.getWidth() * Constants.MERLION_SCALE), (int) (originalMerlionBm.getHeight() * Constants.MERLION_SCALE), true);
        originalMerlionBm.recycle();

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

    private void initFonts() {
        chikiBubblesFont = Typeface.createFromAsset(context.getAssets(), Constants.CHIKI_FONT_ID);
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

        // Draw merlion
        if (merlionBitmap != null) {
            Paint paint = new Paint();
            paint.setAlpha(Constants.MERLION_ALPHA);
            canvas.drawBitmap(merlionBitmap, Constants.MERLION_X, Constants.MERLION_Y, paint);
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
            // Save money
            saveMoneyState();

            if (gameOverStartTime == -1) {
                gameOverStartTime = System.currentTimeMillis();
            }
            else {
                if (System.currentTimeMillis() - gameOverStartTime <= Constants.GAMEOVER_DURATION * 1000) {
                    Paint p = new Paint();
                    p.setTypeface(chikiBubblesFont);
                    p.setTextSize(Constants.GAMEOVER_TEXT_SIZE);
                    p.setColor(Constants.GAMEOVER_TEXT_COLOR);

                    // Render game over text
                    drawCenterText(canvas, p, Constants.GAMEOVER_TEXT);
                }
                else {
                    activity.showEndScreen(fishesCaught, moneyManager.getMoney());
//                    if (context instanceof GameActivity) {
//                        ((GameActivity) context).finish();
//                    } else {
////                        // Create new MainActivity - Should not reach here
////                        Intent intent = new Intent(context, MainActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
////                        context.startActivity(intent);
//                    }
//                    activity.finish();
                    // Navigate back to MainActivity
//                    Intent intent = new Intent(context, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear back stack
//                    context.startActivity(intent);

                }
            }

        }



    }


    public void drawUPS(Canvas canvas){
        String averageUPS = Integer.toString((int) gameThread.getAverageUPS());
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

    public void update() {
        // Update oxygen, check game over
        oxygenManager.update();
        isGameOver = oxygenManager.getGameOver();

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
                        fishesCaught++;
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

    public void stop() {
        gameThread.stopLoop();
    }
}
