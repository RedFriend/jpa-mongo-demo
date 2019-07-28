package com.taiji.knowledge.ws.vo;

import java.io.Serializable;

/**
 * Created by Think on 2019-5-24.
 */
public class WsTemplateVo implements Serializable{
    String id;
    String name;//文书名称
    String url;//pdf路径
    int num; //使用次数
    String sort;//排序字段
    String maker;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }
}
