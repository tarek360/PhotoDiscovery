package com.tarek.photodiscovery.drawables;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

/**
 * Created by tarek on 11/7/15.
 */

public class CircularLoaderDrawable extends Drawable {

    private final static float VIEW_BOX_WIDTH = 64;
    private final static float VIEW_BOX_HEIGHT = 64;
    private final static int CIRCULAR_LOADER_RADIUS = 24;

    private float factorScale;
    private ValueAnimator rotationAnimator;

    private Paint paint;
    private float rotation;
    private boolean isError;

    public CircularLoaderDrawable() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float viewBoxRatio = VIEW_BOX_WIDTH / VIEW_BOX_HEIGHT;
        float boundsRatio = bounds.width() / (float) bounds.height();

        if (boundsRatio > viewBoxRatio) {
            // canvas larger than view box
            factorScale = bounds.height() / VIEW_BOX_HEIGHT;
        } else {
            // canvas higher (or equals) than view box
            factorScale = bounds.width() / VIEW_BOX_WIDTH;
        }
    }

    @Override
    public void draw(Canvas canvas) {

        Rect bounds = getBounds();
        if (VIEW_BOX_HEIGHT <= 0 || VIEW_BOX_WIDTH <= 0 || bounds.width() <= 0 || bounds.height() <= 0) {
            return;
        }

        canvas.save();

        int newViewBoxHeight = Math.round(factorScale * VIEW_BOX_HEIGHT);
        int newViewBoxWidth = Math.round(factorScale * VIEW_BOX_WIDTH);
        int marginX = bounds.width() - newViewBoxWidth;
        int marginY = bounds.height() - newViewBoxHeight;
        canvas.translate(bounds.left, bounds.top);
        canvas.translate(Math.round(marginX / 2f), Math.round(marginY / 2f));
        canvas.clipRect(0, 0, newViewBoxWidth, newViewBoxHeight);


        if (isError) {
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("No Image", newViewBoxWidth / 2, newViewBoxHeight / 2, paint);
            return;
        }

        canvas.rotate(rotation, newViewBoxWidth / 2, newViewBoxHeight / 2);

        RectF rectF = new RectF(
                newViewBoxWidth / 2 - CIRCULAR_LOADER_RADIUS,
                newViewBoxHeight / 2 - CIRCULAR_LOADER_RADIUS,
                newViewBoxWidth / 2 + CIRCULAR_LOADER_RADIUS,
                newViewBoxHeight / 2 + CIRCULAR_LOADER_RADIUS);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawArc(rectF, 0, 300, false, paint);

        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public int getMinimumHeight() {
        return 10;
    }

    @Override
    public int getMinimumWidth() {
        return 10;
    }


    public void animate() {

        /** Set isError to false to clear "No Image" text drawing.*/
        isError = false;

        rotationAnimator = ValueAnimator.ofFloat(0, 360);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setDuration(1000);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedValue = (float) animation.getAnimatedValue();
                rotation = animatedValue;
                invalidateSelf();

            }
        });
        rotationAnimator.start();
    }


    /**
     * Cancel circular loader animation.
     */
    public void clearAnimation() {
        rotationAnimator.cancel();
    }


    /**
     * Draw "No Image" text and clear animation.
     */
    public void setError() {
        clearAnimation();
        isError = true;
        invalidateSelf();
    }

}

