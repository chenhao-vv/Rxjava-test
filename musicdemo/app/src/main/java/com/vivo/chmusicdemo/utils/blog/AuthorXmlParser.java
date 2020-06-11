package com.vivo.chmusicdemo.utils.blog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AuthorXmlParser {

    /**
     * 用于解析博客作者(列表)的xml，返回Avatar的List集合对象
     *
     * @param inputStream
     * @param encode
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static List<Author> getListAuthorVolley(InputStream inputStream, String encode) throws XmlPullParserException, IOException {

        List<Author> authors = null;
        Author author = null;

        //获取XmlPullParser实例
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, encode);
        //获取解析事件
        int eventType = parser.getEventType();
        //当xml文档未到尾端时
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                //解析根标签的时候，实例化集合
                case XmlPullParser.START_DOCUMENT:
                    authors = new ArrayList<Author>();
                    author = new Author();

                    break;
                case XmlPullParser.START_TAG:
                    //当解析到entry标签的时候，实例化Avatar对象
                    if ("entry".equals(parser.getName())) {
                        author = new Author();
                    }
                    if ("id".equals(parser.getName())) {
                        parser.next();
                        author.setAuthorUri(parser.getText());
                    } else if ("title".equals(parser.getName())) {
                        parser.next();
                        if (parser.getText().indexOf("博客园") == -1) {
                            author.setAuthorName(parser.getText());
                        }
                    } else if ("avatar".equals(parser.getName())) {
                        parser.next();
                        author.setAuthorPic(parser.getText());
                    } else if ("blogapp".equals(parser.getName())) {
                        parser.next();
                        author.setBlogApp(parser.getText());
                    } else if ("postcount".equals(parser.getName())) {
                        parser.next();
                        author.setBolgCount(parser.getText());
                    } else if ("updated".equals(parser.getName())) {
                        parser.next();
                        //区分日期格式
                        if (parser.getText().indexOf("+") != -1) {
                            author.setUpdate(parser.getText());
                        }

                    }
                    break;
                case XmlPullParser.END_TAG:
                    //当解析到entry标签结束的时候添加入Avatar集合，清空Avatar对象
                    if ("entry".equals(parser.getName())) {
                        authors.add(author);
                        author = null;
                    }
                    break;

            }
            //手动跳转第一次遍历
            eventType = parser.next();
        }
        return authors;
    }

    /**
     * 采用OKHttp
     * 用于解析博客作者(列表)的xml，返回Avatar的List集合对象
     *
     * @param xmlData
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static List<Author> getListAuthorOKHttp(String xmlData) throws XmlPullParserException, IOException {

        List<Author> authors = null;
        Author author = null;

        //获取XmlPullParser实例
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xmlData));
        //获取解析事件
        int eventType = parser.getEventType();
        //当xml文档未到尾端时
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                //解析根标签的时候，实例化集合
                case XmlPullParser.START_DOCUMENT:
                    authors = new ArrayList<Author>();
                    author = new Author();

                    break;
                case XmlPullParser.START_TAG:
                    //当解析到entry标签的时候，实例化Avatar对象
                    if ("entry".equals(parser.getName())) {
                        author = new Author();
                    }
                    if ("id".equals(parser.getName())) {
                        parser.next();
                        author.setAuthorUri(parser.getText());
                    } else if ("title".equals(parser.getName())) {
                        parser.next();
                        if (parser.getText().indexOf("博客园") == -1) {
                            author.setAuthorName(parser.getText());
                        }
                    } else if ("avatar".equals(parser.getName())) {
                        parser.next();
                        author.setAuthorPic(parser.getText());
                    } else if ("blogapp".equals(parser.getName())) {
                        parser.next();
                        author.setBlogApp(parser.getText());
                    } else if ("postcount".equals(parser.getName())) {
                        parser.next();
                        author.setBolgCount(parser.getText());
                    } else if ("updated".equals(parser.getName())) {
                        parser.next();
                        //区分日期格式
                        if (parser.getText().indexOf("+") != -1) {
                            author.setUpdate(parser.getText());
                        }

                    }
                    break;
                case XmlPullParser.END_TAG:
                    //当解析到entry标签结束的时候添加入Avatar集合，清空Avatar对象
                    if ("entry".equals(parser.getName())) {
                        authors.add(author);
                        author = null;
                    }
                    break;

            }
            //手动跳转第一次遍历
            eventType = parser.next();
        }
        return authors;
    }
    }
