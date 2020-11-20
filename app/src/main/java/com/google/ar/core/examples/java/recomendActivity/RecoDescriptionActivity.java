package com.google.ar.core.examples.java.recomendActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.ar.core.examples.java.HomeActivity;
import com.google.ar.core.examples.java.IntroduceItem;
import com.google.ar.core.examples.java.TmapActivity;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.gson.Gson;

public class RecoDescriptionActivity extends AppCompatActivity {

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

    // 추천완료
    private TextView txt_reco_comp;

    // json 변환 라이브러리
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco_description);

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

        // 추천완료 연결
        txt_reco_comp = findViewById(R.id.txt_reco_comp);

        // 추천 완료 클릭처리
        txt_reco_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("추천컨텐츠페이지", "추천완료 클릭");
                // 추천받은 장소 쉐어드에 저장 후
                // 처음 페이지로 이동 (이전 스택 전부 제거)
                String sharedItem = gson.toJson(item);
                updateSharedString("recommendItem", sharedItem);
                // 홈화면 카운트를 위한 정보 저장
                updateSharedString("recommendItemCount", "0");
                Toast.makeText(RecoDescriptionActivity.this, "추천서비스 완료", Toast.LENGTH_SHORT).show();
                startActivityNewTask(HomeActivity.class);
            }
        });

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

    // 쉐어드 함수정의
    public void updateSharedString(String key, String value) {
        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getSharedString(String key) {
        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE);
        String result = prefs.getString(key, "null");
        return result;
    }

    public void deleteShared(String key) {
        SharedPreferences prefs = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
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