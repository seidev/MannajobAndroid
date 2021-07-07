package com.ateam.mannajob.match;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MainActivity;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.OnFragmentItemSelectedListener;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleMatch.MatchAdapter;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.ateam.mannajob.recycleMatch.OnMatchItemClickListener;
import com.ateam.mannajob.recycleMyRequest.MatchDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParserFactory;


import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lib.kingja.switchbutton.SwitchMultiButton;


public class Matching extends Fragment implements MyApplication.OnResponseListener, ServerController {
    private static final String TAG = "Matching";
    RecyclerView matchingRecyc;
    MatchAdapter adapter;
    Context context;
    OnFragmentItemSelectedListener listener;
    SwitchMultiButton switchMultiButton;
    int switchpostion=0;
    EditText search_corp;
    Button search_btn;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof  OnFragmentItemSelectedListener){
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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_matching, container, false);

        initUI(rootView);
        Map<String,String> params = new HashMap<String,String>();
        if(switchpostion ==0){
            params.put("b_category","A");
            ServerSend("MatchList",params);
        }else if(switchpostion == 1){
            params.put("b_category","B");
            ServerSend("MatchList",params);
        }
        return rootView;
    }
    private  void  initUI(ViewGroup rootview) {
        matchingRecyc = rootview.findViewById(R.id.RecyclerView_b_match);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        matchingRecyc.setLayoutManager(layoutManager);
        adapter = new MatchAdapter();

        switchMultiButton = rootview.findViewById(R.id.switchButton);
        switchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                Map<String,String> params = new HashMap<String,String>();
                if(position ==0){
                    params.put("b_category","A");
                    ServerSend("MatchList",params);
                }else if(position == 1){
                    params.put("b_category","B");
                    ServerSend("MatchList",params);
                }
                switchpostion = position;
            }
        });

//////////adapter 리스너 정의//////
        adapter.setOnItemClickListner(new OnMatchItemClickListener() {
            @Override
            public void onItemClick(MatchAdapter.ViewHolder viewHolder, View view, int position) {
                BMatchDTO item = adapter.getItem(position);
                listener.onTabSelected(AppConstants.FRAGMENT_BOARD_MATCH,item);
            }
        });

        search_corp = rootview.findViewById(R.id.search_corp);
        search_btn = rootview.findViewById(R.id.search_corp_btn);

        search_btn.setOnClickListener(v -> {
            Map<String,String> params = new HashMap<String,String>();
            params.put("b_corp",search_corp.getText().toString());
            if(switchpostion ==0){
                params.put("b_category","A");
                ServerSend("MatchSearch",params);
            }else if(switchpostion == 1){
                params.put("b_category","B");
                ServerSend("MatchSearch",params);
            }
        });


    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;
        if(responseCode==200){
            if(requestCode == AppConstants.MATCHDATA){
                gson=  new Gson();
                type = new TypeToken<ArrayList<BMatchDTO>>(){}.getType();
                ArrayList<BMatchDTO> bmatchlist = gson.fromJson(response, type);
                adapter.setItems(bmatchlist);

                matchingRecyc.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }else if(requestCode == AppConstants.SEARCHMATCH){
                gson=  new Gson();
                type = new TypeToken<ArrayList<BMatchDTO>>(){}.getType();
                ArrayList<BMatchDTO> bmatchlist = gson.fromJson(response, type);
                adapter.setItems(bmatchlist);

                matchingRecyc.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
        if(cmd.equals("MatchList")){
            url +="rest/bmatch/list.json";
            MyApplication.send(AppConstants.MATCHDATA, Request.Method.POST,url,params,this);
        }else if(cmd.equals("MatchSearch")){
            url +="rest/bmatch/search.json";
            MyApplication.send(AppConstants.SEARCHMATCH,Request.Method.POST,url,params,this);
        }
    }
}