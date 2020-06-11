package com.vivo.chmusicdemo.utils.blog;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TechNewsXmlParser {
    private static final String TAG = "TechNewsXmlParser";

    public static List<Blog> getTechNews(String xmlData) throws IOException, XmlPullParserException {
        List<Blog> blogs = null;
        Blog blog = null;

        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = xmlPullParserFactory.newPullParser();
        parser.setInput(new StringReader(xmlData));

        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    blogs = new ArrayList<>();
                    blog = new Blog();
                    break;
                case XmlPullParser.START_TAG:
                    if("entry".equals(parser.getName())) {
                        blog = new Blog();
                    }
                    if("id".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogId(parser.getText());
                    } else if("title".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogTitle(parser.getText());
                    } else if("summary".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogSummary(parser.getText());
                    } else if("published".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogPublishedTime(parser.getText());
                    } else if("updated".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogUpdateTime(parser.getText());
                    } else if("sourceName".equals(parser.getName())) {
                        parser.next();
                        blog.setAuthorName(parser.getText());
                    } else if("link".equals(parser.getName())) {
//                        parser.next();
                        //格式不同，特殊处理
                        if(parser.getAttributeName(0).equals("rel")) {
                            blog.setBlogLink(parser.getAttributeValue(1));
                        }
                    } else if("diggs".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogDiggs(parser.getText());
                    } else if("views".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogViews(parser.getText());
                    } else if("comments".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogComments(parser.getText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    //当解析到entry标签结束的时候添加至blogs集合，清空blog对象
                    if("entry".equals(parser.getName())) {
                        blogs.add(blog);
                        blog = null;
                    }
                    break;
                default:
                    Log.d(TAG, "parse has wrong information");
            }
            eventType = parser.next();
        }
        return blogs;
    }
}
