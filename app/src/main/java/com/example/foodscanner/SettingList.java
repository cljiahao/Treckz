package com.example.foodscanner;

public class SettingList {

    private String SettingName;
    private int drawableId;

    public SettingList(String settingName, int drawableId) {
        SettingName = settingName;
        this.drawableId = drawableId;
    }

    public String getSettingName() {
        return SettingName;
    }

    public void setSettingName(String settingName) {
        SettingName = settingName;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
