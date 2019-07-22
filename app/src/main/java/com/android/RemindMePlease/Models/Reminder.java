package com.android.RemindMePlease.Models;

public class Reminder {
    private int id;
    private String reminderDescription="";
    private boolean isImportant = false;
    private int hour;
    private int minute;
    private int day;
    private int month;
    private int year;

    public Reminder(String reminderDescription, boolean isImportant){
        this.reminderDescription = reminderDescription;
        this.isImportant=isImportant;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }


    public String getReminderDescription() {
        return reminderDescription;
    }

    public boolean isImportant() {
        return isImportant;
    }
}
