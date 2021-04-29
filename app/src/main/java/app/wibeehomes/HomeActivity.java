package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.daum.mf.map.api.MapView;

import app.wibeehomes.Map.KakaoMapAPI;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout publicHousingLinearLayout, searchBarLinearLayout, conditionLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        KakaoMapAPI kakaoMapAPI = new KakaoMapAPI(this, (ViewGroup) findViewById(R.id.map_view),37.55835087570901,126.99937335768597 );

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
}