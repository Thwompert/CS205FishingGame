package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;

public class Constants {
    public static final double HARPOON_SPEED = 150;
    public static final double CATCH_SPEED = 20;
    public static int CANVAS_WIDTH;
    public static int CANVAS_HEIGHT;
    public static final int MAX_FISH_COUNT = 10;
    public static final int BG_ID = R.drawable.background2;
    public static final int DIVER_ID = R.raw.diver;
    public static final float COINICON_SCALE = 0.5f;
    public static final int GAMEOVER_DURATION = 2;
    public static final int GAMEOVER_TEXT_SIZE = 200;

    public static final String GAMEOVER_TEXT = "OUT OF OXYGEN!";
    public static final Typeface GAMEOVER_TYPEFACE = Typeface.DEFAULT_BOLD;
    public static final int GAMEOVER_TEXT_COLOR = Color.BLACK;
    public static final int UPS_FPS_COLOR = Color.MAGENTA;
    public static final int UPS_FPS_TEXT_SIZE = 50;
    public static int COINBAR_X = CANVAS_WIDTH - 500;
    public static final int COINBAR_Y = 40;
    public static final int COINBAR_WIDTH = 400;
    public static final int COINBAR_HEIGHT = 100;
    public static int COINICON_X = COINBAR_X - 10;
    public static int COINICON_Y = COINBAR_Y - 8;
    public static int COINVAL_X = COINBAR_X + (COINBAR_WIDTH / 2);
    public static int COINVAL_Y = COINICON_Y;

    public static final int COINVAL_COLOR = Color.BLACK;
    public static final int COINVAL_TEXT_SIZE = 50;
    public static final Typeface COINVAL_TYPEFACE = Typeface.SANS_SERIF;

    public static final int MAX_OXYGEN = 10;
    public static final int OXYGEN_DRAIN_RATE = 1;
    public static int OXYGENBAR_X = CANVAS_WIDTH - 150;
    public static int OXYGENBAR_Y = CANVAS_HEIGHT / 2;
    public static final int OXYGENBAR_WIDTH = 70;
    public static int OXYGENBAR_HEIGHT = 500;

    public static final float OXYGENBAR_CORNER_RADIUS = 30;

    public static final int OXYGENBAR_FILL_START_COLOR = Color.GREEN;
    public static final int OXYGENBAR_FILL_END_COLOR = Color.RED;
    public static final int OXYGENBAR_BORDER_WIDTH = 5;
    public static final int OXYGENBAR_BORDER_COLOR = Color.BLACK;
    public static final float COINBAR_CORNER_RADIUS = 60;

    public static final int COINBAR_BORDER_WIDTH = 5;
    public static final int COINBAR_BORDER_COLOR = Color.BLACK;
    public static final int COINBAR_FILL_COLOR = Color.WHITE;
    public static int JOYSTICK_X;
    public static int JOYSTICK_Y;
    public static final int JOYSTICK_INNER_RADIUS = 80;
    public static final int JOYSTICK_OUTER_RADIUS = 140;
    public static int PLAYER_X;
    public static int PLAYER_Y;

    public Constants(int widthPixels, int heightPixels) {
        CANVAS_HEIGHT = heightPixels;
        CANVAS_WIDTH = widthPixels;

        COINBAR_X = CANVAS_WIDTH - 750;
        COINICON_X = COINBAR_X - 10;
        COINICON_Y = COINBAR_Y - 8;
        COINVAL_X = COINBAR_X + (COINBAR_WIDTH / 2);
        COINVAL_Y = COINBAR_Y + COINBAR_HEIGHT / 3;

        OXYGENBAR_X = CANVAS_WIDTH - 120;
        OXYGENBAR_HEIGHT = CANVAS_HEIGHT / 2 - 60;
        OXYGENBAR_Y = CANVAS_HEIGHT / 2;

        JOYSTICK_X = 275;
        JOYSTICK_Y = CANVAS_HEIGHT - 275;

        PLAYER_X = 275;
        PLAYER_Y = CANVAS_HEIGHT / 2;
    }
}
