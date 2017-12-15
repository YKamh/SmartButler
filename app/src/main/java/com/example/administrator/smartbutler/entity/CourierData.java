package com.example.administrator.smartbutler.entity;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.entity
 * 创建者：    Kamh
 * 创建时间：  2017/12/410:23
 * 描述：      快递查询实体类
 */
public class CourierData {
    //时间
    private String datatime;
    //状态
    private String remark;
    //城市
    private String zone;

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
