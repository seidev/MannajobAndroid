package com.ateam.mannajob.recycleCalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.R;

import java.util.ArrayList;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> implements OnCalendarItemClickListener {
    ArrayList<CalendarDTO> items = new ArrayList<CalendarDTO>();
    OnCalendarItemClickListener listener;
    int layoutType = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.calendar_item,viewGroup,false);

        return new ViewHolder(itemView, this, layoutType);  // 화면을 정의 하기 위해 현재 프레그먼트, 리스너, 리사이클 뷰 종류를 넘긴다
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder viewHolder, int position) { // 뷰 홀더와 데이터를 연결시켜준다.
        CalendarDTO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(CalendarDTO item){
        items.add(item);
    }
    public void setItems(ArrayList<CalendarDTO> items){
        this.items = items;
    }
    public CalendarDTO getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListner(OnCalendarItemClickListener listner){ // adapter를 사용하는 클래스에서 리스너를 정의해서 사용할수 있도록 받은 리스너 업캐스팅 하여 연결한다.
        this.listener = listner;
    }

    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position) {  // item 클릭 리스너 역시 마찬가지
        if(listener!=null){
            listener.onItemClick(viewHolder,view,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView cal_m_id;
        TextView cal_m_phone;
        TextView b_location;
        TextView cal_mat_date;
        TextView cal_mat_hour;


        public ViewHolder(View itemView, final OnCalendarItemClickListener listener, int layoutType){  // 리싸이클 뷰의 화면을 정의 하는 클래스
            super(itemView);

            cal_m_id = itemView.findViewById(R.id.cal_m_id);
            cal_m_phone = itemView.findViewById(R.id.cal_m_phone);
            b_location = itemView.findViewById(R.id.b_location);
            cal_mat_date = itemView.findViewById(R.id.cal_mat_date);
            cal_mat_hour = itemView.findViewById(R.id.cal_mat_hour);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 눌린 리싸이클뷰 인덱스(position을 가지고 리스너 처리가 연결
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }
        public void setItem(CalendarDTO item){ // 데이터화 화면 컴포넌트 연결
            cal_m_id.setText(item.getM_id());
            cal_m_phone.setText(item.getPhone());
            b_location.setText(item.getB_location());
            cal_mat_date.setText(item.getMat_stdate());
            cal_mat_hour.setText(item.getMat_hour());
        }
    }
}
