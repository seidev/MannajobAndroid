package com.ateam.mannajob.serivce;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MainActivity;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.OnFragmentItemSelectedListener;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.match.Matching;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.ateam.mannajob.recycleNotice.NoticeAdapter;
import com.ateam.mannajob.recycleNotice.NoticeDTO;
import com.ateam.mannajob.recycleNotice.OnNoticeItemClickListener;
import com.ateam.mannajob.recycleQna.OnQnAItemClickListener;
import com.ateam.mannajob.recycleQna.QnAAdapter;
import com.ateam.mannajob.recycleQna.QnADTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lib.kingja.switchbutton.SwitchMultiButton;


public class Service extends Fragment implements MainActivity.onKeyBackPressedListener, MyApplication.OnResponseListener, ServerController {
    Context context;
    RecyclerView serviceRecyc;
    NoticeAdapter noticeAdapter;
        QnAAdapter qnAAdapter;
    OnFragmentItemSelectedListener listener;
    SwitchMultiButton switchMultiButton;
    EditText search_txt;
    Button search_btn;

    int switchpostion;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof  OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (context != null) {
            context = null;
            listener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_service, container, false);
        initUI(rootView);
        if (switchpostion == 0) {
            showNoticeList();
            ServerSend("Notice",null);
        } else if (switchpostion == 1) {
            showQnAList();
            ServerSend("QnA",null);
        }
        return rootView;
    }

    private void initUI(ViewGroup rootview) {
        switchMultiButton = rootview.findViewById(R.id.service_switchButton);
        switchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                if(position ==0){
                    showNoticeList();
                    ServerSend("Notice",null);
                }else if(position == 1){
                    showQnAList();
                    ServerSend("QnA",null);
                }
                switchpostion = position;
            }
        });
        serviceRecyc = rootview.findViewById(R.id.service_recyc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        serviceRecyc.setLayoutManager(layoutManager);
        search_txt= rootview.findViewById(R.id.search_service_txt);
        search_btn = rootview.findViewById(R.id.search_service_btn);

        search_btn.setOnClickListener(v -> {
            Map<String,String> params = new HashMap<String,String>();
            if (switchpostion == 0) {
                params.put("n_subject",search_txt.getText().toString());
                ServerSend("NoticeSearch",params);
            } else if (switchpostion == 1) {
                params.put("q_subject",search_txt.getText().toString());
                ServerSend("QnASearch",params);
            }
        });
    }

    public void showNoticeList(){

        noticeAdapter = new NoticeAdapter();

        noticeAdapter.setOnItemClickListner(new OnNoticeItemClickListener() {
            @Override
            public void onItemClick(NoticeAdapter.ViewHolder viewHolder, View view, int position) {
                NoticeDTO item = noticeAdapter.getItem(position);
                listener.onTabSelected(AppConstants.FRAGMENT_BOARD_NOTICE,item);
            }
        });
    }

    public void showQnAList() {

        qnAAdapter = new QnAAdapter();

        qnAAdapter.setOnItemClickListner(new OnQnAItemClickListener() {
            @Override
            public void onItemClick(QnAAdapter.ViewHolder viewHolder, View view, int position) {
                QnADTO item = qnAAdapter.getItem(position);
                listener.onTabSelected(AppConstants.FRAGMENT_BOARD_QNA,item);
            }
        });
    }
    @Override
    public void onBackKey() {
        goToMain();
    }

    //프래그먼트 종료
    private void goToMain(){
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Service.this).commit();
        fragmentManager.popBackStack();
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;
        if(responseCode==200){
            if(requestCode == AppConstants.NOTICEDATA){
                gson=  new Gson();
                type = new TypeToken<ArrayList<NoticeDTO>>(){}.getType();
                ArrayList<NoticeDTO> list = gson.fromJson(response, type);
                noticeAdapter.setItems(list);

                serviceRecyc.setAdapter(noticeAdapter);
                noticeAdapter.notifyDataSetChanged();

            }else if(requestCode == AppConstants.QNADATA){
                gson=  new Gson();
                type = new TypeToken<ArrayList<QnADTO>>(){}.getType();
                ArrayList<QnADTO> list = gson.fromJson(response, type);
                qnAAdapter.setItems(list);

                serviceRecyc.setAdapter(qnAAdapter);
                qnAAdapter.notifyDataSetChanged();
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
        }else{
            System.out.println("failure request code :" + requestCode);
        }
    }

    @Override
    public void ServerSend(String cmd, Map<String, String> params) {
        String url =AppConstants.URL;
        if(cmd.equals("Notice")){
            url +="rest/notice/list.json";
            MyApplication.send(AppConstants.NOTICEDATA, Request.Method.GET,url,params,this);
        }else if(cmd.equals("QnA")){
            url +="rest/qna/list.json";
            MyApplication.send(AppConstants.QNADATA,Request.Method.GET,url,params,this);
        }else if(cmd.equals("NoticeSearch")){
            url +="rest/notice/search.json";
            MyApplication.send(AppConstants.NOTICEDATA,Request.Method.POST,url,params,this);
        }else if(cmd.equals("QnASearch")){
            url +="rest/qna/search.json";
            MyApplication.send(AppConstants.QNADATA,Request.Method.POST,url,params,this);
        }
    }
}
