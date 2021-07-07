package com.ateam.mannajob.match;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.ateam.mannajob.recycleReview.recycleCalendar.ReviewAdapter;
import com.ateam.mannajob.recycleReview.recycleCalendar.ReviewDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopReviewView extends Activity implements MyApplication.OnResponseListener, ServerController {
    RecyclerView recycreview;
    ReviewAdapter adapter;
    String m_id;
    ArrayList<ReviewDTO> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_review_view);
        initUI();
    }

    public void initUI(){
        Intent intent = getIntent();
        m_id=intent.getExtras().getString("m_id");

        recycreview =findViewById(R.id.review_recye);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycreview.setLayoutManager(layoutManager);
        adapter = new ReviewAdapter();
        recycreview.setAdapter(adapter);
        HashMap<String, String> params =  new HashMap<>();
        params.put("m_id",m_id);
        ServerSend("ReviewList",params);
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;
        if(responseCode==200){
            if(requestCode == AppConstants.REVIEWLIST){
                gson=  new Gson();
                type = new TypeToken<ArrayList<ReviewDTO>>(){}.getType();
                list = gson.fromJson(response, type);
                if(list.size()==0){
                    Toast.makeText(getApplicationContext(),"리뷰가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    adapter.setItems(list);
                    adapter.notifyDataSetChanged();
                }
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
        }else{
            System.out.println("failure request code :" + requestCode);
        }
    }

    @Override
    public void ServerSend(String cmd, Map<String, String> params) {
        String url = AppConstants.URL;
        if(cmd.equals("ReviewList")){
            url +="rest/review/list.json";
            MyApplication.send(AppConstants.REVIEWLIST, Request.Method.POST,url,params,this);
        }
    }
}