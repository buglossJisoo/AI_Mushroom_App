package com.android.tensor2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.tensor2.MainActivity;

public class SplashActivity extends AppCompatActivity {

    static int TIMEOUT_LIMITS = 300; // 몇초 동안 이미지 띄우게 할지 정하는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            Thread.sleep(TIMEOUT_LIMITS); //대기 초 설정
            // TIMEOUT_LIMITS초가 지난 후 실행할 Activity class
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } catch (Exception e) { //error 발생시 logcat에 띄울 message
            Log.e("Error", "SplashActivity ERROR", e);
            e.printStackTrace();
        }
    }
}

