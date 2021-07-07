package com.ateam.mannajob.recycleNotice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.R;
import com.ateam.mannajob.recycleQna.QnADTO;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> implements OnNoticeItemClickListener {
    ArrayList<NoticeDTO> items = new ArrayList<NoticeDTO>();
    OnNoticeItemClickListener listener;
    int layoutType = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.notice_item,viewGroup,false);

        return new ViewHolder(itemView, this, layoutType);  // 화면을 정의 하기 위해 현재 프레그먼트, 리스너, 리사이클 뷰 종류를 넘긴다
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder viewHolder, int position) { // 뷰 홀더와 데이터를 연결시켜준다.
        NoticeDTO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(NoticeDTO item){
        items.add(item);
    }
    public void setItems(ArrayList<NoticeDTO> items){
        this.items = items;
    }
    public NoticeDTO getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListner(OnNoticeItemClickListener listner){ // adapter를 사용하는 클래스에서 리스너를 정의해서 사용할수 있도록 받은 리스너 업캐스팅 하여 연결한다.
        this.listener = listner;
    }

    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position) {  // item 클릭 리스너 역시 마찬가지
        if(listener!=null){
            listener.onItemClick(viewHolder,view,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView n_num_item;
        TextView n_subject_item;
        TextView n_wdate_item;


        public ViewHolder(View itemView, final OnNoticeItemClickListener listener, int layoutType){  // 리싸이클 뷰의 화면을 정의 하는 클래스
            super(itemView);

            n_num_item = itemView.findViewById(R.id.n_num_item);
            n_subject_item = itemView.findViewById(R.id.n_subject_item);
            n_wdate_item = itemView.findViewById(R.id.n_wdate_item);


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
        public void setItem(NoticeDTO item){ // 데이터화 화면 컴포넌트 연결
            n_num_item.setText(Integer.toString(item.getN_num()));
            n_subject_item.setText(item.getN_subject());
            n_wdate_item.setText(item.getN_udate());
        }
    }
}
