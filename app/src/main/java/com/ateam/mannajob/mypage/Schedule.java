package com.ateam.mannajob.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.android.volley.Request;
import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.OnFragmentItemSelectedListener;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleCalendar.CalendarDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Schedule extends Fragment implements MyApplication.OnResponseListener, ServerController {
    private static final String TAG = "Schedule";
    Context context;
    OnFragmentItemSelectedListener listener;
    com.applandeo.materialcalendarview.CalendarView calendarView;
    int MonthCnt = 0;
    List<EventDay> events = new ArrayList<>();
    String yearmonth;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentItemSelectedListener) {
            listener = (OnFragmentItemSelectedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (context != null) {
            context = null;
            listener = null;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_schedule, container, false);

        Map<String,String> params = new HashMap<String,String>();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        yearmonth = AppConstants.dateFormat6.format(date);
        Log.d("이번달",yearmonth);

        params.put("yearmonth",yearmonth);
        ServerSend("monthmatch",params);
        ServerSend("monthbmatch",params);


        initUI(rootView);
        return rootView;
    }

    private void initUI(ViewGroup rootview) {
        calendarView = rootview.findViewById(R.id.calendarView);


        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NotNull EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Log.d("데이",Integer.toString(clickedDayCalendar.get(Calendar.YEAR)));
                Log.d("데이",Integer.toString(clickedDayCalendar.get(Calendar.MONTH)+1));
                Log.d("데이",Integer.toString(clickedDayCalendar.get(Calendar.DAY_OF_MONTH)));

                Intent intent = new Intent(getContext(), Popcalendar.class);
                intent.putExtra("mat_stdate" , AppConstants.dateFormat5.format(clickedDayCalendar.getTime()));
                Log.d("클릭 시간",AppConstants.dateFormat5.format(clickedDayCalendar.getTime()));
                startActivity(intent);
            }
        });
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                Calendar calendar = Calendar.getInstance();
                MonthCnt++;
                calendar.add(Calendar.MONTH,MonthCnt);
                yearmonth = Integer.toString(calendar.get(Calendar.YEAR))+"-"+Integer.toString(calendar.get(Calendar.MONTH)+1);
                Log.d("달력......",yearmonth);
                Map<String,String> params = new HashMap<String,String>();
                params.put("yearmonth",yearmonth);
                ServerSend("monthmatch",params);
                ServerSend("monthbmatch",params);

            }
        });
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
             @Override
             public void onChange() {
                 Calendar calendar = Calendar.getInstance();
                 MonthCnt--;
                 calendar.add(Calendar.MONTH,MonthCnt);
                 yearmonth = Integer.toString(calendar.get(Calendar.YEAR))+"-"+Integer.toString(calendar.get(Calendar.MONTH)+1);
                 Log.d("달력......",yearmonth);
                 Map<String,String> params = new HashMap<String,String>();
                 params.put("yearmonth",yearmonth);
                 ServerSend("monthmatch",params);
                 ServerSend("monthbmatch",params);

             }
         });

// 아이템 이미지(이벤트) 추가
//        List<EventDay> events = new ArrayList<>();
//        for(Calendar a:getSelectedDays()) {
//            events.add(new EventDay(a, R.drawable.calendardot));
//        }
//
//        calendarView.setEvents(events);
//    }
//    private List<Calendar> getSelectedDays() {
//        List<Calendar> calendars = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Calendar calendar = DateUtils.getCalendar();
//            calendar.add(Calendar.DAY_OF_MONTH, i);
//            calendars.add(calendar);
//        }
//        return calendars;
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;

        if(responseCode==200){
            if (requestCode == AppConstants.MONTHMATCH) {
                if(response == null){
                    Toast.makeText(context,"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                    listener.onTabSelected(AppConstants.FRAGMENT_MATCH,null);
                }
                gson = new Gson();
                type = new TypeToken<ArrayList<CalendarDTO>>(){}.getType();
                ArrayList<CalendarDTO> list = gson.fromJson(response, type);
                if (list == null){
                    return;
                }
                for(int i=0 ; i <list.size();i++) {
                    try {
                        Date date = AppConstants.dateFormat5.parse(list.get(i).getMat_stdate());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        events.add(new EventDay(calendar, R.drawable.calendardot));
                        calendarView.setEvents(events);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }else if (requestCode == AppConstants.MONTHBMATCH) {
                if(response == null){
                    Toast.makeText(context,"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                    listener.onTabSelected(AppConstants.FRAGMENT_MATCH,null);
                }
                gson = new Gson();
                type = new TypeToken<ArrayList<CalendarDTO>>(){}.getType();
                ArrayList<CalendarDTO> list = gson.fromJson(response, type);
                if (list == null){
                    return;
                }


                for(int i=0 ; i <list.size();i++) {
                    try {
                        Date date = AppConstants.dateFormat5.parse(list.get(i).getMat_stdate());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        events.add(new EventDay(calendar, R.drawable.calendardot));
                        calendarView.setEvents(events);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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