package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import app.wibeehomes.Map.KakaoMapAPI;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout publicHousingLinearLayout, conditionLinearLayout;
    private TextView conditionTextView;
    private String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private KakaoMapAPI kakaoMapAPI = null;
    private ArrayList<ResidentialFacilities> residentialFacilities = new ArrayList<ResidentialFacilities>();

    // 조건
    int bigLocal, smallLocal;
    RENTTYPE rentType;

    private Place selectedPlace;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d("저장된 빅로컬", Integer.toString(PreferenceManager.getInt(this, "bigLocalNum")));
        Log.d("저장된 스몰로컬", Integer.toString(PreferenceManager.getInt(this, "smallLocalNum")));
        Log.d("저장상태", Boolean.toString(PreferenceManager.getBoolean(this, "isSetting")));

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
            // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
            Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
        } else {
            // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
            // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
        }

        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                PERMISSIONS_REQUEST_CODE);

        GPSListener gpsListener = new GPSListener();
        UserLoc.LocBy_gps(this,gpsListener);

        try {
            kakaoMapAPI = new KakaoMapAPI(this, (ViewGroup) findViewById(R.id.map_view),UserLoc.getUserPlace());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        kakaoMapAPI.getMapView().setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                clickHomeMarker();
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        });

        publicHousingLinearLayout = findViewById(R.id.home_ll_menu2);
        conditionLinearLayout = findViewById(R.id.home_ll_condition);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 메뉴전환 : 공공주택사업
                        publicHousingLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(HomeActivity.this, PublicHousingActivity.class);
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        }).start();

        conditionTextView = findViewById(R.id.home_tv_condition);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 조건 다이얼로그 연결
                        conditionLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(HomeActivity.this, HomeConditionActivity.class);
                                // 설정된 조건이 있는 경우 기존 조건 같이 넘기기
                                if (rentType != null) {
                                    intent.putExtra("big_local", bigLocal);
                                    intent.putExtra("small_local", smallLocal);
                                    intent.putExtra("rent_type", rentType);
                                }
                                //startActivity(intent);
                                startActivityForResult(intent,1);
                            }
                        });
                        // 집 리스트
                    }
                });
            }
        }).start();

        //------------------------------------------------------------------------------------------
        // 조건 입력
        Intent conIntent = getIntent();
        String conditionString = "";

        if (PreferenceManager.getBoolean(this, "isSetting") == true) {
            String bigLocalString = PreferenceManager.getString(this, "bigLocal");
            String smallLocalString = PreferenceManager.getString(this, "smallLocal");

            conditionString += bigLocalString + " · ";
            conditionString += smallLocalString + " · ";

            int rent = PreferenceManager.getInt(this, "rentType");
            if (rent == RENTTYPE.JEONSE.getValue()){
                conditionString += "전세";
            } else {
                conditionString += "월세";
            }
            conditionTextView.setText(conditionString);
        }
        // HomeDetailActivity로 가기
        // button -> 마커
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                residentialFacilities = (ArrayList<ResidentialFacilities>) data.getSerializableExtra("homeList"); // condition에서 받은 집 리스트
                try {
                    kakaoMapAPI.residentMaker(residentialFacilities);
                }catch(NullPointerException e){

                }
            }
        }
    }

    private void clickHomeMarker() {
        // 매개변수로 맵마커 객체 받고
        // 맵마커 객체 -> Place 객체 -> selectedPlace에 저장
        Intent detailIntent = new Intent(HomeActivity.this, HomeDetailActivity.class);
        detailIntent.putExtra("selectedHome", selectedPlace);
        startActivity(detailIntent);
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            UserLoc.getUserPlace().set_placeX(location.getLatitude());
            UserLoc.getUserPlace().set_placeY(location.getLongitude());
            kakaoMapAPI.deleteMyLocMarker();
            kakaoMapAPI.myLocMaker(UserLoc.getUserPlace());
        }
    }
    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    //GPS가 업데이트 되면서 장소가 변경되었을때 실행되는 리스너 함수


}