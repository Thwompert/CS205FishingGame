package com.example.cs205fishinggame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class HarpoonLauncher {
    private int outerCircleCenterPositionX;
    private int outerCircleCenterPositionY;
    private int innerCircleCenterPositionX;
    private int innerCircleCenterPositionY;
    private int outerCircleRadius;
    private int innerCircleRadius;
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private Paint indicatorPaint;
    private double centreToTouchDistance;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;
    private double harpoonIndicatorBlockSize = 70;
    private double harpoonIndicatorBlockInterval = 25;
    private Player player;

    public HarpoonLauncher(int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius, Player player) {
        // Outer and inner circle of launcher
        outerCircleCenterPositionX = centerPositionX;
        outerCircleCenterPositionY = centerPositionY;
        innerCircleCenterPositionX = centerPositionX;
        innerCircleCenterPositionY = centerPositionY;
        this.player = player;

        // Radii of circle
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        // Paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setAlpha(165);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setAlpha(165);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(Color.RED);
        indicatorPaint.setAntiAlias(true);
        indicatorPaint.setStrokeWidth(20);
        indicatorPaint.setStyle(Paint.Style.STROKE);
        innerCirclePaint.setAlpha(165);
    }

    public void draw(Canvas canvas) {
        // Draw indicator lines
        if (isPressed) {
            for (int i = 0; i < 10; i++) {
                double startX = actuatorX * (harpoonIndicatorBlockSize + harpoonIndicatorBlockInterval) * i;
                double startY = actuatorY * (harpoonIndicatorBlockSize + harpoonIndicatorBlockInterval) * i;
                double endX =  startX + actuatorX * (harpoonIndicatorBlockSize);
                double endY = startY + actuatorY * (harpoonIndicatorBlockSize);

                canvas.drawLine(
                        (float) (player.getPositionX() - startX),
                        (float) (player.getPositionY() - startY),
                        (float) (player.getPositionX()  - endX),
                        (float) (player.getPositionY()  - endY),
                        indicatorPaint
                );
            }
        }

        canvas.drawCircle(outerCircleCenterPositionX, outerCircleCenterPositionY, outerCircleRadius, outerCirclePaint);
        canvas.drawCircle(innerCircleCenterPositionX, innerCircleCenterPositionY, innerCircleRadius, innerCirclePaint);
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX = (int) (outerCircleCenterPositionX + actuatorX * outerCircleRadius);
        innerCircleCenterPositionY = (int) (outerCircleCenterPositionY + actuatorY * outerCircleRadius);
    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        // Pythagoras theorem to get whether touch distance is within circle
        centreToTouchDistance = Math.sqrt(Math.pow(outerCircleCenterPositionX - touchPositionX, 2) + Math.pow(outerCircleCenterPositionY - touchPositionY, 2));
        return centreToTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchPositionX, double touchPositionY) {
        double deltaX = touchPositionX - outerCircleCenterPositionX;
        double deltaY = touchPositionY - outerCircleCenterPositionY;
        double deltaDistance = Math.sqrt(Math.pow(deltaX,2) + Math.pow(deltaY,2));

        if (deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius;
            actuatorY = deltaY / outerCircleRadius;
        } else {
            actuatorX = deltaX / deltaDistance;
            actuatorY = deltaY / deltaDistance;
        }
    }

    public void resetActuator() {
        actuatorX = 0;
        actuatorY = 0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
