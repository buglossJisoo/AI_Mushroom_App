package com.android.tensor2.info;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tensor2.R;


public class ThirdInfoFragment extends Fragment {

    TextView third_infoname;
    TextView third_infotext1;
    SharedPreferences sharedPreferences;

    int fontSize;


    public ThirdInfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)  inflater.inflate(R.layout.fragment_third_info, container, false);

        third_infoname = (TextView) rootView.findViewById(R.id.third_infoname);
        third_infotext1 = (TextView) rootView.findViewById(R.id.third_infotext1);

        sharedPreferences = getActivity().getSharedPreferences("SAMPLE_PREFERENCE", Context.MODE_PRIVATE);


        third_infoname.setTextSize((float)14);
        third_infotext1.setTextSize((float)14);

        getActivity().setTitle("O / X 퀴즈!");

        ImageView image1 = (ImageView) rootView.findViewById(R.id.third_infoimage1);
        image1.setImageResource(R.drawable.imageinfo3);


        TextView name = (TextView) rootView.findViewById(R.id.third_infoname);
        String namestr = "독버섯에 대한 잘못된 진실 3";
        name.setText(namestr);

        TextView info1 = (TextView) rootView.findViewById(R.id.third_infotext1);
        String info1str = "세로로 찢어지면 독버섯이다?";
        SpannableStringBuilder str1 = new SpannableStringBuilder(info1str);
        str1.setSpan(new ForegroundColorSpan(Color.parseColor("#F80D0D")),0,8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        info1.setText(str1);

        ImageView image3 = (ImageView) rootView.findViewById(R.id.third_quiz1);
        image3.setImageResource(R.drawable.oicon);

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("오답입니다!\n\n다시 한번 풀어보세요!");
                builder1.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder1.show();
            }
        });

        ImageView image4 = (ImageView) rootView.findViewById(R.id.third_quiz2);
        image4.setImageResource(R.drawable.xicon);

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());
                builder2.setMessage("정답입니다!\n\n대부분의 버섯은 세로로 찢어집니다!\n\n다른 문제도 풀어보세요!");
                builder2.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder2.show();
            }
        });

        /*TextView info2 = (TextView) rootView.findViewById(R.id.third_infotext2);
        String info2str = "아닙니다.\n벌레가 먹는 독버섯도 많습니다.";
        SpannableStringBuilder str2 = new SpannableStringBuilder(info2str);
        str2.setSpan(new ForegroundColorSpan(Color.parseColor("#F80D0D")),6,23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        info2.setText(str2);*/

        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();

        //Get font-size from preference
        fontSize = sharedPreferences.getInt("FONT_SIZE",14);

        //Change font-size
        third_infoname.setTextSize((float)fontSize);
        third_infotext1.setTextSize((float)fontSize);

    }
}