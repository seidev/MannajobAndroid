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
import com.ateam.mannajob.recycleQna.QnADTO;

import java.util.Objects;

public class BoardQnA extends Fragment implements MainActivity.onKeyBackPressedListener{
    TextView q_num;
    TextView q_subject;
    TextView q_category_detail;
    TextView q_wdate;
    TextView q_m_id;
    TextView q_contents;
    TextView qs_contents;
    Button q_list_btn;
    QnADTO qnADTO;
    Bundle bundle;
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
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_board_qna, container, false);

        UIinit(rootview);
        bundle = getArguments();
        qnADTO = (QnADTO) bundle.getSerializable("item");
        SetDisplay(qnADTO);
        return  rootview;
    }
    private void UIinit(ViewGroup rootview){
        q_num = rootview.findViewById(R.id.q_num);
        q_subject = rootview.findViewById(R.id.q_subject);
        q_category_detail = rootview.findViewById(R.id.q_category_detail);
        q_wdate = rootview.findViewById(R.id.q_wdate);
        q_m_id = rootview.findViewById(R.id.q_m_id);
        q_contents = rootview.findViewById(R.id.q_contents);
        qs_contents = rootview.findViewById(R.id.qs_contents);
        q_list_btn = rootview.findViewById(R.id.q_list_btn);
        q_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTabSelected(AppConstants.FRAGMENT_SERVICE,null);
            }
        });
    }
    private void SetDisplay(QnADTO qnaDTO){
        q_num.setText(Integer.toString(qnaDTO.getQ_num()));
        q_subject.setText(qnaDTO.getQ_subject());
        switch (qnaDTO.getQ_category()) {
            case "B":
                q_category_detail.setText(R.string.qnacategoryb);
                break;
            case "C":
                q_category_detail.setText(R.string.qnacategoryc);
                break;
            case "D":
                q_category_detail.setText(R.string.qnacategoryd);
                break;
            case "E":
                q_category_detail.setText(R.string.qnacategorye);
                break;
            case "Q":
                q_category_detail.setText(R.string.qnacategoryq);
                break;
            case "X":
                q_category_detail.setText(R.string.qnacategoryx);
                break;
        };
        q_wdate.setText(qnaDTO.getQ_udate().toString());
        q_m_id.setText(qnaDTO.getM_id());
        q_contents.setText(qnaDTO.getQ_contents());
        qs_contents.setText(qnaDTO.getQs_contents());
    }
    @Override
    public void onBackKey() {
        goToMain();
    }

    //프래그먼트 종료
    private void goToMain(){
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(BoardQnA.this).commit();
        fragmentManager.popBackStack();
    }
}