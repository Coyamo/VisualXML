package coyamo.visualxml.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;

import coyamo.visualxml.utils.OutlineFactory;
/*
这个边框绘制暂时用viewoverlay
有很多问题
比如 需要安卓4.3以上
而且状态会和view一起变化（
view不可见，overlay也不可见，overlay透明度随view的透明度一起变化。。。
）
*/
public class OutlineView extends LinearLayout {
    private View selectView = null;

    public OutlineView(Context ctx) {
        super(ctx);
        init();

    }

    public OutlineView(Context ctx, AttributeSet attr) {
        super(ctx, attr);
        init();
    }

    public static void addViewInto(final View v, ViewGroup into, final OutlineView outlineView) {

        v.post(new Runnable() {

            @Override
            public void run() {
                int w = v.getMeasuredWidth(), h = v.getMeasuredHeight();
                final ViewOverlay viewOverlay = v.getOverlay();
                final Drawable normalDrawable = OutlineFactory.getDashline(w, h);
                viewOverlay.add(normalDrawable);
                v.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View p1, MotionEvent p2) {
                        //Debug.log("==");
                        int w = v.getMeasuredWidth(), h = v.getMeasuredHeight();
                        Drawable pressedDrawable = OutlineFactory.getSelectedline(w, h);
                        Drawable normalDrawable = OutlineFactory.getDashline(w, h);


                        if (!outlineView.hasSelectView() || outlineView.getSelectView() == p1)
                            switch (p2.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    outlineView.setSelectView(p1);
                                    viewOverlay.clear();
                                    viewOverlay.add(pressedDrawable);
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    viewOverlay.clear();
                                    viewOverlay.add(normalDrawable);
                                    outlineView.setSelectView(null);
                                    break;
                            }
                        return true;
                    }
                });
               v.addOnLayoutChangeListener(new OnLayoutChangeListener() {

                    @Override
                    public void onLayoutChange(View p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9) {
                        outlineView.setSelectView(null);
                        viewOverlay.clear();
                        int w = v.getMeasuredWidth(), h = v.getMeasuredHeight();

                        Drawable normalDrawable = OutlineFactory.getDashline(w, h);

                        viewOverlay.add(normalDrawable);
                    }
                });
            }
        });


        into.addView(v);
    }

    private void init() {
        //setOrientation(VERTICAL);


    }

    public boolean hasSelectView() {
        return selectView != null;
    }

    public View getSelectView() {
        return selectView;
    }

    public void setSelectView(View v) {
        selectView = v;
    }


}
