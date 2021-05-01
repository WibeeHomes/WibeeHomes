package app.wibeehomes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;


public class LoanDetailActivity extends AppCompatActivity {

    private EditText et_name_1;
    private TextView et_name_2;//고객한글명 (비상금,직장인)
    private EditText et_businessnum; //사업자법인등록번호
    private EditText et_date; //입사일자
    private EditText et_yearmoney_1;
    private TextView et_yearmoney_2;//연소득금액 (직장인,전세대출)
    private TextView submitButton;//조회버튼

    private String businessDate;//입사 일자 'yyyy/MM/dd'
    private String finalDate;//입사 일자 'yyyyMMdd'

    private boolean available_worker_loan;//직장인 대출 가능 여부 -해당되면 true, 아니면 false

    private LinearLayout layout_year;//전세 자금 대출 레이아웃


    CheckBox check_all,check_first,check_second,check_third;//모두 동의, 신용정보조회동의, 개인정보제공동의, 개인신용정보이용동의

    private int textlength;
    boolean validation_date=false;//날짜 유효성 검사

    //intent로 rentType받기
    RENTTYPE rentType;

    /*
    *고객한글명
    -한글만 입력하도록 inputfilter
    -비상금에 입력시 직장인에도 복사
    -이름 같은지 확인 or 직장인 부분에는 수정 못하게--> 일단 후자로 만듦..
    *입사일자-->6개월 근무 이상 신경써야하나..?
    -날짜 유효성 검사
    -날짜 입력시 '/' 두군데 넣기
    *연소득금액
    -직장인에 입력시 전세대출에 복사-->이것도 마찬가지로 전세대출에 입력 못하게..
    *동의
    -모두동의, 첫번째동의, 두번째동의, 세번째 동의
    *intent로 월세가 넘어왔다면->전세대출 layout은 invisible
    *intent로 넘어온 값(from homecondition) 같이 추가해서 넘기기 (to home)
    *enum-JEONSE/WOLSE
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        et_name_1=findViewById(R.id.loandetail_et_name_1);//고객 한글명-비상금
        et_name_2=findViewById(R.id.loandetail_et_name_2);//고객 한글명-직장인
        et_businessnum=findViewById(R.id.loandetail_et_businessnum);//사업자법인등록번호
        et_date=findViewById(R.id.loandetail_et_date);//입사일자
        et_yearmoney_1=findViewById(R.id.loandetail_et_yearmoney_1);//연소득금액-직장인
        et_yearmoney_2=findViewById(R.id.loandetail_et_yearmoney_2);//연간소득금액-전세
        check_all=findViewById(R.id.loandetail_check_allagree);//모두 동의
        check_first=findViewById(R.id.loandetail_check_first);//신용정보조회 동의
        check_second=findViewById(R.id.loandetail_check_second);//개인정보제공 동의
        check_third=findViewById(R.id.loandetail_check_third);//개인신용정보이용 동의

        layout_year=findViewById(R.id.loandetail_ll_year);//전세자금 대출 레이아웃

        Calendar cal=Calendar.getInstance();
        cal.add(cal.MONTH,-6);
        SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");

        final String currentDate=f.format(cal.getTime());//현재 날짜로부터 6개월전

        //전월세 타입 받기-전세=0,월세=1
        final Intent get_homeintent=getIntent();
        rentType=(RENTTYPE)get_homeintent.getSerializableExtra("con_rent_type");

        if(rentType==RENTTYPE.JEONSE) {//전세라면
            for(int n=0;n<layout_year.getChildCount();n++){
                View view=layout_year.getChildAt(n);
                view.setVisibility(View.VISIBLE);
            }
        }else if(rentType==RENTTYPE.WOLSE){//월세라면
            for(int n=0;n<layout_year.getChildCount();n++){
                View view=layout_year.getChildAt(n);
                view.setVisibility(View.GONE);
            }
        }



        //-----------------------------------------------------------
        //고객한글명-한글만 입력하도록, 비상금에 입력시 직장인에도 자동으로 입력
        InputFilter[] filter_kor=new InputFilter[]{filterKor};
        et_name_1.setFilters(filter_kor);
        et_name_2.setFilters(filter_kor);

        et_name_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                et_name_2.setText(et_name_1.getText().toString());
            }
        });

        //--------------------------------------------------------------
        //입사날짜-입력시 '/'넣기, 유효성 검사
        et_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_date.isFocusable() && !s.toString().equals("")) {
                    try{
                        textlength = et_date.getText().toString().length();
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        return;
                    }

                    if (textlength == 4 && before != 1) {

                        et_date.setText(et_date.getText().toString()+"/");
                        et_date.setSelection(et_date.getText().length());

                    }else if (textlength == 7&& before != 1){

                        et_date.setText(et_date.getText().toString()+"/");
                        et_date.setSelection(et_date.getText().length());

                    }else if(textlength == 5 && !et_date.getText().toString().contains("/")){

                        et_date.setText(et_date.getText().toString().substring(0,4)+"/"+et_date.getText().toString().substring(4));
                        et_date.setSelection(et_date.getText().length());

                    }else if(textlength == 8 && !et_date.getText().toString().substring(7,8).equals("/")){

                        et_date.setText(et_date.getText().toString().substring(0,7)+"/"+et_date.getText().toString().substring(7));
                        et_date.setSelection(et_date.getText().length());

                    }
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                businessDate=et_date.getText().toString();
                if(businessDate.length()==10){
                    validation_date=isDate(et_date.getText().toString(),"yyyy/MM/dd");

                    finalDate=businessDate.substring(0,4)+businessDate.substring(5,7)+businessDate.substring(8);

                    //직장인 대출은 6개월 이상 근무->조건이 부합되면 true, 아니면 false
                    if(Integer.parseInt(currentDate)>=Integer.parseInt(finalDate)){
                        available_worker_loan=true;
                    }
                    else{
                        available_worker_loan=false;
                    }

                }}
        });


        //------------------------------------------------------------------------------
        //연소득금액-직장인에 입력시 전세자금에도 자동으로 입력
        et_yearmoney_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                et_yearmoney_2.setText(et_yearmoney_1.getText().toString());
            }
        });

        //-------------------------------------------------------------------------------
        //동의 버튼
        check_all.setOnClickListener(new View.OnClickListener() {//모두 동의 버튼
            @Override
            public void onClick(View v) {
                if(check_all.isChecked()){//모두 동의 버튼 체크시 세가지 모두 체크
                    check_first.setChecked(true);
                    check_second.setChecked(true);
                    check_third.setChecked(true);
                }else{//모두 동의 버튼 해제시 세가지 모두 해제
                    check_first.setChecked(false);
                    check_second.setChecked(false);
                    check_third.setChecked(false);
                }
            }
        });
        check_first.setOnClickListener(new View.OnClickListener() {//첫번째 동의 버튼
            @Override
            public void onClick(View v) {
                if(check_all.isChecked())//모두 동의 버튼 체크된 경우==첫번째가 체크된 경우
                    check_all.setChecked(false);
                else if(check_first.isChecked()&&check_second.isChecked()&&check_third.isChecked())//체크박스가 모두 체크되었다면 모두 동의도 체크
                    check_all.setChecked(true);
            }
        });
        check_second.setOnClickListener(new View.OnClickListener() {//두번째 동의 버튼
            @Override
            public void onClick(View v) {
                if(check_all.isChecked())//모두 동의 버튼 체크된 경우==두번째가 체크된 경우
                    check_all.setChecked(false);
                else if(check_first.isChecked()&&check_second.isChecked()&&check_third.isChecked())//체크박스가 모두 체크되었다면 모두 동의도 체크
                    check_all.setChecked(true);
            }
        });
        check_third.setOnClickListener(new View.OnClickListener() {//세번째 동의 버튼
            @Override
            public void onClick(View v) {
                if(check_all.isChecked())//모두 동의 버튼 체크된 경우==세번째가 체크된 경우
                    check_all.setChecked(false);
                else if(check_first.isChecked()&&check_second.isChecked()&&check_third.isChecked())//체크박스가 모두 체크되었다면 모두 동의도 체크
                    check_all.setChecked(true);
            }
        });

        //---------------------------------------------------------------------------
        //조회 버튼
        //homecondition에서 넘어온 정보 받기-bigLocal,smallLocal,rentType,min_value_jeonse,max_value_jeonse,min_value_wolse,max_value_wolse

        submitButton=findViewById(R.id.loandetail_tv_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //부동산 검색 조건 + 대출 정보 를 서버로 보내고 HomeActivity로 이동
                AlertDialog.Builder builder=new AlertDialog.Builder(LoanDetailActivity.this);
                if(et_name_1.getText().toString().length()==0){//이름을 입력 안한 경우
                    builder.setMessage("이름을 입력해야 합니다.");
                    builder.setPositiveButton("확인",null);
                    builder.create().show();
                }
                else{//이름을 입력한 경우
                    if(et_businessnum.getText().toString().length()==0){//사업자법인등록번호를 입력 안한 경우
                        builder.setMessage("사업자법인등록번호를 입력해야 합니다.");
                        builder.setPositiveButton("확인",null);
                        builder.create().show();
                    }
                    else{//사업자법인등록번호를 입력한 경우
                        if(!validation_date){//입사일자를 정확히 입력 안한 경우
                                builder.setMessage("입사일자를 정확히 입력해야 합니다.");
                                builder.setPositiveButton("확인",null);
                                builder.create().show();
                        }
                        else{//입사일자를 입력한 경우
                            if(et_yearmoney_1.getText().toString().length()==0){//연소득 금액을 입력 안한 경우
                                builder.setMessage("연소득 금액을 입력해야 합니다.");
                                builder.setPositiveButton("확인",null);
                                builder.create().show();
                            }
                            else{//연소득 금액을 입력한 경우
                                //전세라면 모두 동의를 선택해야 함
                                if(rentType==RENTTYPE.JEONSE){
                                    if(!check_all.isChecked()){//모두 동의 선택 안한 경우
                                        builder.setMessage("모두 동의해야 합니다.");
                                        builder.setPositiveButton("확인",null);
                                        builder.create().show();
                                    }
                                    else{//모두 동의 선택한 경우-이름(name), 사업자(businessnum), 입사일자(businessdate), 연소득금액(year_money)
                                        //available_worker_loan 직장인 대출 가능 여부 보내기 request로!!
                                        Intent homeIntent=new Intent(LoanDetailActivity.this,HomeActivity.class);
                                        PreferenceManager.setString(getApplicationContext(),"name",et_name_1.getText().toString());
                                        PreferenceManager.setInt(getApplicationContext(),"businessnum",Integer.parseInt(et_businessnum.getText().toString()));
                                        PreferenceManager.setString(getApplicationContext(),"businessdate",finalDate);
                                        PreferenceManager.setInt(getApplicationContext(),"year_money",Integer.parseInt(et_yearmoney_1.getText().toString()));

                                        startActivity(homeIntent);

                                    }
                                }
                                //월세라면 통과~~
                                else{//모두 동의 선택한 경우-이름(name), 사업자(businessnum), 입사일자(businessdate), 연소득금액(year_money)
                                    //available_worker_loan 직장인 대출 가능 여부 보내기 request로!!
                                    Intent homeIntent=new Intent(LoanDetailActivity.this,HomeActivity.class);
                                    PreferenceManager.setString(getApplicationContext(),"name",et_name_1.getText().toString());
                                    PreferenceManager.setInt(getApplicationContext(),"businessnum",Integer.parseInt(et_businessnum.getText().toString()));
                                    PreferenceManager.setString(getApplicationContext(),"businessdate",finalDate);
                                    PreferenceManager.setInt(getApplicationContext(),"year_money",Integer.parseInt(et_yearmoney_1.getText().toString()));
                                    startActivity(homeIntent);
                                }
                            }
                        }
                    }
                }
            }
        });

        Log.v("name",PreferenceManager.getString(this,"name"));




    }

    //-------------------------------------------------------------------
    //인풋 필터-한글만 입력
    public InputFilter filterKor = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅎ가-흐]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    //----------------------------------------------------------------------
    //날짜 유효성 검사
    public static boolean isDate(String str, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.KOREA);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(str);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

}