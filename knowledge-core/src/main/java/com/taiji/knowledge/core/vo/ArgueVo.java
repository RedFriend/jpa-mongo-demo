package com.taiji.knowledge.core.vo;

import java.io.Serializable;

/**
 * Created by yunsama on 2019/5/23.
 */
public class ArgueVo implements Serializable {
    private String id;            //争议ID
    private String argueContent;  //争议焦点内容
    private int frequency;   //频次

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArgueContent() {
        return argueContent;
    }

    public void setArgueContent(String argueContent) {
        this.argueContent = argueContent;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
