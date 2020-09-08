package com.android.tensor2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button main_camera_button;      //버섯사진찍기 버튼
    Button main_kinds_button;       //버섯퀴즈 버튼
    Button main_map_button;         //버섯지도 버튼
    Button main_instruction_button; //어플사용법 버튼
    Button main_settings_button;    //앱설정 버튼
    ImageView main_imageView1;      //버섯사진찍기 이미지뷰
    ImageView main_imageView2;      //버섯퀴즈 이미지뷰
    ImageView main_imageView3;      //버섯지도 이미지뷰
    ImageView main_imageView4;      //어플사용법 이미지뷰
    ImageView main_imageView5;      //앱설정 이미지뷰
    SharedPreferences sharedPreferences;

    int fontSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AI버섯어플 이용 동의 안내 다이얼로그
        try {
            showDialog();
        } catch (Exception e) { //error 발생시 logcat에 띄울 message
            Log.e("Error", "MainActivity ERROR", e);
            e.printStackTrace();
        }


        // 버섯사진찍기 버튼
        main_camera_button = findViewById(R.id.main_camera_button);
        main_imageView1 = findViewById(R.id.main_imageView1);

        // 클릭 시 --> CameraMushActivity로 넘어감.
        main_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(getApplicationContext(), CameraMushActivity.class);
                startActivity(intent_camera);
            }
        });
        // 클릭 시 --> CameraMushActivity로 넘어감.
        main_imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_camera = new Intent(getApplicationContext(), CameraMushActivity.class);
                startActivity(intent_camera);
            }
        });



        // 버섯퀴즈 버튼
        main_kinds_button = findViewById(R.id.main_kinds_button);
        main_imageView2 = findViewById(R.id.main_imageView2);

        // 클릭 시 --> QuizMushActivity로 넘어감.
        main_kinds_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_kinds = new Intent(getApplicationContext(), QuizMushActivity.class);
                startActivity(intent_kinds);
            }
        });
        // 클릭 시 --> QuizMushActivity로 넘어감.
        main_imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_kinds = new Intent(getApplicationContext(), QuizMushActivity.class);
                startActivity(intent_kinds);
            }
        });



        // 버섯 지도 버튼
        main_map_button = findViewById(R.id.main_map_button);
        main_imageView3 = findViewById(R.id.main_imageView3);

        // 클릭 시 --> MapActivity로 넘어감.
        main_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_map = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent_map);
            }
        });
        // 클릭 시 --> MapActivity로 넘어감.
        main_imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_map = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent_map);
            }
        });



        // 어플 사용법 버튼
        main_instruction_button = findViewById(R.id.main_instruction_button);
        main_imageView4 = findViewById(R.id.main_imageView4);

        // 클릭 시 --> InstructionActivity로 넘어감.
        main_instruction_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_instruction = new Intent(getApplicationContext(), InstructionActivity.class);
                startActivity(intent_instruction);
            }
        });
        // 클릭 시 --> InstructionActivity로 넘어감.
        main_imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_instruction = new Intent(getApplicationContext(), InstructionActivity.class);
                startActivity(intent_instruction);
            }
        });



        // 앱 설정 버튼
        main_settings_button = findViewById(R.id.main_settings_button);
        main_imageView5 = findViewById(R.id.main_imageView5);

        // 클릭 시 --> SettingsActivity로 넘어감.
        main_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent_settings);
            }
        });
        // 클릭 시 --> SettingsActivity로 넘어감.
        main_imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent_settings);
            }
        });



        sharedPreferences = getSharedPreferences("SAMPLE_PREFERENCE", MODE_PRIVATE);

        main_camera_button.setTextSize((float)14);
        main_kinds_button.setTextSize((float)14);
        main_map_button.setTextSize((float)14);
        main_instruction_button.setTextSize((float)14);
        main_settings_button.setTextSize((float)14);
    }


    //AI버섯어플 이용 동의 안내 다이얼로그
    private void showDialog() {
        final AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);
        myAlertBuilder.setTitle("AI버섯어플 이용 동의 안내");
        myAlertBuilder.setMessage("<AI버섯어플>은 사용자가 버섯사진을 찍으면 어떤 버섯인지 인공지능을 통해 알려주는 어플입니다. <AI버섯어플>은 버섯 분류시 참고만 해주시고, 식용버섯 또는 약용버섯이라고 나오더라도 복용,섭취하지 말아주십시오. 복용,섭취해서 문제가 생겨도 저희는 책임지지않습니다. 이에 동의하지 않으시면 어플 서비스를 받으실 수 없습니다. ");
        myAlertBuilder.setPositiveButton("동의", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"동의하셨습니다.",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        myAlertBuilder.setNegativeButton("비동의", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"어플 서비스를 받으실 수 없습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        myAlertBuilder.show();
    }


    //메뉴(앱바)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this,SettingsActivity.class));
                return true;
            case R.id.action_subactivity:
                startActivity(new Intent(this,SubActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        //Get font-size from preference
        fontSize = sharedPreferences.getInt("FONT_SIZE",14);

        //Change font-size
        main_camera_button.setTextSize((float)fontSize);
        main_kinds_button.setTextSize((float)fontSize);
        main_map_button.setTextSize((float)fontSize);
        main_instruction_button.setTextSize((float)fontSize);
        main_settings_button.setTextSize((float)fontSize);
    }
}