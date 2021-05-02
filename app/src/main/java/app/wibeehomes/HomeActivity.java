package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import app.wibeehomes.PublicHousing.PublicHousingData;
import app.wibeehomes.adapter.HomeListAdapter;
import app.wibeehomes.adapter.PublicHousingAdapter;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout publicHousingLinearLayout, conditionLinearLayout;
    private TextView conditionTextView;
    private String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private KakaoMapAPI kakaoMapAPI = null;
    private ArrayList<ResidentialFacilities> residentialFacilities = new ArrayList<ResidentialFacilities>();

    // 조건
    int bigLocal, smallLocal;
    RENTTYPE rentType;

    private Place selectedPlace;
    private Button button;

    private Boolean available_worker_loan;

    private RecyclerView homeListRecyclerView;
    private LinearLayoutManager layoutManager;
    private HomeListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        available_worker_loan = getIntent().getBooleanExtra("available_worker_loan", true);
        Log.d("직장인 대출 가능 여부-Home : ", Boolean.toString(available_worker_loan));

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

        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);


        publicHousingLinearLayout = findViewById(R.id.home_ll_menu2);
        conditionLinearLayout = findViewById(R.id.home_ll_condition);
        homeListRecyclerView = findViewById(R.id.home_rv_list);

        layoutManager = new LinearLayoutManager(getApplication());
        homeListRecyclerView.setLayoutManager(layoutManager);
        homeListRecyclerView.setHasFixedSize(true);

        adapter = new HomeListAdapter(residentialFacilities);
        homeListRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setHomeListClickListener(new HomeListAdapter.OnHomeListClickListener() {
            @Override
            public void onHomeListItemClick(View v, int pos) {
                // 집 선택 시, HomeDetailActivity
                Intent homeDetailIntent = new Intent(HomeActivity.this, HomeDetailActivity.class);
                homeDetailIntent.putExtra("selectedHome", residentialFacilities.get(pos));
                startActivity(homeDetailIntent);
            }
        });


        // 메뉴전환 : 공공주택사업
        publicHousingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PublicHousingActivity.class);
                startActivity(intent);
            }
        });


        conditionTextView = findViewById(R.id.home_tv_condition);
        conditionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeConditionActivity.class);
                startActivityForResult(intent,1);
                //startActivity(intent);
            }
        });

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
            if (rent == RENTTYPE.JEONSE.getValue()) {
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
                System.out.println("리스트 길이"+residentialFacilities.size());

                adapter = new HomeListAdapter(residentialFacilities);
                homeListRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        // HomeDetailActivity로 가기
        // button -> 마커
    }


}