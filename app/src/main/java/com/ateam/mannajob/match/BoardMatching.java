package com.ateam.mannajob.match;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MainActivity;
import com.ateam.mannajob.MyApplication;
import com.ateam.mannajob.OnFragmentItemSelectedListener;
import com.ateam.mannajob.R;
import com.ateam.mannajob.ServerController;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.ateam.mannajob.recycleMyRequest.MatchDTO;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class BoardMatching extends Fragment implements MainActivity.onKeyBackPressedListener, MyApplication.OnResponseListener, ServerController {

    TextView b_subject_detail;
    TextView b_m_id_detail;
    TextView b_wdate_detail;
    TextView b_price_detail;
    TextView b_corp_detail;
    TextView b_task_detail;
    TextView b_stdate_detail;
    TextView b_endate_detail;
    TextView b_period_detail;
    TextView b_e_intro;
    CircleImageView profile_image;
    Button b_request_btn;
    ImageView compl_btn;
    Bundle bundle;
    BMatchDTO BMatchDTO;
    Button review_view_btn;

    Context context;
    OnFragmentItemSelectedListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if(context instanceof  OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;
        }
    }
    @Override
    public void onDetach(){
        super.onDetach();
        if(context!= null){
            context=null;
            listener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_boardmatching, container, false);

        UIinit(rootview);

        bundle = getArguments();
        BMatchDTO = (BMatchDTO) bundle.getSerializable("item");
        Log.d("받은 데이터 : ", BMatchDTO.getB_contents());
        SetDisplay(BMatchDTO);

        b_request_btn.setOnClickListener(v -> {
            ServerSend("matchrequest",null);
//
        });
        compl_btn.setOnClickListener(v -> {
            ServerSend("compl",null);
//
        });
        review_view_btn.setOnClickListener(v -> {
            Intent intent = new Intent(context,PopReviewView.class);
            intent.putExtra("m_id", BMatchDTO.getM_id());
            startActivity(intent);
        });

        return rootview;
    }

    private void UIinit(ViewGroup rootview){
        b_subject_detail = rootview.findViewById(R.id.b_subject_detail);
        b_m_id_detail = rootview.findViewById(R.id.b_m_id_detail);
        b_price_detail = rootview.findViewById(R.id.b_price_detail);
        b_corp_detail = rootview.findViewById(R.id.b_corp_detail);
        b_task_detail = rootview.findViewById(R.id.b_task_detail);
        b_stdate_detail = rootview.findViewById(R.id.b_stdate_detail);
        b_endate_detail = rootview.findViewById(R.id.b_endate_detail);
        b_period_detail = rootview.findViewById(R.id.b_period_detail);
        b_e_intro = rootview.findViewById(R.id.b_e_intro);
        b_request_btn = rootview.findViewById(R.id.b_request_btn);
        compl_btn = rootview.findViewById(R.id.compl_btn);
        b_wdate_detail = rootview.findViewById(R.id.b_wdate_detail);
        profile_image = rootview.findViewById(R.id.b_profile_detail);
        review_view_btn = rootview.findViewById(R.id.review_view_btn);
    }

    private void SetDisplay(BMatchDTO BMatchDTO){
        b_subject_detail.setText(BMatchDTO.getB_subject());
        b_m_id_detail.setText(BMatchDTO.getM_id());
        b_price_detail.setText(Integer.toString(BMatchDTO.getB_price()));
        b_corp_detail.setText(BMatchDTO.getB_corp());
        b_task_detail.setText(BMatchDTO.getB_task());
        b_stdate_detail.setText(BMatchDTO.getB_stdate());
        b_endate_detail.setText(BMatchDTO.getB_endate());
        b_period_detail.setText(BMatchDTO.getB_period());
        b_e_intro.setText(BMatchDTO.getB_contents());
        b_wdate_detail.setText(BMatchDTO.getB_wdate().toString());
        MainActivity activity = new MainActivity();
        activity.getImageToServer(profile_image, BMatchDTO.getProfileImage());
    }
    @Override
    public void onBackKey() {
        goToMain();
    }

    //프래그먼트 종료
    private void goToMain(){
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(BoardMatching.this).commit();
        fragmentManager.popBackStack();
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {


        if(responseCode==200){
            if (requestCode == AppConstants.GOMATCHREQUSET) {
                if(response.equals("2")){
                    Toast.makeText(context,"로그인이 필요합니다",Toast.LENGTH_SHORT).show();
                }else if(response.equals("1")){
                    Intent intent = new Intent(context,PopRequestMatch.class);
                    intent.putExtra("b_num", Integer.toString(BMatchDTO.getB_num()));
                    startActivity(intent);
                }
            }else if(requestCode == AppConstants.GOCOMPL){
                if(response.equals("2")){
                    Toast.makeText(context,"로그인이 필요합니다",Toast.LENGTH_SHORT).show();
                }else if(response.equals("1")){
                    Intent intent = new Intent(context,PopCompl.class);
                    intent.putExtra("b_num", Integer.toString(BMatchDTO.getB_num()));
                    intent.putExtra("m_id", BMatchDTO.getM_id());
                    startActivity(intent);
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
        if(cmd.equals("matchrequest")) {
            url += "rest/check.json";
            Log.d("url:", url);
            MyApplication.send(AppConstants.GOMATCHREQUSET, Request.Method.GET, url, params, this);
        }else if(cmd.equals("compl")){
            url += "rest/check.json";
            Log.d("url:", url);
            MyApplication.send(AppConstants.GOCOMPL, Request.Method.GET, url, params, this);
        }
    }
}