package coyamo.visualxml.proxy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Constructor;

import coyamo.visualxml.ui.DefaultView;
import coyamo.visualxml.ui.proxy.BlinkLayout;

public class ViewCreator {
    //private static final String TAG_MERGE = "merge";
    private static final String TAG_INCLUDE = "include";
    private static final String TAG_1995 = "blink";
    //private static final String TAG_REQUEST_FOCUS = "requestFocus";
    //private static final String TAG_TAG = "tag";
    private static final String[] sClassPrefixList = {
            "android.widget.",
			"android.view.",
            "android.webkit.",
            "android.app."
			
    };

    public static View create(String cla, Context ctx) {
		if(cla.startsWith("coyamo.visualxml."))return createDefault(ctx,cla);
		
		if (isFullPackage(cla)) {
            try {
                return _create(cla, ctx);
            } catch (Exception e) {
            }
        } else {
            try {
                return createSpecial(cla, ctx);
            } catch (Exception e) {
            }
            for (String prefix : sClassPrefixList) {
                try {
                    View v = _create(prefix + cla, ctx);
                    if (v != null) {
                        return v;
                    }
                } catch (Exception e) {

                }
            }
        }
        return createDefault(ctx, cla);
    }

    public static View create(String cla, Context ctx, int defStyle) {
		if(cla.startsWith("coyamo.visualxml."))return createDefault(ctx,cla);
		
		if (isFullPackage(cla)) {
            try {
                return _create(cla, ctx, defStyle);
            } catch (Exception e) {
            }
        } else {
            try {
                return createSpecial(cla, ctx);
            } catch (Exception e) {
            }
            for (String prefix : sClassPrefixList) {
                try {
                    View v = _create(prefix + cla, ctx, defStyle);
                    if (v != null) {
                        return v;
                    }
                } catch (Exception e) {
                }
            }
        }
        return createDefault(ctx, cla);
    }

    private static View createDefault(Context ctx, String text) {
        DefaultView v = new DefaultView(ctx);
        v.setDisplayText(text);
        return v;
    }

    private static View _create(String cla, Context ctx) throws Exception {

        Class<?> clszz = Class.forName(cla);
        Constructor<?> con = clszz.getDeclaredConstructor(Context.class);
        con.setAccessible(true);
        return (View) con.newInstance(ctx);


    }

    private static View _create(String cla, Context ctx, int defstyle) {
        try {
            Class<?> clszz = Class.forName(cla);
            Constructor<?> con = clszz.getDeclaredConstructor(Context.class, AttributeSet.class, int.class);
            con.setAccessible(true);
            return (View) con.newInstance(ctx, null, defstyle);
        } catch (Exception e) {
        }
        return null;
    }


    private static View createSpecial(String tag, Context ctx) throws Exception {
        	switch (tag) {
            case TAG_INCLUDE:
                return createDefault(ctx, tag);
            case TAG_1995:
                return new BlinkLayout(ctx, null);
        }

        throw new Exception();

    }

    private static boolean isFullPackage(String s) {
        return s.contains(".");
    }
}
