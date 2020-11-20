package com.google.ar.core.examples.java.gamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.dialog.DialogNoAnswer;

public class ep6_answer extends AppCompatActivity {

    private ImageView back_game_card;
    private EditText ep6_answer_;
    private Button answer_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ep6_answer);

        back_game_card = (ImageView) findViewById(R.id.back_game_card);
        back_game_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ep6_answer_ = (EditText) findViewById(R.id.ep6_answer_);
        ep6_answer_.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        answer_button = (Button) findViewById(R.id.answer_button);
        answer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //공백 제거가 안돼.........
                String answer = ep6_answer_.getText().toString().trim();
                if (answer.length() <= 0) {
                    DialogNoAnswer dialogNoAnswer = new DialogNoAnswer(ep6_answer_.getContext());
                    dialogNoAnswer.show();
                } else {
                    Log.d("backwoon", answer);
                    //정답 맞췄을 때
                    if (answer.equals("ten") || answer.equals("10") || answer.equals("십") || answer.equals("열")) {
                        Toast.makeText(ep6_answer.this, "정답", Toast.LENGTH_SHORT).show();
                        startActivityC(ep7.class);
                        finish();
                    }
                    //정답 아닐때
                    else {
                        startActivityC(ep6_wrong.class);
                    }
                }
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