package com.ateam.mannajob.serivce;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ateam.mannajob.AppConstants;
import com.ateam.mannajob.MainActivity;
import com.ateam.mannajob.OnFragmentItemSelectedListener;
import com.ateam.mannajob.R;
import com.ateam.mannajob.match.Matching;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.ateam.mannajob.recycleNotice.NoticeDTO;

import java.util.Objects;

public class BoardNotice extends Fragment implements MainActivity.onKeyBackPressedListener{
    TextView n_num_detail;
    TextView n_subject_detail;
    TextView n_wdate_detail;
    TextView n_m_id_detail;
    TextView n_contents;
    Button notice_list_btn;

    Bundle bundle;
    Context context;
    OnFragmentItemSelectedListener listener;
    NoticeDTO noticeDTO;

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
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_board_notice, container, false);

        UIinit(rootview);
        bundle = getArguments();
        noticeDTO = (NoticeDTO) bundle.getSerializable("item");
        SetDisplay(noticeDTO);
        return  rootview;
    }
    private void UIinit(ViewGroup rootview){
        n_num_detail = rootview.findViewById(R.id.n_num_detail);
        n_subject_detail = rootview.findViewById(R.id.n_subject_detail);
        n_wdate_detail = rootview.findViewById(R.id.n_wdate_detail);
        n_m_id_detail = rootview.findViewById(R.id.n_m_id_detail);
        n_contents = rootview.findViewById(R.id.n_contents);
        notice_list_btn = rootview.findViewById(R.id.notice_list_btn);
        notice_list_btn.setOnClickListener(v -> {
            listener.onTabSelected(AppConstants.FRAGMENT_SERVICE,null);
        });
    }
    private void SetDisplay(NoticeDTO noticeDTO){
        n_num_detail.setText(Integer.toString(noticeDTO.getN_num()));
        n_subject_detail.setText(noticeDTO.getN_subject());
        n_wdate_detail.setText(noticeDTO.getN_udate().toString());
        n_m_id_detail.setText(noticeDTO.getAd_id());
        n_contents.setText(noticeDTO.getN_contents());
    }
    @Override
    public void onBackKey() {
        goToMain();
    }

    //프래그먼트 종료
    private void goToMain(){
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(BoardNotice.this).commit();
        fragmentManager.popBackStack();
    }
}