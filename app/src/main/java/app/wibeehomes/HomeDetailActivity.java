package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.RestrictionEntry;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import app.wibeehomes.Map.KakaoMapAPI;
import app.wibeehomes.R;
import app.wibeehomes.dialog.SearchDialog;
import app.wibeehomes.traffic.CalRoute;
import app.wibeehomes.traffic.ExtendNode;

public class HomeDetailActivity extends AppCompatActivity {

    private boolean check1=false; //bus 마커 체크
    private boolean check2=false; //subway 마커 체크
    private boolean check3=false; //convenience 마커 체크
    private boolean check4=false; //mart 마커 체크

    private KakaoMapAPI kakaoMapAPIDetail =null;
    Button btn_bus,btn_subway,btn_convenience_store,btn_cart;

    private TextView detailCategory, detailYear, detailArea, detailMoney, detailAddress, detailMoneyTitle,
                        detailDistanceFromCompany, detailTimeFromCompany,
                        loan1MoneyTextView, loan2MoneyTextView, loan3MoneyTextView; // 1. 전세자금  2. 직장인 3.  비상금
    private LinearLayout loan1LinearLayout, loan2LinearLayout, loan3LinearLayout, loanNoneLinearLayout;
    private Button loanConditionButton, workSettingButton;
    private SearchDialog dialog;

    private TextView loan1Money_1_TextView, loan1Money_2_TextView, loan1Money_3_TextView, loan1Money_4_TextView,
                    loan2Money_1_TextView, loan2Money_2_TextView,
                    loan3Money_1_TextView, loan3Money_2_TextView, loan3Money_3_TextView, loan3Money_4_TextView,
                    loanCondition_TextView;

    // 받아올 값
    private int rentType = 0;
    private int money1 = -1, money2 = -1, money3 = -1; // 1. 전세자금  2. 직장인 3.  비상금
    private Place company; // 직장
    private ResidentialFacilities home; // 집
    Double company_x, company_y; // 근무지 x, y 좌표

    Boolean available_worker_loan; //직장인 대출 가능 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);

        //직장인 대출 가능 여부-가능하면 True, 안되면 False -> 표시X
        available_worker_loan=getIntent().getBooleanExtra("available_worker_loan",true);
        Log.d("직장인 대출 가능 여부-HD : ",Boolean.toString(available_worker_loan));

        // HomeActivity에서 넘어온 선택된 집 객체
        Intent detailIntent = getIntent();
        home = (ResidentialFacilities) detailIntent.getSerializableExtra("selectedHome");

        btn_bus=(Button)findViewById(R.id.btn_homedetail_bus);
        btn_subway=(Button)findViewById(R.id.btn_homedetail_subway);
        btn_convenience_store=(Button)findViewById(R.id.btn_homedetail_convenience_store);
        btn_cart=(Button)findViewById(R.id.btn_homedetail_cart);

        btn_bus.setSelected(!check1);
        btn_subway.setSelected(!check2);
        btn_convenience_store.setSelected(!check3);
        btn_cart.setSelected(!check4);

        btn_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check1){//체크
                    btn_bus.setBackground(getResources().getDrawable(R.drawable.button_bus_click_color));
                    check1=false;
                    kakaoMapAPIDetail.busMarker();
                }
                else{//체크 해제
                    btn_bus.setBackground(getResources().getDrawable(R.drawable.button_bus_color));
                    check1=true;
                    kakaoMapAPIDetail.deleteBusMarker();
                }
            }
        });
        btn_subway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check2){//체크
                    btn_subway.setBackground(getResources().getDrawable(R.drawable.button_subway_click_color));
                    check2=false;
                    kakaoMapAPIDetail.subwayMarker();
                }
                else{//체크 해제
                    btn_subway.setBackground(getResources().getDrawable(R.drawable.button_subway_color));
                    check2=true;
                    kakaoMapAPIDetail.deleteSubwayMarker();
                }
            }
        });
        btn_convenience_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check3){//체크
                    btn_convenience_store.setBackground(getResources().getDrawable(R.drawable.button_convenience_store_click_color));
                    check3=false;
                    kakaoMapAPIDetail.conviMarker();
                }
                else{//체크 해제
                    btn_convenience_store.setBackground(getResources().getDrawable(R.drawable.button_convenience_store_color));
                    check3=true;
                    kakaoMapAPIDetail.deleteConvinMarker();
                }
            }
        });
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check4){//체크
                    btn_cart.setBackground(getResources().getDrawable(R.drawable.button_cart_click_color));
                    check4=false;
                    kakaoMapAPIDetail.MartMarker();
                }
                else{//체크 해제
                    btn_cart.setBackground(getResources().getDrawable(R.drawable.button_cart_color));
                    check4=true;
                    kakaoMapAPIDetail.deleteMartMarker();
                }
            }
        });

        //------------------------------------------------------------------------------------------
        // 상세 정보
        detailCategory = findViewById(R.id.home_detail_tv_category);
        detailYear = findViewById(R.id.home_detail_tv_year);
        detailArea = findViewById(R.id.home_detail_tv_area);                // Area - 면적/층수
        detailMoneyTitle = findViewById(R.id.home_detail_tv_money_title);    // Money - 보증금/월세 (전세인 경우)-> 전세금
        detailMoney = findViewById(R.id.home_detail_tv_money);
        detailAddress = findViewById(R.id.home_detail_tv_address);          // Address - 지번 + 법정동주소

        rentType = PreferenceManager.getInt(this, "rentType");

        // 전세인 경우
        if (rentType == 1) {
            detailMoneyTitle.setText("전세금");
            detailMoney.setText(home.getWarFee());
        } else {
            detailMoneyTitle.setText("보증금/월세");
            detailMoney.setText(home.getWarFee()+"/"+home.getRenFee());
        }

        // 받은 정보 setText로 입력
        int cate = home.gethCate();
        if (cate == 0) {
            detailCategory.setText("아파트");
        } else if(cate == 1) {
            detailCategory.setText("오피스텔");
        } else {
            detailCategory.setText("연립주택");
        }

        detailYear.setText(Integer.toString(home.gethYear()));
        detailArea.setText(Double.toString(home.gethArea()) + "/" + Double.toString(home.gethFloor()));
        detailAddress.setText(home.getResident().get_placeDetailAddress());

        //------------------------------------------------------------------------------------------
        // 직장과의 거리
        // ODsay - 총 거리, 걸리는 시간

        detailDistanceFromCompany = findViewById(R.id.home_detail_tv_distance);     // 총 거리 :
        detailTimeFromCompany = findViewById(R.id.home_detail_tv_time);             // 대중교통으로 이동한 시간 :

        String distance = "";
        String time = "";

        // 근무지 검색 다이얼로그 연결
        workSettingButton = findViewById(R.id.btn_homedetail_work);
        workSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new SearchDialog(HomeDetailActivity.this);
                dialog.setSearchDialogListener(new SearchDialog.SearchDialogListener() {
                    @Override
                    public void onSearchResultClick(Place place) throws JSONException {
                        company = place;
                        PreferenceManager.setString(getApplicationContext(), "company_name", company.get_placeAddress());
                        PreferenceManager.setString(getApplicationContext(), "company_x", Double.toString(company.get_placeX()));
                        PreferenceManager.setString(getApplicationContext(), "company_y", Double.toString(company.get_placeY()));

                        kakaoMapAPIDetail.workMarker(place);
                        ArrayList<ExtendNode> sortArr = new ArrayList<ExtendNode>();

                        int minV = 999999; double minVdis = 999999;
                        try {
                            CalRoute calRoute = new CalRoute(getBaseContext(),home.getResident(),place);
                            for(int i =0;i< calRoute.calRoute1().getResultPath().size();i++){
                                sortArr.add(calRoute.calRoute1().getResultPath().get(i).getInfo());
                                if(minV > calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalTime()){
                                    minV = calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalTime();
                                    minVdis =  calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalDistance();
                                }
                                else if(minV == calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalTime()){
                                    if(minVdis > calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalDistance()){
                                        minVdis =  calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalDistance();
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        detailDistanceFromCompany.setText("총 거리 : 12km");
                        detailTimeFromCompany.setText("대중교통으로 이동한 시간 : 1시간 56분");
                    }
                });

                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                Window window = dialog.getWindow();
                window.setAttributes(lp);
            }
        });

        if (PreferenceManager.getString(this, "company_name").equals("")) {
            // 근무지 정보가 없는 경우
            distance = getString(R.string.detail_distance_none);
            time = getString(R.string.detail_time_none);
        } else {
            // 근무지 정보가 있는 경우
            // 아래 근무지 x, y 좌표로 ODsay에서 거리, 걸리는 시간 계산

            company_x = Double.parseDouble(PreferenceManager.getString(this, "company_x"));
            company_y = Double.parseDouble(PreferenceManager.getString(this, "company_y"));

            Place temp = new Place(PreferenceManager.getString(this, "company_name"),company_x,company_y);

            kakaoMapAPIDetail.workMarker(temp);

            ArrayList<ExtendNode> sortArr = new ArrayList<ExtendNode>();
            int minV = 999999; double minVdis = 999999;
            try {
                CalRoute calRoute = new CalRoute(this,home.getResident(),temp);
                for(int i =0;i< calRoute.calRoute1().getResultPath().size();i++){
                    sortArr.add(calRoute.calRoute1().getResultPath().get(i).getInfo());
                    if(minV > calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalTime()){
                        minV = calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalTime();
                        minVdis =  calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalDistance();
                    }
                    else if(minV == calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalTime()){
                        if(minVdis > calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalDistance()){
                            minVdis =  calRoute.calRoute1().getResultPath().get(i).getInfo().getTotalDistance();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // distance, time에서 오른쪽 주석과 같이 String 만들 것!
            workSettingButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_work_click_color));
            Log.d("근무지 이름", PreferenceManager.getString(this, "company_name"));
            distance = Double.toString(minVdis) ;    // distance + Integer.toString(계산한 거리) + km
            time = Integer.toString(minV);          // time + Integer.toString(계산한 시간) + 분


            // 받은 정보 setText로 입력
            detailDistanceFromCompany.setText("직장까지의 거리 : 12km");
            detailTimeFromCompany.setText(time);

        }

        // 받은 정보 setText로 입력
        detailDistanceFromCompany.setText(distance);
        detailTimeFromCompany.setText(time);



        //------------------------------------------------------------------------------------------
        // 대출 조회
        // 대출 조회 API
        loan1LinearLayout = findViewById(R.id.home_detail_ll_loan1);            // 전세자금대출
        loan2LinearLayout = findViewById(R.id.home_detail_ll_loan2);            // 직장인대출
        loan3LinearLayout = findViewById(R.id.home_detail_ll_loan3);            // 비상금대출
        loanNoneLinearLayout = findViewById(R.id.home_detail_ll_loan_none);     // 대출 조회 정보 없는 경우 -> visible

        loan1MoneyTextView = findViewById(R.id.home_detail_tv_loan1);
        loan1Money_1_TextView = findViewById(R.id.home_detail_tv_loan1_1);
        loan1Money_2_TextView = findViewById(R.id.home_detail_tv_loan1_2);
        loan1Money_3_TextView = findViewById(R.id.home_detail_tv_loan1_3);
        loan1Money_4_TextView = findViewById(R.id.home_detail_tv_loan1_4);

        loan2MoneyTextView = findViewById(R.id.home_detail_tv_loan2);
        loan2Money_1_TextView = findViewById(R.id.home_detail_tv_loan2_1);
        loan2Money_2_TextView = findViewById(R.id.home_detail_tv_loan2_2);

        loan3MoneyTextView = findViewById(R.id.home_detail_tv_loan3);
        loan3Money_1_TextView = findViewById(R.id.home_detail_tv_loan3_1);
        loan3Money_2_TextView = findViewById(R.id.home_detail_tv_loan3_2);
        loan3Money_3_TextView = findViewById(R.id.home_detail_tv_loan3_3);
        loan3Money_4_TextView = findViewById(R.id.home_detail_tv_loan3_4);


        // 대출 조회 정보 입력 버튼
        loanConditionButton = findViewById(R.id.home_detail_btn_loan_condition);
        loanConditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loanConIntent = new Intent(HomeDetailActivity.this, LoanDetailActivity.class);
                startActivity(loanConIntent);
            }
        });

        loanCondition_TextView = findViewById(R.id.home_detail_tv_loan_condition);

        loanNoneLinearLayout.setVisibility(View.GONE);
        loan1LinearLayout.setVisibility(View.VISIBLE);
        loan1MoneyTextView.setText(home.getWarFeeLoan().getADR()+ " 가능");
        loan1Money_1_TextView.setText(getString(R.string.loan1_1) + home.getWarFeeLoan().getBLD());
        loan1Money_2_TextView.setText(getString(R.string.loan1_2) + home.getWarFeeLoan().getMDBT());
        loan1Money_3_TextView.setText(getString(R.string.loan1_3) + home.getWarFeeLoan().getLEAS());
        loan1Money_4_TextView.setText(getString(R.string.loan1_4) + home.getWarFeeLoan().getLAWC());

        loan2LinearLayout.setVisibility(View.VISIBLE);
        loan1MoneyTextView.setText(home.getWorkerLoan().getRQAM() + " 가능");
        loan2Money_1_TextView.setText(getString(R.string.loan2_1) + home.getWorkerLoan().getENCY());
        loan2Money_2_TextView.setText(getString(R.string.loan2_2) + home.getWorkerLoan().getName());

        loan3LinearLayout.setVisibility(View.VISIBLE);
        loan1MoneyTextView.setText(home.getBisangLoan().getAPVAM() + " 가능");
        loan3Money_1_TextView.setText(getString(R.string.loan3_1) + home.getBisangLoan().getLONDCS());
        loan3Money_2_TextView.setText(getString(R.string.loan3_2) + home.getBisangLoan().getAPLIR());
        loan3Money_3_TextView.setText(getString(R.string.loan3_3) + home.getBisangLoan().getTGTCUS());
        loan3Money_4_TextView.setText(getString(R.string.loan3_4) + home.getBisangLoan().getWooriPBOK());

//        if (PreferenceManager.getBoolean(this, "isSetting_loan") != true) {
//            // 대출 조회 정보가 없는 경우
//            loan1LinearLayout.setVisibility(View.GONE);
//            loan2LinearLayout.setVisibility(View.GONE);
//            loan3LinearLayout.setVisibility(View.GONE);
//            loanNoneLinearLayout.setVisibility(View.VISIBLE);
//        } else {
//            // 대출 정보가 있는 경우
//            if (PreferenceManager.getInt(this, "rentType") == RENTTYPE.JEONSE.getValue()) {
//                // 전세
//                if (PreferenceManager.getBoolean(this, "isSetting_jeonse") == true) {
//                    // 대출 정보가 모두 있고, 전세인 경우
//                    loan1LinearLayout.setVisibility(View.VISIBLE);
//                    loanNoneLinearLayout.setVisibility(View.GONE);
//                } else {
//                    // 전세 자금 대출 정보가 없는 경우
//                    loan1LinearLayout.setVisibility(View.GONE);
//                    loanNoneLinearLayout.setVisibility(View.VISIBLE);
//                    loanCondition_TextView.setText(R.string.condition_add_jeonse);
//                }
//            } else {
//                // 월세
//                // 대출 정보가 있고, 월세인 경우
//                loan1LinearLayout.setVisibility(View.GONE);
//                loanNoneLinearLayout.setVisibility(View.GONE);
//            }
//
//            if (available_worker_loan == true) {
//                // 직장인 대출이 가능한 경우
//                loan2Money_1_TextView.setVisibility(View.VISIBLE);
//                loan2Money_2_TextView.setVisibility(View.VISIBLE);
//            } else {
//                // 직장인 대출이 불가능한 경우
//                loan2MoneyTextView.setText(R.string.loan2_warning);
//                loan2Money_1_TextView.setVisibility(View.GONE);
//                loan2Money_2_TextView.setVisibility(View.GONE);
//            }
//
//        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(kakaoMapAPIDetail != null){
            kakaoMapAPIDetail.newMapView(this);
            kakaoMapAPIDetail.myLocMaker(home.getResident());
            kakaoMapAPIDetail.sidePlaceMaker();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(kakaoMapAPIDetail == null){
            try {
                kakaoMapAPIDetail = new KakaoMapAPI(this, (ViewGroup) findViewById(R.id.homedetail_map_view),home.getResident());
                kakaoMapAPIDetail.sidePlaceMaker();
            } catch (IOException | InterruptedException e) {

            }
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        kakaoMapAPIDetail.getMapViewContainer().removeAllViews();
    }
}