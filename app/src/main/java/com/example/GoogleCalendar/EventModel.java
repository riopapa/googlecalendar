package com.example.GoogleCalendar;

import org.joda.time.LocalDate;

import androidx.annotation.NonNull;

public class EventModel implements Comparable<EventModel> {
    private String eventName;
    private LocalDate localDate;
    private int type;

    public EventModel(String eventName, LocalDate localDate, int type) {
        this.eventName = eventName;
        this.localDate = localDate;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public int compareTo(@NonNull EventModel eventModel) {
        return localDate.compareTo(eventModel.localDate);
    }
}
