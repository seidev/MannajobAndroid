package com.ateam.mannajob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Join extends AppCompatActivity implements MyApplication.OnResponseListener,ServerController {

    TextView id;
    TextView passwd;
    TextView passwd_re;
    TextView name;
    TextView phone;
    TextView email;
    Button reg_ok_btn;
    Button reg_cancel_btn;
    String useremail;
    String username;
    String userid;
    String m_api;
    TextView txtId;
    TextView txtREpasswd;
    TextView txtName;
    TextView txtEmail;
    TextView txtPasswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Intent intent = getIntent();
        m_api = intent.getExtras().getString("m_api");
        if(intent.getExtras().getString("m_id") !=null) {
            userid = intent.getExtras().getString("m_id");
            if (m_api.equals("n")) {
                useremail = intent.getExtras().getString("m_email");
            }
            username = intent.getExtras().getString("m_name");
            Log.d("회원가입",username+userid+useremail);
        }


        UIinit();
        reg_ok_btn.setOnClickListener(v -> {
            Pattern p_id = Pattern.compile("^[a-zA-Z0-9]*$");
            Pattern p_passwd = Pattern.compile("^[a-zA-Z0-9]{8,}$");
            Pattern p_name = Pattern.compile("^[a-zA-Z가-힣]*$");
            Pattern p_email = Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");
            Pattern p_phone = Pattern.compile("^\\d{3}-\\d{3,4}-\\d{4}$");
            Matcher mpasswd = p_passwd.matcher(passwd.getText().toString());
            Matcher mphone = p_phone.matcher(phone.getText().toString());

//          정규표현식 사용
            if(!mpasswd.find()){
                Toast.makeText(getApplicationContext(),"비밀번호를 영문+숫자로 8자리이상 입력하세요",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!mphone.find()){
                Toast.makeText(getApplicationContext(),"전화번호 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                return;
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("m_passwd",passwd.getText().toString());
            params.put("m_phone",phone.getText().toString());
            if(m_api.equals("n") || m_api.equals("k")) {
                params.put("m_id",userid);
                params.put("m_api",m_api);
                params.put("m_name",username);
                if(m_api.equals("n")) {
                    params.put("m_email", useremail);
                }else{
                    params.put("m_email",email.getText().toString());
                }
                ServerSend("apijoin", params);
            }else {
                if(!passwd.getText().toString().equals(passwd_re.getText().toString())){
                    Toast.makeText(getApplicationContext(),"패스워드가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

//          정규표현식 사용
                Matcher mid = p_id.matcher(id.getText().toString());
                Matcher mname = p_name.matcher(name.getText().toString());
                Matcher memail = p_email.matcher(email.getText().toString());
                if(!mid.find()){
                    Toast.makeText(getApplicationContext(),"아이디를 영문+숫자로 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!mname.find()){
                    Toast.makeText(getApplicationContext(),"이름을 영어 또는 한글로 입력하세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!memail.find()){
                    Toast.makeText(getApplicationContext(),"이메일 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                params.put("m_id",id.getText().toString());
                params.put("m_name",name.getText().toString());
                params.put("m_email",email.getText().toString());
                Log.d("join",String.valueOf(params));
                ServerSend("memberjoin", params);
            }

        });
        reg_cancel_btn.setOnClickListener(v -> {
            finish();
        });
    }
    private void UIinit(){
        id = findViewById(R.id.reg_id);
        passwd = findViewById(R.id.reg_passwd);
        passwd_re = findViewById(R.id.reg_passwd_re);
        name = findViewById(R.id.reg_name);
        phone = findViewById(R.id.reg_phone);
        email = findViewById(R.id.reg_email);
        reg_ok_btn = findViewById(R.id.reg_ok_btn);
        reg_cancel_btn = findViewById(R.id.reg_exit_btn);
        txtId = findViewById(R.id.textID);
        txtREpasswd= findViewById(R.id.textREPASSWD);
        txtName = findViewById(R.id.textName);
        txtEmail = findViewById(R.id.textEmail);
        txtPasswd = findViewById(R.id.textPasswd);
        if(m_api.equals("n")){
            id.setVisibility(View.GONE);
            passwd_re.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
             txtId.setVisibility(View.GONE);
             txtREpasswd.setVisibility(View.GONE);
             txtName.setVisibility(View.GONE);
             txtEmail.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(210,300,150,10);
             txtPasswd.setLayoutParams(params);
        }
        if(m_api.equals("k")){
            id.setVisibility(View.GONE);
            passwd_re.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            txtId.setVisibility(View.GONE);
            txtREpasswd.setVisibility(View.GONE);
            txtName.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(210,300,150,10);
            txtPasswd.setLayoutParams(params);
        }
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        if(responseCode==200){
            if(requestCode == AppConstants.MEMBERJOIN) {
//                XmlParserCreator parserCreator = () -> {
//                    try{
//                        return XmlPullParserFactory.newInstance().newPullParser();
//                    }catch (Exception e){
//                        throw new RuntimeException(e);
//                    }
//                };
//                GsonXml gsonXml = new GsonXmlBuilder().setXmlParserCreator(parserCreator).setSameNameLists(true).create();
//                check chk = gsonXml.fromXml(response, check.class);
                String result = response;
                if (result.equals("1")) {
                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (result.equals("2")) {
                    Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == AppConstants.APIJOIN){
                String result = response;
                if (result.equals("1")) {
                    Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (result.equals("2")) {
                    Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
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
        if(cmd.equals("memberjoin")){
            url +="rest/memberjoin.json";
            Log.d("url:" , url);
            MyApplication.send(AppConstants.MEMBERJOIN, Request.Method.POST,url,params,Join.this);
        }else if(cmd.equals("apijoin")){
            url +="rest/apijoin.json";
            Log.d("url:" , url);
            MyApplication.send(AppConstants.APIJOIN, Request.Method.POST,url,params,Join.this);
        }
    }
}