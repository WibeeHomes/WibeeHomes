package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import app.wibeehomes.R;

public class IntroActivity extends AppCompatActivity {

    ImageView intro_image;

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
        }, 3000);

        //앱 시작시 preferencemanager에서 대출 정보(name,businessnum,businessdate,year_money,isSetting_Loan) 초기화
        PreferenceManager.clear(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}