package com.ateam.mannajob.recycleMyRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.R;

import java.util.ArrayList;

public class RequestMatchAdapter extends RecyclerView.Adapter<RequestMatchAdapter.ViewHolder> implements OnRequestMatchItemClickListener {
    ArrayList<MatchDTO> items = new ArrayList<MatchDTO>();
    OnRequestMatchItemClickListener listener;
    int layoutType = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mypage_request_item,viewGroup,false);

        return new ViewHolder(itemView, this, layoutType);  // 화면을 정의 하기 위해 현재 프레그먼트, 리스너, 리사이클 뷰 종류를 넘긴다
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) { // 뷰 홀더와 데이터를 연결시켜준다.
        MatchDTO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(MatchDTO item){
        items.add(item);
    }
    public void setItems(ArrayList<MatchDTO> items){
        this.items = items;
    }
    public MatchDTO getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListner(OnRequestMatchItemClickListener listner){ // adapter를 사용하는 클래스에서 리스너를 정의해서 사용할수 있도록 받은 리스너 업캐스팅 하여 연결한다.
        this.listener = listner;
    }

    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position,int btn) {  // item 클릭 리스너 역시 마찬가지
        if(listener!=null){
            listener.onItemClick(viewHolder,view,position,btn);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView b_num_request;
        TextView b_subject_request;
        TextView mat_state_request;
        Button request_cancel_btn;
        Button request_review_btn;


        public ViewHolder(View itemView, final OnRequestMatchItemClickListener listener, int layoutType){  // 리싸이클 뷰의 화면을 정의 하는 클래스
            super(itemView);

            b_num_request = itemView.findViewById(R.id.b_num_request);
            b_subject_request = itemView.findViewById(R.id.b_subject_request);
            mat_state_request = itemView.findViewById(R.id.mat_state_request);
            request_cancel_btn = itemView.findViewById(R.id.request_cancel_btn);
            request_review_btn = itemView.findViewById(R.id.request_review_btn);


            request_cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 눌린 리싸이클뷰 인덱스(position을 가지고 리스너 처리가 연결
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position, AppConstants.ADAPTER_BTN_CANCEL);
                    }
                }
            });
            request_review_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 눌린 리싸이클뷰 인덱스(position을 가지고 리스너 처리가 연결
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position, AppConstants.ADAPTER_BTN_REVIEW);
                    }
                }
            });
        }
        public void setItem(MatchDTO item){ // 데이터화 화면 컴포넌트 연결
            b_num_request.setText(Integer.toString(item.getB_num()));
            b_subject_request.setText(item.getB_subject());

            String state = item.getMat_state(); //A,B,C,D //요청,거절,완료,취소
            if(state.equals("A")){
                mat_state_request.setText(AppConstants.REQUEST);
            }else if(state.equals("B")){
                mat_state_request.setText(AppConstants.REJECT);
                request_cancel_btn.setVisibility(View.GONE);
            }else if(state.equals("C")){
                mat_state_request.setText(AppConstants.FINISH);
                request_cancel_btn.setVisibility(View.GONE);
                request_review_btn.setVisibility(View.VISIBLE);
            }else if(state.equals("D")){
                mat_state_request.setText(AppConstants.CANCEL);
                request_cancel_btn.setVisibility(View.GONE);
            }
        }
    }
}
