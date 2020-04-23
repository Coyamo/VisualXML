package coyamo.visualxml.lib.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

//注意 这个类只是为了防止影响到解析
//不是保留XmlPullParser的状态
//XmlPullParser对象内还是在执行的
//所以得到这个对象时要自己保存需要的数据
//否则XmlPullParser会指向其他位置了
public class ReadOnlyParser {
    private XmlPullParser parser;

    ReadOnlyParser(XmlPullParser parser) {
        this.parser = parser;
    }

    public boolean getFeature(String name) {
        return parser.getFeature(name);
    }

    public Object getProperty(String name) {
        return parser.getProperty(name);
    }

    public String getInputEncoding() {
        return parser.getInputEncoding();
    }

    public int getNamespaceCount(int depth) throws XmlPullParserException {
        return parser.getNamespaceCount(depth);
    }

    public String getNamespacePrefix(int pos) throws XmlPullParserException {
        return parser.getNamespacePrefix(pos);
    }

    public String getNamespaceUri(int pos) throws XmlPullParserException {
        return parser.getNamespaceUri(pos);
    }

    public String getNamespace(String prefix) {
        return parser.getNamespace(prefix);
    }

    public int getDepth() {
        return parser.getDepth();
    }

    public String getPositionDescription() {
        return parser.getPositionDescription();
    }

    public int getLineNumber() {
        return parser.getLineNumber();
    }

    public int getColumnNumber() {
        return parser.getColumnNumber();
    }

    public boolean isWhitespace() throws XmlPullParserException {
        return parser.isWhitespace();
    }

    public String getText() {
        return parser.getText();
    }

    public char[] getTextCharacters(int[] holderForStartAndLength) {
        return parser.getTextCharacters(holderForStartAndLength);
    }

    public String getNamespace() {
        return parser.getNamespace();
    }

    public String getName() {
        return parser.getName();
    }

    public String getPrefix() {
        return parser.getPrefix();
    }

    public boolean isEmptyElementTag() throws XmlPullParserException {
        return parser.isEmptyElementTag();
    }

    public int getAttributeCount() {
        return parser.getAttributeCount();
    }

    public String getAttributeNamespace(int index) {
        return parser.getAttributeNamespace(index);
    }

    public String getAttributeName(int index) {
        return parser.getAttributeName(index);
    }

    public String getAttributePrefix(int index) {
        return parser.getAttributePrefix(index);
    }

    public String getAttributeType(int index) {
        return parser.getAttributeType(index);
    }

    public boolean isAttributeDefault(int index) {
        return parser.isAttributeDefault(index);
    }

    public String getAttributeValue(int index) {
        return parser.getAttributeValue(index);
    }

    public String getAttributeValue(String namespace, String name) {
        return parser.getAttributeValue(namespace, name);
    }

    public String getAttributeValueInAndroid(String name) {
        return parser.getAttributeValue(AndroidXmlParser.ANDROID_NS, name);
    }

    public int getEventType() throws XmlPullParserException {
        return parser.getEventType();
    }

}
