package com.mahmood_anas.weeat;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.IBinder;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SMSNotifiIntintService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    FirebaseDatabase database;
    DatabaseReference ref;
    String name, number;


    public SMSNotifiIntintService() {
        super("SMSNotifiIntintService");
    }


    // TODO: Customize helper method

    @Override
    protected void onHandleIntent(Intent intent) {
        String group_id = intent.getStringExtra("group_id");
        final int group_size = intent.getIntExtra("group_size", 0);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        ref.child("Groups").child(group_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = -1;

                while (size - 5 != group_size)
                    size = (int) dataSnapshot.getChildrenCount();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!ds.getKey().equals("Time") && !ds.getKey().equals("imageUrl") && !ds.getKey().equals("location") && !ds.getKey().equals("numberofmembers") && !ds.getKey().equals("restaurantName")) {
                        name = (String) ds.child("name").getValue();
                        number = (String) ds.child("phoneNumber").getValue();
                        sendSMS(number);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void sendSMS(String phoneNumber){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "sms message", null, null);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
