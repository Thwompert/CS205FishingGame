package com.example.cs205fishinggame;

import android.graphics.Canvas;

/**
 * GameObject is an abstract class for objects in the game
 */
public abstract class GameObject {
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;
    protected double directionX;
    protected double directionY;

    public GameObject (double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * isColliding checks if two gameobjects are colliding based on their positions and radii
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean isColliding(GameObject obj1, GameObject obj2) {
        double distance = getDistanceBetweenGameObjects(obj1, obj2);
//        double distanceForCollision = obj1.getRadius() + obj2.getRadius();
        double distanceForCollision = 0;
        if (distance < distanceForCollision) {
            return true;
        }
        return false;
    }

    public static double getDistanceBetweenGameObjects(GameObject obj1, GameObject obj2) {
        // Pythagoras theorem
        return Math.sqrt(Math.pow(obj1.positionX - obj2.positionX, 2) + Math.pow(obj1.positionY - obj2.positionY, 2));
    }

    public static double getDistanceBetweenPoints(double p1x, double p1y, double p2x, double p2y) {
        // Pythagoras theorem
        return Math.sqrt(Math.pow(p1x - p2x, 2) + Math.pow(p1y - p2y, 2));
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    protected double getDirectionX() {
        return directionX;
    }

    protected double getDirectionY() {
        return directionY;
    }

    public abstract void draw(Canvas canvas);
    public abstract void update(float deltaTime);
}
