package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.EditText;

public class OxygenManager {
    private final int MAX_OXYGEN = 50;
    private final int OXYGEN_DRAIN_RATE = 1;

    private int currentOxygen;
    private Context context;

    private EditText oxygenMeter;
    private Thread oxygenThread;
    private long startTime;

    public OxygenManager(Context context) {
        this.context = context;
        currentOxygen = MAX_OXYGEN;
        startTime = System.currentTimeMillis();
        System.out.println("Oxygen created");
        //startLoop();
    }


    public int getOxygen() {
        return currentOxygen;
    }


    private int getColorOfCurrentOxy() {
        float percent = (float) currentOxygen / (float) MAX_OXYGEN;
        int startColor = Color.GREEN;
        int endColor = Color.RED;

        int red = (int) (Color.red(startColor) * percent + Color.red(endColor) * (1 - percent));
        int green = (int) (Color.green(startColor) * percent + Color.green(endColor) * (1 - percent));
        int blue = (int) (Color.blue(startColor) * percent + Color.blue(endColor) * (1 - percent));

        return Color.rgb(red, green, blue);

//        if (currentOxygen <= 0.25 * MAX_OXYGEN) {
//            return Color.RED;
//        } else if (currentOxygen <= 0.75 * MAX_OXYGEN) {
//            return Color.YELLOW;
//        } else {
//            return Color.GREEN;
//        }
    }

    public void draw(Canvas canvas) {
        int oxygenXPos = canvas.getWidth() - 150;
        int oxygenYPos = canvas.getHeight() / 2;
        int oxygenWidth = 70;
        int oxygenHeight = 500;
        float cornerRadius = 20;


        // Draw inner rect
        Paint paint = new Paint();
        paint.setColor(getColorOfCurrentOxy()); // Set color of the bar

        // Calculate height of the bar based on current countdown value
        int barHeight = (int) (((float) currentOxygen / (float) MAX_OXYGEN) * oxygenHeight);

        // Draw the bar using a rectangle
        RectF rect = new RectF(oxygenXPos, oxygenYPos + (oxygenHeight - barHeight), oxygenXPos + oxygenWidth, oxygenYPos + oxygenHeight); // Adjust height and position as needed
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);


        // Draw border rect
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.BLACK); // Set color of the bar
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(5);

        RectF borderRect = new RectF(oxygenXPos, oxygenYPos, oxygenXPos + oxygenWidth, oxygenYPos + oxygenHeight);
        canvas.drawRoundRect(borderRect, cornerRadius, cornerRadius, borderPaint);

//        Paint paint = new Paint();
//        paint.setColor(Color.WHITE);
//        paint.setTextSize(50);
//        canvas.drawText(Integer.toString(currentOxygen), canvas.getWidth() - 400, 30, paint);
    }

    public void update() {
        System.out.println("Oxygen delta time: " + Long.toString(System.currentTimeMillis() - startTime));
        if (System.currentTimeMillis() - startTime >= OXYGEN_DRAIN_RATE * 1000) {
            currentOxygen--;
            startTime = System.currentTimeMillis();
        }
        //System.out.println("Current oxygen: " + currentOxygen);
    }

}
