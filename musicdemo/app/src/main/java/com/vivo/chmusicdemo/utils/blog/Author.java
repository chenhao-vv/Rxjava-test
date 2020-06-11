package com.vivo.chmusicdemo.utils.blog;

public class Author {

    private String mAuthorUri;
    private String mAuthorName;
    private String mAuthorPic;
    //博客名
    private String mBlogApp;
    //博客数量
    private String mBolgCount;

    private String mUpdate;

    public Author() {}

    public Author(String authorUri, String authorName, String authorPic, String blogApp,
                  String blogCount, String update) {
        mAuthorUri = authorUri;
        mAuthorName = authorName;
        mAuthorPic = authorPic;
        mBlogApp = blogApp;
        mBolgCount = blogCount;
        mUpdate = update;
    }

    public String getAuthorUri() {
        return mAuthorUri;
    }

    public void setAuthorUri(String authorUri) {
        mAuthorUri = authorUri;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String name) {
        mAuthorName = name;
    }

    public String getAuthorPic() {
        return mAuthorPic;
    }

    public void setAuthorPic(String authorPic) {
        mAuthorPic = authorPic;
    }

    public String getBlogApp() {
        return mBlogApp;
    }

    public void setBlogApp(String blogApp) {
        mBlogApp = blogApp;
    }

    public String getBolgCount() {
        return mBolgCount;
    }

    public void setBolgCount(String bolgCount) {
        mBolgCount = bolgCount;
    }

    public String getUpdate() {
        return mUpdate;
    }

    public void setUpdate(String update) {
        mUpdate = update;
    }
}
