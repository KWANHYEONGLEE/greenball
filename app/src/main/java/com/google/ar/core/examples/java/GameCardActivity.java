package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.ar.core.examples.java.Model.GameItem;
import com.google.ar.core.examples.java.Model.SliderItem;
import com.google.ar.core.examples.java.adapter.GameSliderAdapter;
import com.google.ar.core.examples.java.adapter.SliderAdapterExample;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.game.GameStory1;
import com.google.ar.core.examples.java.game.GameStory2;
import com.google.ar.core.examples.java.game.GameStory6;
import com.google.ar.core.examples.java.gamelist.ep1_answer;
import com.google.ar.core.examples.java.gamelist.ep2_answer;
import com.google.ar.core.examples.java.gamelist.ep3_question;
import com.google.ar.core.examples.java.gamelist.ep7;
import com.google.ar.core.examples.java.monster.MonsterThreeActivity;
import com.google.ar.core.examples.java.monster.MonsterTwoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameCardActivity extends AppCompatActivity {

    private SliderView sliderView;
    private GameSliderAdapter adapter;

    //게임 카드목록이 들어갈 리스트
    ArrayList<GameItem> gameItems = new ArrayList<>();

    // json 변환 라이브러리
    private Gson gson = new Gson();

    private ImageView back_game_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_card);

        back_game_card = (ImageView) findViewById(R.id.back_game_card);
        back_game_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sliderView = findViewById(R.id.gameSlider);

        adapter = new GameSliderAdapter(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

        // 쉐어드에 진행중이던 게임이 있는경우
        String gameItemData = getSharedString("gameItem");
        Log.i("게임카드액티비티", "gameItemData:" + gameItemData);

        if (gameItemData.equals("null") || gameItemData.equals("[]")) {
            // 저장중인 게임이 없는경우
            Log.i("게임카드액티비티", "저장중인 게임이 없는경우");
            renewGameItems();
        } else {
            // 저장중인 게임이 있는경우
            Log.i("게임카드액티비티", "저장중인 게임이 있는경우");
            Type type = new TypeToken<ArrayList<GameItem>>() {
            }.getType();
            gameItems = gson.fromJson(gameItemData, type);
            adapter.renewGameItems(gameItems);
        }

        Log.i("게임카드액티비티", "gameItems:"+gameItems);

        // 어댑터 클릭처리
        adapter.renewGameItems(gameItems);
        adapter.setOnItemClickListener(new GameSliderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean lock) {
                GameItem gameItem = gameItems.get(position);

                Log.i("게임카드액티비티", "gameItems lock:"+gameItems.get(position).getLock());
                Log.i("게임카드액티비티", "lock:"+lock);

//                startActivityObject(DescriptionActivity.class, "gameItem", gameItem);
                if (position == 0) {
                    //ep 1
                    //npc : 백운아! 정신차려봐! 여기가 어딘지 알겠어?
                    //버튼1 : 여기가 어디더라... (정답입력 페이지)
                    if (lock) {
                        startActivityC(GameStory1.class);
                    }
                } else if (position == 1) {
                    //ep 2
                    //npc : 시간이 없어! 육교를 내려가서 백운광장역 6번출입구 기둥을 스캔해봐!
                    //버튼 : 스캔하기 (스캔 페이지)
                    if (lock) {
                        startActivityC(GameStory2.class);
                    }
                } else if (position == 2) {
                    //ep 3
                    //텍스트 : 스트리트 푸드존 입구 솜사탕 가게 옆에 붙어있는 몬스터 그림을 찾아 특수 카메라로 스캔해보자
                    //버튼 : 스캔하기 (스캔 페이지)
                    if (lock) {
                        startActivityC(ep3_question.class);
                    }
                } else if (position == 3) {
                    if (lock) {
                        startActivityC(MonsterTwoActivity.class);
                    }
                } else if (position == 4) {
                    if (lock) {
                        startActivityC(MonsterThreeActivity.class);
                    }
                } else if (position == 5) {
                    if (lock) {
                        startActivityC(GameStory6.class);
                    }
                } else if (position == 7) {
                    if (lock) {
                        startActivityC(ep7.class);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("게임카드액티비티", "온리줌");
        adapter.renewGameItems(gameItems);
        // 쉐어드에 진행중이던 게임이 있는경우
        String gameItemData = getSharedString("gameItem");
        Log.i("게임카드액티비티", "gameItemData:" + gameItemData);

        // 저장중인 게임이 있는경우
        Log.i("게임카드액티비티", "저장중인 게임이 있는경우");
        Type type = new TypeToken<ArrayList<GameItem>>() {
        }.getType();
        gameItems = gson.fromJson(gameItemData, type);
        adapter.renewGameItems(gameItems);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("게임카드액티비티", "온디스트로이드");
        // 쉐어드에 현재 상태 업데이트
//        String saveGameItemData = gson.toJson(gameItems);
//        updateSharedString("gameItem", saveGameItemData);

    }

    public void renewGameItems() {
        gameItems.add(new GameItem("1단계\n백운이의 모험", R.drawable.ep1, true));
        gameItems.add(new GameItem("2단계\n모험의 시작", R.drawable.ep2, false));
        gameItems.add(new GameItem("3단계\n몬스터의 저주", R.drawable.ep3, false));
        gameItems.add(new GameItem("4단계\n저주를 풀다 1", R.drawable.ep4, false));
        gameItems.add(new GameItem("5단계\n저주를 풀다 2", R.drawable.ep5, false));
        gameItems.add(new GameItem("6단계\n저주를 풀다 3", R.drawable.ep6, false));
        gameItems.add(new GameItem("7단계\n구름이 되어", R.drawable.ep7, false));
        // 쉐어드에 현재 상태 업데이트
        String saveGameItemData = gson.toJson(gameItems);
        updateSharedString("gameItem", saveGameItemData);
    }

    public void removeLastGame(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewGame(View view) {
//        GameItem gameItem = new GameItem();
//        gameItem.setDescription("Slider Item Added Manually");
//        gameItem.setImageDrawable(R.drawable.skyblue);
//        adapter.addItem(gameItem);
    }

    // 인텐트 액티비티 전환함수
    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // GameItem 인텐트 전달 함수
    public void startActivityObject(Class c, String name, GameItem sendItem) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(name, sendItem);
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