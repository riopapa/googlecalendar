package com.example.GoogleCalendar;

public class EventInfo {
    public String[] titles;
    public boolean isAllDay;
    public int id;
    public long startTime;
    public long finishTime;
    public EventInfo nextNode;
    public String title;
    public String timeZone;
    public String calName;
    public int eventcolor;
    public EventInfo(String[] titles){
        this.titles = titles;
    }
    public EventInfo(){
    }
}
