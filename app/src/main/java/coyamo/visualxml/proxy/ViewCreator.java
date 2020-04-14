package coyamo.visualxml.proxy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

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

    public static View create(@NonNull String name, @NonNull Context ctx) {
        if (name.startsWith("coyamo.visualxml.")) return createDefault(ctx, name);
        View v = null;
        if (isFullPackage(name)) v = _create(name, ctx);
        if (v == null) v = createSpecial(name, ctx);
        if (v == null) {
            for (String prefix : sClassPrefixList) {
                v = _create(prefix + name, ctx);
                if (v != null) return v;
            }
        }
        if (v == null) return createDefault(ctx, name);
        return v;
    }

    public static View create(@NonNull String name, @NonNull Context ctx, int defStyle) {
        if (name.startsWith("coyamo.visualxml.")) return createDefault(ctx, name);
        View v = null;
        if (isFullPackage(name)) v = _create(name, ctx, defStyle);
        if (v == null) v = createSpecial(name, ctx);
        if (v == null) {
            for (String prefix : sClassPrefixList) {
                v = _create(prefix + name, ctx, defStyle);
                if (v != null) return v;
            }
        }
        if (v == null) return createDefault(ctx, name);
        return v;
    }

    private static View createDefault(@NonNull Context ctx, String text) {
        DefaultView v = new DefaultView(ctx);
        v.setDisplayText(text);
        return v;
    }

    private static View _create(@NonNull String name, @NonNull Context ctx) {
        try {
            Class<?> clazz = Class.forName(name);
            Constructor<?> con = clazz.getDeclaredConstructor(Context.class);
            con.setAccessible(true);
            return (View) con.newInstance(ctx);
        } catch (Exception e) {
        }
        return null;
    }

    private static View _create(@NonNull String cla, @NonNull Context ctx, int defstyle) {
        try {
            Class<?> clazz = Class.forName(cla);
            Constructor<?> con = clazz.getDeclaredConstructor(Context.class, AttributeSet.class, int.class);
            con.setAccessible(true);
            return (View) con.newInstance(ctx, null, defstyle);
        } catch (Exception e) {
        }
        return null;
    }


    private static View createSpecial(@NonNull String tag, @NonNull Context ctx) {
        switch (tag) {
            case TAG_INCLUDE:
                return createDefault(ctx, tag);
            case TAG_1995:
                return new BlinkLayout(ctx, null);
        }
        return null;
    }

    private static boolean isFullPackage(@NonNull String s) {
        return s.contains(".");
    }
}
