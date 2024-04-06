package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.IconMarginSpan;
import android.widget.EditText;

public class OxygenManager {

    private int currentOxygen;
    private long startTime;

    private boolean isGameOver = false;

    public OxygenManager() {
        currentOxygen = Constants.MAX_OXYGEN;
        startTime = System.currentTimeMillis();
    }

    public boolean getGameOver() {
        return isGameOver;
    }

    public int getOxygen() {
        return currentOxygen;
    }


    private int getColorOfCurrentOxy() {
        float percent = (float) currentOxygen / (float) Constants.MAX_OXYGEN;

        int red = (int) (Color.red(Constants.OXYGENBAR_FILL_START_COLOR) * percent + Color.red(Constants.OXYGENBAR_FILL_END_COLOR) * (1 - percent));
        int green = (int) (Color.green(Constants.OXYGENBAR_FILL_START_COLOR) * percent + Color.green(Constants.OXYGENBAR_FILL_END_COLOR) * (1 - percent));
        int blue = (int) (Color.blue(Constants.OXYGENBAR_FILL_START_COLOR) * percent + Color.blue(Constants.OXYGENBAR_FILL_END_COLOR) * (1 - percent));

        return Color.rgb(red, green, blue);

    }

    public void draw(Canvas canvas) {

        // Draw inner bar first
        Paint paint = new Paint();
        paint.setColor(getColorOfCurrentOxy()); // Set color of the bar

        // Calculate height of the bar based on current countdown value
        int innerBarHeight = (int) (((float) currentOxygen / (float) Constants.MAX_OXYGEN) * Constants.OXYGENBAR_HEIGHT);

        // Draw inner bar using a rounded rect
        RectF rect = new RectF(Constants.OXYGENBAR_X, Constants.OXYGENBAR_Y + (Constants.OXYGENBAR_HEIGHT - innerBarHeight),
                Constants.OXYGENBAR_X + Constants.OXYGENBAR_WIDTH, Constants.OXYGENBAR_Y + Constants.OXYGENBAR_HEIGHT);
        canvas.drawRoundRect(rect, Constants.OXYGENBAR_CORNER_RADIUS, Constants.OXYGENBAR_CORNER_RADIUS, paint);

        // Draw border
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Constants.OXYGENBAR_BORDER_COLOR); // Set color of the bar
        borderPaint.setStrokeWidth(Constants.OXYGENBAR_BORDER_WIDTH);

        RectF borderRect = new RectF(Constants.OXYGENBAR_X, Constants.OXYGENBAR_Y,Constants.OXYGENBAR_X + Constants.OXYGENBAR_WIDTH, Constants.OXYGENBAR_Y + Constants.OXYGENBAR_HEIGHT);
        canvas.drawRoundRect(borderRect, Constants.OXYGENBAR_CORNER_RADIUS,  Constants.OXYGENBAR_CORNER_RADIUS, borderPaint);

    }

    public void update() {
        if (System.currentTimeMillis() - startTime >= Constants.OXYGEN_DRAIN_RATE * 1000) {
            currentOxygen--;
            startTime = System.currentTimeMillis();
        }

        if (currentOxygen < 0) {
            currentOxygen = 0;
            isGameOver = true;
        }
    }

}