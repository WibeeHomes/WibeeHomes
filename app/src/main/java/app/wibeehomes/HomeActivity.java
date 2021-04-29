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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.wibeehomes.Map.KakaoMapAPI;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout publicHousingLinearLayout, searchBarLinearLayout, conditionLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        GPSListener gpsListener = new GPSListener();
        UserLoc.LocBy_gps(this,gpsListener);
        KakaoMapAPI kakaoMapAPI = new KakaoMapAPI(this, (ViewGroup) findViewById(R.id.map_view),UserLoc.getUserPlace().get_placeX(),UserLoc.getUserPlace().get_placeY() );

        publicHousingLinearLayout = findViewById(R.id.home_ll_menu2);
        searchBarLinearLayout = findViewById(R.id.home_ll_search_bar);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 서치바 다이얼로그 연결
                        searchBarLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                });
            }
        }).start();

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

                            }
                        });
                    }
                });
            }
        }).start();
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