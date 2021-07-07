package com.ateam.mannajob.recycleReview.recycleCalendar;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateam.mannajob.R;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements OnReviewItemClickListener {
    ArrayList<ReviewDTO> items = new ArrayList<ReviewDTO>();
    OnReviewItemClickListener listener;
    int layoutType = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.review_item,viewGroup,false);

        return new ViewHolder(itemView, this, layoutType);  // 화면을 정의 하기 위해 현재 프레그먼트, 리스너, 리사이클 뷰 종류를 넘긴다
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder viewHolder, int position) { // 뷰 홀더와 데이터를 연결시켜준다.
        ReviewDTO item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(ReviewDTO item){
        items.add(item);
    }
    public void setItems(ArrayList<ReviewDTO> items){
        this.items = items;
    }
    public ReviewDTO getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListner(OnReviewItemClickListener listner){ // adapter를 사용하는 클래스에서 리스너를 정의해서 사용할수 있도록 받은 리스너 업캐스팅 하여 연결한다.
        this.listener = listner;
    }

    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position) {  // item 클릭 리스너 역시 마찬가지
        if(listener!=null){
            listener.onItemClick(viewHolder,view,position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView review_good_image;
        TextView review_contents;
        TextView review_w_id;
        TextView review_wdate;



        public ViewHolder(View itemView, final OnReviewItemClickListener listener, int layoutType){  // 리싸이클 뷰의 화면을 정의 하는 클래스
            super(itemView);

            review_good_image = itemView.findViewById(R.id.review_good_image);
            review_contents = itemView.findViewById(R.id.review_contents);
            review_w_id = itemView.findViewById(R.id.review_w_id);
            review_wdate = itemView.findViewById(R.id.review_wdate);



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
        public void setItem(ReviewDTO item){ // 데이터화 화면 컴포넌트 연결
            review_contents.setText(item.getR_contents());
            if(item.getR_good().equals("G")){
                review_good_image.setImageResource(R.drawable.like32);
            }else {
                review_good_image.setImageResource(R.drawable.dislike32);
            }
            review_w_id.setText(item.getR_w_m_id());
            review_wdate.setText(item.getR_wdate());
        }
    }
}
