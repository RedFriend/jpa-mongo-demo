package com.taiji.knowledge.core.vo;

import java.io.Serializable;

/**
 * Created by yunsama on 2019/5/23.
 */
public class LawVo implements Serializable {

    private String id;
    private String law;      //法条
    private int frequency;   //频次

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLaw() {
        return law;
    }

    public void setLaw(String law) {
        this.law = law;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
