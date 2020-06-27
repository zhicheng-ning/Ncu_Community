package com.example.learncommunity.bean;

import cn.bmob.v3.BmobObject;

/***
 *@Authot: niko
 *@Date: Created in 20:55 2020/6/17
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class Comment extends BmobObject {
    private String content;//评论内容
    private MyUser zuozhe;//评论该帖子的人
    private Topic post;//关联对应的帖子

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getZuozhe() {
        return zuozhe;
    }

    public void setZuozhe(MyUser zuozhe) {
        this.zuozhe = zuozhe;
    }

    public Topic getPost() {
        return post;
    }

    public void setPost(Topic post) {
        this.post = post;
    }
}
