package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.ar.core.examples.java.Model.GameItem;
import com.google.ar.core.examples.java.Model.SliderItem;
import com.google.ar.core.examples.java.adapter.GameSliderAdapter;
import com.google.ar.core.examples.java.adapter.SliderAdapterExample;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.gamelist.ep1_answer;
import com.google.ar.core.examples.java.gamelist.ep2_answer;
import com.google.ar.core.examples.java.gamelist.ep3_question;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class GameCardActivity extends AppCompatActivity {

    private SliderView sliderView;
    private GameSliderAdapter adapter;

    //게임 카드목록이 들어갈 리스트
    ArrayList<GameItem> gameItems = new ArrayList<>();

    private ImageView back_game_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_card);

        back_game_card = (ImageView)findViewById(R.id.back_game_card);
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

        renewGameItems();
    }

    public void renewGameItems() {
        gameItems.add(new GameItem("모험의 시작", R.drawable.dessert_somsatang));
        gameItems.add(new GameItem("모험의 시작", R.drawable.dessert_somsatang));

        adapter.renewGameItems(gameItems);
        adapter.setOnItemClickListener(new GameSliderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GameItem gameItem = gameItems.get(position);
//                startActivityObject(DescriptionActivity.class, "gameItem", gameItem);
                if(position == 0){
                    //ep 1
                    //npc : 백운아! 정신차려봐! 여기가 어딘지 알겠어?
                    //버튼1 : 여기가 어디더라... (정답입력 페이지)
                    startActivityC(ep1_answer.class);
                }
                else if(position == 1){
                    //ep 2
                    //npc : 시간이 없어! 육교를 내려가서 백운광장역 6번출입구 기둥을 스캔해봐!
                    //버튼 : 스캔하기 (스캔 페이지)
                    startActivityC(ep3_question.class);
                }
                else if(position == 2){
                    //ep 3
                    //텍스트 : 스트리트 푸드존 입구 솜사탕 가게 옆에 붙어있는 몬스터 그림을 찾아 특수 카메라로 스캔해보자
                    //버튼 : 스캔하기 (스캔 페이지)
                }
            }
        });
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
    public void startActivityObject(Class c, String name , GameItem sendItem) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(name, sendItem);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }
}