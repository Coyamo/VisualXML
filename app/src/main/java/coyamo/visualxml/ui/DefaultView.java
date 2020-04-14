package coyamo.visualxml.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.FrameLayout;

//继承viewgroup 防止出现未知tag内的view被解析到同一层次
public class DefaultView extends FrameLayout {
    private String text = "null";
    private int minSize;
    private Paint paint;

    public DefaultView(Context ctx) {
        super(ctx);
        setWillNotDraw(false);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, ctx.getResources().getDisplayMetrics());
        setPadding(padding, padding, padding, padding);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setColor(Color.GRAY);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, ctx.getResources().getDisplayMetrics()));
        minSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, ctx.getResources().getDisplayMetrics());
    }

    public void setDisplayText(CharSequence cs) {
        if (cs == null) text = "null";
        else text = cs.toString();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLineY = getHeight() / 2 + dy;
        int baseLineX = (getWidth() - Math.min(getWidth(), (int) paint.measureText(text))) / 2;
        canvas.drawText(text, baseLineX, baseLineY, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Rect bounds = new Rect();

        paint.getTextBounds(text, 0, text.length(), bounds);

        if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.max(minSize, bounds.width() + getPaddingLeft() + getPaddingRight());

        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.max(minSize, bounds.height() + getPaddingTop() + getPaddingBottom());
        }
        setMeasuredDimension(width, height);

    }


}
