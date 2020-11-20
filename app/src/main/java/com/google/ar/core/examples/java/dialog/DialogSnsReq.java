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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ar.core.examples.java.adapter.SliderAdapterRecommend;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.listener.OnDialogReturnResultListener;

import static com.google.ar.core.examples.java.augmentedimage.R.*;


public class DialogSnsReq {

    private Context context;




    // Yes 버튼
    private Button btn_yes;
    // No 버튼
    private Button btn_no;

    // 확인 활성화 체크 변수
    private Boolean btn_conf_bool = false;

    // 인터페이스 생성
    public interface OnItemClickListener {
        void onitemClick(boolean result);
    }

    DialogSnsReq.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(DialogSnsReq.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // 생성장 선언
    // 컨텍스트 연결
    public DialogSnsReq(Context context) {this.context = context;}

    // ArNpc1연결하는 인터페이스 변수
    private OnDialogReturnResultListener onDialogReturnResultListener;
    // ArNpc1연결하는 인터페이스 생성자
    public void setOnDialogReturnResultListener(OnDialogReturnResultListener onDialogReturnResultListener) {
        this.onDialogReturnResultListener = onDialogReturnResultListener;
    }

    // 호출할 다이얼로그 메소드
    public void callDialog() {

        // 다이얼로그 설정
        final Dialog dig = new Dialog(context);
        dig.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dig.setContentView(layout.dailog_sns_request);

        WindowManager.LayoutParams params = dig.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.FIRST_SUB_WINDOW;
        params.gravity = Gravity.CENTER;
        dig.getWindow().setAttributes((WindowManager.LayoutParams) params);
        // 배경을 투명도 주어 모서리 둥근부분만 남게
        dig.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 보여주기
        dig.show();


        // 다이얼로그 요소정의

        // Yes 버튼
        btn_yes = dig.findViewById(R.id.btn_yes);
        // No 버튼
        btn_no = dig.findViewById(R.id.btn_no);

        // Yes버튼 클릭처리
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SNS다이얼로그", "Yes버튼 클릭");
                onItemClickListener.onitemClick(true);
            }
        });

        // No 버튼 클릭처리
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SNS다이얼로그", "No버튼 클릭");
                onItemClickListener.onitemClick(false);
                dig.dismiss();
            }
        });


    }

    //dig.dismiss(); 다이얼로그 끄기

}

