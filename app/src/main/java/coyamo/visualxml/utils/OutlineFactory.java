package coyamo.visualxml.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class OutlineFactory {
    private OutlineFactory() {
    }

    public static Drawable getSelectedline(final int w, final int h) {

        final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xffff80ab);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setPathEffect(new DashPathEffect(new float[] {5, 5}, 0));

        Drawable d = new Drawable() {


            @Override
            public void draw(Canvas p1) {

                p1.drawRoundRect(5, 5, w - 5, h - 5, 5, 5, mPaint);

            }

            @Override
            public void setAlpha(int p1) {
                // TODO: Implement this method
            }

            @Override
            public void setColorFilter(ColorFilter p1) {
                // TODO: Implement this method
            }

            @Override
            public int getOpacity() {
                // TODO: Implement this method
                return 0;
            }
        };
        d.setBounds(0, 0, w, h);
        return d;
    }

    public static Drawable getDashline(final int w, final int h) {

        final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));

        Drawable d = new Drawable() {


            @Override
            public void draw(Canvas p1) {
                p1.drawRect(0, 0, w, h, mPaint);

            }

            @Override
            public void setAlpha(int p1) {
                // TODO: Implement this method
            }

            @Override
            public void setColorFilter(ColorFilter p1) {
                // TODO: Implement this method
            }

            @Override
            public int getOpacity() {
                // TODO: Implement this method
                return 0;
            }
        };
        d.setBounds(0, 0, w, h);
        return d;
    }
}
