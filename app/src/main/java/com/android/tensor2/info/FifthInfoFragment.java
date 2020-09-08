package com.android.tensor2.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tensor2.R;
import com.android.tensor2.SettingsActivity;

import java.util.Set;


public class FifthInfoFragment extends Fragment {

    TextView fifth_infoname;
    TextView fifth_infotext1;
    SharedPreferences sharedPreferences;

    int fontSize;


    public FifthInfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup)  inflater.inflate(R.layout.fragment_fifth_info, container, false);

        fifth_infoname = (TextView) rootView.findViewById(R.id.fifth_infoname);
        fifth_infotext1 = (TextView) rootView.findViewById(R.id.fifth_infotext1);

        sharedPreferences = getActivity().getSharedPreferences("SAMPLE_PREFERENCE",Context.MODE_PRIVATE);


        fifth_infoname.setTextSize((float)14);
        fifth_infotext1.setTextSize((float)14);

        getActivity().setTitle("O / X 퀴즈!");

        ImageView image1 = (ImageView) rootView.findViewById(R.id.fifth_infoimage1);
        image1.setImageResource(R.drawable.imageinfo5_1sik);

        ImageView image2 = (ImageView) rootView.findViewById(R.id.fifth_infoimage2);
        image2.setImageResource(R.drawable.imageinfo5_2dok);


        TextView name = (TextView) rootView.findViewById(R.id.fifth_infoname);
        String namestr = "독버섯에 대한 잘못된 진실 5";
        name.setText(namestr);

        TextView info1 = (TextView) rootView.findViewById(R.id.fifth_infotext1);
        String info1str = "둘중 하나는 독버섯입니다.\n어느것이 독버섯일까요?\n(왼쪽이라고 생각하면 O, 오른쪽이라 생각하면 X)";

        info1.setText(info1str);

        ImageView image3 = (ImageView) rootView.findViewById(R.id.fifth_quiz1);
        image3.setImageResource(R.drawable.oicon);

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("오답입니다!\n\n선택하신 버섯은 노란달걀버섯이라는 식용버섯입니다!");
                builder1.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder1.show();
            }
        });

        ImageView image4 = (ImageView) rootView.findViewById(R.id.fifth_quiz2);
        image4.setImageResource(R.drawable.xicon);

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());
                builder2.setMessage("정답입니다!\n\n선택하신 버섯은 개나리광대버섯이라는 독버섯입니다!");
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
        fifth_infoname.setTextSize((float)fontSize);
        fifth_infotext1.setTextSize((float)fontSize);

    }
}