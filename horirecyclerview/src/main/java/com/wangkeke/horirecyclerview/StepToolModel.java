package com.wangkeke.horirecyclerview;

import java.util.ArrayList;
import java.util.List;

public class StepToolModel {

    /**
     * 子布局类型
     */
    private int type;

    private boolean isOpen;

    /**
     * 未展开内容
     */
    private String text;

    /**
     * 展开后的内容
     */
    private String childrenText;

    /**
     * 是否是选中状态
     */
    private boolean isSelected;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChildrenText() {
        return childrenText;
    }

    public void setChildrenText(String childrenText) {
        this.childrenText = childrenText;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
