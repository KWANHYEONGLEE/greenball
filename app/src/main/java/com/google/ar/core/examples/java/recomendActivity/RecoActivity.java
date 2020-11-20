package com.google.ar.core.examples.java.recomendActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.ar.core.examples.java.DescriptionActivity;
import com.google.ar.core.examples.java.IntroduceItem;
import com.google.ar.core.examples.java.Model.SliderItem;
import com.google.ar.core.examples.java.adapter.SliderAdapterExample;
import com.google.ar.core.examples.java.adapter.SliderAdapterRecommend;
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
    private SliderAdapterRecommend adapter;

    // PersonChoiceInfo 사용자 선택 정보담는 객체
    private PersonChoiceInfo pschoice;

    //추천 정보 아이템을 저장하는 리스트
    private ArrayList<IntroduceItem> List_recommend = new ArrayList<IntroduceItem>();

    // 정보 아이템을 잠시 담을 그릇
    private ArrayList<IntroduceItem> List_test = new ArrayList<IntroduceItem>();

    //볼거리 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_sightseeing = new ArrayList<IntroduceItem>();
    //놀거리 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_playing = new ArrayList<IntroduceItem>();
    //먹거리 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_eating = new ArrayList<IntroduceItem>();
    //후식 정보 아이템 리스트
    private ArrayList<IntroduceItem> List_dessert = new ArrayList<IntroduceItem>();

    // 뒤로가기 버튼
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reco);

        // 뒤로가기 뷰 연결
        back_btn = findViewById(R.id.back_btn);

        // 뒤로가기 클릭
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        // 추천 받을 카테고리 (먹거리, 후식, 놀거리, 볼거리) = pschoice.getWantSomthing()
        // 리스트에서 뽑아서 반복을 해서 추천 리스트를 보여줄 것임
        // 리스트 크기만큼 반복 (5곳 까지만)

        if(pschoice.getWantSomthing().equals("먹거리")) {
            // List_eating 먹거리 리스트
            List_test = List_eating;
            makeListRecommend();

        }else if(pschoice.getWantSomthing().equals("후식")) {
            // List_dessert 후식 리스트
            List_test = List_dessert;
            makeListRecommend();

        }else if(pschoice.getWantSomthing().equals("놀거리")) {
            // List_playing 놀거리 리스트
            List_test = List_playing;
            makeListRecommend();

        }else if(pschoice.getWantSomthing().equals("볼거리")) {
            // List_sightseeing 볼거리 리스트
            List_test = List_sightseeing;
            makeListRecommend();
        }

        Log.i("추천액티비티", "결과 List_test : " + List_test);
        Log.i("추천액티비티", "결과 List_recommend : " + List_recommend);

        sliderView = findViewById(R.id.imageSlider);


        Log.i("추천액티비티", "어댑터 설정");
        Log.i("추천액티비티", "List_recommend : " + List_recommend);
        adapter = new SliderAdapterRecommend(this, List_recommend);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_LEFT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);

        adapter.renewRecommend();

        Log.i("추천액티비티", "adapter.notifyDataSetChanged()");

        adapter.setOnItemClickListener(new SliderAdapterRecommend.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 추천컨텐츠 페이지로 액티비티 이동
                startActivityObject(RecoDescriptionActivity.class, "item", List_recommend.get(position));
            }
        });

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

    }

    public void makeListRecommend() {
        int ranNum;
        for(int i=0; i<List_test.size();) {
            // 리스트 크기내에 랜덤 숫자 뽑아 리스트 추출
            ranNum = (int) (Math.random() * List_test.size());
            Log.i("추천액티비티", "포문 : " + i);
            Log.i("추천액티비티", "List_test.size() : " + List_test.size());
            Log.i("추천액티비티", "ranNum : " + ranNum);
            // 추천 리스트에 추가
            List_recommend.add(List_test.get(ranNum));
            List_test.remove(ranNum);
            if(List_recommend.size() >= 5) {
                Log.i("추천액티비티", "List_recommend size:" + List_recommend.size());
                Log.i("추천액티비티", "그만?");
                // 5곳까지만 추천
                break;
            }
        }
    }

    // 관광지 정보 리스트 추가 메소드
    private void List_add() {
        List_sightseeing.add(new IntroduceItem("미디어 파사드", "자연의 빛과 남구를 상징하는 미디어 파사드. 색채와 소리를 활용한 풍부한 볼거리를 제공합니다.", "남구청사", "ereerererqrqwe", "", R.drawable.sightseeing_media));
        List_sightseeing.add(new IntroduceItem("원형공중정원", "도로 위 육교에 꽃과 나무를 심어 쉼 공간을 제공하는 원형공중정원입니다.", "백운광장", "ereerererqrqwe", "",  R.drawable.sightseeing_garden));
        List_sightseeing.add(new IntroduceItem("분수대", "낮에는 활기차게, 밤에는 분위기 있게 변신합니다.", "푸른길", "", "",  R.drawable.sightseeing_fountain));
        List_sightseeing.add(new IntroduceItem("로컬푸드 직매장", "광주 남구의 로컬 푸드를 구경하고 저렴하게 구매해보세요.", "스트리트 푸드존", "", "",  R.drawable.sightseeing_localfood));
        List_dessert.add(new IntroduceItem("구름솜사탕", "몽실몽실 솜사탕 안에 시원한 아이스크림이 들어있어요.", "스트리트 푸드존", "", "",  R.drawable.dessert_somsatang));
        List_dessert.add(new IntroduceItem("크레페", "푸드존의 명물! 달달한 크레페에 초코시럽을 듬뿍 뿌려 드셔보세요!", "스트리트 푸드존", "", "",  R.drawable.dessert_crepe));
        List_playing.add(new IntroduceItem("원데이 클래스", "즐기며 배우고 나의 취미, 특기를 찾을 수 있습니다.", "백운광장", "", "",  R.drawable.playing_onedayclass));
        List_playing.add(new IntroduceItem("셀카존", "사랑하는 가족, 친구, 연인과 찰칵! 남는건 사진입니다.", "푸른길", "", "",  R.drawable.playing_selfi));
        List_eating.add(new IntroduceItem("오매라멘", "장인의 손맛을 그대로 재현해 낸 생라멘과 진한 육수를 즐겨보세요.", "스트리트 푸드존", "", "",  R.drawable.food_ramen));
        List_eating.add(new IntroduceItem("큐브스테이크", "불판위에서 노릇하게 구워지는 큐브스테이크입니다.", "스트리트 푸드존", "", "",  R.drawable.food_steak));
        List_eating.add(new IntroduceItem("카츠샌드", "샌드위치 안에 야들야들한 고기가 들어있어요.", "스트리트 푸드존", "", "",  R.drawable.food_sandwitch));
        List_eating.add(new IntroduceItem("엉터리생고기", "삼겹살과 볶음밥을 스트리트 푸드존에서! 간단하게 즐겨봐요.", "스트리트 푸드존", "", "",  R.drawable.food_gogi));
        ///List_eating.add(new IntroduceItem("낙곱상점", "탱글탱글한 낙지와 고소한 곱창을 함께! 매콤한 낙곱전골 드시러오세요~", "스트리트 푸드존", "", "",  R.drawable.food_steak));
    }


    // IntroduceItem 인텐트 전달 함수
    public void startActivityObject(Class c, String name , IntroduceItem sendItem) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(name, sendItem);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

}