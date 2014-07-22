package com.example.rest;

public class AlarmModel {

    public static int sLengthWeek = 7;
    private boolean[] weekBoolean = new boolean[sLengthWeek];
    private int[] time = new int[2];
    private int leftPadding;
    private int id;

    public AlarmModel(){
        new AlarmModel(null,null);
    }

    public AlarmModel(boolean[] weekBoolean, int time[]) {
        this.weekBoolean = weekBoolean;
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int[] getTime() {
        return time;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
