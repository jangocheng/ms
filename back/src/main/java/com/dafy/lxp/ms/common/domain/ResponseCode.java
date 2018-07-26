package com.dafy.lxp.ms.common.domain;

/**
 * Created by liaoxudong
 * Date:2018/1/30
 */

public enum ResponseCode {
    SUCCESS("00","成功"),
    LOGIN_SUCCESS("10","登录成功"),
    LOGIN_ERROR("11","登录失败，请检查用户名/密码"),
    LOGIN_EXPIRE("12","登录已过期，请重新登录"),
    ;

    private String code;
    private String desc;

    ResponseCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
