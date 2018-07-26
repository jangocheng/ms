package com.dafy.lxp.ms.common.datasource;

/**
 * Created by liaoxudong
 * Date:2017/11/8
 */

public class MultiModuleDSHolder {
    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();

    public static void setDataSourceKey(String dataSourceKey) {
        if (dataSourceKey == null) {
            throw new IllegalArgumentException("dataSourceKey can't be null");
        }
        dataSourceHolder.set(dataSourceKey);
    }

    public static String getDataSourceKey() {
        return dataSourceHolder.get();
    }

    public static void clearDataSource(){
        dataSourceHolder.remove();
    }
}
