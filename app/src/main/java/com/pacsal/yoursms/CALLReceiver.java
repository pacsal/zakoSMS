package com.pacsal.yoursms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by pacsal on 12/6/16.
 */
public class CALLReceiver extends BroadcastReceiver {
    static boolean ring=false;
    static boolean callReceived=false;
    public String callerPhoneNumber;
    public String callerName;
    DatabaseHelper myDb;
    public String number;
    public String status;


    @Override
    public void onReceive(Context mContext, Intent intent)
    {

        // Get the current Phone State
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String sendThis = new String();
        number="";
        status="";

        if(state==null)
            return;

        // If phone state "Rininging"
        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
            ring =true;
        }

        // If incoming call is received
        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            callReceived=true;
        }


        // If phone is Idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            //get the phone number
            Bundle bundle = intent.getExtras();
            callerPhoneNumber= bundle.getString("incoming_number");
            callerName= bundle.getString("incoming_name");
            //compose a msg
            sendThis += "ZakoSMS: \n";
            sendThis += "Mwenye namba:";
            sendThis += "\n";
            sendThis += callerPhoneNumber;
            sendThis += "amekupigia, tafadhali wasiliana nae.";


            myDb = new DatabaseHelper(mContext);
            Cursor res = myDb.getAllData();
            if(res.getCount()==0) {
                //show msg
                //Toast.makeText(context, "No Phone number avalable to send data", Toast.LENGTH_LONG).show();
                return;
            }else {
                SmsManager smsManager = SmsManager.getDefault();
                while (res.moveToNext()) {
                    number = res.getString(1);
                    status = res.getString(2);
                }//end while
                int p = Integer.parseInt(status);
                if(p==1) {
                    // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
                    if (ring == true && callReceived == false) {
                        smsManager.sendTextMessage(number, null, sendThis, null, null);
                    }
                } else { }
            }
        }
}
}
