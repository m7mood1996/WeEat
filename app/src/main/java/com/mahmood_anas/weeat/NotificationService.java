package com.mahmood_anas.weeat;

import android.app.IntentService;
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

public class NotificationService extends Service {
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
        System.out.println("in notification service"+admin);
        createNotificationChannel();

        // admin notification
        if (admin){
            /*Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("This is the Admin")
                    .setContentText("You are the admin")
                    .setSmallIcon(R.drawable.burger_icon)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(1,notification);
            System.out.println("admin notification");*/
            Intent intent1 = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

// build notification
// the addAction re-use the same intent to keep the example short
            Notification n  = new Notification.Builder(this)
                    .setContentTitle("This is the admin")
                    .setContentText("Subject")
                    .setSmallIcon(R.drawable.burger_icon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.burger_icon, "Call", pIntent)
                    .addAction(R.drawable.burger_icon, "More", pIntent)
                    .addAction(R.drawable.burger_icon, "And more", pIntent).build();


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);

        }
        // other member notification
        else{

            System.out.println("in notification service member");

            Intent intent1 = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

            // build notification
            // the addAction re-use the same intent to keep the example short
            Notification n  = new Notification.Builder(this)
                    .setContentTitle("This is a member")
                    .setContentText("Subject")
                    .setSmallIcon(R.drawable.burger_icon)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.burger_icon, "Call", pIntent)
                    .addAction(R.drawable.burger_icon, "More", pIntent)
                    .addAction(R.drawable.burger_icon, "And more", pIntent).build();


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);





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
