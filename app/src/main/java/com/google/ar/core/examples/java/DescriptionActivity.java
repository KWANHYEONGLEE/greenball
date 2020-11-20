package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.ar.core.examples.java.augmentedimage.R;

import org.w3c.dom.Text;

public class DescriptionActivity extends AppCompatActivity {

    private IntroduceItem item;

    //뒤로가기 버튼
    private ImageView btn_back;
    //약도보기 버튼
    private ImageView btn_map;

    //관광지 이미지
    private ImageView info_image;
    //관광지 이름
    private TextView info_name;
    //관광지 설명
    private TextView info_location;

    //AR 길안내 시작 버튼
    private ConstraintLayout btn_go_ar_navigation;

    //관광지 이름
    private TextView info_name_about;
    //관광지 주소
    private TextView info_address_about;
    //관광지 설명
    private TextView info_description_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        item = (IntroduceItem) getIntent().getSerializableExtra("item");

        btn_back = (ImageView) findViewById(R.id.btn_back);
        //btn_map = (ImageView) findViewById(R.id.btn_map);

        info_image = (ImageView) findViewById(R.id.info_image);
        info_name = (TextView) findViewById(R.id.info_name);
        info_location = (TextView) findViewById(R.id.info_location);

        btn_go_ar_navigation = (ConstraintLayout) findViewById(R.id.btn_go_ar_navigation);

        info_name_about = (TextView) findViewById(R.id.info_name_about);
        //info_address_about = (TextView) findViewById(R.id.info_address_about);
        info_description_about = (TextView) findViewById(R.id.info_description_about);

        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_go_ar_navigation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //위도, 경도 푸른길로 설정해서 ar 길찾기 실행
                //항상 백운광장을 가르킴
                Intent intent=new Intent(getApplicationContext(), TmapActivity.class);
                intent.putExtra("latitude",35.132889);
                intent.putExtra("longitude",126.902474);
                startActivity(intent);
            }
        });


        setting();
    }

    public void setting(){
        info_image.setImageResource(item.getImage());

        info_name.setText(item.getName());
        info_location.setText(item.getLocation());

        info_name_about.setText(item.getName());
        //info_address_about.setText(item.getAddress());
        info_description_about.setText(item.getDescription());
    }
}