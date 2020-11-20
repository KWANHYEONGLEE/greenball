package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.dialog.DialogSnsReq;
import com.google.ar.core.examples.java.dialog.DialogWithWho;
import com.google.ar.core.examples.java.game.Game1Activity;
import com.google.ar.core.examples.java.listener.OnDialogReturnResultListener;
import com.google.ar.core.examples.java.recomendActivity.ArNpc1;

public class HomeActivity extends AppCompatActivity {

    LinearLayout linearLayout_Home_Recommend, linearLayout_Home_Introduce, linearLayout_Home_ARroad, linearLayout_Home_ARgame;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //xml과 연결하기
        linearLayout_Home_Recommend = findViewById(R.id.linearLayout_Home_Recommend);
        linearLayout_Home_Introduce = findViewById(R.id.linearLayout_Home_Introduce);
        linearLayout_Home_ARroad = findViewById(R.id.linearLayout_Home_ARroad);
        linearLayout_Home_ARgame = findViewById(R.id.linearLayout_Home_ARgame);


        // 쉐어드에 추천받은 정보가 있  다면
        // 스낵바를 통해 알림 예) 추천 받은 곳은 마음에 드셨나요?
        String recommendItem = getSharedString("recommendItem");
        Log.i("홈액티비티", "recommendItem: " + recommendItem);
        if (recommendItem.equals("null")) {
            Log.i("홈액티비티", "recommendItem: null 과 같다.");

        } else {
            Log.i("홈액티비티", "recommendItem: null이 아님으로 추천받은 곳이 있다.");
            // 추천받은 곳이 있는 경우
            if (getSharedString("recommendItemCount").equals("0")) {
                // 업데이트 이후 처음 홈화면에 온 경우에는 스낵바를 띄우지 않는다.
                updateSharedString("recommendItemCount", "1");
            } else if (getSharedString("recommendItemCount").equals("1")) {
                // 1 인 겨우 2번째 본것으로 판단하고 스낵바를 띄우는 로직 실행한다.
                // 스낵바는 상단에 띄운다.

                final View viewPos = findViewById(R.id.myCoordinatorLayout);

                Snackbar snackbar = Snackbar.make(viewPos, "추천 받은 곳은 마음에 드셨나요?", Snackbar.LENGTH_INDEFINITE)
                        .setAction("이벤트 확인", new View.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(HomeActivity.this, "스낵바 클릭", Toast.LENGTH_SHORT).show();

                                DialogSnsReq dialogSnsReq = new DialogSnsReq(HomeActivity.this);
                                dialogSnsReq.setOnItemClickListener(new DialogSnsReq.OnItemClickListener() {
                                    @Override
                                    public void onitemClick(boolean result) {
                                        Log.i("홈액티비티", "result :" + result);

                                        // 추천받았던 정보 쉐어드에서 삭제
                                        deleteShared("recommendItem");
                                        deleteShared("recommendItemCount");

                                        if (result == true) {
                                            // YES버튼 클릭시 처리

                                            // 추천받았던 정보 쉐어드에서 삭제
                                            deleteShared("recommendItem");
                                            deleteShared("recommendItemCount");


                                            
//                                            Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
//                                                    "://" + getResources().getResourcePackageName(R.drawable.green_instagram)
//                                                    + '/' + getResources().getResourceTypeName(R.drawable.green_instagram) + '/' + getResources().getResourceEntryName(R.drawable.green_instagram) );
//
//                                            // 인스타 추천 로직 실행
//                                            Intent intent = new Intent(Intent.ACTION_SEND);
//                                            intent.setType("image/*");
//                                            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                                            intent.setPackage("com.instagram.android");

//                                            startActivity(intent);


                                            startActivityC(InstagramActivity.class);
                                        } else {
                                            // 추천받았던 정보 쉐어드에서 삭제
                                            deleteShared("recommendItem");
                                            deleteShared("recommendItemCount");
                                        }

                                    }
                                });
                                dialogSnsReq.callDialog();
                            }
                        });

                View layout = snackbar.getView();
                //setting background color
                layout.setBackgroundColor(this.getResources().getColor(R.color.d));
                // action 버튼 색상변경
                snackbar.setActionTextColor(Color.WHITE);

                snackbar.show();
            }
        }


        // 푸른길 추천 클릭
        linearLayout_Home_Recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(ArNpc1.class);

            }
        });

        // 관광지 소개 클릭
        linearLayout_Home_Introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(IntroduceActivity.class);

            }
        });

        // AR 길안내 클릭
        linearLayout_Home_ARroad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(Map.class);
            }
        });

        // AR 게임 클릭
        linearLayout_Home_ARgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityC(GameInfoActivity.class);
            }
        });

    }


    // 액티비티 전환 함수

    // 인텐트 액티비티 전환함수
    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // 인텐트 화면전환 하는 함수
    // FLAG_ACTIVITY_CLEAR_TOP = 불러올 액티비티 위에 쌓인 액티비티 지운다.
    public void startActivityflag(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // 문자열 인텐트 전달 함수
    public void startActivityString(Class c, String name, String sendString) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(name, sendString);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // 백스택 지우고 새로 만들어 전달
    public void startActivityNewTask(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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


}
