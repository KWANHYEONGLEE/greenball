package com.google.ar.core.examples.java.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ar.core.examples.java.listener.OnDialogReturnResultListener;

import static com.google.ar.core.examples.java.augmentedimage.R.drawable;
import static com.google.ar.core.examples.java.augmentedimage.R.id;
import static com.google.ar.core.examples.java.augmentedimage.R.layout;


public class DialogWhereGo {

    private Context context;

    private boolean eatCheckBool = false;
    private boolean dessertCheckBool = false;
    private boolean playingCheckBool = false;
    private boolean attractionCheckBool= false;

    // 확인 버튼
    private TextView btn_conf;
    // 확인 활성화 체크 변수
    private Boolean btn_conf_bool = false;

    // ArNpc1연결하는 인터페이스 변수
    private OnDialogReturnResultListener onDialogReturnResultListener;
    // ArNpc1연결하는 인터페이스 생성자
    public void setOnDialogReturnResultListener(OnDialogReturnResultListener onDialogReturnResultListener) {
        this.onDialogReturnResultListener = onDialogReturnResultListener;
    }

    // 생성장 선언
    // 컨텍스트 연결
    public DialogWhereGo(Context context) {this.context = context;}

    // 호출할 다이얼로그 메소드
    public void callDialog() {

        // 다이얼로그 설정
        final Dialog dig = new Dialog(context);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dig.setContentView(layout.dailog_where_go);

        WindowManager.LayoutParams params = dig.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.FIRST_SUB_WINDOW;
        params.gravity = Gravity.BOTTOM;
        dig.getWindow().setAttributes((WindowManager.LayoutParams) params);
        // 배경을 투명도 주어 모서리 둥근부분만 남게
        dig.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 보여주기
        dig.show();

        // 다이얼로그 요소정의

        // 먹거리 버튼
        final LinearLayout img_btn_eat = (LinearLayout) dig.findViewById(id.img_btn_eat);
        // 먹거리 체크박스
        final FrameLayout check_eat = (FrameLayout) dig.findViewById(id.check_eat);
        // 후식 버튼
        final LinearLayout img_btn_dessert = (LinearLayout)dig.findViewById(id.img_btn_dessert);
        // 후식 체크박스
        final FrameLayout check_dessert = (FrameLayout) dig.findViewById(id.check_dessert);
        // 놀거리 버튼
        final LinearLayout img_btn_playing = (LinearLayout) dig.findViewById(id.img_btn_playing);
        // 놀거리 체크박스
        final FrameLayout check_playing = (FrameLayout) dig.findViewById(id.check_playing);
        // 볼거리 버튼
        final LinearLayout img_btn_attraction = (LinearLayout)dig.findViewById(id.img_btn_attraction);
        // 볼거리 체크박스
        final FrameLayout check_attraction = (FrameLayout) dig.findViewById(id.check_attraction);

        btn_conf = (TextView) dig.findViewById(id.btn_conf);

        // 확인 버튼 클릭
        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_conf_bool == true) {
                    Log.i("체크", "확인 버튼 클릭");
                    // 다이얼로그 끄기
                    dig.dismiss();
                    // 인터페이스로
                    // ArNpc1.class 파일 에서 onResult 실행
                    if(eatCheckBool == true) {
                        // 먹거리 선택
                        onDialogReturnResultListener.onWantResult("먹거리");
                    }else if(dessertCheckBool == true) {
                        // 후식 선택
                        onDialogReturnResultListener.onWantResult("후식");
                    }else if(playingCheckBool == true) {
                        // 놀거리 선택
                        onDialogReturnResultListener.onWantResult("놀거리");
                    }else if(attractionCheckBool == true) {
                        // 볼거리 선택
                        onDialogReturnResultListener.onWantResult("볼거리");
                    }
                }else {
                    Log.i("체크", "메뉴 선택 후 버튼 클릭 가능");
                }
            }
        });

        // 먹거리 버튼 클릭
        img_btn_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    eatCheckBool = false;
                    img_btn_eat.setBackgroundResource(0);
                    check_eat.setVisibility(View.INVISIBLE);
                    dessertCheckBool = false;
                    img_btn_dessert.setBackgroundResource(0);
                    check_dessert.setVisibility(View.INVISIBLE);
                    playingCheckBool = false;
                    img_btn_playing.setBackgroundResource(0);
                    check_playing.setVisibility(View.INVISIBLE);
                    attractionCheckBool = false;
                    img_btn_attraction.setBackgroundResource(0);
                    check_attraction.setVisibility(View.INVISIBLE);

                    eatCheckBool = true;
                    img_btn_eat.setBackgroundResource(drawable.border);
                    check_eat.setVisibility(View.VISIBLE);
                }else {
                    // 먹거리 변수 선택안되어 있어 선택
                    eatCheckBool = true;
                    img_btn_eat.setBackgroundResource(drawable.border);
                    check_eat.setVisibility(View.VISIBLE);
                }
            }
        });

        // 후식 버튼 클릭
        img_btn_dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    eatCheckBool = false;
                    img_btn_eat.setBackgroundResource(0);
                    check_eat.setVisibility(View.INVISIBLE);
                    dessertCheckBool = false;
                    img_btn_dessert.setBackgroundResource(0);
                    check_dessert.setVisibility(View.INVISIBLE);
                    playingCheckBool = false;
                    img_btn_playing.setBackgroundResource(0);
                    check_playing.setVisibility(View.INVISIBLE);
                    attractionCheckBool = false;
                    img_btn_attraction.setBackgroundResource(0);
                    check_attraction.setVisibility(View.INVISIBLE);

                    dessertCheckBool = true;
                    img_btn_dessert.setBackgroundResource(drawable.border);
                    check_dessert.setVisibility(View.VISIBLE);
                }else {
                    // 후식 변수 선택안되어 있어 선택
                    dessertCheckBool = true;
                    img_btn_dessert.setBackgroundResource(drawable.border);
                    check_dessert.setVisibility(View.VISIBLE);
                }


            }
        });

        // 놀거리 버튼 클릭
        img_btn_playing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    eatCheckBool = false;
                    img_btn_eat.setBackgroundResource(0);
                    check_eat.setVisibility(View.INVISIBLE);
                    dessertCheckBool = false;
                    img_btn_dessert.setBackgroundResource(0);
                    check_dessert.setVisibility(View.INVISIBLE);
                    playingCheckBool = false;
                    img_btn_playing.setBackgroundResource(0);
                    check_playing.setVisibility(View.INVISIBLE);
                    attractionCheckBool = false;
                    img_btn_attraction.setBackgroundResource(0);
                    check_attraction.setVisibility(View.INVISIBLE);

                    playingCheckBool = true;
                    img_btn_playing.setBackgroundResource(drawable.border);
                    check_playing.setVisibility(View.VISIBLE);

                }else {
                    // 놀거리 변수 선택안되어 있어 선택
                    playingCheckBool = true;
                    img_btn_playing.setBackgroundResource(drawable.border);
                    check_playing.setVisibility(View.VISIBLE);
                }


            }
        });

        // 볼거리 버튼 클릭
        img_btn_attraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스틱코드에 접속하여 생산성을 향상시켜 보세요, https://stickode.com/
                Log.i("체크", "" + checkResult());

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    eatCheckBool = false;
                    img_btn_eat.setBackgroundResource(0);
                    check_eat.setVisibility(View.INVISIBLE);
                    dessertCheckBool = false;
                    img_btn_dessert.setBackgroundResource(0);
                    check_dessert.setVisibility(View.INVISIBLE);
                    playingCheckBool = false;
                    img_btn_playing.setBackgroundResource(0);
                    check_playing.setVisibility(View.INVISIBLE);
                    attractionCheckBool = false;
                    img_btn_attraction.setBackgroundResource(0);
                    check_attraction.setVisibility(View.INVISIBLE);

                    attractionCheckBool = true;
                    img_btn_attraction.setBackgroundResource(drawable.border);
                    check_attraction.setVisibility(View.VISIBLE);
                }else {
                    // 볼거리 변수 선택안되어 있어 선택
                    attractionCheckBool = true;
                    img_btn_attraction.setBackgroundResource(drawable.border);
                    check_attraction.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    //dig.dismiss(); 다이얼로그 끄기

    public boolean checkResult() {
//        Log.i("체크", "eatCheckBool:" + eatCheckBool);
//        Log.i("체크", "dessertCheckBool:" + dessertCheckBool);
//        Log.i("체크", "playingCheckBool:" + playingCheckBool);
//        Log.i("체크", "attractionCheckBool:" + attractionCheckBool);

        // 처음 확인버튼 활성화
        if(btn_conf_bool == false) {
            btn_conf_bool = true;
            // 확인 버튼 활성화
            btn_conf.setTextColor(Color.parseColor("#333333"));
        }

        if(eatCheckBool == true || dessertCheckBool == true || playingCheckBool == true || attractionCheckBool == true) {
            // 선택되어 있는 변수 확인 후 있다면
            return true;
        }else {
            return false;
        }

    }





}

