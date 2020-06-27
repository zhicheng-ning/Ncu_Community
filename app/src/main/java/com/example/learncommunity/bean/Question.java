package com.example.learncommunity.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

import java.io.Serializable;
import java.util.List;

/***
 *@Authot: niko
 *@Date: Created in 23:20 2020/6/14
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class Question extends BmobObject implements Serializable {
    private String problem;//问题
    private String qA;//a选项
    private String qB;//b选项
    private String qC;//c选项
    private String qD;//d选项
    private BmobRelation wrong;//错题集（关联用户）
    private Integer answer;//正确答案
    private Integer myanswer;//选择的答案
    private String jiexi;//解析
    private List<String> sheet;//题目类型

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getqA() {
        return qA;
    }

    public void setqA(String qA) {
        this.qA = qA;
    }

    public String getqB() {
        return qB;
    }

    public void setqB(String qB) {
        this.qB = qB;
    }

    public String getqC() {
        return qC;
    }

    public void setqC(String qC) {
        this.qC = qC;
    }

    public String getqD() {
        return qD;
    }

    public void setqD(String qD) {
        this.qD = qD;
    }

    public BmobRelation getWrong() {
        return wrong;
    }

    public void setWrong(BmobRelation wrong) {
        this.wrong = wrong;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public Integer getMyanswer() {
        return myanswer;
    }

    public void setMyanswer(Integer myanswer) {
        this.myanswer = myanswer;
    }

    public String getJiexi() {
        return jiexi;
    }

    public void setJiexi(String jiexi) {
        this.jiexi = jiexi;
    }

    public List<String> getSheet() {
        return sheet;
    }

    public void setSheet(List<String> sheet) {
        this.sheet = sheet;
    }
}
