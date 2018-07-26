package com.dafy.lxp.ms.dto.pub;

import java.io.Serializable;
import java.util.Date;

public class ClientLog implements Serializable{
    private Long id;

    private String uuid1;

    private String uuid2;

    private String userid;

    private String userphone;

    private String level;

    private String title;

    private Date logtime;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid1() {
        return uuid1;
    }

    public void setUuid1(String uuid1) {
        this.uuid1 = uuid1 == null ? null : uuid1.trim();
    }

    public String getUuid2() {
        return uuid2;
    }

    public void setUuid2(String uuid2) {
        this.uuid2 = uuid2 == null ? null : uuid2.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone == null ? null : userphone.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getLogtime() {
        return logtime;
    }

    public void setLogtime(Date logtime) {
        this.logtime = logtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}