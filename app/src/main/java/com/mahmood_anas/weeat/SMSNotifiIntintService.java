package com.mahmood_anas.weeat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SMSNotifiIntintService extends Service {
    // TODO: Rename actions, choose action names that describe tasks that this
    FirebaseDatabase database;
    DatabaseReference ref;
    String name, number;
    int size = -1;
    int count = 0;
    boolean allSet;
    ArrayList<String> stringArrayList;
    NotificationManager notificationManager;
    int notificationId ;
    String CHANNEL_ID = "channel1";
    String CHANNEL_ID_2 = "channel2";
    String CHANNEL_NAME = "Channel 1 Demo";
    Thread thread;
    String group_id;
    int group_size;

    public int onStartCommand(Intent intent, int flags, int startId) {

        group_id = intent.getStringExtra("group_id");
        group_size = intent.getIntExtra("group_size", 0);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        allSet = false;

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Waiting for Group members")
                .setSmallIcon(R.drawable.burger_icon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);


        thread.start();



        return START_NOT_STICKY;
    }

    private Runnable Work = new Runnable() {
        @Override
        public void run() {
            createNotificationChannel();
            System.out.println("hello for the service");
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    size = (int) dataSnapshot.getChildrenCount();
                    if (size == group_size)
                        allSet = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };


            while (size - 5 < group_size) {
                database = FirebaseDatabase.getInstance();
                ref = database.getReference();
                ref.child("Groups").child(group_id).addValueEventListener(valueEventListener);
                System.out.println("in the while loop");
            }
            System.out.println("after size < grouplo for the service");
            ref.child("Groups").child(group_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (size - 5 == group_size)
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (!ds.getKey().equals("Time") && !ds.getKey().equals("imageUrl") && !ds.getKey().equals("location") && !ds.getKey().equals("numberofmembers") && !ds.getKey().equals("restaurantName")) {
                                name = (String) ds.child("name").getValue();
                                number = (String) ds.child("phoneNumber").getValue();
                                //stringArrayList.add(number);
                                count++;
                                System.out.println(count + "\tthis is the name" + number+ "this is the number");
                                sendSMS(number);
                            }
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            System.out.println("bye bye from the service");

            stopSelf();

        }
    };

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        //stopping the player when service is destroyed
    }

    public void sendSMS(String phoneNumber){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "This is WeEat App. Your group is ready to order food", null, null);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        // Build Notification with NotificationCompat.Builder
        // on Build.VERSION < Oreo the notification avoid the CHANEL_ID
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_2)
//                .setSmallIcon(R.drawable.burger_icon)  //Set the icon
//                .setContentTitle("hello")         //Set the title of notification
//                .setContentText("5ara")           //Set the text for notification
//                .setContentIntent(pendingIntent)            // Starts Intent when notification clicked
//                //.setOngoing(true)                         // stick notification
//                .setAutoCancel(true)                        // close notification when clicked
//                .build();
//
//        // Send the notification to the device Status bar.
//        notificationManager.notify(1, notification);
//
//        notificationId++;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
       thread =new Thread(Work);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    " Channel-2",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
