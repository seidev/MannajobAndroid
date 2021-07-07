package com.ateam.mannajob.recycleMatch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MainActivity;
import com.ateam.mannajob.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> implements OnMatchItemClickListener{
    ArrayList<BMatchDTO> items = new ArrayList<BMatchDTO>();
    OnMatchItemClickListener listener;
    int layoutType = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.match_item, viewGroup,false);

        return new ViewHolder(itemView, this, layoutType);  // 화면을 정의 하기 위해 현재 프레그먼트, 리스너, 리사이클 뷰 종류를 넘긴다
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.ViewHolder viewHolder, int position) { // 뷰 홀더와 데이터를 연결시켜준다.
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

    public void setOnItemClickListner(OnMatchItemClickListener listner){ // adapter를 사용하는 클래스에서 리스너를 정의해서 사용할수 있도록 받은 리스너 업캐스팅 하여 연결한다.
        this.listener = listner;
    }

    @Override
    public void onItemClick(ViewHolder viewHolder, View view, int position) {  // item 클릭 리스너 역시 마찬가지
        if(listener!=null){
            listener.onItemClick(viewHolder,view,position);
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{


        TextView b_num;
        TextView b_subject;
        TextView b_corp;
        TextView b_task;
        TextView b_price;
        TextView b_m_id;
        TextView b_location;
        TextView b_wdate;
        CircleImageView image_profile;



        public ViewHolder(View itemView, final OnMatchItemClickListener listener, int layoutType){  // 리싸이클 뷰의 화면을 정의 하는 클래스
            super(itemView);

            b_num = itemView.findViewById(R.id.b_num);
            b_subject = itemView.findViewById(R.id.b_subject);
            b_corp = itemView.findViewById(R.id.b_corp);
            b_task = itemView.findViewById(R.id.b_task);
            b_price = itemView.findViewById(R.id.b_price);
            b_m_id = itemView.findViewById(R.id.b_m_id);
            b_location = itemView.findViewById(R.id.b_location);
            b_wdate = itemView.findViewById(R.id.b_wdate);
            image_profile = itemView.findViewById(R.id.image_profile);


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
        public void setItem(BMatchDTO item){ // 데이터화 화면 컴포넌트 연결
            b_num.setText(Integer.toString(item.getB_num()));
            b_subject.setText(item.getB_subject());
            b_corp.setText(item.getB_corp());
            b_task.setText(item.getB_task());
            b_price.setText(Integer.toString(item.getB_price()));
            b_m_id.setText(item.getM_id());
            b_location.setText(item.getB_location());
            b_wdate.setText(item.getB_wdate().substring(0,11));
            String imageName = item.getProfileImage();
            MainActivity activity = new MainActivity();
            activity.getImageToServer(image_profile ,imageName);
        }
    }
}
