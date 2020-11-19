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


public class DialogWithWho {

    private Context context;

    private boolean aloneCheckBool = false;
    private boolean friendCheckBool = false;
    private boolean coupleCheckBool = false;
    private boolean familyCheckBool= false;

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
    public DialogWithWho(Context context) {this.context = context;}

    // 호출할 다이얼로그 메소드
    public void callDialog() {

        // 다이얼로그 설정
        final Dialog dig = new Dialog(context);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dig.setContentView(layout.dailog_with_who);

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
        final LinearLayout img_btn_alone = (LinearLayout) dig.findViewById(id.img_btn_alone);
        // 먹거리 체크박스
        final FrameLayout check_alone = (FrameLayout) dig.findViewById(id.check_alone);
        // 후식 버튼
        final LinearLayout img_btn_friend = (LinearLayout)dig.findViewById(id.img_btn_friend);
        // 후식 체크박스
        final FrameLayout check_friend = (FrameLayout) dig.findViewById(id.check_friend);
        // 놀거리 버튼
        final LinearLayout img_btn_couple = (LinearLayout) dig.findViewById(id.img_btn_couple);
        // 놀거리 체크박스
        final FrameLayout check_couple = (FrameLayout) dig.findViewById(id.check_couple);
        // 볼거리 버튼
        final LinearLayout img_btn_family = (LinearLayout)dig.findViewById(id.img_btn_family);
        // 볼거리 체크박스
        final FrameLayout check_family = (FrameLayout) dig.findViewById(id.check_family);

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
                    if(aloneCheckBool == true) {
                        // 혼자왔어요 선택
                        onDialogReturnResultListener.onWhoResult("혼자");
                    }else if(friendCheckBool == true) {
                        // 친구랑 함께 선택
                        onDialogReturnResultListener.onWhoResult("친구");
                    }else if(coupleCheckBool == true) {
                        // 연인과 함께 선택
                        onDialogReturnResultListener.onWhoResult("연인");
                    }else if(familyCheckBool == true) {
                        // 가족이랑 함께 선택
                        onDialogReturnResultListener.onWhoResult("가족");
                    }

                }else {
                    Log.i("체크", "메뉴 선택 후 버튼 클릭 가능");
                }
            }
        });

        // 먹거리 버튼 클릭
        img_btn_alone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    aloneCheckBool = false;
                    img_btn_alone.setBackgroundResource(0);
                    check_alone.setVisibility(View.INVISIBLE);
                    friendCheckBool = false;
                    img_btn_friend.setBackgroundResource(0);
                    check_friend.setVisibility(View.INVISIBLE);
                    coupleCheckBool = false;
                    img_btn_couple.setBackgroundResource(0);
                    check_couple.setVisibility(View.INVISIBLE);
                    familyCheckBool = false;
                    img_btn_family.setBackgroundResource(0);
                    check_family.setVisibility(View.INVISIBLE);

                    aloneCheckBool = true;
                    img_btn_alone.setBackgroundResource(drawable.border);
                    check_alone.setVisibility(View.VISIBLE);
                }else {
                    // 먹거리 변수 선택안되어 있어 선택
                    aloneCheckBool = true;
                    img_btn_alone.setBackgroundResource(drawable.border);
                    check_alone.setVisibility(View.VISIBLE);
                }
            }
        });

        // 후식 버튼 클릭
        img_btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    aloneCheckBool = false;
                    img_btn_alone.setBackgroundResource(0);
                    check_alone.setVisibility(View.INVISIBLE);
                    friendCheckBool = false;
                    img_btn_friend.setBackgroundResource(0);
                    check_friend.setVisibility(View.INVISIBLE);
                    coupleCheckBool = false;
                    img_btn_couple.setBackgroundResource(0);
                    check_couple.setVisibility(View.INVISIBLE);
                    familyCheckBool = false;
                    img_btn_family.setBackgroundResource(0);
                    check_family.setVisibility(View.INVISIBLE);

                    friendCheckBool = true;
                    img_btn_friend.setBackgroundResource(drawable.border);
                    check_friend.setVisibility(View.VISIBLE);
                }else {
                    // 후식 변수 선택안되어 있어 선택
                    friendCheckBool = true;
                    img_btn_friend.setBackgroundResource(drawable.border);
                    check_friend.setVisibility(View.VISIBLE);
                }


            }
        });

        // 놀거리 버튼 클릭
        img_btn_couple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    aloneCheckBool = false;
                    img_btn_alone.setBackgroundResource(0);
                    check_alone.setVisibility(View.INVISIBLE);
                    friendCheckBool = false;
                    img_btn_friend.setBackgroundResource(0);
                    check_friend.setVisibility(View.INVISIBLE);
                    coupleCheckBool = false;
                    img_btn_couple.setBackgroundResource(0);
                    check_couple.setVisibility(View.INVISIBLE);
                    familyCheckBool = false;
                    img_btn_family.setBackgroundResource(0);
                    check_family.setVisibility(View.INVISIBLE);

                    friendCheckBool = true;
                    img_btn_couple.setBackgroundResource(drawable.border);
                    check_couple.setVisibility(View.VISIBLE);

                }else {
                    // 놀거리 변수 선택안되어 있어 선택
                    friendCheckBool = true;
                    img_btn_couple.setBackgroundResource(drawable.border);
                    check_couple.setVisibility(View.VISIBLE);
                }


            }
        });

        // 볼거리 버튼 클릭
        img_btn_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스틱코드에 접속하여 생산성을 향상시켜 보세요, https://stickode.com/
                Log.i("체크", "" + checkResult());

                if(checkResult()){
                    // 체크된 버튼이 있다면 전체 취소처리
                    aloneCheckBool = false;
                    img_btn_alone.setBackgroundResource(0);
                    check_alone.setVisibility(View.INVISIBLE);
                    friendCheckBool = false;
                    img_btn_friend.setBackgroundResource(0);
                    check_friend.setVisibility(View.INVISIBLE);
                    coupleCheckBool = false;
                    img_btn_couple.setBackgroundResource(0);
                    check_couple.setVisibility(View.INVISIBLE);
                    familyCheckBool = false;
                    img_btn_family.setBackgroundResource(0);
                    check_family.setVisibility(View.INVISIBLE);

                    friendCheckBool = true;
                    img_btn_family.setBackgroundResource(drawable.border);
                    check_family.setVisibility(View.VISIBLE);
                }else {
                    // 볼거리 변수 선택안되어 있어 선택
                    friendCheckBool = true;
                    img_btn_family.setBackgroundResource(drawable.border);
                    check_family.setVisibility(View.VISIBLE);
                }

            }
        });



    }

    //dig.dismiss(); 다이얼로그 끄기

    public boolean checkResult() {
//        Log.i("체크", "aloneCheckBool:" + aloneCheckBool);
//        Log.i("체크", "friendCheckBool:" + friendCheckBool);
//        Log.i("체크", "coupleCheckBool:" + coupleCheckBool);
//        Log.i("체크", "familyCheckBool:" + familyCheckBool);

        // 처음 확인버튼 활성화
        if(btn_conf_bool == false) {
            btn_conf_bool = true;
            // 확인 버튼 활성화
            btn_conf.setTextColor(Color.parseColor("#333333"));
        }

        if(aloneCheckBool == true || friendCheckBool == true || coupleCheckBool == true || familyCheckBool == true) {
            // 선택되어 있는 변수 확인 후 있다면
            return true;
        }else {
            return false;
        }

    }





}

