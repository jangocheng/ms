package com.dafy.lxp.ms.common.domain;

import com.dafy.yihui.common.util.JsonUtils;
import com.dafy.yihui.common.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * spring MVC请求对象封装
 * Created by liaoxudong
 * Date:2018/1/30
 */

public class Request extends LinkedHashMap<String,Object>{

    public Request() {
        super();
    }

    public Request(int i, float v) {
        super(i, v);
    }

    public Request(int i) {
        super(i);
    }

    public Request(Map<? extends String, ?> map) {
        super(map);
    }

    public static Request parse(Map<? extends String, ?> map){
        return new Request(map);
    }

    public Request(int i, float v, boolean b) {
        super(i, v, b);
    }

    public String getString(String key) {
        Object o = this.get(key);
        if(o == null) return null;
        else return o.toString();
    }

    public int getInt(String key) {
        Object value = this.get(key);
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean getBoolean(String key) {
        String value = getString(key);
        return Boolean.valueOf(value);
    }

    public <T> T get(String key,Class<T> clazz) {
        Object value = this.get(key);
        try {
            return (T) value;
        } catch (Exception e) {
            return null;
        }
    }

    public long getLong(String key) {
        String s = getString(key);
        return Long.parseLong(StringUtils.isEmpty(s)?"0":s);
    }

    public List getList(String key) {
        return get(key, List.class);
    }

    @Override
    public Object put(String  o, Object o2) {
        return super.put(o, o2);
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
