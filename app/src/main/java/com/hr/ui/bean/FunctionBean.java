package com.hr.ui.bean;

import java.io.Serializable;

/**
 * Created by wdr on 2017/8/10.
 */

public class FunctionBean implements Serializable {
    private String id;
    private String name;
    private boolean isSelect;
    private boolean showImage;

    public boolean isShowImage() {
        return showImage;
    }

    public void setShowImage(boolean showImage) {
        this.showImage = showImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "FunctionBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
