package com.taiji.knowledge.core.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yunsama on 2019/6/13.
 */
public class CaseCountQueryVo  implements Serializable {
    String fydm;//2577
    String fjm;//J35 查全市数据(NULL)
    Date larqStart;//立案日期开始
    Date larqEnd;//立案日期结束
    String jarqStart; //结案日期 2019-01-01
    String jarqEnd;//结案日期 2019-01-01
    String standardStart;//案件类型法标开始 0301
    String standardEnd;//案件类型法标结束 0301
    String caseStateStart;//案件状态开始 60
    String caseStateEnd;//案件状态结束 60
    Integer sfss;//是否上诉

    public String getFydm() {
        return fydm;
    }

    public void setFydm(String fydm) {
        this.fydm = fydm;
    }

    public String getFjm() {
        return fjm;
    }

    public void setFjm(String fjm) {
        this.fjm = fjm;
    }

    public Date getLarqStart() {
        return larqStart;
    }

    public void setLarqStart(Date larqStart) {
        this.larqStart = larqStart;
    }

    public Date getLarqEnd() {
        return larqEnd;
    }

    public void setLarqEnd(Date larqEnd) {
        this.larqEnd = larqEnd;
    }

    public String getJarqStart() {
        return jarqStart;
    }

    public void setJarqStart(String jarqStart) {
        this.jarqStart = jarqStart;
    }

    public String getJarqEnd() {
        return jarqEnd;
    }

    public void setJarqEnd(String jarqEnd) {
        this.jarqEnd = jarqEnd;
    }

    public String getStandardStart() {
        return standardStart;
    }

    public void setStandardStart(String standardStart) {
        this.standardStart = standardStart;
    }

    public String getStandardEnd() {
        return standardEnd;
    }

    public void setStandardEnd(String standardEnd) {
        this.standardEnd = standardEnd;
    }

    public String getCaseStateStart() {
        return caseStateStart;
    }

    public void setCaseStateStart(String caseStateStart) {
        this.caseStateStart = caseStateStart;
    }

    public String getCaseStateEnd() {
        return caseStateEnd;
    }

    public void setCaseStateEnd(String caseStateEnd) {
        this.caseStateEnd = caseStateEnd;
    }

    public Integer getSfss() {
        return sfss;
    }

    public void setSfss(Integer sfss) {
        this.sfss = sfss;
    }
}
