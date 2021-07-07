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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FindPasswdReset extends Activity implements MyApplication.OnResponseListener, ServerController{

    EditText passwd;
    EditText passwd_re;
    Button passwd_ok_btn;
    String m_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_passwd_reset);
        Intent intent = getIntent();
        m_id = intent.getExtras().get("m_id").toString();

        initUI();
    }

    public void initUI(){
        passwd = findViewById(R.id.resetpasswd) ;
        passwd_re = findViewById(R.id.resetpasswd2);
        passwd_ok_btn = findViewById(R.id.reset_ok_btn);
        passwd_ok_btn.setOnClickListener(v -> {
            Pattern p_passwd = Pattern.compile("^[a-zA-Z0-9]{8,}$");
            Matcher mpasswd = p_passwd.matcher(passwd.getText().toString());
            if(!mpasswd.find()){
                Toast.makeText(getApplicationContext(),"비밀번호를 영문+숫자로 8자리이상 입력하세요",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!passwd.getText().toString().equals(passwd_re.getText().toString())){
                Toast.makeText(getApplicationContext(),"패스워드가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("m_passwd",passwd.getText().toString());
            params.put("m_passwd2",passwd_re.getText().toString());
            params.put("m_id",m_id);
            ServerSend("resetpasswd",params);
        });
    }

    @Override
    public void ServerSend(String cmd, Map<String,String> params) {
        String url =AppConstants.URL;
        if(cmd.equals("resetpasswd")){
            url +="rest/login/resetpasswd";
            MyApplication.send(AppConstants.RESETPASSWD, Request.Method.POST,url,params,this);
        }
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        if(responseCode==200){
            if(requestCode == AppConstants.RESETPASSWD){
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(),"비밀번호 변경이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
        }else{
            System.out.println("failure request code :" + requestCode);
        }
    }

}