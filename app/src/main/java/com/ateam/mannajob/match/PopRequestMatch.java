package com.ateam.mannajob.match;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PopRequestMatch extends Activity implements MyApplication.OnResponseListener, ServerController {

    Spinner mat_stdate_year;
    Spinner mat_stdate_month;
    Spinner mat_stdate_day;
    Spinner mat_stdate_hour;
    Button mat_request;
    Button mat_cancel;
    String mat_stdate;
    String mat_hour;
    String b_num;
    String year;
    String month;
    String day;
    String hour;
//    TextView mat_stdate_txt;
//    TextView mat_hour_txt;
    private DatePickerDialog.OnDateSetListener callbackMethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_pop_request_match);
        UiInit();


        Intent intent = getIntent();
        b_num = intent.getExtras().getString("b_num");
        getSpinnerData();
        mat_request.setOnClickListener(v -> {
            mat_stdate = year+"-"+month+"-"+day;
            mat_hour = hour;
            Map<String,String> params = new HashMap<String,String>();
            params.put("mat_stdate",mat_stdate);
            params.put("mat_hour",mat_hour);
            params.put("b_num",b_num);
            ServerSend("matchinsert",params);
        });

        mat_cancel.setOnClickListener(v -> {
            finish();
        }
        );
    }
    private void UiInit(){
        mat_stdate_year = findViewById(R.id.mat_stdate_year);
        mat_stdate_month = findViewById(R.id.mat_stdate_month);
        mat_stdate_day = findViewById(R.id.mat_stdate_day);
        mat_stdate_hour = findViewById(R.id.mat_stdate_hour);
        mat_request = findViewById(R.id.mat_request);
        mat_cancel = findViewById(R.id.mat_cancel);
//        mat_stdate_txt = findViewById(R.id.mat_stdate);
//        InitializeListener();
//        mat_hour_txt = findViewById(R.id.mat_hour);

    }
    public void getSpinnerData(){
        mat_stdate_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mat_stdate_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mat_stdate_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mat_stdate_hour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {

        if(responseCode==200){
            if (requestCode == AppConstants.MATCHINSERT) {
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(),"정상적으로 신청됨",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"요청에 실패했습니다.",Toast.LENGTH_SHORT).show();
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
        if(cmd.equals("matchinsert")) {
            url += "rest/match/insert.json";
            Log.d("url:", url);
            MyApplication.send(AppConstants.MATCHINSERT, Request.Method.POST, url, params, this);
        }
    }

//
//    public void InitializeListener()
//    {
//        callbackMethod = new DatePickerDialog.OnDateSetListener()
//        {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
//            {
//                mat_stdate_txt.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
//                mat_stdate=year+"-"+monthOfYear+"-"+dayOfMonth;
//            }
//        };
//    }
//
//    public void OnClickHandler(View view)
//    {
//        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
//
//        dialog.show();
//    }


}