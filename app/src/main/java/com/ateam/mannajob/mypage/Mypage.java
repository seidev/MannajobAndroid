package com.ateam.mannajob.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MainActivity;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.OnFragmentItemSelectedListener;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.match.Matching;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;


public class Mypage extends Fragment implements MainActivity.onKeyBackPressedListener, MyApplication.OnResponseListener, ServerController {
    CircleImageView profileImage;
    TextView mypageID;
    TextView mypageName;
    TextView mypagePhone;
    TextView mypageEmail;
    MemberDTO myprofile;


    Context context;
    Button update_profile_btn;
    OnFragmentItemSelectedListener listener;
    SwitchMultiButton switchMultiButton;
    FrameLayout container;
    int switchpostion;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentItemSelectedListener) {
            listener = (OnFragmentItemSelectedListener)context;
        }


    }
    @Override
    public void onDetach(){
        super.onDetach();
        if(context!= null){
            context=null;
            listener=null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);



        initUI(rootView);
        ServerSend("MyProfile",null);

        return rootView;
    }

    private  void  initUI(ViewGroup rootview) {
        mypageID =rootview.findViewById(R.id.mypage_ID);
        mypageName= rootview.findViewById(R.id.mypageName);
        mypagePhone= rootview.findViewById(R.id.mypage_phone);
        mypageEmail= rootview.findViewById(R.id.mypage_email);
        profileImage = rootview.findViewById(R.id.profile_image);
//        update_profile_btn = rootview.findViewById(R.id.update_profile_btn);
//        update_profile_btn.setOnClickListener(v -> {
//            Intent intent = new Intent(context,PopPasswdCheck.class);
//            startActivityForResult(intent,101);
//        });
        container = rootview.findViewById(R.id.container_mypage);
        switchMultiButton = rootview.findViewById(R.id.mypage_switchButton);
        switchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
              @Override
              public void onSwitch(int position, String tabText) {
                  if (position == 0) {
                      listener.onTabSelected(AppConstants.FRAGMENT_CALENDAR,null);
                  } else if (position == 1) {
                      listener.onTabSelected(AppConstants.FRAGMENT_MATCHINGMANGER,null);
                  }
                  switchpostion = position;
              }
        });

    }
    public void SettingDisplay(){
        mypageID.setText(myprofile.getM_id());
        mypageName.setText(myprofile.getM_name());
        mypagePhone.setText(myprofile.getM_phone());
        mypageEmail.setText(myprofile.getM_email());
        MainActivity activity = new MainActivity();
        activity.getImageToServer(profileImage, myprofile.getStored_file_name());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (switchpostion == 0) {
            listener.onTabSelected(AppConstants.FRAGMENT_CALENDAR,null);
        } else if (switchpostion == 1) {
            listener.onTabSelected(AppConstants.FRAGMENT_MATCHINGMANGER,null);
        }
    }

    @Override
    public void onBackKey() {
        goToMain();
    }

    //프래그먼트 종료
    private void goToMain(){
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Mypage.this).commit();
        fragmentManager.popBackStack();
    }


    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        Gson gson;
        Type type;
        if(responseCode==200){
            if (requestCode == AppConstants.MYPROFILE) {
                gson = new Gson();
                myprofile = gson.fromJson(response, MemberDTO.class);
                if(myprofile==null){
                    Toast.makeText(context,"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                SettingDisplay();
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
        if(cmd.equals("MyProfile")){
            url +="rest/myprofile.json";
            MyApplication.send(AppConstants.MYPROFILE, Request.Method.GET,url,params,this);
        }
    }
}