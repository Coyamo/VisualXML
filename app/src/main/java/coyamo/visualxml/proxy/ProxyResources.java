package coyamo.visualxml.proxy;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import android.view.*;
import coyamo.visualxml.*;
import coyamo.visualxml.utils.*;
import java.lang.reflect.*;
import java.util.*;
/*
处理xml中的引用类型的值（不全面）
*/
public class ProxyResources {
    private static ProxyResources instance;
    private MessageArray debug=MessageArray.getInstanse();
    private Map<String, Drawable> drawableMap = new HashMap<>();
    private Map<String, Integer> viewIdMap = new HashMap<>();
    private Map<String, Integer> colorMap = new HashMap<>();
    private Map<String, String> stringMap = new HashMap<>();


    private Context ctx;

    private ProxyResources(Context ctx) {
        this.ctx = ctx;
    }

    public static void init(Context ctx) {
        instance = new ProxyResources(ctx);
    }

    public static ProxyResources getInstance() {
        return instance;
    }

    public void reset() {
        drawableMap.clear();
        viewIdMap.clear();
        colorMap.clear();

		
        instance.putColor("red", Color.RED);
        instance.putDrawable("ic_launcher", ctx.getResources().getDrawable(R.mipmap.ic_launcher));

    }

	/*
	 If true, resource references will be walked; if
	 false, <var>outValue</var> may be a
	 TYPE_REFERENCE.  In either case, it will never
	 be a TYPE_ATTRIBUTE.
	 */

	//不太清楚具体
    public int attr2style(String attr) {
        if (attr.startsWith("@android:style/")) return getRes(attr);
        int id = getAttr(attr);
        Resources.Theme theme = ctx.getTheme();
        TypedValue typeValue = new TypedValue();
        if (theme.resolveAttribute(id, typeValue, true)) {
            return typeValue.data;
        }
        debug.logE("attr2style err："+attr);
        return -1;
    }

	//获取套娃引用的最终值
    public int getRes(String res) {
        if (res.startsWith("@android:style/")) {
            String name = parseReferName(res);
            return getSystemResourceId(android.R.style.class, name.replace(".", "_"));
        }
        debug.logE("找不到 res 值："+res);
        return -1;
    }

    public int getAttr(String attr) {
        String name = null;
        if (attr.startsWith("?android:attr/")) {
            name = parseReferName(attr);
        } else if (attr.startsWith("?android:")) {
            name = parseReferName(attr, ":");
        } else if (attr.startsWith("@android:attr/")) {
            name = parseReferName(attr);
        } else if (attr.startsWith("?attr/android:")) {
            name = parseReferName(attr, ":");
        } else{
            debug.logE("找不到 attr 值："+attr);
            return -1;
        }

        int v = getSystemResourceId(android.R.attr.class, name);
        return v;
    }

    public void registerViewId(View v, String id) {
        String name = parseReferName(id);
        if (!viewIdMap.containsKey(name)) {
            v.setId(View.generateViewId());
            viewIdMap.put(name, v.getId());
        }
    }

    public int getId(String id) {
        if (viewIdMap.containsKey(parseReferName(id))) {
            return viewIdMap.get(parseReferName(id)).intValue();
        }
        return View.NO_ID;
    }

    public View findViewById(Activity activity, String id) {
        if (viewIdMap.containsKey(parseReferName(id)))
            return activity.findViewById(getId(id));
        debug.logE("找不到 View ："+id);
        return null;
    }

    public View findViewById(String id) {
        return findViewById((Activity) ctx, id);
    }


    public Drawable getDrawable(String reference) {
       //颜色
		if (Utils.isColor(reference)) {
            ColorDrawable cd = new ColorDrawable();
            cd.setColor(Color.parseColor(reference));
            return cd;
        }
        String name = parseReferName(reference);
        if (reference.startsWith("@android:drawable/")) {
            int id = getSystemResourceId(android.R.drawable.class, name);
            if (id != -1) {
                return ctx.getResources().getDrawable(id);
            }
        } else if (reference.startsWith("@drawable/")) {
            if (drawableMap.containsKey(name)) {
                return drawableMap.get(name);
            }
        }else if(reference.startsWith("?android:attr/")){
			int i=getAttr(reference);
			TypedArray a = ctx.obtainStyledAttributes(new int[]{i});
			Drawable d = a.getDrawable(a.getIndex(0)); 
			a.recycle();
			
			return d;
		}
        debug.logE("找不到 Drawable ："+reference);
        return null;
    }

    public int getColor(String reference) {
        if (Utils.isColor(reference)) {
            return Color.parseColor(reference);
        }
        String name = parseReferName(reference);
        if (reference.startsWith("@android:color/")) {
            int id = getSystemResourceId(android.R.color.class, name);
            if (id != -1) {
                return ctx.getResources().getColor(id);
            }
        } else if (reference.startsWith("@color/")) {
            if (colorMap.containsKey(name)) {
                return colorMap.get(name);
            }
        }
        debug.logE("找不到 Color ："+reference);
        return Color.BLACK;
    }

    public String parseReferName(String reference) {
        return parseReferName(reference, "/");
    }

    public String parseReferName(String reference, String sep) {
        return reference.substring(reference.indexOf(sep) + 1);
    }

    public void putDrawable(String name, Drawable drawable) {
        drawableMap.put(name, drawable);
    }

    public void putColor(String name, int color) {
        colorMap.put(name, color);
    }

    public int getSystemResourceId(Class clazz, String name) {
        try {
            Field field = clazz.getField(name);
            return field.getInt(clazz);
        } catch (Exception e) {
            debug.logE("找不到系统资源 ："+clazz+" "+name);
        }
        return -1;
    }
}
