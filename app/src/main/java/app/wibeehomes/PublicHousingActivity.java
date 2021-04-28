package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import app.wibeehomes.adapter.PublicHousingAdapter;
import app.wibeehomes.adapter.PublicHousingData;

public class PublicHousingActivity extends AppCompatActivity {

    /* UI 관련 컴포넌트 */
    private LinearLayout homeLinearLayout;

    /* Spinner 관련 컴포넌트 */
    private Spinner localSpinner, categorySpinner;
    private ArrayAdapter localAdapter, categoryAdapter;
    private int localNum = 0, categoryNum = 0;

    /* RecyclerView 관련 컴포넌트 */
    private RecyclerView housingResultRecyclerView;
    private ArrayList publicHousingData;
    private LinearLayoutManager layoutManager;
    private PublicHousingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_housing);

        homeLinearLayout = findViewById(R.id.public_housing_ll_menu1);


        // 메뉴전환 : 부동산 검색
        homeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicHousingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        //------------------------------------------------------------------------------------------
        /* Spinner 연결 */
        localSpinner = findViewById(R.id.public_housing_spinner_local);
        categorySpinner = findViewById(R.id.public_housing_spinner_category);

        localAdapter = ArrayAdapter.createFromResource(this, R.array.local_list, R.layout.support_simple_spinner_dropdown_item);
        localAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        localSpinner.setAdapter(localAdapter);

        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.public_housing_list, R.layout.support_simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        LocalSpinnerAction();
        CategorySpinnerAction();

        //------------------------------------------------------------------------------------------
        /* RecyclerView 연결 */
        publicHousingData = new ArrayList<PublicHousingData>();
        // 더미데이터
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 1", "www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 2", "www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 3", "www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 4", "www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 5", "www.google.com", "2021.04.28"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 6", "www.google.com", "2021.04.28"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 7", "www.google.com", "2021.04.28"));

        housingResultRecyclerView = findViewById(R.id.public_housing_rc);
        layoutManager = new LinearLayoutManager(getApplication());
        housingResultRecyclerView.setLayoutManager(layoutManager);
        housingResultRecyclerView.setHasFixedSize(true);

        adapter = new PublicHousingAdapter(publicHousingData);
        housingResultRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setPublicHousingClickListener(new PublicHousingAdapter.OnPublicHousingClickListener() {
            @Override
            public void onPublicHousingItemClick(View v, int pos) {
                // URL로 화면 전환
            }
        });


    }

    // 지역 Spinner 선택/비선택 액션 메소드
    // API 통신 추가
    private void LocalSpinnerAction() {
        localSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                localNum = i;
                Log.d("지역 스피너 결과", Integer.toString(i));
                Log.d("지역 선택 결과", Integer.toString(localNum));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                localNum = 0;
                Log.d("지역 선택X 결과", Integer.toString(localNum));
            }
        });
    }

    // 카테고리 Spinner 선택/비선택 액션 메소드
    // API 통신 추가
    private void CategorySpinnerAction() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryNum = i;
                Log.d("카테고리 스피너너 결과", Integer.toString(i));
                Log.d("카테고리 선택 결과", Integer.toString(categoryNum));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categoryNum = 0;
                Log.d("카테고리 선택X 결과 : ", Integer.toString(categoryNum));
            }
        });
    }
}