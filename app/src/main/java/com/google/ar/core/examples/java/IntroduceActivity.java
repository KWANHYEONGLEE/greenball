package com.google.ar.core.examples.java;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.ar.core.examples.java.augmentedimage.R;

import java.util.ArrayList;

public class IntroduceActivity extends AppCompatActivity {

    //볼거리, 놀거리, 먹거리 선택
    private RadioGroup radioGroup;
    private RadioButton r1;
    private RadioButton r2;
    private RadioButton r3;

    private androidx.recyclerview.widget.RecyclerView RecyclerView = null;
    private IntroduceAdapter Adapter = null;
    private ArrayList<IntroduceItem> List = new ArrayList<IntroduceItem>();

    private ArrayList<IntroduceItem> List_sightseeing = new ArrayList<IntroduceItem>();
    private ArrayList<IntroduceItem> List_playing = new ArrayList<IntroduceItem>();
    private ArrayList<IntroduceItem> List_eating = new ArrayList<IntroduceItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        List_add();

        recycler_add();
        initRecyclerAdd();

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        r1 = (RadioButton)findViewById(R.id.btnRadio_sightseeing);
        r2 = (RadioButton)findViewById(R.id.btnRadio_playing);
        r3 = (RadioButton)findViewById(R.id.btnRadio_eating);

    }


    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.btnRadio_sightseeing) {
                r1.setTextColor(Color.parseColor("#ffffff"));
                r2.setTextColor(Color.parseColor("#000000"));
                r3.setTextColor(Color.parseColor("#000000"));
                List.clear();
                List.addAll(List_sightseeing);
                Adapter.notifyDataSetChanged();
            } else if (i == R.id.btnRadio_playing) {
                r2.setTextColor(Color.parseColor("#ffffff"));
                r1.setTextColor(Color.parseColor("#000000"));
                r3.setTextColor(Color.parseColor("#000000"));
                List.clear();
                List.addAll(List_playing);
                Adapter.notifyDataSetChanged();
            } else if (i == R.id.btnRadio_eating) {
                r3.setTextColor(Color.parseColor("#ffffff"));
                r2.setTextColor(Color.parseColor("#000000"));
                r1.setTextColor(Color.parseColor("#000000"));
                List.clear();
                List.addAll(List_eating);
                Adapter.notifyDataSetChanged();
            }
        }
    };

    private void List_add() {
        List_sightseeing.add(new IntroduceItem("미디어 파사드", "자연의 빛과 남구를 상징하는 미디어 파사드. 색채와 소리를 활용한 풍부한 볼거리를 제공합니다.", "남구청사", "", "", 1));
        List_sightseeing.add(new IntroduceItem("원형공중정원", "도로 위 육교에 꽃과 나무를 심어 쉼 공간을 제공하는 원형공중정원입니다.", "백운광장", "", "",  1));
        List_sightseeing.add(new IntroduceItem("분수대", "낮에는 활기차게, 밤에는 분위기 있게 변신합니다.", "푸른길", "", "",  1));
        List_sightseeing.add(new IntroduceItem("로컬푸드 직매장", "광주 남구의 로컬 푸드를 구경하고 저렴하게 구매해보세요.", "스트리트 푸드존", "", "",  1));
        List_playing.add(new IntroduceItem("AR게임", "가족과 함께 즐길 수 있는 현실 공간 속의 가상의 게임을 즐겨봐요!", "푸른길", "", "",  1));
        List_playing.add(new IntroduceItem("원데이 클래스", "즐기며 배우고 나의 취미, 특기를 찾을 수 있습니다.", "백운광장", "", "",  3));
        List_playing.add(new IntroduceItem("셀카존", "사랑하는 가족, 친구, 연인과 찰칵! 남는건 사진입니다.", "푸른길", "", "",  1));
        List_eating.add(new IntroduceItem("오매라멘", "장인의 손맛을 그대로 재현해 낸 생라멘과 진한 육수를 즐겨보세요.", "스트리트 푸드존", "", "",  1));
        List_eating.add(new IntroduceItem("큐브스테이크", "불판위에서 노릇하게 구워지는 큐브스테이크입니다.", "스트리트 푸드존", "", "",  1));
        List_eating.add(new IntroduceItem("구름솜사탕", "몽실몽실 솜사탕 안에 시원한 아이스크림이 들어있어요.", "스트리트 푸드존", "", "",  1));
        List_eating.add(new IntroduceItem("크레페", "푸드존의 명물! 달달한 크레페에 초코시럽을 듬뿍 뿌려 드셔보세요!", "스트리트 푸드존", "", "",  1));
        List_eating.add(new IntroduceItem("카츠샌드", "샌드위치 안에 야들야들한 고기가 들어있어요.", "스트리트 푸드존", "", "",  1));
        List_eating.add(new IntroduceItem("엉터리생고기", "삼겹살과 볶음밥을 스트리트 푸드존에서! 간단하게 즐겨봐요.", "스트리트 푸드존", "", "",  1));
        List_eating.add(new IntroduceItem("낙곱상점", "탱글탱글한 낙지와 고소한 곱창을 함께! 매콤한 낙곱전골 드시러오세요~", "스트리트 푸드존", "", "",  1));
    }

    private void initRecyclerAdd() {
        List.addAll(List_sightseeing);
        Adapter.notifyDataSetChanged();
    }

    private void recycler_add() {
        RecyclerView = findViewById(R.id.recycler_introduce);

        Adapter = new IntroduceAdapter(List);
        Adapter.setOnItemClickListener(new IntroduceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntroduceItem item = List.get(position);
                Intent intent = new Intent(IntroduceActivity.this, DescriptionActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
            }
        });

        RecyclerView.setAdapter(Adapter);

        RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


}
