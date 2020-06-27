package com.example.learncommunity.bean;

import cn.bmob.v3.BmobObject;

/***
 *@Authot: niko
 *@Date: Created in 20:56 2020/6/17
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class Study extends BmobObject {
    private String type;
    private String content;
    private String count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
