package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class LoanDetailActivity extends AppCompatActivity {

    EditText et_name_1,et_name_2;//고객한글명 (비상금,직장인)
    EditText et_businessnum; //사업자법인등록번호
    EditText et_date; //입사일자
    EditText et_yearmoney_1,et_yearmoney_2;//연소득금액 (직장인,전세대출)


    /*
    잃어버린 코드목록
    *고객한글명
    -한글만 입력하도록 inputfilter
    -비상금에 입력시 직장인에도 복사
    *입사일자
    -날짜 유효성 검사
    -날짜 입력시 '/' 두군데 넣기
    *연소득금액
    -직장인에 입력시 전세대출에 복사
    *동의
    -모두동의, 첫번째동의, 두번째동의, 세번쨰 동의
    *intent로 월세가 넘어왔다면->전세대출 layout은 invisible
    *intent로 넘어온 값(from homecondition) 같이 추가해서 넘기기 (to home)
    *enum 추가-전세/월세
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        et_name_1=findViewById(R.id.loandetail_et_name_1);
        et_name_2=findViewById(R.id.loandetail_et_name_2);
        et_businessnum=findViewById(R.id.loandetail_et_businessnum);
        et_date=findViewById(R.id.loandetail_et_date);
        et_yearmoney_1=findViewById(R.id.loandetail_et_yearmoney_1);
        et_yearmoney_2=findViewById(R.id.loandetail_et_yearmoney_2);







    }
}