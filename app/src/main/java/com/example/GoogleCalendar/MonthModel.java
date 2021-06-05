package com.example.GoogleCalendar;

import java.util.ArrayList;

public class MonthModel {
    private String monthNameStr;
    private int month;
    private int year;
    private int noofDay;
    private int noofWeek;
    private ArrayList<DayModel> dayModelArrayList;
    private int firstDay;

    public int getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }

    public ArrayList<DayModel> getDayModelArrayList() {
        return dayModelArrayList;
    }

    public void setDayModelArrayList(ArrayList<DayModel> dayModelArrayList) {
        this.dayModelArrayList = dayModelArrayList;
    }

    public String getMonthNameStr() {
        return monthNameStr;
    }

    public void setMonthNameStr(String monthNameStr) {
        this.monthNameStr = monthNameStr;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNoofDay() {
        return noofDay;
    }

    public void setNoofDay(int noofDay) {
        this.noofDay = noofDay;
    }

    public int getNoofWeek() {
        return noofWeek;
    }

    public void setNoofWeek(int noofWeek) {
        this.noofWeek = noofWeek;
    }
}
