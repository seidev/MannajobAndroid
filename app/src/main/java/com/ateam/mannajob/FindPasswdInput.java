package com.ateam.mannajob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;


public class FindPasswdInput extends Activity implements MyApplication.OnResponseListener, ServerController{

EditText id;
EditText email;
EditText name;
Button okButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_passwd_input);

        initUI();
    }

    public void initUI(){
        id = findViewById(R.id.findpasswd_id);
        email = findViewById(R.id.findpasswd_email);
        name = findViewById(R.id.findpasswd_name);
        okButton = findViewById(R.id.findpasswd_ok_btn);
        okButton.setOnClickListener(v -> {
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("m_id",id.getText().toString());
            params.put("m_email",email.getText().toString());
            params.put("m_name",name.getText().toString());

            ServerSend("findpasswd",params);

        });
    }
    @Override
    public void ServerSend(String cmd, Map<String,String> params) {
        String url =AppConstants.URL;
        if(cmd.equals("findpasswd")){
            url +="rest/login/findpasswd";
            MyApplication.send(AppConstants.FINDPASSWD, Request.Method.POST,url,params,this);
        }
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        if(responseCode==200){
            if(requestCode == AppConstants.FINDPASSWD){
                if(response.equals("1")){
                    Intent intent = new Intent(this, FindPasswdReset.class);
                    intent.putExtra("m_id",id.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"입력된 정보가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
        }else{
            System.out.println("failure request code :" + requestCode);
        }
    }

}