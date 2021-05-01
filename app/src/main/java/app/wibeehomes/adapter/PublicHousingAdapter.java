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

/**
 * 공공주택사업 Activity RecyclerView Adapter
 */
public class PublicHousingAdapter extends RecyclerView.Adapter<PublicHousingAdapter.PHViewHolder> {

    private ArrayList<PublicHousingData> datalist;

    // 클릭 이벤트 리스너 -> 해당 URL로 전환
    public interface OnPublicHousingClickListener {
        void onPublicHousingItemClick(View v, int pos);
    }
    private OnPublicHousingClickListener publicHousingClickListener = null;
    public void setPublicHousingClickListener(OnPublicHousingClickListener listener) {
        this.publicHousingClickListener = listener;
    }

    public PublicHousingAdapter(ArrayList<PublicHousingData> data) {
        this.datalist = data;
        Log.d("공공주택사업 데이터 크기", Integer.toString(datalist.size()));
    }

    public class PHViewHolder extends RecyclerView.ViewHolder {
        private TextView publichousingTitle, publicHousingCategory, publicHousingDate;
        public PHViewHolder(@NonNull View v) {
            super(v);
            publicHousingCategory = v.findViewById(R.id.item_ph_tv_category);
            publichousingTitle = v.findViewById(R.id.item_ph_tv_title);
            publicHousingDate = v.findViewById(R.id.item_ph_tv_date);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if (publicHousingClickListener != null) {
                            publicHousingClickListener.onPublicHousingItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public PHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_public_housing, parent, false);
        PHViewHolder vh = new PHViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PHViewHolder holder, int position) {
        holder.publicHousingCategory.setText(datalist.get(position).getCategory());
        holder.publichousingTitle.setText(datalist.get(position).getTitle());
        holder.publicHousingDate.setText("등록일 : "+datalist.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return (null != datalist ? datalist.size() : 0);
    }



}
