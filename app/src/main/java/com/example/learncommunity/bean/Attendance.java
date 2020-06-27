package com.example.learncommunity.bean;

import cn.bmob.v3.BmobObject;

/***
 *@Authot: niko
 *@Date: Created in 22:13 2020/6/14
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class Attendance extends BmobObject {
    private String name;//签到的用户名
    private MyUser user;//关联的用户
    private Boolean state;//签到状态
    private String time; //签到的时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
