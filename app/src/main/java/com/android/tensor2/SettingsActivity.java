package com.android.tensor2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    //카메라 권한 불러오기 관련
    static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    //앨범 불러오기 관련
    private static final int REQUEST_IMAGE_PICK = 2;
    //위치 관련 허용 번수
    final int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION =0;
    Location mLastLocation;
    private static final String TAG = "MapActivity";

    private SeekBar seekBar;
    private Switch switch11;
    private Switch switch12;
    private Switch switch13;
    private TextView textView2;

    //시크바 최초 글자크기 설정
    private static final int progress = 14;

    SharedPreferences sharedPreferences;
    //sharedpreferences로 글자 크기 수정 될 때마다 변하는 크기 변수 선언
    SharedPreferences.Editor editor;

    public Handler mainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //글자크기 14로 맞춤
        int fontSize = 14;

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        switch11 = (Switch)findViewById(R.id.switch11);
        switch12 = (Switch)findViewById(R.id.switch12);
        switch13 = (Switch)findViewById(R.id.switch13);
        textView2 = (TextView)findViewById(R.id.textView2);

        sharedPreferences = getSharedPreferences("SAMPLE_PREFERENCE", MODE_PRIVATE);
        editor =sharedPreferences.edit();

        //seekbar 프로그레스 변경 될 때마다 업데이트
        updateView(progress);

        // 최대 글자 크기 50
        seekBar.setMax(50);

        // seekbar 프로그레스도 커질 수록 글자 크기도 커지게 설정
        seekBar.setProgress(fontSize);

        //스위치나 텍스트뷰 글자크기 설정 14로 맞추기
        switch11.setTextSize((float)14);
        switch12.setTextSize((float)14);
        switch13.setTextSize((float)14);
        textView2.setTextSize((float)14);

        //seekbar 변경될 때마다 바뀐 값 업데이트 리스너
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) { }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) { }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                //프로그레스 증감에 따라 글자크기 설정됨
                switch11.setTextSize(progress);
                switch12.setTextSize(progress);
                switch13.setTextSize(progress);
                textView2.setTextSize(progress);

                updateView(progress);
                //글자크기 프레퍼렌스에 저장
                editor.putInt("FONT_SIZE",progress);
                editor.commit();
            }
        });


        //카메라 권한 스위치
        Switch upload = (Switch) findViewById(R.id.switch13);
        upload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) { //스위치 켜지면 카메라 권한 요청
                    requestCameraPermission();
                }
                else{ //아니면 폰 내부 권한 설정으로 가는 다이얼로그 열기
                    makeDialog();
                }

            }
        });

        //저장소(앨범)권한 스위치
        Switch storage = (Switch) findViewById(R.id.switch12);
        storage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) { //스위치 켜지면 앨범 권한 요청
                    requestAlbumPermission();
                }
                else{ //아니면 폰 내부 권한 설정으로 가는 다이얼로그 열기
                    makeDialog();
                }

            }
        });

        //위치 권한 스위치
        Switch location = (Switch) findViewById(R.id.switch11);
        location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) { //스위치 켜지면 위치 권한 요청
                    requestLocatePermission();
                }
                else{ //아니면 폰 내부 권한 설정으로 가는 다이얼로그 열기
                    makeDialog();
                }
            }
        });
    }


    //변경된 글자크기와 프로그레스 연결, 프로그레스 움직일 때 글자크기도 변경
    public void updateView(int fontSize) {
        switch11.setTextSize((float)fontSize);
        switch12.setTextSize((float)fontSize);
        switch13.setTextSize((float)fontSize);
        textView2.setTextSize((float)fontSize);
    }


    // 위치 권한
    private void requestLocatePermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //권한 체크가 거부일 때
            if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                //위치 권한 요청
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION);
                }
            else{ //아니면 토스트 메시지
                Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();
            }

        }
    }

    // 외부 저장소 권한(Album 관련)
    private void requestAlbumPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 외부저장소 허용 권한 요청 ok면 log함수 나옴
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.e("External Storage", "Storage Permission Required");
            }
            // 허용 안되어있으면
            // 외부저장소 사용 권한 요청
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
        }
    }

    // 카메라 권환
    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 카메라 허용 권한 요청 ok면 log함수 나옴
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Log.e("Camera", "Camera Permission Required");
            }
            // 허용 안되어있으면
            // 카메라 사용 권한 요청
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);}
    }


    //퍼미션 체크 코드를 이용하여 스위치 다시 끄면 권한 설정 다시 유도
    private void makeDialog() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("권한 설정");
        localBuilder.setMessage("권한 거절로 인해 일부기능이 제한됩니다.");
        localBuilder.setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt){
                try { //애플리케이션 내부 설정으로 가기, 세팅 액티비티로 불러오기
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    startActivity(intent);
                }
            }}) //권한 설정하러 가기 말고 취소하기 누를 경우
                .setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    }})
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //리퀘스트 코드가 카메라 허용 코드이면
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            //permission 다 허용이면
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this,"아직 승인받지 않았습니다. ",Toast.LENGTH_LONG).show();
                switch13.setChecked(false); //승인 거부될 경우 스위치 켜지지 말기

            }

        }

        //리퀘스트 코드가 앨범 허용 코드이면
        else if (requestCode == REQUEST_IMAGE_PICK) {
            // If request is cancelled, the result arrays are empty.면
            //permission 다 허용이면
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"아직 승인받지 않았습니다. ",Toast.LENGTH_LONG).show();
                switch12.setChecked(false); //승인 거부될 경우 스우치 켜지지 말기
            }
        }

        switch (requestCode) { //리퀘스트 코드가 위치 허용 코드이면
            case REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,"아직 승인받지 않았습니다. ",Toast.LENGTH_LONG).show();
                    requestLocatePermission();//위치 권한 재요청
                }
                break;
            }
        }
    }
}








