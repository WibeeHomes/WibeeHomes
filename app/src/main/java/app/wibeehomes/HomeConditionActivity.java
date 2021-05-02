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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    private static OkHttpClient client=new OkHttpClient().newBuilder().build();
    private static String OkhttpUrl="http://3.34.216.87:8080/Wibee_Server/androidDB.jsp";
    //private static String OkhttpUrl="http://192.168.1.34:8080/Wibee_Server/androidDB.jsp";
    private static MediaType mediaType= MediaType.parse("text/plain");

    private RadioGroup rg_lease;
    private RadioButton rb_lease_year,rb_lease_month;
    private RENTTYPE rentType = RENTTYPE.JEONSE;

    private TextView tv_homecondition_budget,tv_homecondition_monthly, tv_condition_loan;
    RangeSeekBar rangeSeekBar_budget,rangeSeekBar_monthly;
    LinearLayout layout_monthly;

    // Spinner 관련 컴포넌트
    private Spinner bigLocSpinner, smallLocSpinner;
    private ArrayAdapter bigAdapter, smallAdapter;
    private int bigLocal, smallLocal;

    private static JSONArray jsonArray=null;

    private ArrayList<ResidentialFacilities> residentialFacilities = new ArrayList<ResidentialFacilities>();

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
        } else {
            bigLocal = 0;
            smallLocal = 0;
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

        //조건이 선택된 경우라면
        if(PreferenceManager.getBoolean(this,"isSetting")){
            //전세, 월세를 선택했다면->전세금(보증금) setting
            int temp_min=PreferenceManager.getInt(this,"minMoneyYear");
            int temp_max=PreferenceManager.getInt(this,"maxMoneyYear");
            rangeSeekBar_budget.setSelectedMinValue(temp_min);
            rangeSeekBar_budget.setSelectedMaxValue(temp_max);

            String temp_year=moneyToString(temp_min)+"~"+moneyToString(temp_max);
            tv_homecondition_budget.setText(temp_year);

            Log.d("저장됨?",Boolean.toString(PreferenceManager.getBoolean(getApplicationContext(),"isSetting")));
            Log.d("전세범위",Integer.toString(temp_min));

            //월세를 선택했다면->월세 setting
            if(PreferenceManager.getInt(this, "rentType")==1){
                int temp_min_month=PreferenceManager.getInt(this,"minMoneyMonth");
                int temp_max_month=PreferenceManager.getInt(this,"maxMoneyMonth");
                rangeSeekBar_monthly.setSelectedMinValue(temp_min_month);
                rangeSeekBar_monthly.setSelectedMaxValue(temp_max_month);

                String temp_month=moneyToString(temp_min_month)+"~"+moneyToString(temp_max_month);
                tv_homecondition_monthly.setText(temp_month);
                Log.d("월세범위",temp_year);
            }
        }

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

                min_budget=moneyToString(min_value_jeonse);

                max_budget=moneyToString(max_value_jeonse);

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

        int arrayResId = getResources().getIdentifier("array_"+Integer.toString(bigLocal), "array", getApplicationContext().getPackageName());
        smallAdapter = ArrayAdapter.createFromResource(this, R.array.array_0, R.layout.item_spinner);
        smallAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        smallLocSpinner.setAdapter(smallAdapter);
        smallLocSpinner.setSelection(PreferenceManager.getInt(this, "smallLocalNum"));
        int smallSelected = smallLocSpinner.getSelectedItemPosition();
        Log.d("선택된 작은 로컬", Integer.toString(smallSelected));

        BigSpinnerAction();
        SmallSpinnerAction();

        //------------------------------------------------------------------------------------------
        // 제출 버튼 위의 텍스트
        tv_condition_loan = findViewById(R.id.condition_tv_loan);
        if (PreferenceManager.getBoolean(this, "isSetting_loan") != true) {
            // 대출 조회 정보가 없는 경우
            tv_condition_loan.setText(R.string.condition_none_loan);
        } else {
            if (rentType == RENTTYPE.JEONSE) {
                // 현재 전세
                if (PreferenceManager.getBoolean(this, "isSetting_jeonse") != true) {
                    // 전세자금대출 정보가 없는 경우
                    tv_condition_loan.setText(R.string.condition_add_jeonse);
                }
            }
            // 대출 정보가 있는 경우 - 수정
            tv_condition_loan.setText(R.string.condition_edit);
        }

        // 제출 버튼-지역은 기본 설정으로 되어 있고, 전월세는 선택을 해야만! 조회로 넘어갈 수 있음
        submitButton = findViewById(R.id.condition_btn_submit);

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
                    PreferenceManager.setInt(getApplicationContext(), "minMoneyYear", min_value_jeonse);
                    PreferenceManager.setInt(getApplicationContext(), "maxMoneyYear", max_value_jeonse);

                    //월세
                    if(rentType==RENTTYPE.WOLSE){
                        // preference 월세 최소, 최대 금액
                        PreferenceManager.setInt(getApplicationContext(), "minMoneyMonth", (int)min_value_wolse);
                        PreferenceManager.setInt(getApplicationContext(), "maxMoneyMonth", (int)max_value_wolse);
                    }

                    // 요청 보내기 ------------------------------------------------------------------
                    String localCodeName = bigArray[bigLocal]+" "+smallArray[smallLocal]; // DTO 시티 네임

                    ArrayList<CityCode> cityCodes = DTO.getCityArr();
                    for(int i =0; i <cityCodes.size();i++) {
                        if (cityCodes.get(i).getName().equals(localCodeName)) {
                            RequestBody body = null;
                            if(rb_lease_year.isChecked()){
                                body = new FormBody.Builder().add("localCode","a"+cityCodes.get(i).getCode()).build();
                            }
                            else {
                                body = new FormBody.Builder().add("localCode","b"+cityCodes.get(i).getCode()).build();
                            }

                            Request request = new Request.Builder().url(OkhttpUrl).method("POST", body).build();
                            // 서버에 법정동 코드 넘겨준다.
                            final CountDownLatch countDownLatch = new CountDownLatch(1);
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("연결 실패", "error Connect Server error is" + e.toString());
                                    e.printStackTrace();
                                    countDownLatch.countDown();
                                }

                                @Override
                                public void onResponse(Call call, Response response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            String responseData = response.body().string();
                                            jsonArray = new JSONArray(responseData);
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                        }
                                        countDownLatch.countDown();
                                    }
                                }
                            });
                            try {
                                countDownLatch.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    for(int i =0; i < jsonArray.length();i++){
                        try {
                            JSONObject object = (JSONObject)jsonArray.get(i);
                            String address = String.valueOf(object.get("adddong"))+String.valueOf(object.get("addjibun"));
                            Place temp = new Place(String.valueOf(object.get("hname")),address,Double.parseDouble(String.valueOf(object.get("pointx"))),
                                    Double.parseDouble(String.valueOf(object.get("pointy"))));
                            residentialFacilities.add(new ResidentialFacilities(temp,Integer.parseInt(String.valueOf(object.get("hyear"))),
                                    Integer.parseInt(String.valueOf(object.get("hfloor"))),
                                    Double.parseDouble(String.valueOf(object.get("harea"))),
                                    Integer.parseInt(String.valueOf(object.get("hcate"))),String.valueOf(object.get("addjibun")),
                                    String.valueOf(object.get("warfee")),String.valueOf(object.get("renfee"))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // 부동산 검색 조건 서버로 보내고 HomeActivity로 이동
                    Intent homeIntent = new Intent(HomeConditionActivity.this, HomeActivity.class);
                    homeIntent.putExtra("homeList", residentialFacilities);
                    setResult(RESULT_OK,homeIntent);
                    finish();
                    //startActivity(homeIntent);
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                smallLocal = 0;
            }
        });
    }

    public String moneyToString(int money){
        String moneystring="";
        final DecimalFormat formatter=new DecimalFormat("###,###");
        if(money>=10000){//1억 이상
            if(money%10000==0){//억단위
                moneystring=Integer.toString(money/10000)+"억원";
            }
            else{
                moneystring=Integer.toString(money/10000)+"억"+formatter.format(money%10000)+"만원";
            }
        }
        else{//1억 이하
            moneystring=formatter.format(money)+"만원";
        }
        return moneystring;
    }
}