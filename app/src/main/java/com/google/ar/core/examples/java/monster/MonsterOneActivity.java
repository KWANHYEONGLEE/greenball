package com.google.ar.core.examples.java.game;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.examples.java.Model.GameItem;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.examples.java.monster.MonsterTwoActivity;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class MonsterOneActivity extends AppCompatActivity {

    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;

    // json 변환 라이브러리
    private Gson gson = new Gson();

    private Button btn_answer;


    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //지원하지 않는 디바이스일 경우
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_monster1);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);


        //바닥 이미지 바꾸기
        arFragment.getPlaneDiscoveryController().hide();
        View aaa = (View) findViewById(R.id.btn_answer) ;
        arFragment.getPlaneDiscoveryController().setInstructionView(aaa);
        changePlane();

        // 답변하기 버튼
        btn_answer = (Button) findViewById(R.id.btn_answer_m1) ;
        btn_answer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Game1Activity.this, "123123", Toast.LENGTH_SHORT).show();

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
                    gameItems.get(3).setLock(true);
                    String gameitemData = gson.toJson(gameItems);
                    updateSharedString("gameItem", gameitemData);

                }

                Intent intent= new Intent(MonsterOneActivity.this, MonsterTwoActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //모델 설정(렌더링)
        setUpModel();

        //바닥 설정(바닥 클릭 이벤트)
        setUpPlane();
    }

    //3D 모델을 노드에 연결(?)하여 렌더링
    private void setUpModel() {


        Node model = new Node();
        model.setParent(arFragment.getArSceneView().getScene());
        model.setLocalPosition(new Vector3(0.0f, -1.0f, -2.0f));

        //Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
        Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), 202); // rotate Y axis 90 degrees
        model.setLocalRotation(rotation2);
        model.setLocalScale(new Vector3(3.0f, 3.0f, 3.0f));



        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.monster)
                .build()
                .thenAccept(
                        renderable -> {

                            // 평평한것 렌더링 해줌
                            andyRenderable = renderable;
                            model.setRenderable(renderable);



                        })
                .exceptionally(
                        throwable -> {
                            Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        //////////////////////////////////////////////////////////////////////////
        // !!!!!!!!!!!!!!!!!! tigerTitleNode 노드!!!!!!!!!!!!!!!!!!!
        Node tigerTitleNode = new Node();

        //이거끄고
        //tigerTitleNode.setParent(videoRenderable);
        tigerTitleNode.setParent(model);
        tigerTitleNode.setEnabled(false);
        tigerTitleNode.setLocalPosition(new Vector3(0.0f, 0.4f, 0.0f));

        Quaternion rotation4 = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), -202); // rotate Y axis 90 degrees
        tigerTitleNode.setLocalRotation(rotation4);
        tigerTitleNode.setLocalScale(new Vector3(0.34f, 0.34f, 0.34f));

        ViewRenderable.builder()
                .setView(this, R.layout.monster_card_view)
                .build()
                .thenAccept(
                        (renderable) -> {

                            tigerTitleNode.setRenderable(renderable);
                            tigerTitleNode.setEnabled(true);

//                            renderable.getView("")

                            View view = (View) renderable.getView();

                            TextView textView = view.findViewById(R.id.monster_talk);

                            textView.setText("내가 너 친구들한테 \n" + " 저주를 걸어놨다!");

                            //앞으로 내가 낼 문제들을 맞추면 친구들의 저주를 풀어주도록하지!

                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(textView.getText().equals("내가 너 친구들한테 \n" + " 저주를 걸어놨다!")){


                                        textView.setText("앞으로 내가 낼 \n" + " 문제들을 맞추면...");

                                    }
                                    else if (textView.getText().equals("앞으로 내가 낼 \n" + " 문제들을 맞추면...")) {


                                        textView.setText("친구들의 저주를 \n" + "풀어주도록하지!");
                                        btn_answer.setVisibility(View.VISIBLE);


                                    }
                                }
                            });


                            ////////////////////////////////////////////////////////////////////
                            //////////////////////////////////////////////////////////////////////////
                        })
                .exceptionally(
                        (throwable) -> {
                            throw new AssertionError("Could not load card view.", throwable);
                        }
                );

        // !!!!!!!!!!!!!!!!!! tigerTitleNode 노드!!!!!!!!!!!!!!!!!!!

    }

    //바닥 설정
    private void setUpPlane(){
        // Build texture sampler



//        arFragment.getArSceneView().setCameraStreamRenderPriority();
//        arFragment.getArSceneView().getPlaneRenderer().getMaterial();





        //바닥 클릭 이벤트
        arFragment.setOnTapArPlaneListener(
                //HitResult : Ray(?)와 추정된 실제 기하학 사이의 교차점을 젇의
                //Plane : 터치한 평면
                //MotionEvent : tap을 작동시킨 모션 이벤트
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    Log.d("LogTest", "setOnTapArPlaneListener");

//                    Log.d("LogTest",
//                            arFragment.getArSceneView().getPlaneRenderer().getMaterial().toString());
                    //3D 모델이 렌더링이 불가능한 상태면 종료
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    Log.d("LogTest", arFragment.getArSceneView().getScene().toString());




                    // 움직이는거
//                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
//                    andy.setParent(anchorNode);
//                    andy.setRenderable(andyRenderable);
//                    andy.select();

                });


    }

    private void changePlane(){
        Texture.Sampler sampler = Texture.Sampler.builder()
                .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                .build();

        // Build texture with sampler
        CompletableFuture<Texture> trigrid = Texture.builder()
                .setSource(this, R.drawable.opacity)
                .setSampler(sampler).build();

        // Set plane texture
        this.arFragment.getArSceneView()
                .getPlaneRenderer()
                .getMaterial()
                .thenAcceptBoth(trigrid, (material, texture) -> {
                    material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture);
//                    material.setFloat3(PlaneRenderer.MATERIAL_COLOR, new Color(1.0f, 1.0f, 0.9f, 1.0f));
//                    material.setFloat2(PlaneRenderer.MATERIAL_UV_SCALE, 50.0f, 50.0f);
//                    material.setFloat(PlaneRenderer.MATERIAL_SPOTLIGHT_RADIUS, Float.MAX_VALUE);
                });

    }



    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {

            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {

            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
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
