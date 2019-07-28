package com.taiji.knowledge.core.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yunsama on 2019/6/12.
 */
public class ArgueAndLawVo implements Serializable {
    private String ayCode;
    private String caseReaon;
    private List<ArgueVo> argueVos;
    private List<LawVo> lawVos;

    public String getAyCode() {
        return ayCode;
    }

    public void setAyCode(String ayCode) {
        this.ayCode = ayCode;
    }

    public String getCaseReaon() {
        return caseReaon;
    }

    public void setCaseReaon(String caseReaon) {
        this.caseReaon = caseReaon;
    }

    public List<LawVo> getLawVos() {
        return lawVos;
    }

    public void setLawVos(List<LawVo> lawVos) {
        this.lawVos = lawVos;
    }

    public List<ArgueVo> getArgueVos() {
        return argueVos;
    }

    public void setArgueVos(List<ArgueVo> argueVos) {
        this.argueVos = argueVos;
    }
}
