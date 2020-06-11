package com.vivo.chmusicdemo.utils.blog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 对博客列表xml数据进行解析
 */
public class BlogsListXmlParser {

    /**
     * 采用volley
     * 用于解析博客列表的xml，返回Blog的List集合
     */
    public static List<Blog> getListBlogsVolley(InputStream inputStream, String encode) throws XmlPullParserException, IOException {
        List<Blog> blogs = null;
        Blog blog = null;

        //获取XmlParser实例
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, encode);

        //获取解析事件
        int eventType = parser.getEventType();
        //当XML文档到达末尾
        while(eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    blogs = new ArrayList<>();
                    blog = new Blog();
                    break;
                case XmlPullParser.START_TAG:
                    if("entry".equals(parser.getName())) {
                        blog = new Blog();
                    }
                    if ("id".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogId(parser.getText());
                    } else if("title".equals(parser.getName())){
                        parser.next();
                        //特殊处理
                        if(!"博客园".equals(parser.getText())) {
                            blog.setBlogTitle(parser.getText());
                        }
                    } else if("summary".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogSummary(parser.getText());
                    } else if("published".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogPublishedTime(parser.getText());
                    } else if("name".equals(parser.getName())) {
                        parser.next();
                        blog.setAuthorName(parser.getText());
                    } else if("uri".equals(parser.getName())) {
                        parser.next();
                        blog.setAuthorUri(parser.getText());
                    } else if("avatar".equals(parser.getName())) {
                        parser.next();
                        blog.setAuthorAvatar(parser.getText());
                    } else if ("link".equals(parser.getName())) {
                        //特殊处理
                        if(parser.getAttributeName(0).equals("rel")) {
                            blog.setBlogLink(parser.getAttributeValue(1));
                        }
                    } else if ("diggs".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogDiggs(parser.getText());
                    } else if ("views".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogViews(parser.getText());
                    } else if ("comments".equals(parser.getName())) {
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
            }
            //手动跳转第一次遍历
            eventType = parser.next();
        }
        return blogs;
    }


    /**
     * 采用OKHttp
     * 用于解析博客列表的xml，返回Blog的List集合
     */
    public static List<Blog> getListBlogsOKHttp(String xmlData) throws XmlPullParserException, IOException {
        List<Blog> blogs = null;
        Blog blog = null;

        //获取XmlParser实例
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlData));

        //获取解析事件
        int eventType = parser.getEventType();
        //当XML文档到达末尾
        while(eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    blogs = new ArrayList<>();
                    blog = new Blog();
                    break;
                case XmlPullParser.START_TAG:
                    if("entry".equals(parser.getName())) {
                        blog = new Blog();
                    }
                    if ("id".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogId(parser.getText());
                    } else if("title".equals(parser.getName())){
                        parser.next();
                        //特殊处理
                        if(!"博客园".equals(parser.getText())) {
                            blog.setBlogTitle(parser.getText());
                        }
                    } else if("summary".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogSummary(parser.getText());
                    } else if("published".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogPublishedTime(parser.getText());
                    } else if("name".equals(parser.getName())) {
                        parser.next();
                        blog.setAuthorName(parser.getText());
                    } else if("uri".equals(parser.getName())) {
                        parser.next();
                        blog.setAuthorUri(parser.getText());
                    } else if("avatar".equals(parser.getName())) {
                        parser.next();
                        blog.setAuthorAvatar(parser.getText());
                    } else if ("link".equals(parser.getName())) {
                        //特殊处理
                        if(parser.getAttributeName(0).equals("rel")) {
                            blog.setBlogLink(parser.getAttributeValue(1));
                        }
                    } else if ("diggs".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogDiggs(parser.getText());
                    } else if ("views".equals(parser.getName())) {
                        parser.next();
                        blog.setBlogViews(parser.getText());
                    } else if ("comments".equals(parser.getName())) {
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
            }
            //手动跳转第一次遍历
            eventType = parser.next();
        }
        return blogs;
    }
}
