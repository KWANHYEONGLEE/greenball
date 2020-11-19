package com.google.ar.core.examples.java.recomendActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.ar.core.examples.java.IntroduceItem;
import com.google.ar.core.examples.java.Model.SliderItem;
import com.google.ar.core.examples.java.adapter.SliderAdapterExample;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.itemdata.PersonChoiceInfo;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class RecoActivity extends AppCompatActivity {

    SliderView sliderView;
    private SliderAdapterExample adapter;

    // PersonChoiceInfo 사용자 선택 정보담는 객체
    private PersonChoiceInfo pschoice;

    //추천 정보 아이템을 저장하는 리스트
    private ArrayList<IntroduceItem> List_recommend = new ArrayList<IntroduceItem>();

    //볼거리 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_sightseeing = new ArrayList<IntroduceItem>();
    //놀거리 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_playing = new ArrayList<IntroduceItem>();
    //먹거리 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_eating = new ArrayList<IntroduceItem>();
    //후식 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_dessert = new ArrayList<IntroduceItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco);

        //객체전달받기
        Log.i("추천액티비티", "객체전달받는 부분");
        Intent intent = getIntent();
        if(intent != null){
            Log.i("추천액티비티", "널이 아니라 들어옴");
            pschoice = (PersonChoiceInfo) intent.getParcelableExtra("psChoice");
            Log.i("추천액티비티", pschoice.getFromWho());
            Log.i("추천액티비티", pschoice.getWantSomthing());
        }

        //리스트에 정보 추가
        List_add();



        sliderView = findViewById(R.id.imageSlider);


        adapter = new SliderAdapterExample(this);
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

    }

    // 관광지 정보 리스트 추가 메소드
    private void List_add() {
        List_sightseeing.add(new IntroduceItem("미디어 파사드", "자연의 빛과 남구를 상징하는 미디어 파사드. 색채와 소리를 활용한 풍부한 볼거리를 제공합니다.", "남구청사", "ereerererqrqwe", "", R.drawable.sightseeing_media));
        List_sightseeing.add(new IntroduceItem("원형공중정원", "도로 위 육교에 꽃과 나무를 심어 쉼 공간을 제공하는 원형공중정원입니다.", "백운광장", "ereerererqrqwe", "",  R.drawable.sightseeing_garden));
        List_sightseeing.add(new IntroduceItem("분수대", "낮에는 활기차게, 밤에는 분위기 있게 변신합니다.", "푸른길", "", "",  R.drawable.sightseeing_fountain));
        List_sightseeing.add(new IntroduceItem("로컬푸드 직매장", "광주 남구의 로컬 푸드를 구경하고 저렴하게 구매해보세요.", "스트리트 푸드존", "", "",  R.drawable.sightseeing_localfood));
        List_playing.add(new IntroduceItem("원데이 클래스", "즐기며 배우고 나의 취미, 특기를 찾을 수 있습니다.", "백운광장", "", "",  R.drawable.playing_onedayclass));
        List_playing.add(new IntroduceItem("셀카존", "사랑하는 가족, 친구, 연인과 찰칵! 남는건 사진입니다.", "푸른길", "", "",  R.drawable.playing_selfi));
        List_eating.add(new IntroduceItem("오매라멘", "장인의 손맛을 그대로 재현해 낸 생라멘과 진한 육수를 즐겨보세요.", "스트리트 푸드존", "", "",  R.drawable.food_ramen));
        List_eating.add(new IntroduceItem("큐브스테이크", "불판위에서 노릇하게 구워지는 큐브스테이크입니다.", "스트리트 푸드존", "", "",  R.drawable.food_steak));
        List_eating.add(new IntroduceItem("구름솜사탕", "몽실몽실 솜사탕 안에 시원한 아이스크림이 들어있어요.", "스트리트 푸드존", "", "",  R.drawable.dessert_somsatang));
        List_eating.add(new IntroduceItem("크레페", "푸드존의 명물! 달달한 크레페에 초코시럽을 듬뿍 뿌려 드셔보세요!", "스트리트 푸드존", "", "",  R.drawable.dessert_crepe));
        List_eating.add(new IntroduceItem("카츠샌드", "샌드위치 안에 야들야들한 고기가 들어있어요.", "스트리트 푸드존", "", "",  R.drawable.food_sandwitch));
        List_eating.add(new IntroduceItem("엉터리생고기", "삼겹살과 볶음밥을 스트리트 푸드존에서! 간단하게 즐겨봐요.", "스트리트 푸드존", "", "",  R.drawable.food_gogi));
        List_eating.add(new IntroduceItem("낙곱상점", "탱글탱글한 낙지와 고소한 곱창을 함께! 매콤한 낙곱전골 드시러오세요~", "스트리트 푸드존", "", "",  R.drawable.food_steak));
    }

    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
            } else {
                sliderItem.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        adapter.addItem(sliderItem);
    }
}