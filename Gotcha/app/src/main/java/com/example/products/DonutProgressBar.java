package com.example.products;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.github.lzyzsd.circleprogress.DonutProgress;

public class DonutProgressBar extends View {
    private Paint paint;
    private RectF oval;
    private int progress;
    private int max;
    private int strokeWidth;

    private int mColor = Color.BLUE;

    public void setColor(int color) {
        mColor = color;
        invalidate();
    }

    public DonutProgressBar(Context context) {
        super(context);
        init();
    }

    public DonutProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DonutProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        oval = new RectF();
        strokeWidth = 20; // adjust as needed
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        int radius = Math.min(halfWidth, halfHeight) - strokeWidth / 2;

        // draw background circle
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(halfWidth, halfHeight, radius, paint);

        // draw progress arc
        paint.setColor(Color.BLUE); // adjust color as needed
        paint.setStrokeWidth(strokeWidth);
        oval.set(halfWidth - radius, halfHeight - radius, halfWidth + radius, halfHeight + radius);
        canvas.drawArc(oval, -90, 360 * progress / max, false, paint);
    }

    public void setProgress(int progress) {
        ValueAnimator animator = ValueAnimator.ofInt(this.progress, progress);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DonutProgressBar.this.progress = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
