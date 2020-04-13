package coyamo.visualxml.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MessageArray {
    private static MessageArray instanse;

    public List<XmlMessage> getList() {
        return list;
    }

    private List<XmlMessage> list;
    private OnNewMessageListener listener;
    private MessageArray(){
        list=new ArrayList<>();
    }

    public static MessageArray getInstanse(){
        if(instanse==null) instanse=new MessageArray();
        return instanse;
    }

    public void add(XmlMessage m){
        list.add(m);
        if(listener!=null)listener.onNew(list,m);
    }

    public void logD(String msg){
        XmlMessage m=new XmlMessage();
        m.setType(XmlMessage.TYPE_DEBUG);
        m.setMessage(msg);
        add(m);
    }
    public void logW(String msg){
        XmlMessage m=new XmlMessage();
        m.setType(XmlMessage.TYPE_WARN);
        m.setMessage(msg);
        add(m);
    }
    public void logE(String msg){
        XmlMessage m=new XmlMessage();
        m.setType(XmlMessage.TYPE_ERROR);
        m.setMessage(msg);
        add(m);
    }
    public void clear(){
        list.clear();
    }
    public void setListener(OnNewMessageListener listener) {
        this.listener = listener;
    }
    public interface OnNewMessageListener{
        void onNew(List<XmlMessage> list,XmlMessage m);
    }
}
