package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.wibeehomes.R;

public class HomeDetailActivity extends AppCompatActivity {

    private boolean check1=false; //bus 마커 체크
    private boolean check2=false; //subway 마커 체크
    private boolean check3=false; //convenience 마커 체크
    private boolean check4=false; //cart 마커 체크
    Button btn_bus,btn_subway,btn_convenience_store,btn_cart;


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




    }


}