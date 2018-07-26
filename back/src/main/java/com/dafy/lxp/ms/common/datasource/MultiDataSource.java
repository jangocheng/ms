package com.dafy.lxp.ms.common.datasource;

/**
 * 多模块引用
 * Created by liaoxudong
 * Date:2017/11/8
 */

public enum MultiDataSource {

    DEFAULT("ms","后台管理数据库"),
    PUBLIC("Public","公共模块"),
    USER("User","用户模块"),
    LOAN("Loan","合同模块");

    String code;
    String desc;

    MultiDataSource(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
