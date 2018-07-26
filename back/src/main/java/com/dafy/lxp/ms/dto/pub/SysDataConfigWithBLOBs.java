package com.dafy.lxp.ms.dto.pub;

public class SysDataConfigWithBLOBs extends SysDataConfig {
    private String data;

    private String data1;

    private String data2;

    private String data3;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1 == null ? null : data1.trim();
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2 == null ? null : data2.trim();
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3 == null ? null : data3.trim();
    }
}