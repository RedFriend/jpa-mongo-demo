package com.taiji.knowledge.core.vo;

import javax.naming.directory.SearchResult;
import java.io.Serializable;

/**
 * Created by yunsama on 2019/6/13.
 */
public class CountVo implements Serializable {
    String name;
    int num;
    String value; //num 对应string

    int sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
