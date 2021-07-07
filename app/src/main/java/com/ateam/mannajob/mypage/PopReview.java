package com.ateam.mannajob.mypage;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PopReview extends Activity implements MyApplication.OnResponseListener, ServerController {
    RadioGroup review_radio_group;
    RadioButton review_like;
    RadioButton review_dislike;
    EditText review_contents;
    Button review_ok_btn;
    Button review_cancel_btn;
    String likechk;
    String mat_num;
    String b_num;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_pop_review);
        Intent intent = getIntent();
        type=intent.getExtras().get("type").toString();
        b_num = intent.getExtras().get("b_num").toString();
        if(type.equals("1")) {
            mat_num = intent.getExtras().get("mat_num").toString();
        }
        initUI();
    }

    private void initUI(){
        review_radio_group = findViewById(R.id.review_radio_group);
        review_like = findViewById(R.id.review_like);
        likechk = "G";
        review_dislike = findViewById(R.id.review_dislike);
        review_contents = findViewById(R.id.review_contents);
        review_ok_btn = findViewById(R.id.review_ok_btn);
        review_ok_btn.setOnClickListener(v -> {
            if(type.equals("1")) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mat_num", mat_num);
                params.put("b_num", b_num);
                params.put("r_good", likechk);
                params.put("r_contents", review_contents.getText().toString());
                ServerSend("reviewinsert", params);
            }else{
                Map<String, String> params = new HashMap<String, String>();
                params.put("b_num", b_num);
                params.put("r_good", likechk);
                params.put("r_contents", review_contents.getText().toString());
                ServerSend("reviewinsertbmatch", params);
            }
        });
        review_cancel_btn = findViewById(R.id.review_cancel_btn);
        review_cancel_btn.setOnClickListener(v -> {
            finish();
        });

        review_radio_group.setOnCheckedChangeListener(groupClickListener);
    }
    RadioGroup.OnCheckedChangeListener groupClickListener = new RadioGroup.OnCheckedChangeListener() {
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.review_like){
                likechk = "G";
            } else if(i == R.id.review_dislike){
                likechk = "B";
            }
        }
    };

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {

        if(responseCode==200){
            if (requestCode == AppConstants.REVIEWINSERT) {
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(),"리뷰 작성이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"리뷰 작성에 실패했습니다.",Toast.LENGTH_SHORT).show();
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
        if(cmd.equals("reviewinsert")) {
            url += "rest/review/insert.json";
            Log.d("url:", url);
            MyApplication.send(AppConstants.REVIEWINSERT, Request.Method.POST, url, params, this);
        }else if(cmd.equals("reviewinsertbmatch")) {
            url += "rest/review/insertbmatch.json";
            Log.d("url:", url);
            MyApplication.send(AppConstants.REVIEWINSERT, Request.Method.POST, url, params, this);
        }
    }
}