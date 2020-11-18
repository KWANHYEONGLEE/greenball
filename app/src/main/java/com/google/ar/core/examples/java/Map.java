package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.ar.core.examples.java.augmentedimage.R;

public class Map extends AppCompatActivity {

    ImageView button_office1,button_office2,button_office3,button_office4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //xml과 연결하기
        button_office1=findViewById(R.id.button_office1);
        button_office2=findViewById(R.id.button_office2);
        button_office3=findViewById(R.id.button_office3);
        button_office4=findViewById(R.id.button_office4);

        //버튼 이벤트
        //1사무실
        button_office1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Road.class);
                intent.putExtra("latitude",37.484083);
                intent.putExtra("longitude",126.972013);
                startActivity(intent);
            }
        });

        //5사무실
        button_office2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Road.class);
                intent.putExtra("latitude",37.482928);
                intent.putExtra("longitude",126.973840);
                startActivity(intent);
            }
        });

        //6사무실
        button_office3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Road.class);
                intent.putExtra("latitude",37.483080);
                intent.putExtra("longitude",126.975181);
                startActivity(intent);
            }
        });

        //7사무실
        button_office4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Road.class);
                intent.putExtra("latitude",37.485801);
                intent.putExtra("longitude",126.972067);
                startActivity(intent);
            }
        });
    }
}