package com.ateam.mannajob.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.applandeo.materialcalendarview.EventDay;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleCalendar.CalendarAdapter;
import com.ateam.mannajob.recycleCalendar.CalendarDTO;
import com.ateam.mannajob.recycleMatch.MatchAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Popcalendar extends Activity implements MyApplication.OnResponseListener, ServerController {
    RecyclerView scheduleRecyc;
    CalendarAdapter adapter;
    CalendarDTO calendarDTO;
    Calendar calendar_date;
    TextView noMatchCount;
    String yearmonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popcalendar);
        Intent intent = getIntent();
        yearmonth = intent.getExtras().getString("mat_stdate");


        UiInit();
    }

    public void UiInit(){
        noMatchCount = findViewById(R.id.noMatchCount);
        scheduleRecyc = findViewById(R.id.calendar_recyc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        scheduleRecyc.setLayoutManager(layoutManager);

        ArrayList<CalendarDTO> list = new ArrayList<>();
        adapter = new CalendarAdapter();
        scheduleRecyc.setAdapter(adapter);
        Map<String,String> params = new HashMap<String,String>();
        params.put("yearmonth",yearmonth);
        ServerSend("monthmatch",params);
        ServerSend("monthbmatch",params);


        if(adapter.getItemCount()==0){
            noMatchCount.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;

        if(responseCode==200){
            if (requestCode == AppConstants.MONTHMATCH) {
                if(response == null){
                    Toast.makeText(getApplicationContext(),"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                gson = new Gson();
                type = new TypeToken<ArrayList<CalendarDTO>>(){}.getType();
                ArrayList<CalendarDTO> list = gson.fromJson(response, type);
                Log.d("리스트",list.toString());
                if (list != null){
                    for(int i=0;i<list.size(); i++ ) {
                        Log.d("리스트",list.toString());
                        adapter.addItem(list.get(i));
                    }
                }
                adapter.notifyDataSetChanged();



            }else if (requestCode == AppConstants.MONTHBMATCH) {
                if(response == null){
                    Toast.makeText(getApplicationContext(),"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                gson = new Gson();
                type = new TypeToken<ArrayList<CalendarDTO>>(){}.getType();
                ArrayList<CalendarDTO> list = gson.fromJson(response, type);
                if (list != null){
                    for(int i=0;i<list.size(); i++ ) {
                        Log.d("리스트",list.toString());
                        adapter.addItem(list.get(i));
                    }
                }
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
        if(cmd.equals("monthmatch")) {
            url += "rest/scheduleMatch.json";
            Log.d("url:", url);
            MyApplication.send(AppConstants.MONTHMATCH, Request.Method.POST, url, params, this);
        }else if(cmd.equals("monthbmatch")){
            url +="rest/scheduleBmatch.json";
            Log.d("url:" , url);
            MyApplication.send(AppConstants.MONTHBMATCH, Request.Method.POST,url,params,this);
        }
    }
}