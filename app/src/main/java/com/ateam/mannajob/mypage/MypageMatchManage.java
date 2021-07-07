package com.ateam.mannajob.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.OnFragmentItemSelectedListener;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.match.PopOkMatching;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.ateam.mannajob.recycleMyMatch.MyMatchAdapter;
import com.ateam.mannajob.recycleMyMatch.OnMyMatchItemClickListener;
import com.ateam.mannajob.recycleMyRequest.MatchDTO;
import com.ateam.mannajob.recycleMyRequest.OnRequestMatchItemClickListener;
import com.ateam.mannajob.recycleMyRequest.RequestMatchAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MypageMatchManage extends Fragment implements MyApplication.OnResponseListener, ServerController {
    private static final String TAG = "MypageMatchManage";
    RecyclerView mypage_requset_state_recyc;
    RequestMatchAdapter requestAdapter;
    RecyclerView mypage_edit_state_recyc;
    MyMatchAdapter myMatchAdapter;
    Context context;
    OnFragmentItemSelectedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;
        }
    }
    @Override
    public void onDetach(){
        super.onDetach();
        if(context!= null){
            context=null;
            listener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage_match_manage, container, false);
        initUI(rootView);
        return rootView;
    }
    private  void  initUI(ViewGroup rootview) {
        mypage_edit_state_recyc = rootview.findViewById(R.id.mypage_edit_state_recyc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mypage_edit_state_recyc.setLayoutManager(layoutManager);
        myMatchAdapter = new MyMatchAdapter();

        SettingEditRecyc();

        ServerSend("Bmatchlist",null);

        mypage_requset_state_recyc = rootview.findViewById(R.id.mypage_request_state_recyc);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        mypage_requset_state_recyc.setLayoutManager(layoutManager2);
        requestAdapter = new RequestMatchAdapter();

        SettingRequestRecyc();

        ServerSend("Matchlist",null);


    }


//        작성 현황 프래그먼트
    public void SettingEditRecyc(){

//////////adapter 리스너 정의//////
        myMatchAdapter.setOnItemClickListner(new OnMyMatchItemClickListener() {
            @Override
            public void onItemClick(MyMatchAdapter.ViewHolder viewHolder, View view, int position,int btn) {
                if(btn == AppConstants.ADAPTER_BTN_OK){
                    BMatchDTO item = myMatchAdapter.getItem(position);
                    Intent intent = new Intent(context, PopOkMatching.class);
                    intent.putExtra("b_num",item.getB_num());
                    startActivity(intent);
                }else if(btn == AppConstants.ADAPTER_BTN_REVIEW){
                    BMatchDTO item = myMatchAdapter.getItem(position);
                    Intent intent = new Intent(context, PopReview.class);
                    intent.putExtra("type","2"); // 글쓴 사람이 작성하는 리뷰
                    intent.putExtra("b_num",Integer.toString(item.getB_num()));
                    startActivity(intent);
                }
            }
        });
    }
    public void SettingRequestRecyc(){

//////////adapter 리스너 정의//////
        requestAdapter.setOnItemClickListner(new OnRequestMatchItemClickListener() {
            @Override
            public void onItemClick(RequestMatchAdapter.ViewHolder viewHolder, View view, int position, int btn) {
                if(btn == AppConstants.ADAPTER_BTN_CANCEL){
                    MatchDTO item = requestAdapter.getItem(position);
                    Intent intent = new Intent(context, PopCancelChk.class);

                    intent.putExtra("mat_num", Integer.toString(item.getMat_num()));
                    intent.putExtra("b_num",Integer.toString(item.getB_num()));
                    startActivity(intent);
                }else if(btn == AppConstants.ADAPTER_BTN_REVIEW){
                    MatchDTO item = requestAdapter.getItem(position);

                    Intent intent = new Intent(context, PopReview.class);
                    intent.putExtra("type","1"); // 매칭 신청 한 사람이 작성하는 리뷰
                    intent.putExtra("mat_num", Integer.toString(item.getMat_num()));
                    intent.putExtra("b_num",Integer.toString(item.getB_num()));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;
        if(responseCode==200){
            if (requestCode == AppConstants.BMATCHLIST) {
                if(response == null){
                    Toast.makeText(context,"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                }
                gson = new Gson();
                type = new TypeToken<ArrayList<BMatchDTO>>(){}.getType();
                ArrayList<BMatchDTO> list = gson.fromJson(response, type);
                myMatchAdapter.setItems(list);

                mypage_edit_state_recyc.setAdapter(myMatchAdapter);

            }else if (requestCode == AppConstants.MATCHLIST) {
                if(response == null){
                    Toast.makeText(context,"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                }
                gson = new Gson();
                type = new TypeToken<ArrayList<MatchDTO>>(){}.getType();
                ArrayList<MatchDTO> list = gson.fromJson(response, type);
                requestAdapter.setItems(list);
                mypage_requset_state_recyc.setAdapter(requestAdapter);
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
            System.out.println("failure request code :" + requestCode);
        }
    }

    @Override
    public void ServerSend(String cmd, Map<String, String> params) {
        String url =AppConstants.URL;
        if(cmd.equals("Bmatchlist")){
            url +="rest/bmatchlist.json";
            Log.d("url:" , url);
            MyApplication.send(AppConstants.BMATCHLIST, Request.Method.GET,url,params,this);
        }else if(cmd.equals("Matchlist")){
            url +="rest/matchlist.json";
            Log.d("url:" , url);
            MyApplication.send(AppConstants.MATCHLIST, Request.Method.GET,url,params,this);
        }
    }
}