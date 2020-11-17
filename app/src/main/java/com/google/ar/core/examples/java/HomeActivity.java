package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.ar.core.examples.java.augmentedimage.R;

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
    }
}
