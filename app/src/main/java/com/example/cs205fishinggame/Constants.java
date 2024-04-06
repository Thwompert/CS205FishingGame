package com.example.cs205fishinggame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

public class Constants {
    public static final int CANVAS_WIDTH = 2305;
    public static final int CANVAS_HEIGHT = 1080;
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
    public static final int COINBAR_X = CANVAS_WIDTH - 500;
    public static final int COINBAR_Y = 60;
    public static final int COINBAR_WIDTH = 400;
    public static final int COINBAR_HEIGHT = 100;
    public static final int COINICON_X = COINBAR_X - 10;
    public static final int COINICON_Y = COINBAR_Y - 8;
    public static final int COINVAL_X = COINBAR_X + (COINBAR_WIDTH / 2);
    public static final int COINVAL_Y = COINICON_Y;

    public static final int COINVAL_COLOR = Color.BLACK;
    public static final int COINVAL_TEXT_SIZE = 50;
    public static final Typeface COINVAL_TYPEFACE = Typeface.SANS_SERIF;

    public static final int MAX_OXYGEN = 10;
    public static final int OXYGEN_DRAIN_RATE = 1;
    public static final int OXYGENBAR_X = CANVAS_WIDTH - 150;
    public static final int OXYGENBAR_Y = CANVAS_HEIGHT / 2;
    public static final int OXYGENBAR_WIDTH = 70;
    public static final int OXYGENBAR_HEIGHT = 500;

    public static final float OXYGENBAR_CORNER_RADIUS = 30;

    public static final int OXYGENBAR_FILL_START_COLOR = Color.GREEN;
    public static final int OXYGENBAR_FILL_END_COLOR = Color.RED;
    public static final int OXYGENBAR_BORDER_WIDTH = 5;
    public static final int OXYGENBAR_BORDER_COLOR = Color.BLACK;
    public static final float COINBAR_CORNER_RADIUS = 60;

    public static final int COINBAR_BORDER_WIDTH = 5;
    public static final int COINBAR_BORDER_COLOR = Color.BLACK;
    public static final int COINBAR_FILL_COLOR = Color.WHITE;


}
