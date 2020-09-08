package com.android.tensor2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InstructionActivity extends AppCompatActivity {

    TextView ins_instruction1;
    TextView ins_camera1;
    TextView ins_camera2;
    TextView ins_camera3;
    TextView ins_camera4;
    TextView ins_camera5;
    TextView ins_quiz1;
    TextView ins_quiz2;
    TextView ins_map1;
    TextView ins_map2;
    TextView ins_setting1;
    TextView ins_setting2;
    TextView ins_setting3;
    TextView first;     //어플사용법 텍스트뷰
    TextView second;    //버섯사진찍기 텍스트뷰
    TextView third;     //버섯퀴즈 텍스트뷰
    TextView fourth;    //버섯지도 텍스트뷰
    TextView fifth;     //앱설정 텍스트뷰

    SharedPreferences sharedPreferences;

    int fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        TextView instruction1 = (TextView) findViewById(R.id.ins_instruction1);
        instruction1.setText("▷ AI 버섯 어플의 기능들에 대한 설명입니다.");


        TextView camera1 = (TextView) findViewById(R.id.ins_camera1);
        camera1.setText(" ▷ 버섯 사진을 인공지능이 분석해서 어떤 종류의 버섯인지 분류해줍니다.");
        TextView camera2 = (TextView) findViewById(R.id.ins_camera2);
        camera2.setText(" <사진 업로드>버튼을 누르세요.");
        TextView camera3 = (TextView) findViewById(R.id.ins_camera3);
        camera3.setText(" <허용>버튼을 누르면 버섯 사진을 찍을 수 있습니다.");
        TextView camera4 = (TextView) findViewById(R.id.ins_camera4);
        camera4.setText(" <사진 촬영>을 누르면 버섯을 직접 촬영할 수 있고, <앨범 선택>을 누르면 핸드폰 앨범에 있는 버섯 사진을 업로드할 수 있습니다.");
        TextView camera5 = (TextView) findViewById(R.id.ins_camera5);
        camera5.setText(" 인공지능이 여러분이 업로드한 사진을 분석하여 가장 일치하는 버섯의 이름, 종류, 섭취했을 시 증상, 닮은 버섯을 알려줍니다.");


        TextView quiz1 = (TextView) findViewById(R.id.ins_quiz1);
        quiz1.setText(" ▷ 버섯 상식 퀴즈를 풀어볼 수 있습니다.");
        TextView quiz2 = (TextView) findViewById(R.id.ins_quiz2);
        quiz2.setText(" 맞는 정답 버튼을 누르면 됩니다.\n다음 문제로 넘어가려면 화면을 오른쪽에서 왼쪽으로 밀어주시면됩니다.");


        TextView map1 = (TextView) findViewById(R.id.ins_map1);
        map1.setText(" ▷ 대한민국 버섯 지도를 보여줍니다.");
        TextView map2 = (TextView) findViewById(R.id.ins_map2);
        map2.setText(" 여러분이 제공해주신 버섯 사진들과 위치 정보를 이용하여 버섯 지도가 만들어집니다^^");

        TextView setting1 = (TextView) findViewById(R.id.ins_setting1);
        setting1.setText(" ▷ 어플의 접근 권한, 글자 크기 등을 설정할 수 있습니다.");
        TextView setting2 = (TextView) findViewById(R.id.ins_setting2);
        setting2.setText(" 위치, 저장소, 카메라 접근 권한을 설정할 수 있습니다. 글자 크기를 변경하고 싶으시면 동그라미를 좌우로 이동시켜주시면 됩니다.");
        TextView setting3 = (TextView) findViewById(R.id.ins_setting3);
        setting3.setText(" 버튼이 초록색이면 접근 권한을 허용하는 상태이고, 회색이면 허용하지 않는 상태입니다.\n※접근 권한을 허용하지 않으시면 서비스 제공에 제한이 있습니다.");

        ins_instruction1 = (TextView)findViewById(R.id.ins_instruction1);
        ins_camera1 = (TextView)findViewById(R.id.ins_camera1);
        ins_camera2 = (TextView)findViewById(R.id.ins_camera2);
        ins_camera3 = (TextView)findViewById(R.id.ins_camera3);
        ins_camera4 = (TextView)findViewById(R.id.ins_camera4);
        ins_camera5 = (TextView)findViewById(R.id.ins_camera5);
        ins_quiz1 = (TextView)findViewById(R.id.ins_quiz1);
        ins_quiz2 = (TextView)findViewById(R.id.ins_quiz2);
        ins_map1 = (TextView)findViewById(R.id.ins_map1);
        ins_map2 = (TextView)findViewById(R.id.ins_map2);
        ins_setting1 = (TextView)findViewById(R.id.ins_setting1);
        ins_setting2 = (TextView)findViewById(R.id.ins_setting2);
        ins_setting3 = (TextView)findViewById(R.id.ins_setting3);
        first = (TextView)findViewById(R.id.first);
        second = (TextView)findViewById(R.id.second);
        third = (TextView)findViewById(R.id.third);
        fourth = (TextView)findViewById(R.id.fourth);
        fifth = (TextView)findViewById(R.id.fifth);

        sharedPreferences = getSharedPreferences("SAMPLE_PREFERENCE", MODE_PRIVATE);

        ins_instruction1.setTextSize((float)14);
        ins_camera1.setTextSize((float)14);
        ins_camera2.setTextSize((float)14);
        ins_camera3.setTextSize((float)14);
        ins_camera4.setTextSize((float)14);
        ins_camera5.setTextSize((float)14);
        ins_quiz1.setTextSize((float)14);
        ins_quiz2.setTextSize((float)14);
        ins_map1.setTextSize((float)14);
        ins_map2.setTextSize((float)14);
        ins_setting1.setTextSize((float)14);
        ins_setting2.setTextSize((float)14);
        ins_setting3.setTextSize((float)14);
        first.setTextSize((float)14);
        second.setTextSize((float)14);
        third.setTextSize((float)14);
        fourth.setTextSize((float)14);
        fifth.setTextSize((float)14);
    }

    @Override
    protected void onResume(){
        super.onResume();
        fontSize = sharedPreferences.getInt("FONT_SIZE",14);

        ins_instruction1.setTextSize((float)fontSize);
        ins_camera1.setTextSize((float)fontSize);
        ins_camera2.setTextSize((float)fontSize);
        ins_camera3.setTextSize((float)fontSize);
        ins_camera4.setTextSize((float)fontSize);
        ins_camera5.setTextSize((float)fontSize);
        ins_quiz1.setTextSize((float)fontSize);
        ins_quiz2.setTextSize((float)fontSize);
        ins_map1.setTextSize((float)fontSize);
        ins_map2.setTextSize((float)fontSize);
        ins_setting1.setTextSize((float)fontSize);
        ins_setting2.setTextSize((float)fontSize);
        ins_setting3.setTextSize((float)fontSize);
        first.setTextSize((float)fontSize);
        second.setTextSize((float)fontSize);
        third.setTextSize((float)fontSize);
        fourth.setTextSize((float)fontSize);
        fifth.setTextSize((float)fontSize);
    }
}