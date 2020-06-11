package com.vivo.chmusicdemo.utils.blog;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class BlogContentXmlParser {

    public static String getBlogContent(String xmlData) throws XmlPullParserException, IOException {
        String content = "";
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlData));
        Log.d("BlogContent", "进行xml解析");
        //获取解析事件
        int eventType = parser.getEventType();
        //当xml文档未到末尾时；
        while(eventType != XmlPullParser.END_DOCUMENT) {
            Log.d("BlogContent", "Xmlpullparser");
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if("string".equals(parser.getName())) {
                        parser.next();
                        content = parser.getText();
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return content;
    }
}
