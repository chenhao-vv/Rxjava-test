package com.vivo.chmusicdemo.utils.blog;

import java.io.Serializable;

/**
 * 博客实体类
 *
 */

public class Blog implements Serializable {
    //文章id
    private String mBlogId;
    //文章标题
    private String mBlogTitle;
    //文章概要
    private String mBlogSummary;
    //发布时间
    private String mBlogPublishedTime;
    //更新时间
    private String mBlogUpdateTime;
    //博主昵称
    private String mAuthorName;
    //博主头像地址
    private String mAuthorAvatar;
    //博主博客地址
    private String mAuthorUri;
    //博文链接
    private String mBlogLink;
    //博文评论数
    private String mBlogComments;
    //博文浏览数
    private String mBlogViews;
    //博文推荐数
    private String mBlogDiggs;

    public Blog() {
    }

    public Blog(String blogId, String blogTitle, String blogSummary, String blogPublishedTime, String authorName, String authorAvatar,
                String authorUri, String blogLink, String blogComments, String blogViews, String blogDiggs) {
        mBlogId = blogId;
        mBlogTitle = blogTitle;
        mBlogSummary = blogSummary;
        mBlogPublishedTime = blogPublishedTime;
        mAuthorName = authorName;
        mAuthorAvatar = authorAvatar;
        mAuthorUri = authorUri;
        mBlogLink = blogLink;
        mBlogComments = blogComments;
        mBlogViews = blogViews;
        mBlogDiggs = blogDiggs;
    }

    public String getBlogId() {
        return mBlogId;
    }

    public void setBlogId(String blogId) {
        mBlogId = blogId;
    }

    public String getBlogTitle() {
        return mBlogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        mBlogTitle = blogTitle;
    }

    public String getBlogSummary() {
        return mBlogSummary;
    }

    public void setBlogSummary(String blogSummary) {
        mBlogSummary = blogSummary;
    }

    public String getBlogPublishedTime() {
        return mBlogPublishedTime;
    }

    public void setBlogPublishedTime(String blogPublishedTime) {
        mBlogPublishedTime = blogPublishedTime;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getAuthorAvatar() {
        return mAuthorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        mAuthorAvatar = authorAvatar;
    }

    public String getAuthorUri() {
        return mAuthorUri;
    }

    public void setAuthorUri(String authorUri) {
        mAuthorUri = authorUri;
    }

    public String getBlogLink() {
        return mBlogLink;
    }

    public void setBlogLink(String blogLink) {
        mBlogLink = blogLink;
    }

    public String getBlogComments() {
        return mBlogComments;
    }

    public void setBlogComments(String blogComments) {
        mBlogComments = blogComments;
    }

    public String getBlogViews() {
        return mBlogViews;
    }

    public void setBlogViews(String blogViews) {
        mBlogViews = blogViews;
    }

    public String getBlogDiggs() {
        return mBlogDiggs;
    }

    public void setBlogDiggs(String blogDiggs) {
        mBlogDiggs = blogDiggs;
    }

    public String getBlogUpdateTime() {
        return mBlogUpdateTime;
    }

    public void setBlogUpdateTime(String mBlogUpdateTime) {
        this.mBlogUpdateTime = mBlogUpdateTime;
    }

    @Override
    public String toString() {
        return "Blog(" +
                "BlogId = " + mBlogId +'\'' +
                ", BlogTitle = " + mBlogTitle + '\'' +
                ", BlogSummary = " + mBlogSummary + '\'' +
                ", BlogPublishedTime = " + mBlogPublishedTime + '\'' +
                ", AuthorName = " + mAuthorName + '\'' +
                ", AuthorAvatar = " + mAuthorAvatar + '\'' +
                ", AuthorUri = " + mAuthorUri + '\'' +
                ", BlogLink = " + mBlogLink + '\'' +
                ", BlogComments = " + mBlogComments + '\'' +
                ", BlogViews = " + mBlogViews + '\'' +
                ", BlogDiggs = " + mBlogDiggs + '\'' + "}";

    }
}
