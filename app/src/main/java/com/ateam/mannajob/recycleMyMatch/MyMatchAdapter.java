package com.ateam.mannajob.recycleMyMatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.R;
import com.ateam.mannajob.recycleMatch.BMatchDTO;

import java.util.ArrayList;

public class MyMatchAdapter extends RecyclerView.Adapter<MyMatchAdapter.ViewHolder> implements OnMyMatchItemClickListener {
    ArrayList<BMatchDTO> items = new ArrayList<BMatchDTO>();
    OnMyMatchItemClickListener listener;
    int layoutType = 0;

    @NonNull
    @Override
    public MyMatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mypage_b_match_item, viewGroup,false);

        return new MyMatchAdapter.ViewHolder(itemView, this, layoutType);  // 화면을 정의 하기 위해 현재 프레그먼트, 리스너, 리사이클 뷰 종류를 넘긴다
    }

    @Override
    public void onBindViewHolder(@NonNull MyMatchAdapter.ViewHolder viewHolder, int position) { // 뷰 홀더와 데이터를 연결시켜준다.
        BMatchDTO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(BMatchDTO item){
        items.add(item);
    }
    public void setItems(ArrayList<BMatchDTO> items){
        this.items = items;
    }
    public BMatchDTO getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListner(OnMyMatchItemClickListener listner){ // adapter를 사용하는 클래스에서 리스너를 정의해서 사용할수 있도록 받은 리스너 업캐스팅 하여 연결한다.
        this.listener = listner;
    }

    @Override
    public void onItemClick(MyMatchAdapter.ViewHolder viewHolder, View view, int position ,int btn) {  // item 클릭 리스너 역시 마찬가지
        if(listener!=null){
            listener.onItemClick(viewHolder,view,position,btn);
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{


        TextView b_num_item;
        TextView b_subject_item;
        TextView b_state_item;
        public Button b_match_end_btn;
        public Button review_btn;




        public ViewHolder(View itemView, final OnMyMatchItemClickListener listener, int layoutType){  // 리싸이클 뷰의 화면을 정의 하는 클래스
            super(itemView);

            b_num_item = itemView.findViewById(R.id.b_num_item);
            b_subject_item = itemView.findViewById(R.id.b_subject_item);
            b_state_item = itemView.findViewById(R.id.b_state_item);
            b_match_end_btn = itemView.findViewById(R.id.b_match_end_btn);
            review_btn = itemView.findViewById(R.id.review_btn);


            b_match_end_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 완료버튼 눌렸을때
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(MyMatchAdapter.ViewHolder.this, view, position,AppConstants.ADAPTER_BTN_OK);
                    }
                }
            });
            review_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // 리뷰버튼 눌렸을때
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(MyMatchAdapter.ViewHolder.this, view, position,AppConstants.ADAPTER_BTN_REVIEW);
                    }
                }
            });
        }
        public void setItem(BMatchDTO item){ // 데이터화 화면 컴포넌트 연결
            b_num_item.setText(Integer.toString(item.getB_num()));
            b_subject_item.setText(item.getB_subject());
            String state = item.getB_state();
            if(state.equals("A")){
                b_state_item.setText(AppConstants.PROCEEDING);
            }else if(state.equals("B")){
                b_state_item.setText(AppConstants.FINISH);
                b_match_end_btn.setVisibility(View.GONE);
                review_btn.setVisibility(View.VISIBLE);
            }else if(state.equals("C")){
                b_state_item.setText(AppConstants.CANCEL);
                b_match_end_btn.setVisibility(View.GONE);
            }
        }
    }
}
