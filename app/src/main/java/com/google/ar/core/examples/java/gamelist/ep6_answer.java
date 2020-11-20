package com.google.ar.core.examples.java.gamelist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.examples.java.Model.GameItem;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.dialog.DialogNoAnswer;
import com.google.ar.core.examples.java.game.MonsterOneActivity;
import com.google.ar.core.examples.java.monster.MonsterTwoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ep6_answer extends AppCompatActivity {

    private ImageView back_game_card;
    private EditText ep6_answer_;
    private Button answer_button;

    // json 변환 라이브러리
    private Gson gson = new Gson();

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

                        // 쉐어드에 진행중이던 게임이 있는경우
                        String gameItemData = getSharedString("gameItem");
                        Log.i("게임카드액티비티", "gameItemData:" + gameItemData);

                        if(gameItemData.equals("null") || gameItemData.equals("[]")) {
                            // 저장중인 게임이 없는경우
                            Log.i("게임카드액티비티", "저장중인 게임이 없는경우");
                        }else {
                            // 저장중인 게임이 있는경우
                            Log.i("게임카드액티비티", "저장중인 게임이 있는경우");
                            Type type = new TypeToken<ArrayList<GameItem>>() {}.getType();
                            ArrayList<GameItem> gameItems = gson.fromJson(gameItemData, type);
                            // 다음스테이지 오픈
                            gameItems.get(6).setLock(true);
                            String gameitemData = gson.toJson(gameItems);
                            updateSharedString("gameItem", gameitemData);
                        }

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

    // EditText가 아닌 다른 곳 클릭시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.i("login_log", "이벤트 발생");
        View focusView = getCurrentFocus();
        if (focusView != null) {
            //Log.i("login_log", "포커스가 있다면");
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                // 터치위치가 태그위치에 속하지 않으면 키보드 내리기
                //Log.i("login_log", "터치위치가 태그위치에 속하지 않으면 키보드 내리기");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}