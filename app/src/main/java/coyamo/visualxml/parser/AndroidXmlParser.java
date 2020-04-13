package coyamo.visualxml.parser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;
import coyamo.visualxml.proxy.ProxyAttributeSet;
import coyamo.visualxml.ui.OutlineView;
import coyamo.visualxml.proxy.*;
import coyamo.visualxml.utils.MessageArray;

public class AndroidXmlParser
{
    private XmlPullParser parser;
    private Context context;
	//存放解析过程中的所有view
    private Stack<View> allViewStack = new Stack<>();
    //存放解析过程中的viewgroup
	private Stack<ViewGroup> viewGroupStack = new Stack<>();
    private OutlineView container;
    private MessageArray debug=MessageArray.getInstanse();
    private AndroidXmlParser(Context context, OutlineView container)
	{
        this.context = context;
        this.container = container;
        viewGroupStack.push(container);
		allViewStack.push(container);
        try
		{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
        }
		catch (Exception e)
		{
		    debug.logW("初始化解析失败："+e);
        }
    }

    public static AndroidXmlParser with(OutlineView container)
	{
        AndroidXmlParser axp = new AndroidXmlParser(container.getContext(), container);
        return axp;
    }


    public AndroidXmlParser parse(String xml)
	{
        StringReader sr = new StringReader(xml);
        parse(sr);
        return this;
    }
    public AndroidXmlParser parse(Reader reader)
	{
        try
		{
            parser.setInput(reader);
            parse();
            reader.close();
        }
		catch (Exception e)
		{
            debug.logW("从Reader解析失败："+e);
        }
        return this;
    }
    public AndroidXmlParser parse(File path)
	{
        try
		{
            FileReader fileReader = new FileReader(path);
            parse(fileReader);
        }
		catch (Exception e)
		{
            debug.logW("从File解析失败："+e);
        }
        return this;
    }

    private void parse()
	{
        try
		{
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
			{
                switch (eventType)
				{
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
						View view;
						
						//如果包含style属性，好像只能从构造方法给view设置？
						String attr=parser.getAttributeValue(null, "style");
						if (attr != null)
						{
							int attrInt=ProxyResources.getInstance().getAttr(attr);
							view = ViewCreator.create(tagName, context, attrInt);
						}
						else
						{
							view = ViewCreator.create(tagName, context);
						}
						//当前viewgroup
						ViewGroup viewGroup = viewGroupStack.peek();
						//当前view
						View lastView=allViewStack.peek();
						
						/*
						不知道这个检查错误的方法有没有bug
						
						*/
						if(lastView==viewGroup){
                       		OutlineView.addViewInto(view, viewGroup, container);
						}else{
							//出现了非viewgroup的view包含view的情况
                            debug.logE(parser.getLineNumber()+"行 "+parser.getColumnNumber()+"列："+lastView.getClass().getName()+"不能转换为ViewGroup，已经自动忽略错误的xml片段。");
						}
                        if (view instanceof ViewGroup)
						{
                            //添加到viewgroup栈
                            viewGroupStack.push((ViewGroup) view);
                        }
				
                        allViewStack.push(view);
                        
						//设置属性
                        ProxyAttributeSet attrs = new ProxyAttributeSet(parser, context);
                        attrs.setTo(view);

                        break;
                    case XmlPullParser.END_TAG:
                        View v = allViewStack.pop();
                        if ( v instanceof ViewGroup)
						{
                            viewGroupStack.pop();
                        }
                        break;
                }
                eventType = parser.next();
            }

        }
		catch (Exception e)
		{
            debug.logE(parser.getLineNumber()+"行 "+parser.getColumnNumber()+"列：解析过程中出现错误"+e);

        }
    }

    }
