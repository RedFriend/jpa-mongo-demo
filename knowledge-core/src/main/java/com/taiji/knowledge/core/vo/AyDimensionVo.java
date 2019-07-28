package com.taiji.knowledge.core.vo;

import java.io.Serializable;

/**
 * Created by yunsama on 2019/5/23.
 */
public class AyDimensionVo implements Serializable{

    private String ay;    //案由
    private String ayCode;//案由代码
    private int num1; //一级维度（统计）
    private int num2;  //二级维度（统计）
    private int num3; //三级维度（统计）

    public String getAy() {
        return ay;
    }

    public void setAy(String ay) {
        this.ay = ay;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public int getNum3() {
        return num3;
    }

    public void setNum3(int num3) {
        this.num3 = num3;
    }

    public String getAyCode() {
        return ayCode;
    }

    public void setAyCode(String ayCode) {
        this.ayCode = ayCode;
    }
}
