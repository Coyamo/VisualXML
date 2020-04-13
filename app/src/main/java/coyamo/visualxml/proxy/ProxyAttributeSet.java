package coyamo.visualxml.proxy;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

import coyamo.visualxml.utils.MessageArray;
import coyamo.visualxml.utils.Utils;


public class ProxyAttributeSet
{
    private MessageArray debug=MessageArray.getInstanse();
	//attr中的枚举值与实际需要的参数的映射
    private static Map<String, Map<String, String>> enumMap = new HashMap<>();
    //attr中flag值的映射（可以a|b|c这么设置的那种）
	private static Map<String, Map<String, Integer>> flagMap = new HashMap<>();

	//因为映射的值规律有点蜜汁
	//还没有想到合适的文件结构储存这些关系
	//只能写死在代码里面(•́ω•̀ ٥)

    static {
        Map<String, String> orientationMap = new HashMap<>();
        enumMap.put("orientation", orientationMap);
        orientationMap.put("vertical", "1");
        orientationMap.put("horizontal", "0");

        Map<String, String> layoutMap = new HashMap<>();
        enumMap.put("layout_width", layoutMap);
        enumMap.put("layout_height", layoutMap);
        layoutMap.put("fill_parent", "-1");
        layoutMap.put("match_parent", "-1");
        layoutMap.put("wrap_content", "-2");


        Map<String, String> visibilityMap = new HashMap<>();
        enumMap.put("visibility", visibilityMap);
        visibilityMap.put("visible", "0");
        visibilityMap.put("invisible", "4");
        visibilityMap.put("gone", "8");

        Map<String, String> typefaceMap = new HashMap<>();
        enumMap.put("typeface", typefaceMap);
        typefaceMap.put("sans", "SANS_SERIF");
        typefaceMap.put("serif", "SERIF");
        typefaceMap.put("monospace", "MONOSPACE");
        typefaceMap.put("normal", "DEFAULT");

        Map<String, String> ellipsizeMap = new HashMap<>();
        enumMap.put("ellipsize", ellipsizeMap);
        ellipsizeMap.put("none", "END_SMALL");
        ellipsizeMap.put("start", "START");
        ellipsizeMap.put("middle", "MIDDLE");
        ellipsizeMap.put("end", "END");
        ellipsizeMap.put("marquee", "MARQUEE");

        Map<String, String> textAlignmentMap = new HashMap<>();
        enumMap.put("textAlignment", textAlignmentMap);
        textAlignmentMap.put("inherit", "0");
        textAlignmentMap.put("gravity", "1");
        textAlignmentMap.put("textStart", "2");
        textAlignmentMap.put("textEnd", "3");
        textAlignmentMap.put("center", "4");
        textAlignmentMap.put("viewStart", "5");
        textAlignmentMap.put("viewEnd", "6");


        Map<String, String> alignmentModeMap = new HashMap<>();
        enumMap.put("alignmentMode", alignmentModeMap);
        alignmentModeMap.put("alignBounds", "0");
        alignmentModeMap.put("alignMargins", "1");



        Map<String, String> scaleTypeMap = new HashMap<>();
        enumMap.put("scaleType", scaleTypeMap);
        scaleTypeMap.put("matrix", "MATRIX");
        scaleTypeMap.put("fitXY", "FIT_XY");
        scaleTypeMap.put("fitStart", "FIT_START");
        scaleTypeMap.put("fitCenter", "FIT_CENTER");
        scaleTypeMap.put("fitEnd", "FIT_END");
        scaleTypeMap.put("center", "CENTER");
        scaleTypeMap.put("centerCrop", "CENTER_CROP");
        scaleTypeMap.put("centerInside", "CENTER_INSIDE");


    }

    static {

        Map<String, Integer> textStyleMap = new HashMap<>();
        flagMap.put("textStyle", textStyleMap);
        textStyleMap.put("normal", 0);
        textStyleMap.put("bold", 1);
        textStyleMap.put("italic", 2);


        Map<String, Integer> gravityMap = new HashMap<>();
        flagMap.put("gravity", gravityMap);
        flagMap.put("layout_gravity", gravityMap);
        flagMap.put("foregroundGravity", gravityMap);
        gravityMap.put("top", 0x30);
        gravityMap.put("bottom", 0x50);
        gravityMap.put("left", 0x03);
        gravityMap.put("right", 0x05);
        gravityMap.put("center_vertical", 0x10);
        gravityMap.put("fill_vertical", 0x70);
        gravityMap.put("center_horizontal", 0x01);
        gravityMap.put("fill_horizontal", 0x07);
        gravityMap.put("center", 0x11);
        gravityMap.put("fill", 0x77);
        gravityMap.put("clip_vertical", 0x80);
        gravityMap.put("clip_horizontal", 0x08);
        gravityMap.put("start", 0x00800003);
        gravityMap.put("end", 0x00800005);


        Map<String, Integer> inputTypeMap = new HashMap<>();
        flagMap.put("inputType", inputTypeMap);
        inputTypeMap.put("none", 0x00000000);
        inputTypeMap.put("text", 0x00000001);
        inputTypeMap.put("textCapCharacters", 0x00001001);
        inputTypeMap.put("textCapWords", 0x00002001);
        inputTypeMap.put("textCapSentences", 0x00004001);
        inputTypeMap.put("textAutoCorrect", 0x00008001);
        inputTypeMap.put("textAutoComplete", 0x00010001);
        inputTypeMap.put("textMultiLine", 0x00020001);
        inputTypeMap.put("textImeMultiLine", 0x00040001);
        inputTypeMap.put("textNoSuggestions", 0x00080001);
        inputTypeMap.put("textUri", 0x00000011);
        inputTypeMap.put("textEmailAddress", 0x00000021);
        inputTypeMap.put("textEmailSubject", 0x00000031);
        inputTypeMap.put("textShortMessage", 0x00000041);
        inputTypeMap.put("textLongMessage", 0x00000051);
        inputTypeMap.put("textPersonName", 0x00000061);
        inputTypeMap.put("textPostalAddress", 0x00000071);
        inputTypeMap.put("textPassword", 0x00000081);
        inputTypeMap.put("textVisiblePassword", 0x00000091);
        inputTypeMap.put("textWebEditText", 0x000000a1);
        inputTypeMap.put("textFilter", 0x000000b1);
        inputTypeMap.put("textPhonetic", 0x000000c1);
        inputTypeMap.put("textWebEmailAddress", 0x000000d1);
        inputTypeMap.put("textWebPassword", 0x000000e1);
        inputTypeMap.put("number", 0x00000002);
        inputTypeMap.put("numberSigned", 0x00001002);
        inputTypeMap.put("numberDecimal", 0x00002002);
        inputTypeMap.put("numberPassword", 0x00000012);
        inputTypeMap.put("phone", 0x00000003);
        inputTypeMap.put("datetime", 0x00000004);
        inputTypeMap.put("date", 0x00000014);
        inputTypeMap.put("time", 0x00000024);
    }

    private XmlPullParser mParser;
    private Context ctx;
    private ProxyResources resource;
	/*
	 这里没有对命名空间进行详细的解析
	 */
    private String androidNS = "http://schemas.android.com/apk/res/android";

    public ProxyAttributeSet(XmlPullParser parser, Context ctx)
	{
        this.ctx = ctx;
        mParser = parser;
        resource = ProxyResources.getInstance();
    }

    public void setTo(final View v)
	{

        if (hasAttribute("id"))
		{
            resource.registerViewId(v, getAttributeValue(androidNS, "id"));
        }
        //除了几个特殊的 其他view必须要有
        if (!hasAttribute("layout_width"))
		{
            Utils.setField(v.getLayoutParams(), "width", -2);
            if (!mParser.getName().equals("include"))
                debug.logE(mParser.getLineNumber() + "行 " + mParser.getColumnNumber() + "列：" + mParser.getName() + " layout_width 不能忽略，已经自动设置为wrap_content。");

        }
        if (!hasAttribute("layout_height"))
		{
            Utils.setField(v.getLayoutParams(), "height", -2);
            if (!mParser.getName().equals("include"))
                debug.logE(mParser.getLineNumber() + "行 " + mParser.getColumnNumber() + "列：" + mParser.getName() + " layout_height 不能忽略，已经自动设置为wrap_content。");

        }

        for (int i = 0; i < getAttributeCount(); i++)
		{
            String name = getAttributeName(i);
            final String value = getAttributeValue(i);
            //命名空间
            if (mParser.getAttributeNamespace(i).equals(androidNS))
                switch (name)
				{
                    case "layout_centerInParent":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.CENTER_IN_PARENT);
                        continue;
                    case "layout_centerVertical":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.CENTER_VERTICAL);
                        continue;
                    case "layout_centerHorizontal":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.CENTER_HORIZONTAL);
                        continue;
                    case "layout_alignEnd":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ALIGN_END, resource.getId(value));
                        continue;
                    case "layout_alignStart":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ALIGN_START, resource.getId(value));
                        continue;
                    case "layout_alignLeft":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ALIGN_LEFT, resource.getId(value));
                        continue;
                    case "layout_alignRight":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ALIGN_RIGHT, resource.getId(value));
                        continue;
                    case "layout_alignTop":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ALIGN_TOP, resource.getId(value));
                        continue;
                    case "layout_alignBottom":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ALIGN_BOTTOM, resource.getId(value));
                        continue;
                    case "layout_alignBaseline":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ALIGN_BASELINE, resource.getId(value));
                        continue;
                    case "layout_toRightOf":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.RIGHT_OF, resource.getId(value));
                        continue;
                    case "layout_toLeftOf":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.LEFT_OF, resource.getId(value));
                        continue;
                    case "layout_toStartOf":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.START_OF, resource.getId(value));
                        continue;
                    case "layout_toEndOf":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.END_OF, resource.getId(value));
                        continue;
                    case "layout_above":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.ABOVE, resource.getId(value));
                        continue;
                    case "layout_below":
                        Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class, int.class}, RelativeLayout.BELOW, resource.getId(value));
                        continue;
                    case "layout_alignParentRight":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.ALIGN_PARENT_RIGHT);
                        continue;
                    case "layout_alignParentBottom":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.ALIGN_PARENT_BOTTOM);
                        continue;
                    case "layout_alignParentEnd":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.ALIGN_PARENT_END);
                        continue;
                    case "layout_alignParentLeft":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.ALIGN_PARENT_LEFT);
                        continue;
                    case "layout_alignParentStart":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.ALIGN_PARENT_START);
                        continue;
                    case "layout_alignParentTop":
                        if (Boolean.parseBoolean(value))
                            Utils.invoke(v.getLayoutParams(), "addRule", new Class[]{int.class}, RelativeLayout.ALIGN_PARENT_TOP);
                        continue;
                    case "layout_margin":
                        int layout_margin = (int) Float.parseFloat(value);
                        Utils.invoke(v.getLayoutParams(), "setMargins", new Class[]{int.class, int.class, int.class, int.class}, layout_margin, layout_margin, layout_margin, layout_margin);
                        continue;
                    case "layout_marginStart":
                        if (!hasAttribute("layout_margin"))
                            Utils.invoke(v.getLayoutParams(), "setMarginStart", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "layout_marginEnd":
                        if (!hasAttribute("layout_margin"))
                            Utils.invoke(v.getLayoutParams(), "setMarginEnd", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "layout_marginBottom":
                        if (!hasAttribute("layout_margin"))
                            Utils.setField(v.getLayoutParams(), "bottomMargin", (int) Float.parseFloat(value));
                        continue;
                    case "layout_marginTop":
                        if (!hasAttribute("layout_margin"))
                            Utils.setField(v.getLayoutParams(), "topMargin", (int) Float.parseFloat(value));
                        continue;
                    case "layout_marginLeft":
                        if (!hasAttribute("layout_margin"))
                            Utils.setField(v.getLayoutParams(), "leftMargin", (int) Float.parseFloat(value));
                        continue;
                    case "layout_marginRight":
                        if (!hasAttribute("layout_margin"))
                            Utils.setField(v.getLayoutParams(), "rightMargin", (int) Float.parseFloat(value));
                        continue;
                    case "layout_width":
                        Utils.setField(v.getLayoutParams(), "width", (int) Float.parseFloat(value));
                        continue;
                    case "layout_height":
                        Utils.setField(v.getLayoutParams(), "height", (int) Float.parseFloat(value));
                        continue;
                    case "layout_gravity":
                        Utils.setField(v.getLayoutParams(), "gravity", parseFlag(name, value));
                        continue;
                    case "layout_weight":
                        Utils.setField(v.getLayoutParams(), "weight", Float.parseFloat(value));
                        continue;
                    case "id"://do first
                        continue;
                    case "background":
                        Utils.invoke(v, "setBackgroundDrawable", new Class[]{Drawable.class}, resource.getDrawable(value));
                        continue;
                    case "src":
                        Utils.invoke(v, "setImageDrawable", new Class[]{Drawable.class}, resource.getDrawable(value));
                        continue;
                    case "visibility":
                        Utils.invoke(v, "setVisibility", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "text":
                        Utils.invoke(v, "setText", new Class[]{CharSequence.class}, value);
                        continue;
                    case "alpha":
                        if (getAttributeValue(androidNS, "enabled") == null || getAttributeValue(androidNS, "enabled").equals(true))
                            Utils.invoke(v, "setAlpha", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "elevation":
                        Utils.invoke(v, "setElevation", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "gravity":
                        Utils.invoke(v, "setGravity", new Class[]{int.class}, parseFlag(name, value));
                        continue;
                    case "orientation":
                        Utils.invoke(v, "setOrientation", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "width":
                        Utils.invoke(v, "setWidth", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "height":
                        Utils.invoke(v, "setHeight", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "maxEms":
                        Utils.invoke(v, "setMaxEms", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "ems":
                        Utils.invoke(v, "setEms", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "hint":
                        Utils.invoke(v, "setHint", new Class[]{CharSequence.class}, value);
                        continue;
                    case "lines":
                        Utils.invoke(v, "setLines", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "maxLines":
                        Utils.invoke(v, "setMaxLines", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "maxWidth":
                        Utils.invoke(v, "setMaxWidth", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "minEms":
                        Utils.invoke(v, "setMinEms", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "minHeight":
                        Utils.invoke(v, "setMinHeight", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "minLines":
                        Utils.invoke(v, "setMinLines", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "minWidth":
                        Utils.invoke(v, "setMinWidth", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "maxHeight":
                        Utils.invoke(v, "setMaxHeight", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "singleLine":
                        Utils.invoke(v, "setSingleLine", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "textColor":
                        Utils.invoke(v, "setTextColor", new Class[]{int.class}, resource.getColor(value));
                        continue;
                    case "textColorHighlight":
                        Utils.invoke(v, "setHighlightColor", new Class[]{int.class}, resource.getColor(value));
                        continue;
                    case "textColorHint":
                        Utils.invoke(v, "setHintTextColor", new Class[]{int.class}, resource.getColor(value));
                        continue;
                    case "textColorLink":
                        Utils.invoke(v, "setLinkTextColor", new Class[]{int.class}, resource.getColor(value));
                        continue;
                    case "textScaleX":
                        Utils.invoke(v, "setTextScaleX", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "textIsSelectable":
                        Utils.invoke(v, "setTextIsSelectable", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "textSize":
                        Utils.invoke(v, "setTextSize", new Class[]{int.class, float.class}, TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(value));
                        continue;
                    case "padding": {
							int p = (int) Float.parseFloat(value);
							Utils.invoke(v, "setPadding", new Class[]{int.class, int.class, int.class, int.class}, p, p, p, p);
							continue;
						}
                    case "paddingLeft": {
							if (hasAttribute("padding")) continue;
							int top = (int) Utils.invoke(v, "getPaddingTop", null);
							int right = (int) Utils.invoke(v, "getPaddingRight", null);
							int bottom = (int) Utils.invoke(v, "getPaddingBottom", null);
							Utils.invoke(v, "setPadding", new Class[]{int.class, int.class, int.class, int.class}, (int) Float.parseFloat(value), top, right, bottom);
							continue;
						}
                    case "paddingStart": {
							if (hasAttribute("padding")) continue;
							int top = (int) Utils.invoke(v, "getPaddingTop", null);
							int bottom = (int) Utils.invoke(v, "getPaddingBottom", null);
							int end = (int) Utils.invoke(v, "getPaddingEnd", null);
							Utils.invoke(v, "setPaddingRelative", new Class[]{int.class, int.class, int.class, int.class}, (int) Float.parseFloat(value), top, end, bottom);
							continue;
						}
                    case "paddingRight": {
							if (hasAttribute("padding")) continue;
							int left = (int) Utils.invoke(v, "getPaddingLeft", null);
							int top = (int) Utils.invoke(v, "getPaddingTop", null);
							int bottom = (int) Utils.invoke(v, "getPaddingBottom", null);
							Utils.invoke(v, "setPadding", new Class[]{int.class, int.class, int.class, int.class}, left, top, (int) Float.parseFloat(value), bottom);
							continue;
						}
                    case "paddingEnd": {
							if (hasAttribute("padding")) continue;
							int top = (int) Utils.invoke(v, "getPaddingTop", null);
							int bottom = (int) Utils.invoke(v, "getPaddingBottom", null);
							int start = (int) Utils.invoke(v, "getPaddingStart", null);
							Utils.invoke(v, "setPaddingRelative", new Class[]{int.class, int.class, int.class, int.class}, start, top, (int) Float.parseFloat(value), bottom);
							continue;
						}
                    case "paddingTop": {
							if (hasAttribute("padding")) continue;
							int left = (int) Utils.invoke(v, "getPaddingLeft", null);
							int right = (int) Utils.invoke(v, "getPaddingRight", null);
							int bottom = (int) Utils.invoke(v, "getPaddingBottom", null);
							Utils.invoke(v, "setPadding", new Class[]{int.class, int.class, int.class, int.class}, left, (int) Float.parseFloat(value), right, bottom);
							continue;
						}
                    case "paddingBottom": {
							if (hasAttribute("padding")) continue;
							int left = (int) Utils.invoke(v, "getPaddingLeft", null);
							int top = (int) Utils.invoke(v, "getPaddingTop", null);
							int right = (int) Utils.invoke(v, "getPaddingRight", null);
							Utils.invoke(v, "setPadding", new Class[]{int.class, int.class, int.class, int.class}, left, top, right, (int) Float.parseFloat(value));
							continue;
						}
                    case "rotation":
                        Utils.invoke(v, "setRotation", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "rotationX":
                        Utils.invoke(v, "setRotationX", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "rotationY":
                        Utils.invoke(v, "setRotationY", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "scaleX":
                        Utils.invoke(v, "setScaleX", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "scaleY":
                        Utils.invoke(v, "setScaleY", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "shadowColor": {
							int color = resource.getColor(value);
							float dx = (float) Utils.invoke(v, "getShadowDx", null);
							float dy = (float) Utils.invoke(v, "getShadowDy", null);
							float r = (float) Utils.invoke(v, "getShadowRadius", null);
							Utils.invoke(v, "setShadowLayer", new Class[]{float.class, float.class, float.class, int.class}, r, dx, dy, color);
							continue;
						}

                    case "shadowDx": {
							int color = (int) Utils.invoke(v, "getShadowColor", null);
							float dx = Float.parseFloat(value);
							float dy = (float) Utils.invoke(v, "getShadowDy", null);
							float r = (float) Utils.invoke(v, "getShadowRadius", null);
							Utils.invoke(v, "setShadowLayer", new Class[]{float.class, float.class, float.class, int.class}, r, dx, dy, color);
							continue;
						}

                    case "shadowDy": {
							int color = (int) Utils.invoke(v, "getShadowColor", null);
							float dx = (float) Utils.invoke(v, "getShadowDx", null);
							float dy = Float.parseFloat(value);
							float r = (float) Utils.invoke(v, "getShadowRadius", null);
							Utils.invoke(v, "setShadowLayer", new Class[]{float.class, float.class, float.class, int.class}, r, dx, dy, color);
							continue;
						}
                    case "shadowRadius": {
							int color = (int) Utils.invoke(v, "getShadowColor", null);
							float dx = (float) Utils.invoke(v, "getShadowDx", null);
							float dy = (float) Utils.invoke(v, "getShadowDy", null);
							float r = Float.parseFloat(value);
							Utils.invoke(v, "setShadowLayer", new Class[]{float.class, float.class, float.class, int.class}, r, dx, dy, color);
							continue;
						}
                    case "translationX":
                        Utils.invoke(v, "setTranslationX", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "translationY":
                        Utils.invoke(v, "setTranslationY", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "translationZ":
                        Utils.invoke(v, "setTranslationY", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                        //case "textScaleY":
                    case "typeface": {
							Typeface tf = (Typeface) Utils.getFiledValueFromClass(Typeface.class, value);
							Utils.invoke(v, "setTypeface", new Class[]{Typeface.class}, tf);
							continue;
						}
                    case "ellipsize":
                        Utils.invoke(v, "setEllipsize", new Class[]{TextUtils.TruncateAt.class}, TextUtils.TruncateAt.valueOf(value));
                        continue;
                    case "inputType":
                        Utils.invoke(v, "setInputType", new Class[]{int.class}, parseFlag(name, (value)));
                        continue;
                    case "textAlignment":
                        Utils.invoke(v, "setTextAlignment", new Class[]{int.class}, Integer.valueOf(value));
                        continue;
                    case "adjustViewBounds":
                        Utils.invoke(v, "setAdjustViewBounds", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "baseLine":
                        Utils.invoke(v, "setBaseline", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "scaleType":
                        Utils.invoke(v, "setScaleType", new Class[]{ImageView.ScaleType.class}, ImageView.ScaleType.valueOf(value));
                        continue;
                    case "checked":
                        Utils.invoke(v, "setChecked", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "textOff": {
							Utils.invoke(v, "setTextOff", new Class[]{CharSequence.class}, value);
							boolean b = (boolean) Utils.invoke(v, "isChecked", null);
							Utils.invoke(v, "setChecked", new Class[]{boolean.class}, b);
							continue;
						}
                    case "textOn": {
							Utils.invoke(v, "setTextOn", new Class[]{CharSequence.class}, value);
							boolean b = (boolean) Utils.invoke(v, "isChecked", null);
							Utils.invoke(v, "setChecked", new Class[]{boolean.class}, b);
							continue;
						}
                    case "enabled":
                        if (!Boolean.parseBoolean(value))
                            Utils.invoke(v, "setAlpha", new Class[]{float.class}, 0.3f);
                        continue;
                    case "switchMinWidth":
                        Utils.invoke(v, "setSwitchMinWidth", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "switchPadding":
                        Utils.invoke(v, "setSwitchPadding", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "thumbTextPadding":
                        Utils.invoke(v, "setThumbTextPadding", new Class[]{int.class}, (int) Float.parseFloat(value));
                        continue;
                    case "foregroundGravity":
                        Utils.invoke(v, "setForegroundGravity", new Class[]{int.class}, parseFlag(name, value));
                        continue;
                    case "clipChildren":
                        Utils.invoke(v, "setClipChildren", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "calendarViewShown":
                        Utils.invoke(v, "setCalendarViewShown", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "clipToPadding":
                        Utils.invoke(v, "setClipToPadding", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "measureAllChildren":
                        Utils.invoke(v, "setMeasureAllChildren", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "spinnersShown":
                        Utils.invoke(v, "setSpinnersShown", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "setClipChildren":
                        Utils.invoke(v, "setClipChildren", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "baselineAlignBottom":
                        Utils.invoke(v, "setBaselineAlignBottom", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "cropToPadding":
                        Utils.invoke(v, "setCropToPadding", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "baselineAligned":
                        Utils.invoke(v, "setBaselineAligned", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "baselineAlignedChildIndex":
                        Utils.invoke(v, "setBaselineAlignedChildIndex", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "weightSum":
                        Utils.invoke(v, "setWeightSum", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "indeterminate":
                        Utils.invoke(v, "setIndeterminate", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "indeterminateDrawable":
                        Utils.invoke(v, "setIndeterminateDrawable", new Class[]{Drawable.class}, resource.getDrawable(value));
                        continue;
                    case "indeterminateOnly":
                        //Utils.setField(v,"mIndeterminateOnly",Boolean.valueOf(value));
                        //??
                        continue;
                    case "progressDrawable":
                        Utils.invoke(v, "setProgressDrawable", new Class[]{Drawable.class}, resource.getDrawable(value));
                        continue;
                    case "numStars":
                        Utils.invoke(v, "setNumStars", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "rating":
                        Utils.invoke(v, "setRating", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "isIndicator":
                        Utils.invoke(v, "setIsIndicator", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "stepSize":
                        Utils.invoke(v, "setStepSize", new Class[]{float.class}, Float.parseFloat(value));
                        continue;
                    case "progress":
                        Utils.invoke(v, "setProgress", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "min":
                        Utils.invoke(v, "setMin", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "max":
                        Utils.invoke(v, "setMax", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "fillViewport":
                        Utils.invoke(v, "setFillViewport", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "alignmentMode":
                        Utils.invoke(v, "setAlignmentMode", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "columnCount":
                        Utils.invoke(v, "setColumnCount", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "columnOrderPreserved":
                        Utils.invoke(v, "setColumnOrderPreserved", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "rowCount":
                        Utils.invoke(v, "setRowCount", new Class[]{int.class}, Integer.parseInt(value));
                        continue;
                    case "rowOrderPreserved":
                        Utils.invoke(v, "setRowOrderPreserved", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "setUseDefaultMargins":
                        Utils.invoke(v, "setFillViewport", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "measureWithLargestChild":
                        Utils.invoke(v, "setMeasureWithLargestChildEnabled", new Class[]{boolean.class}, Boolean.parseBoolean(value));
                        continue;
                    case "textStyle": {
							int style = parseFlag(name, value);
							Typeface tf = (Typeface) Utils.invoke(v, "getTypeface", null);
							Utils.invoke(v, "setTypeface", new Class[]{Typeface.class, int.class}, tf, style);
							continue;
						}
                    case "textAppearance": {
							try
							{
								int attrid;
								if (value.startsWith("?android:attr/"))
								{
									attrid = resource.attr2style(value);

									Utils.invoke(v, "setTextAppearance", new Class[]{Context.class, int.class}, ctx, attrid);

								}
								else if (value.startsWith("@android:style/"))
								{
									attrid = resource.getRes(value);
									Utils.invoke(v, "setTextAppearance", new Class[]{Context.class, int.class}, ctx, attrid);
								}
							}
							catch (Exception e)
							{
								debug.logE(mParser.getLineNumber() + "行 " + mParser.getColumnNumber() + "列：" + mParser.getName() + "textAppearance 失败。" + e);

							}
							continue;
						}


                }
        }

    }

    private String parseRowAttributeValue(String name, String value)
	{
        //解析Android默认命名空间内元素
        //暂时不考虑引用资源的情况
        switch (name)
		{
				//处理单位数字
            case "switchMinWidth":
            case "switchPadding":
            case "thumbTextPadding":
            case "baseLine":
            case "translationX":
            case "translationY":
            case "translationZ":
            case "padding":
            case "paddingLeft":
            case "paddingStart":
            case "paddingRight":
            case "paddingEnd":
            case "paddingTop":
            case "paddingBottom":
            case "layout_marginBottom":
            case "layout_marginLeft":
            case "layout_marginStart":
            case "layout_marginEnd":
            case "layout_marginTop":
            case "layout_marginRight":
            case "layout_margin":
            case "elevation":
            case "width":
            case "height":
            case "minWidth":
            case "maxWidth":
            case "minHeight":
            case "maxHeight":
            case "textSize":
                return parseUnitString2Px(value) + "";
				//返回原来的值
            case "layout":
            case "src":
            case "style":
            case "textAppearance":
            case "measureWithLargestChild":
            case "columnCount":
            case "columnOrderPreserved":
            case "rowCount":
            case "rowOrderPreserved":
            case "useDefaultMargins":
            case "fillViewport":
            case "progress":
            case "min":
            case "max":
            case "indeterminate":
            case "indeterminateDrawable":
            case "indeterminateOnly":
            case "progressDrawable":
            case "numStars":
            case "rating":
            case "isIndicator":
            case "stepSize":
            case "baselineAligned":
            case "baselineAlignedChildIndex":
            case "weightSum":
            case "baselineAlignBottom":
            case "cropToPadding":
            case "setClipChildren":
            case "clipChildren":
            case "calendarViewShown":
            case "clipToPadding":
            case "measureAllChildren":
            case "spinnersShown":
            case "enabled":
            case "textOff":
            case "textOn":
            case "adjustViewBounds":
            case "inputType":
            case "shadowColor":
            case "shadowDx":
            case "shadowDy":
            case "shadowRadius":
            case "rotation":
            case "rotationX":
            case "rotationY":
            case "singleLine":
            case "textColor":
            case "textColorHighlight":
            case "textColorHint":
            case "textColorLink":
            case "textScaleX":
            case "scaleX":
            case "scaleY":
            case "textIsSelectable":

                //case "textScaleY":

            case "checked":
            case "maxEms":
            case "ems":
            case "hint":
            case "lines":
            case "maxLines":
            case "minEms":
            case "minLines":
            case "onClick":
            case "layout_centerInParent":
            case "layout_centerVertical":
            case "layout_centerHorizontal":
            case "layout_alignEnd":
            case "layout_alignStart":
            case "layout_alignLeft":
            case "layout_alignRight":
            case "layout_alignTop":
            case "layout_alignBottom":
            case "layout_align":
            case "layout_below":
            case "layout_above":
            case "layout_toRightOf":
            case "layout_toLeftOf":
            case "layout_toStartOf":
            case "layout_toEndOf":
            case "layout_alignParentRight":
            case "layout_alignParentBottom":
            case "layout_alignParentEnd":
            case "layout_alignParentLeft":
            case "layout_alignParentStart":
            case "layout_alignParentTop":
            case "id":
            case "layout_weight":
            case "text":
            case "alpha":
            case "background":
            case "textStyle":
                return value;
				//处理枚举值
            case "alignmentMode":
            case "scaleType":
            case "textAlignment":
            case "ellipsize":
            case "typeface":
            case "visibility":
            case "orientation":

                return parseEnum(name, value);
            case "layout_width":
            case "layout_height":{
					String va=parseEnum(name, value);
					if (va.equals("null"))
					{
						return (int) parseUnitString2Px(value) + "";
					}
					return va;

				}

        }
        debug.logW(mParser.getLineNumber() + "行 " + mParser.getColumnNumber() + "列：没有解析的属性 " + name + " ，请检查属性是否存在（或解析器没有正确处理）。");
        return value;
    }

    private String parseEnum(String name, String value)
	{
        return parseEnum(name, value, "null");
    }

    private String parseEnum(String name, String value, String def)
	{
        Map<String, String> map = enumMap.get(name);
        if (map.containsKey(value))
            return map.get(value);
        return def;
    }

    private int parseFlag(String name, String value)
	{
        // xx或xx|xx...或xx|,不允许xx||
        Map<String, Integer> map = flagMap.get(name);
        if (!value.contains("|"))
		{
            return map.get(value);
        }
		//flag类型好像都是int
        int flag = -1;
        String[] flags = value.split("\\|");
        for (String f : flags)
		{
            if (!f.trim().isEmpty())
			{
                int v = map.get(f);
                if (flag == -1)
				{
                    flag = v;
                }
				else
				{
					//不太确定是否所有flag都是这么|的(•́ω•̀ ٥)
                    flag |= v;
                }
            }
			else
			{
				debug.logE(value + "格式错误");
			}
        }
        return flag;
    }

    private float parseUnitString2Px(String unitString)
	{
		//xml支持的单位
        String[] suffixs = {"sp", "dp", "dip", "pt", "px", "mm", "in"};
        unitString = unitString.trim();
        int lastIndex = -1;
        for (String suffix : suffixs)
		{
            if (unitString.endsWith(suffix))
			{
                lastIndex = unitString.length() - suffix.length();
                try
				{
                    float v = Float.parseFloat(unitString.substring(0, lastIndex));
                    switch (suffix)
					{
                        case "sp":
                            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, ctx.getResources().getDisplayMetrics());
                        case "dp":
                            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, ctx.getResources().getDisplayMetrics());
                        case "dip":
                            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, ctx.getResources().getDisplayMetrics());
                        case "pt":
                            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, v, ctx.getResources().getDisplayMetrics());
                        case "px":
                            return v;
                        case "mm":
                            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, v, ctx.getResources().getDisplayMetrics());
                        case "in":
                            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, v, ctx.getResources().getDisplayMetrics());
                    }

                }
				catch (Exception e)
				{

                }
                break;
            }
        }
        debug.logE(mParser.getLineNumber() + "行 " + mParser.getColumnNumber() + "列：" + unitString + " 不是单位数字！");
        return -1;
    }

    public int getAttributeCount()
	{
        return mParser.getAttributeCount();
    }

    public String getAttributeNamespace(int index)
	{
        return mParser.getAttributeNamespace(index);
    }

    public String getAttributeName(int index)
	{
        return mParser.getAttributeName(index);
    }

    public String getAttributeValue(int index)
	{
        //转换
		return parseRowAttributeValue(getAttributeName(index), mParser.getAttributeValue(index));
    }

    public String getAttributeValue(String namespace, String name)
	{
        //转换
        return parseRowAttributeValue(name, mParser.getAttributeValue(namespace, name));
    }

    public String getPositionDescription()
	{
        return mParser.getPositionDescription();
    }

    public boolean hasAttribute(String name)
	{
        return mParser.getAttributeValue(androidNS, name) != null;
    }
}


