package com.google.ar.core.examples.java.game;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class Game2Activity extends AppCompatActivity {

    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;

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

        setContentView(R.layout.activity_game2);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //모델 설정(렌더링)
        setUpModel();

        //바닥 설정(바닥 클릭 이벤트)
        //setUpPlane();
    }

    //3D 모델을 노드에 연결(?)하여 렌더링
    private void setUpModel() {
        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.monster)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }

    //바닥 설정
    private void setUpPlane(){
        // Build texture sampler

        //바닥 이미지 바꾸기
        changePlane();

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

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                });

//        arFragment.getArSceneView().getScene().addOnPeekTouchListener((hitTestResult, motionEvent) -> {
//            Log.d("LogTest", "addOnPeekTouchListener");
//        });

        //arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> Log.d("LogTest", "addOnUpdateListener"));
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
}
