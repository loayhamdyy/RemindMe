package com.android.RemindMePlease.Models;

import java.util.ArrayList;



public class AppData {
    private static ArrayList<Reminder> allUserReminders = new ArrayList<>();
    private static final String[] months = {"Jan","Feb","Mar","Apr","May","Jun","July","Sept","Oct","Nov","Dec"};

    public static String[] getMonths() {
        return months;
    }

    public static ArrayList<Reminder> getAllUserReminders() {
        return AppData.allUserReminders;
    }
}
