package com.cot.bottomnavigationview.bean;

import com.cot.bottomnavigationview.view.BottomNavigationView;

/**
 * @author COT
 * @version 1.0
 * @since 2019-7-17
 */
public class BottomNavigationBean {
    private String labelName;
    private int labelCheckedIcon;
    private int labelUncheckedIcon;

    private String badgeStyle = "0";//小红点状态 （0 默认不显示 1 单纯小红点  2 带数字）
    private String badgeValue;//小红点数字
    private int badgeColor = -1;//小红点颜色 默认红色
    private boolean isBadge;//是否启用

    private boolean isCheck;

    public BottomNavigationBean(String labelName, int labelCheckedIcon, int labelUncheckedIcon) {
        this.labelName = labelName;
        this.labelCheckedIcon = labelCheckedIcon;
        this.labelUncheckedIcon = labelUncheckedIcon;
    }

    public String getLabelName() {
        return labelName;
    }

    public BottomNavigationBean setLabelName(String labelName) {
        this.labelName = labelName;
        return this;
    }

    public int getLabelCheckedIcon() {
        return labelCheckedIcon;
    }

    public BottomNavigationBean setLabelCheckedIcon(int labelCheckedIcon) {
        this.labelCheckedIcon = labelCheckedIcon;
        return this;
    }

    public int getLabelUncheckedIcon() {
        return labelUncheckedIcon;
    }

    public BottomNavigationBean setLabelUncheckedIcon(int labelUncheckedIcon) {
        this.labelUncheckedIcon = labelUncheckedIcon;
        return this;
    }

    public String getBadgeStyle() {
        return badgeStyle;
    }

    public BottomNavigationBean setBadgeStyle(@BottomNavigationView.BadgeStyle String badgeStyle) {
        if (BottomNavigationView.UNAVAILABLE.equals(badgeStyle)) {
            setBadge(false);
        } else {
            setBadge(true);
        }
        this.badgeStyle = badgeStyle;
        return this;
    }

    public BottomNavigationBean removeBadge() {
        setBadgeStyle(BottomNavigationView.UNAVAILABLE);
        return this;
    }

    public String getBadgeValue() {
        return badgeValue;
    }

    public BottomNavigationBean setBadgeValue(String badgeValue) {
        this.badgeValue = badgeValue;
        return this;
    }

    public int getBadgeColor() {
        return badgeColor;
    }

    public void setBadgeColor(int badgeColor) {
        this.badgeColor = badgeColor;
    }

    public boolean isBadge() {
        return isBadge;
    }

    public BottomNavigationBean setBadge(boolean badge) {
        isBadge = badge;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public BottomNavigationBean setCheck(boolean check) {
        isCheck = check;
        return this;
    }
}
