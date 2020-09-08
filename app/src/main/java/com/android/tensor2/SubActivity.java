package com.android.tensor2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    SharedPreferences sharedPreferences;
    int fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        textView = (TextView)findViewById(R.id.textView);
        textView2 = (TextView)findViewById(R.id.textView2);

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("<AI버섯어플>은 사용자가 버섯사진을 찍으면 어떤 버섯인지 인공지능을 통해 알려주는 어플입니다. <AI버섯어플>은 버섯 분류시 참고만 해주시고, 식용버섯 또는 약용버섯이라고 나오더라도 복용, 섭취하지 말아주십시오.복용,섭취해서 문제가 생겨도 저희는 책임지지 않습니다. 이에 동의하지 않으시면 어플 서비스를 받으실 수 없습니다. ");

        TextView textView2 = (TextView)findViewById(R.id.textView2);
        textView2.setText("이에 동의하셨으므로 <AI버섯어플>을 사용하실 수 있습니다.");

        sharedPreferences = getSharedPreferences("SAMPLE_PREFERENCE", MODE_PRIVATE);

        textView.setTextSize((float)14);
        textView2.setTextSize((float)14);
    }


    protected void onResume(){
        super.onResume();

        //프레퍼렌스로부터 글자 크기 가져오기
        fontSize = sharedPreferences.getInt("FONT_SIZE",14);
        //글자 크기 변경하기
        textView.setTextSize((float)fontSize);
        textView2.setTextSize((float)fontSize);
    }
}