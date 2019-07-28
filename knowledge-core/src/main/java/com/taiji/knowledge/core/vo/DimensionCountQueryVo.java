package com.taiji.knowledge.core.vo;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yunsama on 2019/5/23.
 */
public class DimensionCountQueryVo implements Serializable{
    private String ayCode;//案由代码
    private String dimensionId;//维度id
    private Integer type;  //1-争议焦点  2-案件情节 3-查询维度一  4-查询维度二  5-查询维度三
    private Date startDate; //统计开始日期
    private Date endDate;  //统计结束日期

    public String getAyCode() {
        return ayCode;
    }

    public void setAyCode(String ayCode) {
        this.ayCode = ayCode;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "DimensionCountQueryVo{" +
                "ayCode='" + ayCode + '\'' +
                ", dimensionId='" + dimensionId + '\'' +
                ", type=" + type +
                ", startDate=" + (startDate==null?"":DateFormatUtils.format(startDate,"yyyy-MM-dd") )+
                ", endDate=" + (endDate==null?"":DateFormatUtils.format(endDate,"yyyy-MM-dd") )+
                '}';
    }
}
