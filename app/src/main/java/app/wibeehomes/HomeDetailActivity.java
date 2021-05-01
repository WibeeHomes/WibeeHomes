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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import app.wibeehomes.R;
import app.wibeehomes.dialog.SearchDialog;

public class HomeDetailActivity extends AppCompatActivity {

    private boolean check1=false; //bus 마커 체크
    private boolean check2=false; //subway 마커 체크
    private boolean check3=false; //convenience 마커 체크
    private boolean check4=false; //cart 마커 체크
    Button btn_bus,btn_subway,btn_convenience_store,btn_cart;

    private TextView detailCategory, detailYear, detailArea, detailMoney, detailAddress, detailMoneyTitle,
                        detailDistanceFromCompany, detailTimeFromCompany,
                        loan1MoneyTextView, loan2MoneyTextView, loan3MoneyTextView; // 1. 전세자금  2. 직장인 3.  비상금
    private LinearLayout loan1LinearLayout, loan2LinearLayout, loan3LinearLayout, loanNoneLinearLayout;
    private Button loanConditionButton, workSettingButton;
    private SearchDialog dialog;

    // 받아올 값
    private int rentType = 0;
    private int money1 = -1, money2 = -1, money3 = -1; // 1. 전세자금  2. 직장인 3.  비상금
    private Place company; // 직장
    Double company_x, company_y; // 근무지 x, y 좌표

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_detail);

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
                }
                else{//체크 해제
                    btn_bus.setBackground(getResources().getDrawable(R.drawable.button_bus_color));
                    check1=true;
                }
            }
        });
        btn_subway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check2){//체크
                    btn_subway.setBackground(getResources().getDrawable(R.drawable.button_subway_click_color));
                    check2=false;
                }
                else{//체크 해제
                    btn_subway.setBackground(getResources().getDrawable(R.drawable.button_subway_color));
                    check2=true;
                }
            }
        });
        btn_convenience_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check3){//체크
                    btn_convenience_store.setBackground(getResources().getDrawable(R.drawable.button_convenience_store_click_color));
                    check3=false;
                }
                else{//체크 해제
                    btn_convenience_store.setBackground(getResources().getDrawable(R.drawable.button_convenience_store_color));
                    check3=true;
                }
            }
        });
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check4){//체크
                    btn_cart.setBackground(getResources().getDrawable(R.drawable.button_cart_click_color));
                    check4=false;
                }
                else{//체크 해제
                    btn_cart.setBackground(getResources().getDrawable(R.drawable.button_cart_color));
                    check4=true;
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
        } else {
            detailMoneyTitle.setText("보증금/월세");
        }

        // 받은 정보 setText로 입력



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

        if (PreferenceManager.getString(this, "company_name") == null) {
            // 근무지 정보가 없는 경우
            distance = getString(R.string.detail_distance_none);
            time = getString(R.string.detail_time_none);
        } else {
            // 근무지 정보가 있는 경우
            // 아래 근무지 x, y 좌표로 ODsay에서 거리, 걸리는 시간 계산
            company_x = Double.parseDouble(PreferenceManager.getString(this, "company_x"));
            company_y = Double.parseDouble(PreferenceManager.getString(this, "company_y"));

            // distance, time에서 오른쪽 주석과 같이 String 만들 것!
            workSettingButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_work_click_color));
            Log.d("근무지 이름", PreferenceManager.getString(this, "company_name"));
            distance = getString(R.string.detail_distance);         // distance + Integer.toString(계산한 거리) + km
            time = getString(R.string.detail_time);          // time + Integer.toString(계산한 시간) + 분

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
        loan2MoneyTextView = findViewById(R.id.home_detail_tv_loan2);
        loan3MoneyTextView = findViewById(R.id.home_detail_tv_loan3);

        // 대출 조회 정보 입력 버튼
        loanConditionButton = findViewById(R.id.home_detail_btn_loan_condition);
        loanConditionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loanConIntent = new Intent(HomeDetailActivity.this, LoanDetailActivity.class);
                startActivity(loanConIntent);
            }
        });


        if (PreferenceManager.getBoolean(this, "isSetting_loan") != true) {
            // 대출 조회 정보가 없는 경우
            loan1LinearLayout.setVisibility(View.GONE);
            loan2LinearLayout.setVisibility(View.GONE);
            loan3LinearLayout.setVisibility(View.GONE);
            loanNoneLinearLayout.setVisibility(View.VISIBLE);
        } else {
            // 대출 정보가 있는 경우
            if (PreferenceManager.getInt(this, "rentType") == RENTTYPE.JEONSE.getValue()) {
                if (PreferenceManager.getBoolean(this, "isSetting_jeonse") == true) {
                    // 대출 정보가 모두 있고, 전세인 경우
                    loan1LinearLayout.setVisibility(View.VISIBLE);
                } else {
                    // 전세 자금 대출 정보가 없는 경우
                    loan1LinearLayout.setVisibility(View.GONE);
                    loanNoneLinearLayout.setVisibility(View.VISIBLE);
                }
            } else {
                // 대출 정보가 있고, 월세인 경우
                loan1LinearLayout.setVisibility(View.GONE);
                loanNoneLinearLayout.setVisibility(View.GONE);
            }
            loan2LinearLayout.setVisibility(View.VISIBLE);
            loan3LinearLayout.setVisibility(View.VISIBLE);
        }

    }


}