package com.myteam.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements RequirePermissionDialog.PermissionDialogListener, View.OnClickListener {
    private int RECEIVE_SMS_PERMISSIONs_CODE = 1;
    private int INTERNET_PERMISSIONs_CODE = 2;

    private EditText emailEdit;
    private Button confirmEmailBtn;
    private TextView emailText;
    public static String EMAIL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForSmsPermission();

        emailEdit = findViewById(R.id.email_edittext);
        confirmEmailBtn = findViewById(R.id.confirm_email_button);
        emailText = findViewById(R.id.email_textview);

        setEmail();

        confirmEmailBtn.setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEmail();
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
        ) {
            openDialog();
        }
    }

    private void openDialog() {
        RequirePermissionDialog dialog = new RequirePermissionDialog();
        dialog.show(getSupportFragmentManager(), "Yêu cầu quyền truy cập tin nhắn");
    }

    @Override
    public void denyForPermission(boolean isDeny) {
        if (isDeny == true) {
            System.exit(0);

        }
    }

    @Override
    public void applyForPermission(boolean isApply) {
        if (isApply == true) {
            // Permission not yet granted. Use requestPermissions().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_PERMISSIONs_CODE);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSIONs_CODE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.confirm_email_button:
                String emailReceive = emailEdit.getText().toString().trim();

                if (emailReceive.isEmpty() || !isValid(emailReceive)) {
                    Toast.makeText(MainActivity.this, "Email Không Hợp Lệ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    emailText.setText(emailReceive);
                    emailEdit.setText("");
                    SharedPreferences sharedPref = this.getSharedPreferences("Info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", emailReceive);
                    editor.commit();

                    Toast.makeText(MainActivity.this, "Đã Lưu Email", Toast.LENGTH_SHORT)
                            .show();
                }
        }
    }

    private boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private void setEmail() {
        SharedPreferences sharedPref = this.getSharedPreferences("Info", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "");

        if (!email.isEmpty()) {
            emailText.setText(email);
        }
    }

}