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
        }, 3000);
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}