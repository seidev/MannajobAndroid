package com.ateam.mannajob.match;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleMyRequest.MatchDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopOkMatching extends Activity implements MyApplication.OnResponseListener, ServerController {

    RadioGroup mat_radio_group;
    Button mat_ok_btn;
    Button mat_cancel_btn;
    int chkradio;
    String b_num;
    ArrayList<MatchDTO> list;
    int radioItemCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_pop_ok_matching);
        Intent intent = getIntent();
        b_num = intent.getExtras().get("b_num").toString();
        Map<String,String> params = new HashMap<String,String>();
        params.put("b_num",b_num);
        ServerSend("matchoklist",params);

        initUI();
    }

    private void initUI(){
        mat_radio_group = findViewById(R.id.mat_radio_group);
        mat_ok_btn = findViewById(R.id.mat_ok_btn);
        mat_cancel_btn = findViewById(R.id.mat_cancel_btn);
        mat_radio_group.setOnCheckedChangeListener(groupClickListener);

        mat_ok_btn.setOnClickListener(v -> {
            if(chkradio==0){
                Toast.makeText(getApplicationContext(),"게시글을 취소합니다.",Toast.LENGTH_SHORT).show();
                Map<String,String> params = new HashMap<String,String>();
                //mat_num, m_id, b_num
                params.put("b_num",b_num);
                ServerSend("bmatchcancel",params);
            }else{
                Map<String,String> params = new HashMap<String,String>();
                //mat_num, m_id, b_num
                params.put("b_num",b_num);
                params.put("m_id",list.get(chkradio-1).getM_id());
                params.put("mat_num",Integer.toString(list.get(chkradio-1).getMat_num()));
                ServerSend("matchok",params);

            }
        });
        mat_cancel_btn.setOnClickListener(v -> {
            finish();
        });
    }
    @SuppressLint("ResourceType")
    public void  AddRadioGrouplist(){

    }

    RadioGroup.OnCheckedChangeListener groupClickListener = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            for(int j=0; j<radioItemCnt+1; j++){
                if(i == j){
                    RadioButton radioItem = (RadioButton) mat_radio_group.getChildAt(j);
                    chkradio = j;
                }
            }
        }
    };

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;

        if(responseCode==200){
            if (requestCode == AppConstants.MATCHOKLIST) {
                gson = new Gson();
                type = new TypeToken<ArrayList<MatchDTO>>(){}.getType();
                list = gson.fromJson(response, type);
                if(list != null){
                    radioItemCnt = list.size();
                    for(int i=0;i<radioItemCnt+1;i++){
                        if(i==0) {
                            RadioButton radioButton = new RadioButton(this);
                            radioButton.setText("게시글을 취소합니다.");
                            radioButton.setId(i);
                            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(20, 20, 20, 0);
                            mat_radio_group.addView(radioButton, layoutParams);
                        }else {
                            RadioButton radioButton = new RadioButton(this);
                            radioButton.setText(list.get(i-1).getM_id() + " / " + list.get(i-1).getMat_stdate() + " / " + list.get(i-1).getMat_hour());
                            radioButton.setId(i);
                            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(20, 2, 20, 2);
                            mat_radio_group.addView(radioButton, layoutParams);
                        }
                    }
                }
            }else if(requestCode == AppConstants.MATCHOK){
                if(response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "매칭 결정을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == AppConstants.BMATCHCANCEL){
                if(response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "매칭글이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else if(response.equals("2")){
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "다시 시도해 주세요", Toast.LENGTH_SHORT).show();
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
        String url =AppConstants.URL;
        if(cmd.equals("matchoklist")) {
            url += "rest/match/list.json";
            MyApplication.send(AppConstants.MATCHOKLIST, Request.Method.POST, url, params, this);
        }else if(cmd.equals("matchok")){
            url +="rest/match/matchok.json";
            //mat_num, m_id, b_num
            MyApplication.send(AppConstants.MONTHBMATCH, Request.Method.POST,url,params,this);
        }else if(cmd.equals("bmatchcancel")){
            url +="rest/bmatch/cancel.json";
            //mat_num, m_id, b_num
            MyApplication.send(AppConstants.BMATCHCANCEL, Request.Method.POST,url,params,this);
        }
    }
}