package app.wibeehomes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import app.wibeehomes.Place;
import app.wibeehomes.R;

/**
 * 장소 검색 Dialog RecyclerView Adapter
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private ArrayList<Place> datalist;

    public interface OnSearchClickListener{
        void onSearchItemClick(View v, int pos) throws JSONException;
    }
    private SearchAdapter.OnSearchClickListener searchClickListener = null;
    public void setOnSearchClickListener(SearchAdapter.OnSearchClickListener listener){
        this.searchClickListener = listener;
    }

    public SearchAdapter(ArrayList<Place> data) { this.datalist = data; }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView placeTitle, placeDetail;
        public SearchViewHolder(@NonNull View v) {
            super(v);
            placeTitle = v.findViewById(R.id.item_search_tv_big_location);
            placeDetail = v.findViewById(R.id.item_search_tv_detail);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(searchClickListener != null){
                            try {
                                searchClickListener.onSearchItemClick(view, pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_map, parent, false);
        SearchAdapter.SearchViewHolder vh = new SearchAdapter.SearchViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.placeTitle.setText(datalist.get(position).get_placeAddress());
        holder.placeDetail.setText(datalist.get(position).get_placeDetailAddress());
    }

    @Override
    public int getItemCount() {
        return (datalist != null ? datalist.size() : 0);
    }

}
