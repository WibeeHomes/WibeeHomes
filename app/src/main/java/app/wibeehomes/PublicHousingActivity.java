package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import app.wibeehomes.adapter.PublicHousingAdapter;
import app.wibeehomes.adapter.PublicHousingData;

public class PublicHousingActivity extends AppCompatActivity {

    /* UI 관련 컴포넌트 */
    private LinearLayout homeLinearLayout;
    private ImageButton searchButton;

    /* Spinner 관련 컴포넌트 */
    private Spinner localSpinner, categorySpinner;
    private ArrayAdapter localAdapter, categoryAdapter;
    private int localNum = 0, categoryNum = 0;

    /* RecyclerView 관련 컴포넌트 */
    private RecyclerView housingResultRecyclerView;
    private ArrayList<PublicHousingData> publicHousingData;
    private LinearLayoutManager layoutManager;
    private PublicHousingAdapter adapter;
    private String selectedUri = "";

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

        localAdapter = ArrayAdapter.createFromResource(this, R.array.local_list, R.layout.item_spinner);
        localAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        localSpinner.setAdapter(localAdapter);

        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.public_housing_list, R.layout.item_spinner);
        categoryAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        LocalSpinnerAction();
        CategorySpinnerAction();

        //------------------------------------------------------------------------------------------
        /* RecyclerView 연결 */
        publicHousingData = new ArrayList<PublicHousingData>();
        // 더미데이터
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 1", "https://www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 2", "https://www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 3", "https://www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 4", "https://www.google.com", "2021.04.27"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 5", "https://www.google.com", "2021.04.28"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 6", "https://www.google.com", "2021.04.28"));
        publicHousingData.add(new PublicHousingData("국민임대","공공주택사업 제목 7", "https://www.naver.com", "2021.04.28"));

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
                Intent outIntent = new Intent(Intent.ACTION_VIEW);

                selectedUri = publicHousingData.get(pos).getUrl();
                outIntent.setData(Uri.parse(selectedUri));
                startActivity(outIntent);
            }
        });

        //------------------------------------------------------------------------------------------
        /* 검색 버튼 */
//        searchButton = findViewById(R.id.public_housing_imgbtn_search);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    // 지역 Spinner 선택/비선택 액션 메소드
    private void LocalSpinnerAction() {
        localSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                localNum = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                localNum = 0;
            }
        });
    }

    // 카테고리 Spinner 선택/비선택 액션 메소드
    private void CategorySpinnerAction() {
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryNum = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categoryNum = 0;
                Log.d("카테고리 선택X 결과 : ", Integer.toString(categoryNum));
            }
        });
    }

    // 지역, 카테고리 선택 후 검색 버튼 액션 메소드
    // DB 통신 추가
    private void SearchButtonAction() {
        Log.d("지역 번호", Integer.toString(localNum));
        Log.d("카테고리 번호", Integer.toString(categoryNum));



        adapter.notifyDataSetChanged();
    }
}