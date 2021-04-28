package app.wibeehomes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import app.wibeehomes.Place;
import app.wibeehomes.R;
import app.wibeehomes.adapter.SearchAdapter;

public class SearchDialog extends Dialog {

    private Context context;
    EditText searchEditText;
    ImageButton searchButton, dismissButton;

    RecyclerView searchRecyclerView;
    SearchAdapter adapter;
    ArrayList<Place> resultPlaces;
    LinearLayoutManager layoutManager;
    SearchDialogListener searchDialogListener;
    Place selectedRow;
    //private FindPlace fp;


    public interface SearchDialogListener {
        // 다이얼로그에서 Activity로 선택된 결과 Place 객체를 보내주기 위한 리스너
        void onSearchResultClick(Place place) throws JSONException;
    }
    public void setSearchDialogListener(SearchDialogListener searchDialogListener){
        this.searchDialogListener = searchDialogListener;
        this.resultPlaces = new ArrayList<Place>();
    }

    public SearchDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        searchEditText = findViewById(R.id.dialog_place_search_bar_EditText);
        searchButton = findViewById(R.id.dialog_place_search_button);
        dismissButton = findViewById(R.id.dialog_search_dismiss_Button);

        searchRecyclerView = findViewById(R.id.dialog_search_RecyclerView);

        layoutManager = new LinearLayoutManager(getContext());
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setHasFixedSize(true);

        // 검색 버튼 액션
        searchButton.setOnClickListener(new View.OnClickListener() {
            String searchPlaceInput = searchEditText.getText().toString();  // 검색어
            @Override
            public void onClick(View view) {
                if(searchPlaceInput.equals("") || searchPlaceInput == null) {
                    Toast.makeText(getContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT);
                }
                else {
                    // 검색 구현
                    // 검색 결과는 resultPlaces 배열에 넣으면 됩니다
                    // HomeActivity.java -> '서치바 다이얼로그 연결' 주석 아래 onSearchResultClick() 매개변수로 지도 위치 설정



                    adapter = new SearchAdapter(resultPlaces);
                    searchRecyclerView.setAdapter(adapter);
                    adapter.setOnSearchClickListener(new SearchAdapter.OnSearchClickListener() {
                        @Override
                        public void onSearchItemClick(View v, int pos) throws JSONException {
                            // 검색 리스트 중에서 클릭 액션 메소드
                            selectedRow = resultPlaces.get(pos); // 선택한 검색 Place 객체
                            searchDialogListener.onSearchResultClick(selectedRow);
                            dismiss();
                        }
                    });

                }
            }
        });

        // 닫힘 버튼 액션
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
