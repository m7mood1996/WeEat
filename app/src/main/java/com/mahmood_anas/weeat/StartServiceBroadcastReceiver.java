package com.mahmood_anas.weeat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class StartServiceBroadcastReceiver extends BroadcastReceiver {

    SmsManager sms;

    public void onReceive(Context context, Intent intent) {
        sms = SmsManager.getDefault();
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    // this is the our message we should handle it
                    if(message.equals("This is WeEat App. Your group is ready to order food"))
                    {
                        do_work(context);

                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private void do_work(Context context) {
        SharedPreferences sp = context.getSharedPreferences("my_id", Context.MODE_PRIVATE);
        String s = sp.getString("id", null);
        System.out.println("message from sp" + s);
        //sp.edit().clear().apply();
        // the group creator
        if (s != null){
            System.out.println("message admin bc" );
            Intent i = new Intent(context, NotificationService.class);
            i.putExtra("admin",true);
            i.putExtra("id",s);
            context.startService(i);


        }

        // other group members
        else{
            System.out.println("message member bc" );
            Intent i = new Intent(context, NotificationService.class);
            i.putExtra("admin",false);
            context.startService(i);


        }

    }


}
