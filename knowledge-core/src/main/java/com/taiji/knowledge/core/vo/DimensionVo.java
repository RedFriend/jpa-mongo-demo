package com.taiji.knowledge.core.vo;

import java.io.Serializable;

/**
 * Created by yunsama on 2019/5/23.
 */
public class DimensionVo implements Serializable {
    private String id;  //维度id
    private String dimension;  //维度

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
