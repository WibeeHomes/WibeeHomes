package app.wibeehomes.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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


    interface SearchDialogListener {
        void onSearchResultClick(Place place) throws JSONException;
    }
    public void setSearchDialogListener(SearchDialogListener searchDialogListener){
        this.searchDialogListener = searchDialogListener;
        this.resultPlaces = new ArrayList<Place>();
    }

    public SearchDialog(@NonNull Context context) {
        super(context);
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


        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
