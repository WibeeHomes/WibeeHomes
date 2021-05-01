package app.wibeehomes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static app.wibeehomes.DTO.cityArr;

enum RENTTYPE {
    JEONSE(0), WOLSE(1);
    private final int value;
    private RENTTYPE(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}

public class HomeConditionActivity extends AppCompatActivity {

    private RadioGroup rg_lease;
    private RadioButton rb_lease_year,rb_lease_month;
    private RENTTYPE rentType = RENTTYPE.JEONSE;

    private TextView tv_homecondition_budget,tv_homecondition_monthly;
    RangeSeekBar rangeSeekBar_budget,rangeSeekBar_monthly;
    LinearLayout layout_monthly;

    // Spinner 관련 컴포넌트
    private Spinner bigLocSpinner, smallLocSpinner;
    private ArrayAdapter bigAdapter, smallAdapter;
    private int bigLocal = 0, smallLocal = 0;

    private TextView submitButton;
    private Button btn_loan_info;//대출 정보 입력 버튼

    //전세 최소,최대값
    int min_value_jeonse;
    int max_value_jeonse;
    //월세 최소,최대값
    int min_value_wolse;
    int max_value_wolse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_condition);

        rg_lease=(RadioGroup) findViewById(R.id.rg_lease); //전 월세 그룹
        rb_lease_year=(RadioButton) findViewById(R.id.rb_lease_year); //전세 버튼
        rb_lease_month=(RadioButton) findViewById(R.id.rb_lease_month); //월세 버튼
        tv_homecondition_budget=(TextView) findViewById(R.id.tv_homecondition_budget); //전세금(보증금) 예산 적는 텍스트뷰
        tv_homecondition_monthly=(TextView) findViewById(R.id.tv_homecondition_monthly); //월세 예산 적는 텍스트뷰
        rangeSeekBar_budget=(RangeSeekBar)findViewById(R.id.rangeSeekbar);//전세금(보증금) 범위 선택 바
        rangeSeekBar_monthly=(RangeSeekBar)findViewById(R.id.rangeSeekbar_monthly); //월세 범위 선택 바
        layout_monthly=(LinearLayout) findViewById(R.id.linearLayout2); //월세관련된 모든 내용 포함하는 레이아웃


        if (PreferenceManager.getBoolean(this, "isSetting") == true) {
            bigLocal = PreferenceManager.getInt(this, "bigLocalNum");
            Log.d("빅로컬 전", Integer.toString(bigLocal));
            smallLocal = PreferenceManager.getInt(this, "smallLocalNum");
            int rentNum = PreferenceManager.getInt(this, "rentType");

            // 전·월세에 따라 라디오버튼 선택
            if (rentNum == 0) {
                rg_lease.check(R.id.rb_lease_year);
                for(int n=0;n<layout_monthly.getChildCount();n++){
                    View view=layout_monthly.getChildAt(n);
                    view.setVisibility(View.GONE);
                }
            } else {
                rg_lease.check(R.id.rb_lease_month);
            }
        }

        //------------------------------------------------------------------------------------------
        //전세금(보증금)
        rangeSeekBar_budget.setSelectedMaxValue(50000);
        rangeSeekBar_budget.setSelectedMinValue(0);
        rangeSeekBar_budget.setRangeValues(0,50000);  //0만원~50,000만원(5억원)
        //월세
        rangeSeekBar_monthly.setSelectedMaxValue(500);
        rangeSeekBar_monthly.setSelectedMinValue(0);
        rangeSeekBar_monthly.setRangeValues(0,500);  //0만원~500만원

        final DecimalFormat formatter=new DecimalFormat("###,###");

        rg_lease.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.rb_lease_year){//전세를 선택한 경우->월세 선택X
                        for(int n=0;n<layout_monthly.getChildCount();n++){
                            View view=layout_monthly.getChildAt(n);
                            view.setVisibility(View.GONE);
                        }
                        rentType = RENTTYPE.JEONSE;
                }
                else if(i==R.id.rb_lease_month){//월세를 선택한 경우->월세 선택O
                    for(int n=0;n<layout_monthly.getChildCount();n++){
                        View view=layout_monthly.getChildAt(n);
                        view.setVisibility(View.VISIBLE);
                    }
                    rentType = RENTTYPE.WOLSE;
                }
            }
        });

        //전세금(보증금)
        rangeSeekBar_budget.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min_value_jeonse_bar=bar.getSelectedMinValue();
                Number max_value_jeonse_bar=bar.getSelectedMaxValue();

                min_value_jeonse=(int)min_value_jeonse_bar;
                max_value_jeonse=(int)max_value_jeonse_bar;
                String min_budget,max_budget;

                if(min_value_jeonse>=10000){//1억 이상
                    int num=min_value_jeonse/10000;
                    if(min_value_jeonse%10000==0){
                        min_budget=num+"억";
                    }
                    else{
                        min_value_jeonse=min_value_jeonse%10000;
                        min_budget=num+"억 "+formatter.format(min_value_jeonse)+"만원";
                    }
                }
                else{
                    min_budget=formatter.format(min_value_jeonse)+"만원";
                }

                if(max_value_jeonse>=10000){//1억 이상
                    int num=max_value_jeonse/10000;
                    if(max_value_jeonse%10000==0){
                        max_budget=num+"억";
                    }
                    else{
                        max_value_jeonse=max_value_jeonse%10000;
                        max_budget=num+"억 "+formatter.format(max_value_jeonse)+"만원";
                    }
                }
                else{
                    max_budget=formatter.format(max_value_jeonse)+"만원";
                }

                tv_homecondition_budget.setText(min_budget+"~"+max_budget);
            }

        });

        //월세
        rangeSeekBar_monthly.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min_value_wolse_bar=bar.getSelectedMinValue();
                Number max_value_wolse_bar=bar.getSelectedMaxValue();

                min_value_wolse=(int)min_value_wolse_bar;
                max_value_wolse=(int)max_value_wolse_bar;
                String min_budget=formatter.format(min_value_wolse)+"만원";
                String max_budget=formatter.format(max_value_wolse)+"만원";

                tv_homecondition_monthly.setText(min_budget+"~"+max_budget);
            }
        });

        //------------------------------------------------------------------------------------------
        // Spinner
        bigLocSpinner = findViewById(R.id.condition_spinner_big_local);
        smallLocSpinner = findViewById(R.id.condition_spinner_small_local);

        bigAdapter = ArrayAdapter.createFromResource(this, R.array.big_location_array, R.layout.item_spinner);
        bigAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bigLocSpinner.setAdapter(bigAdapter);
        bigLocSpinner.setSelection(bigLocal);

        int resId = getResources().getIdentifier("array_"+Integer.toString(bigLocal), "array", getApplicationContext().getPackageName());
        smallAdapter = ArrayAdapter.createFromResource(getApplicationContext(), resId, R.layout.item_spinner);
        Log.d("스몰로컬 결정", Integer.toString(smallLocal));

        smallAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        smallLocSpinner.setAdapter(smallAdapter);
        smallLocSpinner.setSelection(smallLocal);

        BigSpinnerAction();
        SmallSpinnerAction();

        //------------------------------------------------------------------------------------------
        // 제출 버튼-지역은 기본 설정으로 되어 있고, 전월세는 선택을 해야만! 조회로 넘어갈 수 있음
        submitButton = findViewById(R.id.condition_tv_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!rb_lease_year.isChecked()&&!rb_lease_month.isChecked()) { //전월세를 선택 안한 경우
                    AlertDialog.Builder builder=new AlertDialog.Builder(HomeConditionActivity.this);
                    builder.setMessage("전세, 월세를 선택해야 합니다.");
                    builder.setPositiveButton("확인",null);
                    builder.create().show();
                }
                else if(rb_lease_year.isChecked() || rb_lease_month.isChecked()) { //전월세를 선택한 경우

                    // sharedPreference-----------------------------------------------------------------
                    // 조회 Boolean
                    PreferenceManager.setBoolean(getApplicationContext(), "isSetting", true);

                    // 전월세 타입 저장
                    PreferenceManager.setInt(getApplicationContext(), "rentType", rentType.getValue());

                    // preference 시/도 이름, 인덱스 저장
                        Log.d("보내기 전 큰 지역", Integer.toString(bigLocal));
                    String[] bigArray = getResources().getStringArray(R.array.big_location_array);
                    PreferenceManager.setString(getApplicationContext(), "bigLocal", bigArray[bigLocal]);
                    PreferenceManager.setInt(getApplicationContext(), "bigLocalNum", bigLocal);

                    // preference 시/군/구 이름, 인덱스 저장
                    Log.d("보내기 전 작은 지역", Integer.toString(smallLocal));
                    int resId = getResources().getIdentifier("array_"+bigLocal, "array", getApplicationContext().getPackageName());
                    String[] smallArray = getResources().getStringArray(resId);
                    PreferenceManager.setString(getApplicationContext(), "smallLocal", smallArray[smallLocal]);
                    PreferenceManager.setInt(getApplicationContext(), "smallLocalNum", smallLocal);

                    // preference 전세 최소, 최대 금액
                    PreferenceManager.setInt(getApplicationContext(), "minMoneyYear", (int)min_value_jeonse);
                    PreferenceManager.setInt(getApplicationContext(), "maxMoneyYear", (int)max_value_jeonse);

                    //월세
                    if(rentType==RENTTYPE.WOLSE){
                        // preference 월세 최소, 최대 금액
                        PreferenceManager.setInt(getApplicationContext(), "minMoneyMonth", (int)min_value_wolse);
                        PreferenceManager.setInt(getApplicationContext(), "maxMoneyMonth", (int)max_value_wolse);
                    }

                    // 요청 보내기 ------------------------------------------------------------------
                    String localCodeName = bigArray[bigLocal]+" "+smallArray[smallLocal];




                    // 부동산 검색 조건 서버로 보내고 HomeActivity로 이동
                    Intent homeIntent = new Intent(HomeConditionActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                }
            }
        });

        //대출 정보 입력 버튼

        btn_loan_info=(Button) findViewById(R.id.btn_homecondition_search);
        btn_loan_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //부동산 검색 조건을 서버로 보내고 LoanDetailActivity로 이동
                if(!rb_lease_year.isChecked()&&!rb_lease_month.isChecked()){//전월세를 선택 안한 경우
                    AlertDialog.Builder builder=new AlertDialog.Builder(HomeConditionActivity.this);
                    builder.setMessage("전세, 월세를 선택해야 합니다.");
                    builder.setPositiveButton("확인",null);
                    builder.create().show();
                }
                else if(rb_lease_year.isChecked() || rb_lease_month.isChecked()) {//전월세를 선택한 경우
                    Intent loanIntent = new Intent(HomeConditionActivity.this, LoanDetailActivity.class);
                    loanIntent.putExtra("con_rent_type", rentType);

                    startActivity(loanIntent);
                }
            }
        });

    }

    private void BigSpinnerAction(){
        bigLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String index = Integer.toString(i);
                int resId = getResources().getIdentifier("array_"+index, "array", getApplicationContext().getPackageName());
                smallAdapter = ArrayAdapter.createFromResource(getApplicationContext(), resId, R.layout.item_spinner);

                bigLocal = i;
                Log.d("큰 도시 선택 : ", Integer.toString(i));

                smallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                smallLocSpinner.setAdapter(smallAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bigLocal = 0;
            }
        });
    }

    private void SmallSpinnerAction(){
        smallLocSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                smallLocal = i;
                Log.d("작은 도시 선택 : ", Integer.toString(i));

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                smallLocal = 0;
            }
        });
    }
}