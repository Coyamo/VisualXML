package coyamo.visualxml.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class OutlineView extends LinearLayout {
    Paint paint;
    private List<Rect> bounds = new ArrayList<>();


    private boolean showOutlineOnly;
    public OutlineView(Context ctx) {
        super(ctx);
        init();
    }


    public OutlineView(Context ctx, AttributeSet a) {
        super(ctx, a);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        setFocusable(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!showOutlineOnly) super.dispatchDraw(canvas);
        toOutlinePaint(showOutlineOnly);
        for (Rect bound : bounds) {
            canvas.drawRect(fixRect(bound), paint);
        }
    }

    private Rect fixRect(Rect rect) {
        Rect r = new Rect(rect);
        int half = (int) paint.getStrokeWidth() / 2;
        r.left += half;
        r.top += half;
        r.right -= half;
        r.bottom -= half;
        return r;
    }

    public boolean isShowOutlineOnly() {
        return showOutlineOnly;
    }

    public void setShowOutlineOnly(boolean showOutlineOnly) {
        this.showOutlineOnly = showOutlineOnly;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        freshOutline();
    }

    private void freshOutline() {
        bounds.clear();
        refreshOutline(OutlineView.this, OutlineView.this);
    }

    //递归查找view位置
    private void refreshOutline(ViewGroup v, ViewGroup topView) {
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);
            Rect rect = new Rect();
            Rect topRect = new Rect();
            child.getGlobalVisibleRect(rect);
            topView.getGlobalVisibleRect(topRect);
            //相对于屏幕的坐标
            //要限制在OutlineView内
            //所以要减去偏移的坐标得到真实位置
            rect.top -= topRect.top;
            rect.left -= topRect.left;
            rect.right -= topRect.left;
            rect.bottom -= topRect.top;

            bounds.add(rect);
            if (child instanceof ViewGroup) {
                refreshOutline((ViewGroup) child, topView);
            }
        }

    }

    private void toOutlinePaint(boolean onlyOutline) {
        paint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        if (onlyOutline) {
            paint.setStrokeWidth(2);
            paint.setColor(0xff40c4ff);
        } else {
            paint.setStrokeWidth(1);
            paint.setColor(Color.GRAY);
        }
    }
}
