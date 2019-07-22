package com.android.RemindMePlease.Services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.android.RemindMePlease.Activities.Reminder_Activity;
import com.android.RemindMePlease.R;



public class NotifyService extends Service {
    private Looper mServiceLooper;
    private Activity activity;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    public NotifyService(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onCreate() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}