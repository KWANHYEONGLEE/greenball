/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.core.examples.java;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.Camera;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;


public class ArNavigationActivity extends AppCompatActivity {




    private static final String TAG = ArNavigationActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;

    private  float meter  = -1.5f;
    private  int userangle  = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar_navigation);

        ///////////////////////////AR용
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        // 노드 생성후 정보확인
        Node infoCard = new Node();
        infoCard.setParent(arFragment.getArSceneView().getScene());
        infoCard.setEnabled(true);



        double angle    = Math.PI * (90-userangle) / 180.0;
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        //1 번이랑 3번을 바꿔야지 //1번이 -가 왼쪽 , 3번은 -가 앞쪽 // 2번은 바닥쪽으로 가도록 한것
        infoCard.setLocalPosition (new Vector3(  (float)(-1 * meter * cosAngle) , -1.5f,  (float)(meter * sinAngle) ));
        // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)

        //infoCard.setLocalPosition(new Vector3(1.0f, -1.5f, -2.5f));

        //Vector3 aaa = new Vector3(0.0f,0.0f,1.0f);
        Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
        Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), userangle); // rotate Y axis 90 degrees
        infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));

        ViewRenderable.builder()
                .setView(this, R.layout.tiger_card_view2)
                .build()
                .thenAccept(
                        (renderable) -> {

                            arFragment.getPlaneDiscoveryController().hide();
                            changePlane();

                            // 이 node를 생성
                            infoCard.setRenderable(renderable);
                            TextView textView = (TextView) renderable.getView();
                            //textView.setText("Value from setImage");


                            Button button1 = (Button) findViewById(R.id.button_new) ;
                            button1.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    meter = meter + -1.0f;

                                    // 노드 생성후 정보확인

                                    infoCard.setParent(arFragment.getArSceneView().getScene());
                                    infoCard.setEnabled(true);
                                    infoCard.setLocalPosition(new Vector3(0f, -1.5f, -1.5f)); // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)
                                    //infoCard.setLocalRotation(new Quaternion(new Vector3(1.0f,0.0f,0.0f),90));

                                    Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
                                    Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), 45); // rotate Y axis 90 degrees
                                    infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));


                                    infoCard.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    textView.setText("바뀐것");




                                }
                            });


                        })
                .exceptionally(
                        (throwable) -> {
                            throw new AssertionError("Could not load info card view.", throwable);
                        });
        ///////////////////////////AR용


    }



    ///////////////////////////AR용
    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private void changePlane(){
        Texture.Sampler sampler = Texture.Sampler.builder()
                .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                .build();

        // Build texture with sampler
        CompletableFuture<Texture> trigrid = Texture.builder()
                /*************
                //R.drawable.arrow_t1 -> 투명 배경으로 변경하시면 됩니다.*/
                .setSource(this, R.drawable.arrow_t1)
                .setSampler(sampler).build();

        // Set plane texture
        this.arFragment.getArSceneView()
                .getPlaneRenderer()
                .getMaterial()
                .thenAcceptBoth(trigrid, (material, texture) -> {
                    material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture);
                });
    }



    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
    ///////////////////////////AR용



}
