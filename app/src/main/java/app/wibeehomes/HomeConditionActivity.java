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

import androidx.appcompat.app.AppCompatActivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.DecimalFormat;

enum RENTTYPE {
    JEONSE, WOLSE
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


        // 조건 설정 후 다시 HomeConditionActivity로 돌아온 경우
        Intent homeIntent = getIntent();
        if (homeIntent.getExtras() != null ){
            bigLocal = homeIntent.getExtras().getInt("big_local");
            smallLocal = homeIntent.getExtras().getInt("small_local");
            rentType = (RENTTYPE) homeIntent.getSerializableExtra("rent_type");

            // 전·월세에 따라 라디오버튼 선택
            if (rentType == RENTTYPE.JEONSE) {
                rg_lease.check(R.id.rb_lease_year);
                for(int n=0;n<layout_monthly.getChildCount();n++){
                    View view=layout_monthly.getChildAt(n);
                    view.setVisibility(View.GONE);
                }
            } else {
                rg_lease.check(R.id.rb_lease_month);
            }

            // 이전 위치 선택
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
                Number min_value=bar.getSelectedMinValue();
                Number max_value=bar.getSelectedMaxValue();

                int min=(int)min_value;
                int max=(int)max_value;
                String min_budget,max_budget;

                if(min>=10000){//1억 이상
                    int num=min/10000;
                    if(min%10000==0){
                        min_budget=num+"억";
                    }
                    else{
                        min=min%10000;
                        min_budget=num+"억 "+formatter.format(min)+"만원";
                    }
                }
                else{
                    min_budget=formatter.format(min)+"만원";
                }

                if(max>=10000){//1억 이상
                    int num=max/10000;
                    if(max%10000==0){
                        max_budget=num+"억";
                    }
                    else{
                        max=max%10000;
                        max_budget=num+"억 "+formatter.format(max)+"만원";
                    }
                }
                else{
                    max_budget=formatter.format(max)+"만원";
                }

                tv_homecondition_budget.setText(min_budget+"~"+max_budget);
            }
        });
        //월세
        rangeSeekBar_monthly.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Number min_value=bar.getSelectedMinValue();
                Number max_value=bar.getSelectedMaxValue();

                int min=(int)min_value;
                int max=(int)max_value;
                String min_budget=formatter.format(min)+"만원";
                String max_budget=formatter.format(max)+"만원";

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

        smallAdapter = ArrayAdapter.createFromResource(this, R.array.array_0, R.layout.item_spinner);
        smallAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        smallLocSpinner.setAdapter(smallAdapter);

        BigSpinnerAction();
        SmallSpinnerAction();



        //------------------------------------------------------------------------------------------
        // 제출 버튼
        submitButton = findViewById(R.id.condition_tv_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 부동산 검색 조건 서버로 보내고 HomeActivity로 이동
                Intent homeIntent = new Intent(HomeConditionActivity.this, HomeActivity.class);
                homeIntent.putExtra("con_big_local", bigLocal);
                homeIntent.putExtra("con_small_local", smallLocal);
                homeIntent.putExtra("con_rent_type", rentType);
                startActivity(homeIntent);
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