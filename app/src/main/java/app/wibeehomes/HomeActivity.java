package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.wibeehomes.Map.KakaoMapAPI;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout publicHousingLinearLayout, conditionLinearLayout;
    private TextView conditionTextView;

    // 조건
    int bigLocal, smallLocal;
    RENTTYPE rentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GPSListener gpsListener = new GPSListener();
        UserLoc.LocBy_gps(this,gpsListener);
        
        try {
            KakaoMapAPI kakaoMapAPI = new KakaoMapAPI(this, (ViewGroup) findViewById(R.id.map_view),UserLoc.getUserPlace());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }).start();


        //------------------------------------------------------------------------------------------
        // 조건 입력
        Intent conIntent = getIntent();
        String conditionString = "";

        if (conIntent.getExtras() != null) {
            bigLocal = conIntent.getExtras().getInt("con_big_local");
            smallLocal = conIntent.getExtras().getInt("con_small_local");
            rentType = (RENTTYPE) conIntent.getSerializableExtra("con_rent_type");

            if (rentType == RENTTYPE.JEONSE){
                conditionString += "전세";
            } else {
                conditionString += "월세";
            }
            conditionTextView.setText(conditionString);
        }


    }


    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            //capture location data sent by current provider
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
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