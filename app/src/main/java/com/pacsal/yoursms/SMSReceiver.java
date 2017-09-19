package com.pacsal.yoursms;

/**
 * Created by pacsal on 12/3/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.IntentCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

//import android.telephony.gsm.SmsMessage;

public class SMSReceiver extends BroadcastReceiver
{
    private static final String LOG_TAG = "SMSReceiver";
    DatabaseHelper myDb;
    public String number;
    public String status;

    /** The Action fired by the Android-System when a SMS was received.
     * We are using the Default Package-Visibility */
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            // if(message starts with SMStretcher recognize BYTE)
            String sb = new String();
            number="";
            status="";
                 //The SMS-Messages are 'hiding' within the extras of the Intent.
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                //Get all messages contained in the Intent
                SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        //Feed the String-Builder with all Messages found.
                for (SmsMessage currentMessage : messages){
                    sb += "ZakoSMS: ";
                        //hapa ni Sender Number
                    sb += currentMessage.getDisplayOriginatingAddress();
                    sb += "\n";
                        //hii ni Actual Message Content
                    sb += currentMessage.getDisplayMessageBody();
                }
            }
            myDb = new DatabaseHelper(context);
            Cursor res = myDb.getAllData();
            if(res.getCount()==0) {
                //show msg
                //Toast.makeText(context, "No Phone number avalable to send data", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                SmsManager smsManager = SmsManager.getDefault();
                while (res.moveToNext()) {
                    number = res.getString(1);
                    status = res.getString(2);
                    //smsManager.sendTextMessage(res.getString(1), null, sb, null, null);
                    //Show the Notification containing the Message.
                    //Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
                }//end while
                int p = Integer.parseInt(status);
                if(p==1) {
                    smsManager.sendTextMessage(number, null, sb, null, null);
                } else { }
                //Show the Notification containing the Message.
                //getContentResolver().delete(Uri.parse("content://sms/sent"), "address = ? and body = ?", new String[] {number.toString(),sb.toString()});
                //Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

}

