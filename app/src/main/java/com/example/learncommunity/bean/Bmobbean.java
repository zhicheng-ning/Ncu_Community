package com.example.learncommunity.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/***
 *@Authot: niko
 *@Date: Created in 8:10 2020/6/17
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class Bmobbean extends BmobObject {
    private String content;//反馈内容
    private MyUser author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }
}

