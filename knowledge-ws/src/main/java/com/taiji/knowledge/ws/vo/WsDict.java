package com.taiji.knowledge.ws.vo;

import java.util.Date;

public class WsDict {

    private static final long serialVersionUID = -287369810087816750L;

    protected Long id;

    protected String createBy;

    protected Date createTime;

    protected String updateBy;

    protected Date updateTime;

    protected Integer enableFlag;
    /**
     * 类型
     */
    private String type;

    /**
     * 值
     */
    private Integer value;

    /**
     * 名字
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取值
     *
     * @return value - 值
     */
    public Integer getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * 获取名字
     *
     * @return name - 名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名字
     *
     * @param name 名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}