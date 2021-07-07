package com.ateam.mannajob.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleMyRequest.MatchDTO;

import java.util.HashMap;
import java.util.Map;

public class PopCancelChk extends Activity implements MyApplication.OnResponseListener, ServerController {
    Button cancelchk_cancel_btn;
    Button cancelchk_ok_btn;
    String mat_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_pop_cancel_chk);

        Intent intent = getIntent();
        mat_num = intent.getExtras().get("mat_num").toString();
        initUI();
    }

    private void initUI(){
        cancelchk_cancel_btn = findViewById(R.id.cancelchk_cancel_btn);
        cancelchk_ok_btn = findViewById(R.id.cancelchk_ok_btn);
        cancelchk_ok_btn.setOnClickListener(v -> {
            Map<String,String> params = new HashMap<String,String>();
            params.put("mat_num",mat_num);
            ServerSend("matchcancel",params);
        });
        cancelchk_cancel_btn.setOnClickListener(v -> {
            finish();
        });
    }
    @Override
    public void processResponse(int requestCode, int responseCode, String response) {


        if(responseCode==200){
            if (requestCode == AppConstants.MATCHCANCEL) {
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(),"매칭이 취소 되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"다시 시도해 주세요",Toast.LENGTH_SHORT).show();
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
        if(cmd.equals("matchcancel")) {
            url += "rest/match/cancel.json";

            MyApplication.send(AppConstants.MATCHCANCEL, Request.Method.POST, url, params, this);
        }
    }
}