package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import app.wibeehomes.R;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent introintent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(introintent);
                finish();
            }
        }, 1500);

        //앱 시작시 preferencemanager에서 대출 정보(name,businessnum,businessdate,year_money,isSetting_Loan) 초기화
        PreferenceManager.removeKey(this,"name");
        PreferenceManager.removeKey(this,"businessdate");
        PreferenceManager.removeKey(this,"year_money");
        PreferenceManager.setBoolean(getApplicationContext(),"isSetting_Loan",false);
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}