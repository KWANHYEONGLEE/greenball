package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.examples.java.augmentedimage.R;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;

import com.google.ar.sceneform.ux.ArFragment;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.concurrent.CompletableFuture;

public class TmapActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback, SensorEventListener {


    private String TAG = "TMAP";

    LinearLayout LinearLayout_tmap;//tmap과 연결시킬 Layout
    Button button_find;
    double curLatitude, curLongitude;// 현재 위치의 위도 및 경도
    TMapMarkerItem TmapMarker_Cur, TmapMarker_Destination;
    TMapPoint tMapPoint_Cur, tMapPoint_Destination;
    TMapView tMapView;

    //화면에 현재 내 위치 바꿔주기 위한 handler
    Handler handler = new Handler();

    //나침반
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];

    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    int azimuthinDegree;


    //intent로 넘어오는 값들
    float DestinationLatitude;
    float DestinationLongitude;


    /////////////////////////////////////////////////////////AR용
    /////////////////////////////////////////////////////////AR용

    //private static final String TAG = ArNavigationActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;

    private float meter = -1.5f;
    private int userangle = 0;
    private int xxx;

    private Node infoCard;

    /////////////////////////////////////////////////////////AR용
    /////////////////////////////////////////////////////////AR용

    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap);

        //TODO
        //intent로 값 받아오기
        //Intent intent=getIntent();
        //DestinationLatitude=intent.getFloatExtra("latitude",0.0f);
        //DestinationLongitude=intent.getFloatExtra("longitude",0.0f);
        level = 0;


        ////////////////////////
        infoCard = new Node();

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        arFragment.getPlaneDiscoveryController().hide();
        changePlane();

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        ////////////////////////

        // 처음 그냥 생기는 것 ( 각도 이런거 안맞음 )
        /////////////////////////////////////////////////////////AR용
        /////////////////////////////////////////////////////////AR용
        /////////////////////////////////////////////////////////AR용


        // 노드 생성후 정보확인


        infoCard.setParent(arFragment.getArSceneView().getScene());
        // infoCard.setEnabled(true);

        double angle = Math.PI * (90 - userangle) / 180.0;
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);

        //1 번이랑 3번을 바꿔야지 //1번이 -가 왼쪽 , 3번은 -가 앞쪽 // 2번은 바닥쪽으로 가도록 한것

        infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle), -1.5f, (float) (meter * sinAngle)));
        // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)

        //infoCard.setLocalPosition(new Vector3(1.0f, -1.5f, -2.5f));

        //Vector3 aaa = new Vector3(0.0f,0.0f,1.0f);
        Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
        Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), userangle); // rotate Y axis 90 degrees
        infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));
        userangle = 0;

        //viewArrow();

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


                            Camera camera = arFragment.getArSceneView().getScene().getCamera();
                            xxx =   (int) camera.getLocalRotation().y;

                        })
                .exceptionally(
                        (throwable) -> {
                            throw new AssertionError("Could not load info card view.", throwable);
                        });
        /////////////////////////////////////////////////////////AR용
        /////////////////////////////////////////////////////////AR용
        /////////////////////////////////////////////////////////AR용


        //xml 과 연결하기
        LinearLayout_tmap = findViewById(R.id.LinearLayout_tmap);
        button_find = findViewById(R.id.button_find);

        button_find.setText(Integer.toString(level));
        //버튼 이벤트
        button_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TmapActivity.this, "버튼 눌림", Toast.LENGTH_SHORT).show();
                //findPath();
//                level = level + 30;
//                button_find.setText(Integer.toString(level));

                /////////////////////////////////////// 테스트용 ///////////////////////////////////////
                // 노드 생성후 정보확인


                infoCard.setRenderable(null);

                arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

                Camera camera = arFragment.getArSceneView().getScene().getCamera();

                //arFragment.getArSceneView().getScene().removeChild(infoCard);
                //infoCard.setLocalPosition(null);
                //Camera camera = arFragment.getArSceneView().getScene().getCamera();
                //infoCard.setLocalPosition(new Vector3(camera.getLocalPosition().x, camera.getLocalPosition().y, camera.getLocalPosition().z));
                //infoCard.worldToLocalDirection(new Vector3(camera.getLocalRotation().x, camera.getLocalRotation().y, camera.getLocalRotation().z));
                //infoCard.world
                //infoCard.setParent(camera);
                //infoCard.setWorldPosition(new Vector3(camera.getWorldPosition().x, camera.getWorldPosition().y, camera.getWorldPosition().z));
                //infoCard.setWorldRotation(new Quaternion(camera.getLocalRotation().x, camera.getLocalRotation().y, camera.getLocalRotation().z, camera.getLocalRotation().w));
                //infoCard.setEnabled(true);



                userangle =   (int) camera.getLocalRotation().y;


                Toast.makeText(TmapActivity.this, userangle, Toast.LENGTH_SHORT).show();

                double angle = Math.PI * (90 - userangle) / 180.0;
                double sinAngle = Math.sin(angle);
                double cosAngle = Math.cos(angle);


                //1 번이랑 3번을 바꿔야지 //1번이 -가 왼쪽 , 3번은 -가 앞쪽 // 2번은 바닥쪽으로 가도록 한것 //

                //infoCard.setLocalPosition(new Vector3((float)  camera.getLocalPosition().x, camera.getLocalPosition().y, (float) -1.5f + camera.getLocalPosition().z));

                infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle) + camera.getLocalPosition().x, -1.5f + camera.getLocalPosition().y, (float) (meter * sinAngle) + camera.getLocalPosition().z));
                //infoCard.setLocalPosition(new Vector3((float) (0) , -1.5f + camera.getLocalPosition().y, (float) (meter) + camera.getLocalPosition().z));

                //infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle) , -1.5f, (float) (meter * sinAngle) )); //원래있던것
                // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)


                Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
                Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), userangle); // rotate Y axis 90 degrees
                infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));


                //viewArrow();

                ViewRenderable.builder()
                        .setView(getApplicationContext(), R.layout.tiger_card_view2)
                        .build()
                        .thenAccept(
                                (renderable) -> {

                                    arFragment.getPlaneDiscoveryController().hide();
                                    changePlane();

                                    // 이 node를 생성
                                    infoCard.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    //textView.setText("Value from setImage");


                                })
                        .exceptionally(
                                (throwable) -> {
                                    throw new AssertionError("Could not load info card view.", throwable);
                                });


                /////////////////////////////////////// 테스트용 ///////////////////////////////////////

            }
        });


        //나침반 센서 선언하기
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //linearLayout에 tmap 생성하기
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1594bcedc86a49e58421f38d190b69fa");
        LinearLayout_tmap.addView(tMapView);

        GpsTracker gpsTracker = new GpsTracker(TmapActivity.this);
        curLatitude = gpsTracker.getLatitude();//현재위치 위도
        curLongitude = gpsTracker.getLongitude();//현재위치 경도


        //처음 tmap 위치 잡아주기(현재 내 위치로)
        tMapView.setCenterPoint(curLongitude, curLatitude);//tmap 시작위치
        tMapView.setCompassMode(true); //나침반 기능
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setSightVisible(true);
        tMapView.setZoomLevel(17);//줌 레벨

        // gps 세팅
        TMapGpsManager gps = new TMapGpsManager(TmapActivity.this);
        gps.setMinTime(20000000);
        gps.setMinDistance(1.0f);
        gps.setProvider(gps.GPS_PROVIDER);
        gps.OpenGps();

        //현재 내 위치에 마커 지정하기
        tMapPoint_Cur = new TMapPoint(curLatitude, curLongitude);
        TmapMarker_Cur = new TMapMarkerItem();
        TmapMarker_Cur.setTMapPoint(tMapPoint_Cur);
        TmapMarker_Cur.setVisible(TMapMarkerItem.VISIBLE);
        TmapMarker_Cur.setPosition(1, 1);
        TmapMarker_Cur.setCanShowCallout(true);
        TmapMarker_Cur.setCalloutTitle("내 위치");
        tMapView.addMarkerItem("cur", TmapMarker_Cur);

        //임시 목적지
        tMapPoint_Destination = new TMapPoint(37.477128, 126.965875);
        TmapMarker_Destination = new TMapMarkerItem();
        TmapMarker_Destination.setTMapPoint(tMapPoint_Destination);
        TmapMarker_Destination.setVisible(TMapMarkerItem.VISIBLE);
        TmapMarker_Destination.setPosition(1, 1);
        TmapMarker_Destination.setCanShowCallout(true);
        TmapMarker_Destination.setCalloutTitle("목적지");

        //TODO
        test(curLatitude, curLongitude, 37.484460, 126.972891);


    }

    //api를 이용하여서 도보 경로 값 받아오기
    public void findPath() {
        TMapData tMapData = new TMapData();//tmap api에서 길찾기 정보 가져오기
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPoint_Cur, tMapPoint_Destination, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {//길찾기 경로보여주기
                tMapPolyLine.setLineColor(Color.RED);//line을 빨간색으로
                tMapPolyLine.setLineWidth(5);//라인 두께
                print_distance(tMapPolyLine);//handler로 값을 보내서 화면에 보여줄 수 있게하기
            }
        });
        tMapData.findPathDataAllType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPoint_Cur, tMapPoint_Destination, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document document) {
                Element root = document.getDocumentElement();
                NodeList nodeListPlacemark = root.getElementsByTagName("Placemark");

                for (int i = 0; i < nodeListPlacemark.getLength(); i++) {
                    NodeList nodeListPlacemarkItem = nodeListPlacemark.item(i).getChildNodes();
                    for (int j = 0; j < nodeListPlacemarkItem.getLength(); j++) {
                        Log.e(TAG, nodeListPlacemarkItem.item(j).getTextContent().trim());
                        if (nodeListPlacemarkItem.item(j).getNodeName().equals("description")) {
//                            Log.e(TAG, nodeListPlacemarkItem.item(j).getTextContent().trim() );
                        }
                    }
                }

            }
        });
        test(curLatitude, curLongitude, 37.477128, 126.965875);
    }

    //도보 경로 받아온 값을 토대로 지도에 표시
    public void print_distance(final TMapPolyLine tMapPolyLine) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                tMapView.addTMapPath(tMapPolyLine);//tmap에 라인 그려주기
            }
        });
    }

    //목적지까지 북쪽을 기준으로 몇도 차이 나는지 확인
    public int test(double p1_latitude, double p1_longitude, double p2_latitude, double p2_longitude) {
        double Cur_Lat_radian = p1_latitude * (3.141592 / 180);
        double Cur_Lon_radian = p1_longitude * (3.141592 / 180);

        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다
        double Dest_Lat_radian = p2_latitude * (3.141592 / 180);
        double Dest_Lon_radian = p2_longitude * (3.141592 / 180);

        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian) * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));
        double true_bearing = 0;

        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0) {
            true_bearing = radian_bearing * (180 / 3.141592);
            true_bearing = 360 - true_bearing;

        } else {
            true_bearing = radian_bearing * (180 / 3.141592);
        }
        Log.e(TAG, "result: " + true_bearing);

        return (int) true_bearing + level;

    }


    //위치가 바뀔 때마다 나침반을 이용하여 목적지까지 몇도 차이나는지 확인
    @Override
    public void onLocationChange(Location location) {
        Log.e(TAG, "체인지");
        Toast.makeText(this, "체인지", Toast.LENGTH_SHORT).show();
        //TODO
        int degree = test(curLatitude, curLongitude, 37.484460, 126.972891);

        if (azimuthinDegree > degree) {
            int resultDegree = azimuthinDegree - degree;
            Log.e(TAG, "resultDegree: " + resultDegree);


            userangle = 0;


            /////////////////////////////////////// 테스트용 ///////////////////////////////////////
            // 노드 생성후 정보확인
            Toast.makeText(TmapActivity.this, "위치바꾸기버튼눌림", Toast.LENGTH_SHORT).show();

            infoCard.setRenderable(null);

            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

            Camera camera = arFragment.getArSceneView().getScene().getCamera();

            //arFragment.getArSceneView().getScene().removeChild(infoCard);
            //infoCard.setLocalPosition(null);
            //Camera camera = arFragment.getArSceneView().getScene().getCamera();
            //infoCard.setLocalPosition(new Vector3(camera.getLocalPosition().x, camera.getLocalPosition().y, camera.getLocalPosition().z));
            //infoCard.worldToLocalDirection(new Vector3(camera.getLocalRotation().x, camera.getLocalRotation().y, camera.getLocalRotation().z));
            //infoCard.world
            //infoCard.setParent(camera);
            //infoCard.setWorldPosition(new Vector3(camera.getWorldPosition().x, camera.getWorldPosition().y, camera.getWorldPosition().z));
            //infoCard.setWorldRotation(new Quaternion(camera.getLocalRotation().x, camera.getLocalRotation().y, camera.getLocalRotation().z, camera.getLocalRotation().w));
            //infoCard.setEnabled(true);


            double angle = Math.PI * (90 - userangle) / 180.0;
            double sinAngle = Math.sin(angle);
            double cosAngle = Math.cos(angle);


            //1 번이랑 3번을 바꿔야지 //1번이 -가 왼쪽 , 3번은 -가 앞쪽 // 2번은 바닥쪽으로 가도록 한것 //

            //infoCard.setLocalPosition(new Vector3((float)  camera.getLocalPosition().x, camera.getLocalPosition().y, (float)  -1.5f + camera.getLocalPosition().z));

            infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle) + camera.getLocalPosition().x, -1.5f + camera.getLocalPosition().y, (float) (meter * sinAngle) + camera.getLocalPosition().z));
            //infoCard.setLocalPosition(new Vector3((float) (0) , -1.5f + camera.getLocalPosition().y, (float) (meter) + camera.getLocalPosition().z));

            //infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle) , -1.5f, (float) (meter * sinAngle) )); //원래있던것
            // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)


            Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
            Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), userangle); // rotate Y axis 90 degrees
            infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));


            //viewArrow();

            ViewRenderable.builder()
                    .setView(getApplicationContext(), R.layout.tiger_card_view2)
                    .build()
                    .thenAccept(
                            (renderable) -> {

                                arFragment.getPlaneDiscoveryController().hide();
                                changePlane();

                                // 이 node를 생성
                                infoCard.setRenderable(renderable);
                                TextView textView = (TextView) renderable.getView();
                                //textView.setText("Value from setImage");


                            })
                    .exceptionally(
                            (throwable) -> {
                                throw new AssertionError("Could not load info card view.", throwable);
                            });


            /////////////////////////////////////// 테스트용 ///////////////////////////////////////


            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용


//            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
//
//            if (!checkIsSupportedDeviceOrFinish(this)) {
//                return;
//            }
//
//            // 노드 생성후 정보확인
//
//            infoCard.setParent(arFragment.getArSceneView().getScene());
//            infoCard.setEnabled(true);
//
//
//            double angle = Math.PI * (90 - userangle) / 180.0;
//            double sinAngle = Math.sin(angle);
//            double cosAngle = Math.cos(angle);
//
//            //1 번이랑 3번을 바꿔야지 //1번이 -가 왼쪽 , 3번은 -가 앞쪽 // 2번은 바닥쪽으로 가도록 한것
//            infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle), -1.5f, (float) (meter * sinAngle)));
//            // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)
//
//            //infoCard.setLocalPosition(new Vector3(1.0f, -1.5f, -2.5f));
//
//            //Vector3 aaa = new Vector3(0.0f,0.0f,1.0f);
//            Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
//            Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), userangle); // rotate Y axis 90 degrees
//            infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));
//
//            viewArrow();
            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용


        } else {
            int resultDegree = degree - azimuthinDegree;
            Log.e(TAG, "resultDegree: " + resultDegree);


            userangle = resultDegree;

            /////////////////////////////////////// 테스트용 ///////////////////////////////////////
            // 노드 생성후 정보확인
            Toast.makeText(TmapActivity.this, "위치바꾸기버튼눌림", Toast.LENGTH_SHORT).show();

            infoCard.setRenderable(null);

            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

            Camera camera = arFragment.getArSceneView().getScene().getCamera();

            double angle = Math.PI * (90 - userangle) / 180.0;
            double sinAngle = Math.sin(angle);
            double cosAngle = Math.cos(angle);


            //1 번이랑 3번을 바꿔야지 //1번이 -가 왼쪽 , 3번은 -가 앞쪽 // 2번은 바닥쪽으로 가도록 한것 //

            infoCard.setLocalPosition(new Vector3((float)  camera.getLocalPosition().x, camera.getLocalPosition().y, (float) camera.getLocalPosition().z));


            //infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle) + camera.getLocalPosition().x, -1.5f + camera.getLocalPosition().y, (float) (meter * sinAngle) + camera.getLocalPosition().z));
            //infoCard.setLocalPosition(new Vector3((float) (0), -1.5f + camera.getLocalPosition().y, (float) (meter) + camera.getLocalPosition().z));

            //infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle) , -1.5f, (float) (meter * sinAngle) )); //원래있던것
            // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)


            Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
            Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), userangle); // rotate Y axis 90 degrees
            infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));


            //viewArrow();

            ViewRenderable.builder()
                    .setView(getApplicationContext(), R.layout.tiger_card_view2)
                    .build()
                    .thenAccept(
                            (renderable) -> {

                                arFragment.getPlaneDiscoveryController().hide();
                                changePlane();

                                // 이 node를 생성
                                infoCard.setRenderable(renderable);
                                TextView textView = (TextView) renderable.getView();
                                //textView.setText("Value from setImage");


                            })
                    .exceptionally(
                            (throwable) -> {
                                throw new AssertionError("Could not load info card view.", throwable);
                            });


            /////////////////////////////////////// 테스트용 ///////////////////////////////////////

            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용

//            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
//
//            if (!checkIsSupportedDeviceOrFinish(this)) {
//                return;
//            }
//
//            // 노드 생성후 정보확인
//
//            infoCard.setParent(arFragment.getArSceneView().getScene());
//            infoCard.setEnabled(true);
//
//
//            double angle = Math.PI * (90 - userangle) / 180.0;
//            double sinAngle = Math.sin(angle);
//            double cosAngle = Math.cos(angle);
//
//            //1 번이랑 3번을 바꿔야지 //1번이 -가 왼쪽 , 3번은 -가 앞쪽 // 2번은 바닥쪽으로 가도록 한것
//            infoCard.setLocalPosition(new Vector3((float) (-1 * meter * cosAngle), -1.5f, (float) (meter * sinAngle)));
//            // v:x오른쪽왼쪽(+-) //  v1:z위아래로(+-) // v2: 앞뒤로(+-)
//
//            //infoCard.setLocalPosition(new Vector3(1.0f, -1.5f, -2.5f));
//
//            //Vector3 aaa = new Vector3(0.0f,0.0f,1.0f);
//            Quaternion rotation1 = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), 90); // rotate X axis 90 degrees
//            Quaternion rotation2 = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), userangle); // rotate Y axis 90 degrees
//            infoCard.setLocalRotation(Quaternion.multiply(rotation1, rotation2));
//
//            viewArrow();
            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용
            /////////////////////////////////////////////////////////AR용


        }

    }

    //센서가 변할 때 마다 나침반 수치가 달라짐
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {

            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {

            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {

            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            azimuthinDegree = (int) (Math.toDegrees(SensorManager.getOrientation(mR, mOrientation)[0]) + 360) % 360;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        //나침반 기능 켜기
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //나침반 기능 끄기
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    /////////////////////////////////////////////////////////AR용
    /////////////////////////////////////////////////////////AR용
    /////////////////////////////////////////////////////////AR용
    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private void changePlane() {
        Texture.Sampler sampler = Texture.Sampler.builder()
                .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                .build();

        // Build texture with sampler
        CompletableFuture<Texture> trigrid = Texture.builder()

                // 투명배경으로 변환
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


    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            //Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
    /////////////////////////////////////////////////////////AR용
    /////////////////////////////////////////////////////////AR용
    /////////////////////////////////////////////////////////AR용

    public void viewArrow() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                ViewRenderable.builder()
                        .setView(getApplicationContext(), R.layout.tiger_card_view2)
                        .build()
                        .thenAccept(
                                (renderable) -> {


                                    // 이 node를 생성
                                    infoCard.setRenderable(renderable);
                                    TextView textView = (TextView) renderable.getView();
                                    //textView.setText("Value from setImage");


                                })
                        .exceptionally(
                                (throwable) -> {
                                    throw new AssertionError("Could not load info card view.", throwable);
                                });
            }
        });
    }

}