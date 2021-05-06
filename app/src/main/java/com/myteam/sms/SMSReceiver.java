package com.myteam.sms;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = SMSReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    private String username = "chuminhtan.test@gmail.com";
    private String password = "Emaildetest";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strFrom = "";
        String strMessage = "";
        String format = bundle.getString("format");

        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);

        if (pdus != null) {
            // Check the android android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

            // Fill the msgs array
            msgs = new SmsMessage[pdus.length];

            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }

                // Build the message to show.
                strFrom = msgs[i].getOriginatingAddress();
                strMessage = msgs[i].getMessageBody();

                // Log and display the SMS message.
                Toast.makeText(context, "Gửi từ " + strFrom + " : " + strMessage, Toast.LENGTH_LONG).show();
            }
        }

        sendMail(context, strFrom, strMessage);
    }

    private void sendMail(Context context, String strFrom, String strMessage) {

        SharedPreferences sharedPref = context.getSharedPreferences("Info", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");

        if (!email.isEmpty()) {
            String subject = "Android Device - Tin Nhắn Mới Từ " + strFrom;

            try {
                GMailSender sender = new GMailSender(username, password);
                sender.sendMail(subject, strMessage, username, email);

            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }

            Toast.makeText(context, "Đã gửi email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Không Thể Gửi Email. Chưa Nhập Email Người Nhận", Toast.LENGTH_SHORT).show();
        }
    }
}
