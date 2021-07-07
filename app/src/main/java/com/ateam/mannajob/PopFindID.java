package com.ateam.mannajob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

public class PopFindID extends Activity implements MyApplication.OnResponseListener, ServerController{
    TextView findid;
    EditText email;
    EditText name;
    Button findOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_find_id);
        initUI();

    }
    public void initUI(){
        findid = findViewById(R.id.find_id);
        email = findViewById(R.id.findid_email);
        name = findViewById(R.id.findid_name);
        findOkBtn = findViewById(R.id.find_ok_btn);
        findOkBtn.setOnClickListener(v->
        {
            HashMap<String, String> params = new HashMap<>();
            params.put("m_email",email.getText().toString());
            params.put("m_name",name.getText().toString());
            ServerSend("findid",params);
        });
    }
    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        if(responseCode==200){
            if(requestCode == AppConstants.LOGINSTATE){
                String m_id = response;
                if(m_id==null){
                    Toast.makeText(getApplicationContext(),"정보와 동일한 회원이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    findid.setVisibility(View.VISIBLE);
                    findid.setText(m_id);
                }
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
        }else{
            System.out.println("failure request code :" + requestCode);
        }
    }
    @Override
    public void ServerSend(String cmd, Map<String,String> params) {
        String url =AppConstants.URL;
        if(cmd.equals("findid")){
            url +="rest/login/findid";
            MyApplication.send(AppConstants.LOGINSTATE, Request.Method.POST,url,params,this);
        }
    }
}