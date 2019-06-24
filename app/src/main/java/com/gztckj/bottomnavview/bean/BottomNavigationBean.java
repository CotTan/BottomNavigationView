package com.gztckj.bottomnavview.bean;

import com.gztckj.bottomnavview.R;

public class BottomNavigationBean {
    private String labelName;
    private int labelCheckedIcon;
    private int labelUncheckedIcon;
    public static int CHECKED_COLOR = R.color.textColor;
    public static int UNCHECKED_COLOR = R.color.textGrayColor;
    public static int CHECKED_TEXT_SIZE = 14;
    public static int UNCHECKED_TEXT_SIZE = 14;

    private boolean isCheck;

    public BottomNavigationBean(String labelName, int labelCheckedIcon, int labelUncheckedIcon) {
        this.labelName = labelName;
        this.labelCheckedIcon = labelCheckedIcon;
        this.labelUncheckedIcon = labelUncheckedIcon;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public int getLabelCheckedIcon() {
        return labelCheckedIcon;
    }

    public void setLabelCheckedIcon(int labelCheckedIcon) {
        this.labelCheckedIcon = labelCheckedIcon;
    }

    public int getLabelUncheckedIcon() {
        return labelUncheckedIcon;
    }

    public void setLabelUncheckedIcon(int labelUncheckedIcon) {
        this.labelUncheckedIcon = labelUncheckedIcon;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
