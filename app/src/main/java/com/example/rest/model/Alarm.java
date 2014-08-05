package com.example.rest.model;

import com.example.rest.Utils;

public class Alarm {
    private Boolean[] mWeekBoolean;

    private int mId;

    public final static int sSizeButton = 7;

    private boolean mIsVibration;

    private Integer mMinute;

    private Integer mHour;

    private boolean mIsSound;

    public Alarm() {
        mId = Utils.getInstance().getPreferenceManager().getAlarmCount();
    }

    public int getId() {
        return mId;
    }

    public Boolean[] getWeekBoolean() {
        return mWeekBoolean;
    }

    public void setWeekBoolean(Boolean[] mWeekBoolean) {
        this.mWeekBoolean = mWeekBoolean;
    }

    public boolean isVibration() {
        return mIsVibration;
    }

    public void setVibration(boolean isVibration) {
        mIsVibration = isVibration;
    }

    public boolean isSound() {
        return mIsSound;
    }

    public void setSound(boolean isMusic) {
        mIsSound = isMusic;
    }

    public Integer getMinute() {
        return mMinute;
    }

    public void setMinute(Integer mMinute) {
        this.mMinute = mMinute;
    }

    public Integer getHour() {
        return mHour;
    }

    public void setHour(Integer mHour) {
        this.mHour = mHour;
    }
}
