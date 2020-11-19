package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.recomendActivity.ArNpc1;

public class GameInfoActivity extends AppCompatActivity {

    private ImageView btn_back;
    private Button btn_gamestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_gamestart = (Button) findViewById(R.id.btn_gamestart);

        // 뒤로 나가기
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 게임시작 클릭
        btn_gamestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clickclick", "했는데:(");
                startActivityC(GameCardActivity.class);

            }
        });
    }

    // 인텐트 액티비티 전환함수
    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }
}