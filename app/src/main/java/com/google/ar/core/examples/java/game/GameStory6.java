
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
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.examples.java.StartActivity;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.concurrent.CompletableFuture;


public class GameStory6 extends AppCompatActivity {
    private static final String TAG = GameStory6.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;

    @Nullable
    private ModelRenderable videoRenderable;
    private MediaPlayer mediaPlayer;

    // The color to filter out of the video.
    private static final Color CHROMA_KEY_COLOR = new Color(0.1843f, 1.0f, 0.098f);

    // Controls the height of the video in world space.
    private static final float VIDEO_HEIGHT_METERS = 0.85f;



    // storyPage index 이야기 흐름 넘버
    private int storyPage = 0;

    //!!!!!!!!!!!텍스트뷰 접근!!!!!!!!!!!!!!
    //private Button button;

    // 초기 변수 설정
    private Button btn_showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }




        setContentView(R.layout.activity_game_story6);


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment_game);


        //!!!!!!!!!!!텍스트뷰 접근!!!!!!!!!!!!!!
        //button = (Button) findViewById(R.id.button);
        //button.setOnClickListener(onClickListener);

        // Create an ExternalTexture for displaying the contents of the video.
        ExternalTexture texture = new ExternalTexture();

        // Create an Android MediaPlayer to capture the video on the external texture's surface.
        mediaPlayer = MediaPlayer.create(this, R.raw.lion_chroma_origin);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);


        // 이게 움직이는 모델
        //Create the transformable model and add it to the anchor.
        //TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
        Node model = new Node();

        model.setParent(arFragment.getArSceneView().getScene());
        model.setLocalPosition(new Vector3(0.0f, -1.5f, -1.5f));


        // !!!!!!!!!!!!!!!!!! video 노드 !!!!!!!!!!!!!!!!!!!
        // Create a node to render the video and add it to the anchor.
        Node videoNode = new Node();
        videoNode.setParent(arFragment.getArSceneView().getScene());

        // Set the scale of the node so that the aspect ratio of the video is correct.
        float videoWidth = mediaPlayer.getVideoWidth();
        float videoHeight = mediaPlayer.getVideoHeight();
        videoNode.setLocalScale(
                new Vector3(VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));

        // Start playing the video when the first node is placed.
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();

            // Wait to set the renderable until the first frame of the  video becomes available.
            // This prevents the renderable from briefly appearing as a black quad before the video
            // plays.
            texture
                    .getSurfaceTexture()
                    .setOnFrameAvailableListener(
                            (SurfaceTexture surfaceTexture) -> {
                                //이거끄고
                                //videoNode.setRenderable(videoRenderable);

                                texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                            });
        } else {
            //이거끄고
            //videoNode.setRenderable(videoRenderable);

        }
        // !!!!!!!!!!!!!!!!!! video 노드 !!!!!!!!!!!!!!!!!!!


        // !!!!!!!!!!!!!!!!!! tigerTitleNode 노드!!!!!!!!!!!!!!!!!!!
        Node tigerTitleNode = new Node();

        //이거끄고
        //tigerTitleNode.setParent(videoRenderable);
        tigerTitleNode.setParent(model);
        tigerTitleNode.setEnabled(false);
        tigerTitleNode.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));

        ViewRenderable.builder()
                .setView(this, R.layout.tiger_card_view)
                .build()
                .thenAccept(
                        (renderable) -> {

                            tigerTitleNode.setRenderable(renderable);
                            tigerTitleNode.setEnabled(true);

//                            renderable.getView("")

                            View view = (View) renderable.getView();
                            TextView textView = view.findViewById(R.id.test_test);
                            textView.setText("백운아!!\n모든 친구들의 저주를 풀었구나!\n\n축하해!");
//                            TextView textView = (TextView) renderable.getView();

                            // 백운이 말 모양 버튼
                            Button bagun_speach6 = findViewById(R.id.bagun_speach6);

                            bagun_speach6.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(bagun_speach6.getText().toString().trim().equals("퀴즈 풀러 가기")) {
                                        // 여기가 어디더라 말버튼 클릭하면
//                                        Log.i("GameStory2액티비티", "스캔하기 클릭");
                                        startActivityC(Game3Activity.class);
                                        finish();
                                    }
                                }
                            });


                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.i("NPC", "텍스트뷰 클릭");
                                    switch (storyPage) {
                                        case 0:
                                            textView.setText("친구들과 함께 구름세계로 올라 갈수있게\n도와줄 사과친구를 데리고왔어!");
                                            storyPage = storyPage +1;
                                            break;
                                        case 1:
                                            // 버튼 보이게, 문구 수정 여기서
                                            //bagun_speach2.setText("스캔하기");
                                            //bagun_speach2.setVisibility(View.VISIBLE);
                                            break;
                                        case 2:

                                            break;
                                        case 3:

                                            break;
                                        case 4:
                                            break;

                                        case 5:
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });


                            ////////////////////////////////////////////////////////////////////
                        })
                .exceptionally(
                        (throwable) -> {
                            throw new AssertionError("Could not load card view.", throwable);
                        }
                );

        // !!!!!!!!!!!!!!!!!! tigerTitleNode 노드!!!!!!!!!!!!!!!!!!!


        // !!!!!!!!!!!!!!!!!! 준비된 쓰리디 모델 띄워주기 !!!!!!!!!!!!!!!!!!!
        ModelRenderable.builder()
                .setSource(this, R.raw.chroma_key_video)
                .build()
                .thenAccept(
                        renderable -> {

                            arFragment.getPlaneDiscoveryController().hide();
                            View aaa = (View) findViewById(R.id.btn_answer) ;
                            arFragment.getPlaneDiscoveryController().setInstructionView(aaa);
                            changePlane();

                            // 평평한것 렌더링 해줌
                            //videoRenderable = renderable;

                            model.setRenderable(renderable);


                            ////////////////////////////////////////////////////////////////////
//                            ImageView textView = (ImageView) renderable.getView();
//                            textView.setOnClickListener(new Button.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    ViewRenderable.builder()
//                                            .setView(getApplicationContext(), R.layout.tiger_card_view)
//                                            .build()
//                                            .thenAccept(
//                                                    (renderable) -> {
//
//                                                        tigerTitleNode.setRenderable(renderable);
//                                                        tigerTitleNode.setEnabled(true);
//
//                                                        //textView.setText("호랑이기운이 솟아나요");
//
//                                                    })
//                                            .exceptionally(
//                                                    (throwable) -> {
//                                                        throw new AssertionError("Could not load card view.", throwable);
//                                                    }
//                                            );
//
//
//                                }
//                            });
                            ////////////////////////////////////////////////////////////////////


                            //ImageView textView = (ImageView) renderable.getView();

                            // 크로마키용 제외요소들
                            renderable.getMaterial().setExternalTexture("videoTexture", texture);
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

        // !!!!!!!!!!!!!!!!!! 준비된 쓰리디 모델 띄워주기 !!!!!!!!!!!!!!!!!!!


//        //////////////////////////// 시작하면 바로뜨던 말풍선 ////////////////////////////
//        Node infoCard = new Node();
//
//        infoCard.setParent(arFragment.getArSceneView().getScene());
//        infoCard.setEnabled(true);
//
//        infoCard.setLocalPosition(new Vector3(0f, 0f, -1.5f)); // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)
//        infoCard.setLocalRotation(new Quaternion(new Vector3(1.0f,0.0f,0.0f),0));
//
//        ViewRenderable.builder()
//                .setView(this, R.layout.tiger_card_view)
//                .build()
//                .thenAccept(
//                        (renderable) -> {
//                            infoCard.setRenderable(renderable);
//                            TextView textView = (TextView) renderable.getView();
//                            textView.setText("Value from setImage");
//
//                            Button button1 = (Button) findViewById(R.id.button) ;
//                            button1.setOnClickListener(new Button.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    Toast.makeText(ArNpc1.this, "123123", Toast.LENGTH_SHORT).show();
//                                    textView.setText("www");
//
//                                }
//                            });
//
//
//                            Button btn_showDialog = (Button) findViewById(R.id.btn_showDialog) ;
//                            btn_showDialog.setOnClickListener(new Button.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//
//                                    Toast.makeText(ArNpc1.this, "123123", Toast.LENGTH_SHORT).show();
////                                    textView.setText("www");
//
//                                    DialogTempProd dialogTempProd = new DialogTempProd(ArNpc1.this);
//                                    dialogTempProd.callDialog();
//                                }
//                            });
//
//
//                            Button btn_imgSlider = (Button) findViewById(R.id.btn_imgSlider) ;
//                            btn_imgSlider.setOnClickListener(new Button.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    startActivityC(RecoActivity.class);
//                                }
//                            });
//
//
//                        })
//                .exceptionally(
//                        (throwable) -> {
//                            throw new AssertionError("Could not load info card view.", throwable);
//                        });
//
//        //////////////////////////// 시작하면 바로뜨던 말풍선 ////////////////////////////


//        //////////////////////////// 탭을 하면!!!!!!!!!!!!! ////////////////////////////
//        arFragment.setOnTapArPlaneListener(
//                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//
//
//
//                    if (videoRenderable == null) {
//                        return;
//                    }
//
//                    // anchor -> anchornode -> model(videoRenderable) -> videonode , tigerTitleNode
//
//
//                    // Create the Anchor.
//                    Anchor anchor = hitResult.createAnchor();
//                    AnchorNode anchorNode = new AnchorNode(anchor);
//                    anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//
//                    // 이게 움직이는 모델
//                    //Create the transformable model and add it to the anchor.
//                    TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
//                    model.setParent(anchorNode);
//                    model.setRenderable(videoRenderable);
//                    model.select();
//
//
//                    // !!!!!!!!!!!!!!!!!! video 노드 !!!!!!!!!!!!!!!!!!!
//                    // Create a node to render the video and add it to the anchor.
//                    Node videoNode = new Node();
//                    videoNode.setParent(anchorNode);
//
//                    // Set the scale of the node so that the aspect ratio of the video is correct.
//                    float videoWidth = mediaPlayer.getVideoWidth();
//                    float videoHeight = mediaPlayer.getVideoHeight();
//                    videoNode.setLocalScale(
//                            new Vector3(VIDEO_HEIGHT_METERS * (videoWidth / videoHeight), VIDEO_HEIGHT_METERS, 1.0f));
//
//                    // Start playing the video when the first node is placed.
//                    if (!mediaPlayer.isPlaying()) {
//                        mediaPlayer.start();
//
//                        // Wait to set the renderable until the first frame of the  video becomes available.
//                        // This prevents the renderable from briefly appearing as a black quad before the video
//                        // plays.
//                        texture
//                                .getSurfaceTexture()
//                                .setOnFrameAvailableListener(
//                                        (SurfaceTexture surfaceTexture) -> {
//                                            //이거끄고
//                                            //videoNode.setRenderable(videoRenderable);
//
//                                            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
//                                        });
//                    } else {
//                        //이거끄고
//                        //videoNode.setRenderable(videoRenderable);
//
//                    }
//                    // !!!!!!!!!!!!!!!!!! video 노드 !!!!!!!!!!!!!!!!!!!
//
//
//                    // !!!!!!!!!!!!!!!!!! tigerTitleNode 노드!!!!!!!!!!!!!!!!!!!
//                    Node tigerTitleNode = new Node();
//
//                    //이거끄고
//                    //tigerTitleNode.setParent(videoRenderable);
//                    tigerTitleNode.setParent(model);
//                    tigerTitleNode.setEnabled(false);
//                    tigerTitleNode.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));
//
//                    ViewRenderable.builder()
//                            .setView(this, R.layout.tiger_card_view)
//                            .build()
//                            .thenAccept(
//                                    (renderable) -> {
//
//                                        tigerTitleNode.setRenderable(renderable);
//                                        tigerTitleNode.setEnabled(true);
//
//                                        TextView textView = (TextView) renderable.getView();
//                                        textView.setText("호랑이기운이 솟아나요");
//
//                                    })
//                            .exceptionally(
//                                    (throwable) -> {
//                                        throw new AssertionError("Could not load card view.", throwable);
//                                    }
//                            );
//
//                    // !!!!!!!!!!!!!!!!!! tigerTitleNode 노드!!!!!!!!!!!!!!!!!!!
//
//
//
//
//                });
//        //////////////////////////// 탭을 하면!!!!!!!!!!!!! ////////////////////////////


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

    // 객체 인텐트 전달 함수
    public void startActivityObject(Class c, String name, Object object) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(name, (Parcelable) object);
        startActivity(intent);
    }

    // 백스택 지우고 새로 만들어 전달
    public void startActivityNewTask(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }


    private void changePlane() {
        Texture.Sampler sampler = Texture.Sampler.builder()
                .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                .build();

        // Build texture with sampler
        CompletableFuture<Texture> trigrid = Texture.builder()
                /*************
                 //R.drawable.arrow_t1 -> 투명 배경으로 변경하시면 됩니다.*/
                .setSource(this, R.drawable.opacity)
                .setSampler(sampler).build();

        // Set plane texture
        this.arFragment.getArSceneView()
                .getPlaneRenderer()
                .getMaterial()
                .thenAcceptBoth(trigrid, (material, texture) -> {
                    material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture);
                });
    }


}
