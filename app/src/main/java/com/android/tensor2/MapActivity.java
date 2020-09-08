package com.android.tensor2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    // MapActivity
    // 어플 사용자의 위치정보를 불러와 지도에 위치 표시
    // 지도위에 Pin과 버섯이름 띄울수 있게 하기
    // 여기는 차차 개발해나가도록 하는 부분~

    // googlemap 실행시 필요 변수
    GoogleMap mGoogleMap = null;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_map);
        mapFragment.getMapAsync(this);

        // 현재 위치 나타내는 함수
        getLastLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // 버섯이 찍은 위치 Map에 표시 함수
        MushroomOnMap();

    }


    // 버섯이 찍은 위치 Map에 표시 함수
    private void MushroomOnMap(){
        if (mGoogleMap != null) {

            // 나중에 밑에 코드 수정
            // 이런식으로 표시된다는거 보여주기 위함함

            LatLng location = new LatLng(38.1195495,128.4568033);
            LatLng location2 = new LatLng(35.3361098,127.7046123);
            LatLng location3 = new LatLng(36.3424982,127.1883237);
            LatLng location4 = new LatLng(38.153749, 128.390037);
            LatLng location5 = new LatLng(36.934420, 128.544523);

            // Map에 위치 Pin 표시
            mGoogleMap.addMarker(new MarkerOptions().position(location).alpha(0.8f));
            mGoogleMap.addMarker(new MarkerOptions().position(location2).alpha(0.8f));
            mGoogleMap.addMarker(new MarkerOptions().position(location3).alpha(0.8f));
            mGoogleMap.addMarker(new MarkerOptions().position(location4).alpha(0.8f));
            mGoogleMap.addMarker(new MarkerOptions().position(location5).alpha(0.8f));
        }
    }


    // Map 위치관련 허용 변수
    final int REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION = 0;
    Location mLastLocation;
    private static final String TAG = "MapActivity";


    // Map에 위치 얻어오는 함수
    private void getLastLocation() {
        // 1. 위치 접근에 필요한 권한 검사 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MapActivity.this,            // MainActivity 액티비티의 객체 인스턴스를 나타냄
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},        // 요청할 권한 목록을 설정한 String 배열
                    REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION    // 사용자 정의 int 상수. 권한 요청 결과를 받을 때
            );
            return;
        }

        // 2. Task<Location> 객체 반환
        Task task = mFusedLocationClient.getLastLocation();

        // 3. Task가 성공적으로 완료 후 호출되는 OnSuccessListener 등록
        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // 4. 마지막으로 알려진 위치(location 객체)를 얻음.
                if (location != null) {
                    Log.e(TAG,"Location Successs");
                    mLastLocation = location;
                    updateUI(); //위치 변할시 update 함수수
               } else
                    Toast.makeText(getApplicationContext(),
                            "No location detected",
                            Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }


    // 위치 update 함수
    private void updateUI() {
        double latitude = 0.0;
        double longitude = 0.0;
        float precision = 0.0f;

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            precision = mLastLocation.getAccuracy();
        }
        LatLng curPoint = new LatLng(latitude, longitude);

        // 현재 위치 표시
        mGoogleMap.addMarker(new MarkerOptions().position(curPoint).alpha(0.8f).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_red_icon_20_20)));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint,7));
    }


    //위치 허용 물어보는 함수
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS_FOR_LAST_KNOWN_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT);
                }
                break;
            }
        }
    }
}
