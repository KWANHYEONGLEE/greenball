package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.recomendActivity.ArNpc1;

public class HomeActivity extends AppCompatActivity {

    LinearLayout linearLayout_Home_Recommend,linearLayout_Home_Introduce,linearLayout_Home_ARroad,linearLayout_Home_ARgame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //xml과 연결하기
        linearLayout_Home_Recommend=findViewById(R.id.linearLayout_Home_Recommend);
        linearLayout_Home_Introduce=findViewById(R.id.linearLayout_Home_Introduce);
        linearLayout_Home_ARroad=findViewById(R.id.linearLayout_Home_ARroad);
        linearLayout_Home_ARgame=findViewById(R.id.linearLayout_Home_ARgame);


        // 푸른길 추천 클릭
        linearLayout_Home_Recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(ArNpc1.class);

            }
        });

        // 관광지 소개 클릭
        linearLayout_Home_Introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(IntroduceActivity.class);

            }
        });

        linearLayout_Home_ARroad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(Map.class);
            }
        });

    }


    // 액티비티 전환 함수

    // 인텐트 액티비티 전환함수
    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }
    // 인텐트 화면전환 하는 함수
    // FLAG_ACTIVITY_CLEAR_TOP = 불러올 액티비티 위에 쌓인 액티비티 지운다.
    public void startActivityflag(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // 문자열 인텐트 전달 함수
    public void startActivityString(Class c, String name , String sendString) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(name, sendString);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // 백스택 지우고 새로 만들어 전달
    public void startActivityNewTask(Class c){
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }


}
