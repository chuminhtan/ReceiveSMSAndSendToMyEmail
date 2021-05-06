package com.myteam.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RequirePermissionDialog.PermissionDialogListener, View.OnClickListener {
    private int RECEIVE_SMS_PERMISSIONs_CODE = 1;

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

        confirmEmailBtn.setOnClickListener(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            openDialog();
        }
    }

    private void openDialog() {
        RequirePermissionDialog dialog = new RequirePermissionDialog();
        dialog.show(getSupportFragmentManager(),"Yêu cầu quyền truy cập tin nhắn");
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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},RECEIVE_SMS_PERMISSIONs_CODE);
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.confirm_email_button :
                String emailReceive = emailEdit.getText().toString().trim();

                if (emailReceive.isEmpty() || !isValid(emailReceive)) {
                    Toast.makeText(MainActivity.this, "Email Không Hợp Lệ", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    EMAIL = emailReceive;
                    emailText.setText(EMAIL);

                    Toast.makeText(MainActivity.this, "Đã Lưu Email Người Nhận", Toast.LENGTH_SHORT)
                            .show();
                }


        }
    }

    private boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}