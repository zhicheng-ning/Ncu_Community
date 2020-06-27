package com.example.learncommunity.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/***
*@Authot: niko
*@Date: Created in 20:56 2020/6/17
*@EMAIL: simaqinsheng@gmail.com
*@VERSION: 1.0
*/
public class Topic extends BmobObject {
    private String content;//发贴文字内容
    private String title;//标题
    private MyUser author;//发帖人
    private BmobFile picture;//图片
    private BmobRelation view;//浏览量(关联到用户)一对多关系：用于存储评论该帖子的所有用户

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public BmobRelation getView() {
        return view;
    }

    public void setView(BmobRelation view) {
        this.view = view;
    }
}
