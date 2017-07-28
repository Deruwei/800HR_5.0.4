package com.hr.ui.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * 作者：Colin
 * 日期：2015/12/21 17:07
 * 邮箱：bestxt@qq.com
 *
 * 序列化Map集合 用于传递参数
 */
public class SerializableMap implements Serializable {
    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
