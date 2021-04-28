package app.wibeehomes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import app.wibeehomes.dialog.SearchDialog;


public class HomeActivity extends AppCompatActivity {

    private LinearLayout publicHousingLinearLayout, conditionLinearLayout;

    /* 장소 검색 관련 컴포넌트 */
    private LinearLayout searchBarLinearLayout;
    private TextView searchBarTextView;
    private SearchDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        publicHousingLinearLayout = findViewById(R.id.home_ll_menu2);
        searchBarLinearLayout = findViewById(R.id.home_ll_search_bar);
        conditionLinearLayout = findViewById(R.id.home_ll_condition);

        // 메뉴전환 : 공공주택사업
        publicHousingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PublicHousingActivity.class);
                startActivity(intent);
            }
        });

        //------------------------------------------------------------------------------------------
        /* 서치바 연결 */
        searchBarTextView = findViewById(R.id.home_tv_search_bar);

        // 서치바 다이얼로그 연결
        searchBarLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new SearchDialog(HomeActivity.this);
                dialog.setSearchDialogListener(new SearchDialog.SearchDialogListener() {
                    @Override
                    public void onSearchResultClick(Place place) throws JSONException {
                        // 매개변수 : 다이얼로그에서 선택한 place 객체
                        searchBarTextView.setText(place.get_placeAddress());

                        // place 객체로 위치 설정

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

        // 조건 다이얼로그 연결
        conditionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }




}