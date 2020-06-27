package com.example.learncommunity.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

import android.os.Parcel;
import android.os.Parcelable;

/***
 *@Authot: niko
 *@Date: Created in 7:58 2020/6/17
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class Knowledge extends BmobObject implements Parcelable {
    private String content;
    private MyUser author;
    private BmobFile picture;
    private String interest;//趣味知识
    private String type;//测试类别
    private String title;
    private BmobRelation record;//学习记录

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

    public BmobFile getPicture() {
        return picture;
    }

    public void setPicture(BmobFile picture) {
        this.picture = picture;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobRelation getRecord() {
        return record;
    }

    public void setRecord(BmobRelation record) {
        this.record = record;
    }

    public Knowledge(String content ,String interest, String type,String title) {
        super();
        this.content = content;

        this.interest = interest;
        this.type = type;
        this.title=title;

    }
    public Knowledge() {
        super();
    }
    public Knowledge(Parcel source) {
        content = source.readString();

        interest=source.readString();
        type = source.readString();
        title=source.readString();

    }
    /**
     * 这里默认返回0即可
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * 把值写*/

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(interest);
        dest.writeString(type);
        dest.writeString(title);

    }

    public static final Creator<Knowledge> CREATOR = new Creator<Knowledge>() {

        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public Knowledge[] newArray(int size) {
            return new Knowledge[size];
        }

        @Override
        public Knowledge createFromParcel(Parcel source) {
            return new Knowledge(source);
        }
    };
}
