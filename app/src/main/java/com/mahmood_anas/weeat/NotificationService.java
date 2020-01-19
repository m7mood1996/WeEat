package com.mahmood_anas.weeat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

class NotificationService extends Service {
    String CHANNEL_ID = "channel3";
    NotificationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("in notification service");
        boolean admin = intent.getBooleanExtra("admin",false);

        createNotificationChannel();

        // admin notification
        if (admin){
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("This is the Admin")
                    .setContentText("You are the admin")
                    .setSmallIcon(R.drawable.burger_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(0,notification);
            System.out.println("admin notification");

        }
        // other member notification
        else{

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("You are member")
                    .setContentText("wait the admin")
                    .setSmallIcon(R.drawable.burger_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(0,notification);
            System.out.println("member notification");



        }
        return Service.START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    " Channel-2",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
