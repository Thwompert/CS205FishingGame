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

    private static double getDistanceBetweenGameObjects(GameObject obj1, GameObject obj2) {
        // Use radius?
        return 0;
    }

    protected double getPositionX() {
        return positionX;
    }

    protected double getPositionY() {
        return positionY;
    }

    protected double getDirectionX() {
        return directionX;
    }

    protected double getDirectionY() {
        return directionY;
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();
}
