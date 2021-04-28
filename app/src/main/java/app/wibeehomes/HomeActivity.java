package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;


public class HomeActivity extends AppCompatActivity {

    private LinearLayout publicHousingLinearLayout, searchBarLinearLayout, conditionLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        publicHousingLinearLayout = findViewById(R.id.home_ll_menu2);
        searchBarLinearLayout = findViewById(R.id.home_ll_search_bar);
        conditionLinearLayout = findViewById(R.id.home_ll_condition);

        // 메뉴전환 : 공공주택사업
        publicHousingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PublicHousingActivity.class);
                startActivity(intent);
            }
        });

        // 서치바 다이얼로그 연결
        searchBarLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 조건 다이얼로그 연결
        conditionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }




}