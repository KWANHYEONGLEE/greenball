package com.google.ar.core.examples.java.gamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.game.MonsterOneActivity;

public class ep3_question extends AppCompatActivity {

    private Button ep3_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep3_question);

        ep3_scan = (Button)findViewById(R.id.ep3_scan);
        ep3_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //몬스터 npc 등장 장면
                startActivityC(MonsterOneActivity.class);

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