package com.google.ar.core.examples.java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ar.core.examples.java.augmentedimage.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

//해당 액티비티는 Tmap과 카메라 화면을 통한 길찾기 부분입니다.
public class Road extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback , SensorEventListener, SurfaceHolder.Callback{
    private String TAG="Road";//Log를 위한 TAG
    LinearLayout LinearLayout_tmap;//tmap과 연결시킬 Layout
    Button button_find;
    double curLatitude, curLongitude;// 현재 위치의 위도 및 경도
    TMapMarkerItem TmapMarker_Cur,TmapMarker_Destination;
    TMapPoint tMapPoint_Cur, tMapPoint_Destination;
    TMapView tMapView;

    //카메라 미리보기
    SurfaceView surfaceView;
    SurfaceHolder mCameraHolder;
    Camera mCamera;
    ImageView imageView;

    //화면에 현재 내 위치 바꿔주기 위한 handler
    Handler handler=new Handler();

    //나침반
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer=new float[3];
    private float[] mLastMagnetometer=new float[3];
    private boolean mLastAccelerometerSet=false;
    private boolean mLastMagnetometerSet=false;
    private float[] mR=new float[9];
    private float[] mOrientation=new float[3];
    int azimuthinDegree;

    //intent로 넘어 올 값들
    double DestinationLatitude;
    double DestinationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);

        //intent로 값 받아오기
        Intent intent=getIntent();
        DestinationLatitude=intent.getDoubleExtra("latitude",0.0f);
        DestinationLongitude=intent.getDoubleExtra("longitude",0.0f);

        //xml 과 연결하기
        LinearLayout_tmap=findViewById(R.id.LinearLayout_tmap);
        button_find=findViewById(R.id.button_find);
        surfaceView=findViewById(R.id.surfaceView);
        imageView=findViewById(R.id.imageView);

        //surfaceView에 cameraPreview해주기
        camerainit();

        //버튼 이벤트
        button_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"버튼 눌림");
//               findPath();

                int degree=test(curLatitude,curLongitude,DestinationLatitude,DestinationLongitude);
                int resultDegree;
                if(azimuthinDegree>degree){
                    resultDegree=360-(azimuthinDegree-degree);
                    Log.e(TAG,"resultDegree: "+resultDegree);
                }else{
                    resultDegree=degree-azimuthinDegree;
                    Log.e(TAG,"resultDegree: "+resultDegree);
                }

                imageView.setImageBitmap(rotateimage(BitmapFactory.decodeResource(getResources(),R.drawable.office1),resultDegree));

            }
        });

        //나침반 센서 선언하기
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer=mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //linearLayout에 tmap 생성하기
        tMapView=new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1594bcedc86a49e58421f38d190b69fa");
        LinearLayout_tmap.addView(tMapView);

        GpsTracker gpsTracker= new GpsTracker(Road.this);
        curLatitude=gpsTracker.getLatitude();//현재위치 위도
        curLongitude=gpsTracker.getLongitude();//현재위치 경도

        //처음 tmap 위치 잡아주기(현재 내 위치로)
        tMapView.setCenterPoint(curLongitude,curLatitude);//tmap 시작위치
        tMapView.setCompassMode(true); //나침반 기능
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setSightVisible(true);
        tMapView.setZoomLevel(17);//줌 레벨

        //gps로 위치찾기 (TMAP으로)
        TMapGpsManager gps=new TMapGpsManager(Road.this);
        gps.setMinTime(1000);
        gps.setMinDistance(5);
        gps.setProvider(gps.GPS_PROVIDER);
        gps.OpenGps();

        //현재 내 위치에 마커 지정하기
        tMapPoint_Cur=new TMapPoint(curLatitude,curLongitude);
        TmapMarker_Cur=new TMapMarkerItem();
        TmapMarker_Cur.setTMapPoint(tMapPoint_Cur);
        TmapMarker_Cur.setVisible(TMapMarkerItem.VISIBLE);
        TmapMarker_Cur.setPosition(1,1);
        TmapMarker_Cur.setCanShowCallout(true);
        TmapMarker_Cur.setCalloutTitle("내 위치");
        tMapView.addMarkerItem("cur",TmapMarker_Cur);

        //임시 목적지
        tMapPoint_Destination=new TMapPoint(37.477128,126.965875);
        TmapMarker_Destination=new TMapMarkerItem();
        TmapMarker_Destination.setTMapPoint(tMapPoint_Destination);
        TmapMarker_Destination.setVisible(TMapMarkerItem.VISIBLE);
        TmapMarker_Destination.setPosition(1,1);
        TmapMarker_Destination.setCanShowCallout(true);
        TmapMarker_Destination.setCalloutTitle("목적지");
    }

    //api를 이용하여서 도보 경로 값 받아오기
    public void findPath(){
        TMapData tMapData=new TMapData();//tmap api에서 길찾기 정보 가져오기
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPoint_Cur, tMapPoint_Destination , new TMapData.FindPathDataListenerCallback() {
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
                Element root= document.getDocumentElement();
                NodeList nodeListPlacemark=root.getElementsByTagName("Placemark");

                for(int i=0; i<nodeListPlacemark.getLength(); i++){
                    NodeList nodeListPlacemarkItem = nodeListPlacemark.item(i).getChildNodes();
                    for( int j=0; j<nodeListPlacemarkItem.getLength(); j++ ) {
                        Log.e(TAG, nodeListPlacemarkItem.item(j).getTextContent().trim() );
                        if( nodeListPlacemarkItem.item(j).getNodeName().equals("description") ) {
//                            Log.e(TAG, nodeListPlacemarkItem.item(j).getTextContent().trim() );
                        }
                    }
                }

            }
        });
        test(curLatitude,curLongitude,37.477128,126.965875);
    }

    //도보 경로 받아온 값을 토대로 지도에 표시
    public void print_distance(final TMapPolyLine tMapPolyLine){
        handler.post(new Runnable() {
            @Override
            public void run() {
                tMapView.addTMapPath(tMapPolyLine);//tmap에 라인 그려주기
            }
        });
    }

    //목적지까지 북쪽을 기준으로 몇도 차이 나는지 확인
    public int test(double p1_latitude, double p1_longitude, double p2_latitude, double p2_longitude){
        double Cur_Lat_radian = p1_latitude * (3.141592 / 180);
        double Cur_Lon_radian = p1_longitude * (3.141592 / 180);

        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다
        double Dest_Lat_radian = p2_latitude * (3.141592 / 180);
        double Dest_Lon_radian = p2_longitude * (3.141592 / 180);

        double radian_distance = 0;
        radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

        double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian) * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));
        double true_bearing = 0;

        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0)

        {
            true_bearing = radian_bearing * (180 / 3.141592);
            true_bearing = 360 - true_bearing;

        }
        else
        {
            true_bearing = radian_bearing * (180 / 3.141592);
        }

        return (int) true_bearing;

    }

    //위치가 바뀔 때마다 나침반을 이용하여 목적지까지 몇도 차이나는지 확인
    @Override
    public void onLocationChange(Location location) {
        //위치가 바뀌었을 때,
        Log.e(TAG,"위치 바뀜");
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        int degree=test(lat,lon,DestinationLatitude,DestinationLongitude);
        int resultDegree;
        if(azimuthinDegree>degree){
            resultDegree=360-(azimuthinDegree-degree);
            Log.e(TAG,"resultDegree: "+resultDegree);
        }else{
            resultDegree=degree-azimuthinDegree;
            Log.e(TAG,"resultDegree: "+resultDegree);
        }

        imageView.setImageBitmap(rotateimage(BitmapFactory.decodeResource(getResources(),R.drawable.office1),resultDegree));
    }

    //센서가 변할 때 마다 나침반 수치가 달라짐
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor==mAccelerometer){

            System.arraycopy(event.values,0,mLastAccelerometer,0,event.values.length);
            mLastAccelerometerSet=true;
        }else if(event.sensor==mMagnetometer){

            System.arraycopy(event.values,0,mLastMagnetometer,0,event.values.length);
            mLastMagnetometerSet=true;
        }
        if(mLastAccelerometerSet && mLastMagnetometerSet){

            SensorManager.getRotationMatrix(mR,null,mLastAccelerometer,mLastMagnetometer);
            azimuthinDegree = (int) (Math.toDegrees(SensorManager.getOrientation(mR,mOrientation)[0])+360)%360;

            int degree=test(curLatitude,curLongitude,DestinationLatitude,DestinationLongitude);
            if(azimuthinDegree>degree){
                int resultDegree=azimuthinDegree-degree;
                Log.e(TAG,"resultDegree: "+resultDegree);
            }else{
                int resultDegree=360-degree+azimuthinDegree;
                Log.e(TAG,"resultDegree: "+resultDegree);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //나침반 기능 켜기
    @Override
    protected void onResume() {
        super.onResume();
        //나침반 기능 켜기
        mSensorManager.registerListener(this,mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,mMagnetometer,SensorManager.SENSOR_DELAY_GAME);
    }

    //나침반 기능 끄기
    @Override
    protected void onPause() {
        super.onPause();
        //나침반 기능 끄기
        mSensorManager.unregisterListener(this,mAccelerometer);
        mSensorManager.unregisterListener(this,mMagnetometer);
    }

    //카메라 프리뷰
    private void camerainit(){
        mCamera=Camera.open();
        mCamera.setDisplayOrientation(90);

        //surfaceview setting
        mCameraHolder=surfaceView.getHolder();
        mCameraHolder.addCallback(this);
        mCameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            if(mCamera==null){
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mCameraHolder.getSurface()==null){
            return;
        }

        try{
            mCamera.stopPreview();
        }catch (Exception e){

        }
        Camera.Parameters parameters=mCamera.getParameters();
        List<String> focusModes=parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.setParameters(parameters);

        try{
            mCamera.setPreviewDisplay(mCameraHolder);
            mCamera.startPreview();
        }catch (Exception e){
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera=null;
        }
    }

    //bitmap회전
    public Bitmap rotateimage(Bitmap src, float degree){
        Matrix matrix=new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,true);
    }
}