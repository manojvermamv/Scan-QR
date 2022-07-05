package com.manoj.widget.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.baoyz.widget.PullRefreshLayout;
import com.baoyz.widget.RefreshDrawable;

public class BasicRefreshDrawable extends RefreshDrawable {

    private boolean isRunning = false;

    /**
     * Paint for drawing the shape
     */
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * Icon drawable to be drawn to the center of the shape
     */
    private Drawable icon;
    /**
     * Desired width and height of icon
     */
    private int desiredIconHeight, desiredIconWidth;

    /**
     * Public default constructor
     */
    public BasicRefreshDrawable(Context context, PullRefreshLayout layout) {
        super(context, layout);
        icon = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_rotate);
        paint.setColor(ContextCompat.getColor(context, android.R.color.black));
        desiredIconWidth = 50;
        desiredIconHeight = 50;
    }

    /**
     * Public method for the setting Icon drawable
     *
     * @param icon            pass the drawable of the icon to be drawn at the center
     * @param backgroundColor background color of the shape
     */
    public void setDrawable(Drawable icon, int backgroundColor) {
        this.icon = icon;
        paint.setColor(backgroundColor);
        desiredIconWidth = 50;
        desiredIconHeight = 50;
    }


    @Override
    public void setAlpha(int alpha) {
        //sets alpha to your whole shape
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        //sets color filter to your whole shape
        paint.setColorFilter(colorFilter);
    }


    @Override
    public void setPercent(float percent) {
        // Percentage of the maximum distance of the drop-down refresh.
    }

    @Override
    public void setColorSchemeColors(int[] colorSchemeColors) {

    }

    @Override
    public void offsetTopAndBottom(int offset) {
        // Drop-down offset.
    }

    @Override
    public void start() {
        isRunning = true;
        // Refresh started, start refresh animation.
    }

    @Override
    public void stop() {
        isRunning = false;
        // Refresh completed, stop refresh animation.
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void draw(Canvas canvas) {
        // Draw custom style.
        //if we are setting this drawable to a 80dpX80dp imageview
        //getBounds will return that measurements,we can draw according to that width.
        Rect bounds = getBounds();
        //drawing the circle with center as origin and center distance as radius
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), bounds.centerX(), paint);
        //set the icon drawable's bounds to the center of the shape
        icon.setBounds(bounds.centerX() - (desiredIconWidth / 2), bounds.centerY() - (desiredIconHeight / 2), (bounds.centerX() - (desiredIconWidth / 2)) + desiredIconWidth, (bounds.centerY() - (desiredIconHeight / 2)) + desiredIconHeight);
        //draw the icon to the bounds
        icon.draw(canvas);
    }

}
