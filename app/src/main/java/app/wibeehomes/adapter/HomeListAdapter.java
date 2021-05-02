package app.wibeehomes.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.wibeehomes.PublicHousing.PublicHousingData;
import app.wibeehomes.R;
import app.wibeehomes.ResidentialFacilities;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeViewHolder> {

    private ArrayList<ResidentialFacilities> datalist;

    // 클릭 이벤트 리스너 -> 해당 URL로 전환
    public interface OnHomeListClickListener {
        void onHomeListItemClick(View v, int pos);
    }
    private HomeListAdapter.OnHomeListClickListener homeListClickListener = null;
    public void setHomeListClickListener(HomeListAdapter.OnHomeListClickListener listener) {
        this.homeListClickListener = listener;
    }

    public HomeListAdapter(ArrayList<ResidentialFacilities> datalist) {
        this.datalist = datalist;
        Log.d("부동산 리스트", Integer.toString(datalist.size()));
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView homeCategory, homeAddress, homeDetail, homeArea;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            homeCategory = itemView.findViewById(R.id.item_home_category);
            homeAddress = itemView.findViewById(R.id.item_home_address);
            homeDetail = itemView.findViewById(R.id.item_home_detail_address);
            homeArea = itemView.findViewById(R.id.item_home_area);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if (homeListClickListener != null) {
                            homeListClickListener.onHomeListItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);
        HomeViewHolder vh = new HomeViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        if (datalist.get(position).gethCate() == 0) {
            holder.homeCategory.setText("아파트");
        } else if (datalist.get(position).gethCate() == 1) {
            holder.homeCategory.setText("오피스텔");
        } else {
            holder.homeCategory.setText("연립주택");
        }

        holder.homeAddress.setText(datalist.get(position).getResident().get_placeAddress());
        holder.homeDetail.setText(datalist.get(position).getResident().get_placeDetailAddress());
        holder.homeArea.setText("면적/층수 : " + Double.toString(datalist.get(position).gethArea()) +"/" + Integer.toString(datalist.get(position).gethFloor()));

    }

    @Override
    public int getItemCount() {
        return (null != datalist ? datalist.size() : 0);
    }


}
