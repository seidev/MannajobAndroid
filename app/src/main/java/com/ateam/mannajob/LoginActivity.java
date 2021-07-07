package com.ateam.mannajob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements MyApplication.OnResponseListener,ServerController{
    public static OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
    private SessionCallback sessionCallback;

    private String accessToken;
    public Map<String,String> mUserInfoMap;
    private String category;
    private String id;
    private MeV2Response kakaoresult;


    private static Context mContext;

    private static TextView mOauthAT;
    private static TextView mOauthRT;
    private static TextView mOauthExpires;
    private static TextView mOauthTokenType;
    private static TextView mOAuthState;
    EditText ID;
    EditText PASSWORD;
    Button loginButton;
    Button registerButton;
    TextView findid;
    TextView findpasswd;

    LoginButton kakaobtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        naverLogin();
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private void init(){
        ID = findViewById(R.id.login_id);
        PASSWORD =findViewById(R.id.login_passwd);
        loginButton = findViewById(R.id.login_ok_btn);
        // 아이디 찾기
        findid = findViewById(R.id.findid);
        findid.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),PopFindID.class);
            startActivity(intent);
        });
        // 패스워드 찾기
        findpasswd = findViewById(R.id.findpasswd);
        findpasswd.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),FindPasswdInput.class);
            startActivity(intent);
        });




        kakaobtn = findViewById(R.id.kakaobtn);
        kakaobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"카카오 로그인.",Toast.LENGTH_SHORT).show();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> params = new HashMap<String,String>();
                String id = ID.getText().toString();
                String passwd = PASSWORD.getText().toString();
                params.put("m_id",id);
                params.put("m_passwd",passwd);
                Log.d("m_id",id);
                Log.d("m_passwd",passwd);
                ServerSend("logincheck",params);
            }
        });

        registerButton=findViewById(R.id.register_m_Btn);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinAgree.class);
                intent.putExtra("m_api","x");
                startActivity(intent);
            }
        });
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void ServerSend(String cmd, Map<String, String> params) {
        String url =AppConstants.URL;
        if(cmd.equals("logincheck")){
            url +="rest/logincheck.json";
            Log.d("url:" , url);
            MyApplication.send(AppConstants.LOGINCHECK, Request.Method.POST,url,params,LoginActivity.this);
        }else if(cmd.equals("membercheck")){
            url +="rest/membercheck.json";
            MyApplication.send(AppConstants.MEMBERCHECK,Request.Method.POST,url,params,LoginActivity.this);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        if(responseCode==200){
            if(requestCode == AppConstants.LOGINCHECK){

                String loginckh = response;
                if(loginckh.equals("1")){
                    Toast.makeText(getApplicationContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }else if(loginckh.equals("3")){
                    Toast.makeText(getApplicationContext(),"회원가입 정보가 없습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"로그인 정보가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == AppConstants.MEMBERCHECK){
                String memckh = response;
                if(memckh.equals("1")) {
                    if(category.equals("n")) {
                        PreferenceManager.setString(getApplicationContext(), "id", mUserInfoMap.get("id"));
                    }else if(category.equals("k")) {
                        PreferenceManager.setString(getApplicationContext(), "id", id);
                    }
                    Toast.makeText(getApplicationContext(),"로그인 되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    if(category.equals("n")) {
                        Intent intent = new Intent(getApplicationContext(), JoinAgree.class);
                        intent.putExtra("id", mUserInfoMap.get("id"));
                        intent.putExtra("m_api", "n");
                        intent.putExtra("email", mUserInfoMap.get("email"));
                        intent.putExtra("name", mUserInfoMap.get("name"));
                        startActivity(intent);
                    }else if(category.equals("k")) {
                            Intent intent = new Intent(getApplicationContext(), JoinAgree.class);
                            intent.putExtra("id", id);
                            intent.putExtra("m_api","k");
                            intent.putExtra("name", kakaoresult.getNickname());
                            startActivity(intent);
                    }
                }
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
        }else{
            System.out.println("failure request code :" + requestCode);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////// 네이버
    private void naverLogin(){
        mOAuthLoginModule.init(
                com.ateam.mannajob.LoginActivity.this
                ,"_eXpHwpM6yRnIuDaTTcR"
                , "HmouEO_Gpg"
                ,"mannajob"
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );
        final OAuthLoginButton mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

    }
    @SuppressLint("HandlerLeak")
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                 accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);
                Log.d("accessToken : ",accessToken);
                Log.d("refreshToken : ",refreshToken);
                Log.d("expiresAt : ",String.valueOf(expiresAt));
                Log.d("tokenType : ",tokenType);
                Log.d("LoginModule:getState  :",mOAuthLoginModule.getState(mContext).toString());
                new RequestApiTask().execute();
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };
    private class RequestApiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginModule.getAccessToken(mContext);
            mUserInfoMap = requestNaverUserInfo(mOAuthLoginModule.requestApi(mContext, at, url));

            Map<String,String> idparams = new HashMap<String,String>();
            idparams.put("m_id",mUserInfoMap.get("id"));
            category = "n";
            ServerSend("membercheck",idparams);
            return null;
        }

        protected void onPostExecute(Void content) {
            if (mUserInfoMap.get("email") == null) {
                Toast.makeText(mContext, "로그인 실패하였습니다.  잠시후 다시 시도해 주세요!!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("naver", String.valueOf(mUserInfoMap));

            }

        }
    }
    private Map<String,String> requestNaverUserInfo(String data) { // xml 파싱
        String f_array[] = new String[9];

        try {
            XmlPullParserFactory parserCreator = XmlPullParserFactory
                    .newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            InputStream input = new ByteArrayInputStream(
                    data.getBytes("UTF-8"));
            parser.setInput(input, "UTF-8");

            int parserEvent = parser.getEventType();
            String tag;
            boolean inText = false;
            boolean lastMatTag = false;

            int colIdx = 0;

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if (tag.compareTo("xml") == 0) {
                            inText = false;
                        } else if (tag.compareTo("data") == 0) {
                            inText = false;
                        } else if (tag.compareTo("result") == 0) {
                            inText = false;
                        } else if (tag.compareTo("resultcode") == 0) {
                            inText = false;
                        } else if (tag.compareTo("message") == 0) {
                            inText = false;
                        } else if (tag.compareTo("response") == 0) {
                            inText = false;
                        } else {
                            inText = true;

                        }
                        break;
                    case XmlPullParser.TEXT:
                        tag = parser.getName();
                        if (inText) {
                            if (parser.getText() == null) {
                                f_array[colIdx] = "";
                            } else {
                                f_array[colIdx] = parser.getText().trim();
                            }

                            colIdx++;
                        }
                        inText = false;
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        inText = false;
                        break;

                }

                parserEvent = parser.next();
            }
        } catch (Exception e) {
            Log.e("dd", "Error in network call", e);
        }
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("id"           ,f_array[0]);
        resultMap.put("profile_image"        ,f_array[1]);
        resultMap.put("email"   ,f_array[2]);
        resultMap.put("name"             ,f_array[3]);
        resultMap.put("gender"          ,f_array[4]);
        resultMap.put("name2"              ,f_array[5]);
        resultMap.put("nickname"            ,f_array[6]);
        resultMap.put("birthday"        ,f_array[7]);
        return resultMap;
    }

    /////////////////////////////////////////////////////////////////////카카오
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                //로그인 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                //로그인 비정상적인 실패
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }
                //로그인성공
                @Override
                public void onSuccess(MeV2Response result) {
                    kakaoresult = result;
                    Map<String,String> params= new HashMap<>();
                    params.put("m_id",Long.toString(result.getId()));
                    Log.d("로그인 정보 확인.",Long.toString(result.getId()));
                    category = "k";
                    id = Long.toString(result.getId());
                    ServerSend("membercheck",params);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}