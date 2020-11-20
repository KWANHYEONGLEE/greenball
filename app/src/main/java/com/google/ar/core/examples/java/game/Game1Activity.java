
package com.google.ar.core.examples.java.game;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
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
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


public class Game1Activity extends AppCompatActivity {
    private static final String TAG = Game1Activity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;

    @Nullable
    private ModelRenderable videoRenderable;
    private MediaPlayer mediaPlayer;

    // The color to filter out of the video.
    private static final Color CHROMA_KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);

    // Controls the height of the video in world space.
    private static final float VIDEO_HEIGHT_METERS = 0.85f;

    private int xxx = 0;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_game1);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);



        //
        Button btn_answer = (Button) findViewById(R.id.btn_answer) ;
        btn_answer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(Game1Activity.this, "123123", Toast.LENGTH_SHORT).show();

                Intent intent= new Intent(Game1Activity.this, Game1Next.class);
                startActivity(intent);

            }
        });

        Button btn_restart = (Button) findViewById(R.id.btn_restart) ;
        btn_restart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    mediaPlayer.seekTo(0,MediaPlayer.SEEK_CLOSEST);
                else
                    mediaPlayer.seekTo((int)0);
                mediaPlayer.start();


            }
        });

        Button btn_pause = (Button) findViewById(R.id.btn_pause) ;
        btn_pause.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {


                mediaPlayer.pause();



            }
        });

        Button btn_seek = (Button) findViewById(R.id.btn_seek) ;
        btn_seek.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {





                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    mediaPlayer.seekTo(3000,MediaPlayer.SEEK_CLOSEST);
                else
                    mediaPlayer.seekTo((int)3000);



            }
        });



        // !!!쓰리디에 입혀질 텍스트
        ExternalTexture texture = new ExternalTexture();

        // mediaplayer -> texture
        mediaPlayer = MediaPlayer.create(this, R.raw.sound_test);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(false);

        ModelRenderable.builder()
                .setSource(this, R.raw.chroma_key_video)
                .build()
                .thenAccept(
                        renderable -> {

                            // 평평한것 렌더링 해줌
                            videoRenderable = renderable;
                            // mediaplayer -> texture -> 3dmodel
                            renderable.getMaterial().setExternalTexture("videoTexture", texture);

                            // 색 제외시키기
                            renderable.getMaterial().setFloat4("keyColor", CHROMA_KEY_COLOR);

                        })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        //탭을 하면!!!!!!!!!!!!!
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (videoRenderable == null) {
                        return;
                    }

                    //////////// mediaplayer -> texture -> videoRenderable -> model_node -> anchorNode ->  hitResult


                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // 이게 움직이는 모델
                    TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
                    model.setParent(anchorNode);
                    model.setRenderable(videoRenderable);
                    //model.select(); // 끄면 안움직임

                    //Node videoNode = new Node();
                    //videoNode.setParent(anchorNode);

                    // 영상노드 크기정해주고
                    float videoWidth = mediaPlayer.getVideoWidth();
                    float videoHeight = mediaPlayer.getVideoHeight();
                    model.setLocalScale(
                            new Vector3(
                                    VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));


                    // 쓰리디 모델 리스너
                    model.setOnTouchListener(new Node.OnTouchListener() {
                        @Override
                        public boolean onTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                            Toast.makeText(Game1Activity.this, "아래캐릭터", Toast.LENGTH_SHORT).show();
                            return true;

                        }
                    });


                    // 미디어 플레이 중에 따라 텍스쳐를 프레임별로 갱신 -> 그리고 렌더러블해줌
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();


                        // Wait to set the renderable until the first frame of the  video becomes available.
                        // This prevents the renderable from briefly appearing as a black quad before the video
                        // plays.
                        texture
                                .getSurfaceTexture()
                                .setOnFrameAvailableListener(
                                        (SurfaceTexture surfaceTexture) -> {

                                            //videoNode.setRenderable(videoRenderable);

                                            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                                        });
                    } else {

                        // 주석한다면 새로 생기지 않겠지?
                        //videoNode.setRenderable(videoRenderable);

                        arFragment.getPlaneDiscoveryController().hide();
                        //changePlane();

                    }


                    ////////////////////////// 말풍선 ////////////////////////

                    // 노드라는건 새로운 하나의 쓰리디일뿐...
                    Node tigerTitleNode = new Node();

                    tigerTitleNode.setParent(model);
                    tigerTitleNode.setEnabled(false);
                    tigerTitleNode.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));

                    ViewRenderable.builder()
                            .setView(this, R.layout.game1_text)
                            .build()
                            .thenAccept(
                                    (renderable) -> {

                                        tigerTitleNode.setRenderable(renderable);
                                        tigerTitleNode.setEnabled(true);

                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText("Value from setImageㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ");

                                        textView.setOnClickListener(new Button.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                textView.setText("testtest");

                                            }
                                        });


                                    })
                            .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load card view.", throwable);
                                    }
                            );

                    ////////////////////////// 말풍선 ////////////////////////

                });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
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


}
