package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.airbnb.lottie.LottieAnimationView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final HarpoonLauncher harpoonLauncher;
    private GameThread gameThread;
    private Context context;
    private LottieDrawable lottieDrawable;

    public GameView(Context context) {
        super(context);
        this.context = context;
        // Get surface holder and callback
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);


        gameThread = new GameThread(this, surfaceHolder);

        // Initialise game objects
        harpoonLauncher = new HarpoonLauncher(275, 700, 70, 40);
        setFocusable(true);
        initLottieAnimation();
    }

    private void initLottieAnimation() {
        lottieDrawable = new LottieDrawable();
        // Load your animation with the LottieDrawable here
        LottieCompositionFactory.fromRawRes(context, R.raw.diver).addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition composition) {
                lottieDrawable.setComposition(composition);
                lottieDrawable.setRepeatCount(LottieDrawable.INFINITE);
                // Now the surface is created, can get its dimensions
                int canvasWidth = getWidth();
                System.out.println(canvasWidth);
                int canvasHeight = getHeight();


                // Define the size of the animation
                int animationWidth = 700; // The width of Lottie animation
                int animationHeight = 700; // The height of Lottie animation

                // Calculate the starting X and Y coordinates for center alignment
                int startX = (canvasWidth - animationWidth) / 2;
                int startY = (canvasHeight - animationHeight) / 2;


                lottieDrawable.setBounds(startX, startY, startX + animationWidth, startY + animationHeight);
                lottieDrawable.playAnimation();
            }


        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
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
                harpoonLauncher.setIsPressed(false);
                harpoonLauncher.resetActuator();
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        harpoonLauncher.draw(canvas);

        if (lottieDrawable != null) {
            // Save the current state of the canvas
            int saveCount = canvas.save();

            // Flip the canvas horizontally around its vertical center
            canvas.scale(-1f, 1f, getWidth() / 2f, 0);

            // Draw the LottieDrawable (which is now positioned to be flipped to the left side)
            lottieDrawable.draw(canvas);

            // Restore the canvas to its previous state
            canvas.restoreToCount(saveCount);
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

    public void update() {
        // Update game state
        harpoonLauncher.update();
    }
}
