package com.dafy.lxp.ms.dto.user;

import com.dafy.yihui.common.util.JsonUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志
 */
public class UserOpLog implements Serializable{
    private Long id;

    private String phoneno;

    private String opType;

    private String opDesc;

    private Date opTime;

    private String opDevice;

    private String opGps;

    private String deviceModel;

    private String osVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno == null ? null : phoneno.trim();
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType == null ? null : opType.trim();
    }

    public String getOpDesc() {
        return opDesc;
    }

    public void setOpDesc(String opDesc) {
        this.opDesc = opDesc == null ? null : opDesc.trim();
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getOpDevice() {
        return opDevice;
    }

    public void setOpDevice(String opDevice) {
        this.opDevice = opDevice == null ? null : opDevice.trim();
    }

    public String getOpGps() {
        return opGps;
    }

    public void setOpGps(String opGps) {
        this.opGps = opGps == null ? null : opGps.trim();
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel == null ? null : deviceModel.trim();
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion == null ? null : osVersion.trim();
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}