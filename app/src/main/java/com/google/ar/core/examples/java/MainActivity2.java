package com.google.ar.core.examples.java;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.examples.java.augmentedimage.R;

public class MainActivity2 extends AppCompatActivity{

    ImageView button_office1,button_office5,button_office6,button_office7;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //xml과 연결하기
        button_office1=findViewById(R.id.button_office1);
        button_office5=findViewById(R.id.button_office5);
        button_office6=findViewById(R.id.button_office6);
        button_office7=findViewById(R.id.button_office7);

        //버튼 이벤트
            //1사무실
            button_office1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),TmapActivity.class);
                    intent.putExtra("latitude",37.484083);
                    intent.putExtra("longitude",126.972013);
                    startActivity(intent);
                }
            });

            //5사무실
            button_office5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),TmapActivity.class);
                    intent.putExtra("latitude",37.482928);
                    intent.putExtra("longitude",126.973840);
                    startActivity(intent);
                }
            });

            //6사무실
            button_office6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),TmapActivity.class);
                    intent.putExtra("latitude",37.483080);
                    intent.putExtra("longitude",126.975181);
                    startActivity(intent);
                }
            });

            //7사무실
            button_office7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),TmapActivity.class);
                    intent.putExtra("latitude",37.485801);
                    intent.putExtra("longitude",126.972067);
                    startActivity(intent);
                }
            });
    }
}