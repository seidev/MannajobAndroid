package com.ateam.mannajob;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.ateam.mannajob.match.BoardMatching;
import com.ateam.mannajob.match.Matching;
import com.ateam.mannajob.mypage.Mypage;
import com.ateam.mannajob.mypage.MypageMatchManage;
import com.ateam.mannajob.mypage.Schedule;
import com.ateam.mannajob.recycleMatch.BMatchDTO;
import com.ateam.mannajob.recycleNotice.NoticeDTO;
import com.ateam.mannajob.recycleQna.QnADTO;
import com.ateam.mannajob.serivce.BoardNotice;
import com.ateam.mannajob.serivce.BoardQnA;
import com.ateam.mannajob.serivce.Service;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhn.android.naverlogin.OAuthLogin;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener,OnFragmentItemSelectedListener, ImageFormToServer, Runnable,MyApplication.OnResponseListener,ServerController {
    private static final String TAG= "MainActivity";
    private final MyHandler handler = new MyHandler(this);
    OAuthLogin LoginState = OAuthLogin.getInstance();
    Matching matching_f;
    Service service_f;
    Mypage mypage_f;
    BoardMatching boardMatching_f;
    MypageMatchManage mypageMatchManage_f;
    BoardNotice boardNotice_f;
    BoardQnA boardQnA_f;
    Schedule schedule_f;
    Bundle bundle;


    Toolbar toolbar;
    //????????? ??????
    Bitmap bitmap;
    CircleImageView profile;
    String profile_file_name;
    public class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity){
            this.weakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            profile.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImageToServer(CircleImageView imageView, String imageName) {
        this.profile = imageView;
        this.profile_file_name = imageName;
        Thread thread = new Thread(MainActivity.this);
        thread.start();
    }

    BottomNavigationView bottomNavigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_MannajobAndroid);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UIinit();

        toolbar = findViewById(R.id.toolbar);
        //??????(?????????) ??????
        toolbarSetting(toolbar);
        //???????????? ??????
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, matching_f).commit();
        //?????? ??????
        AutoPermissions.Companion.loadAllPermissions(this, 101);



        //??????????????? ????????? ????????? ?????? ?????? ?????? ??????
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, matching_f).commit();
                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, service_f).addToBackStack(null).commit();
                        return true;
                    case R.id.tab3:
                        ServerSend("loginState_fragment3",null);
                        return true;
//                    case R.id.tab3:
//                        if (LoginState.getState(getApplicationContext()).toString().equals("OK")) {
//                            Toast.makeText(getApplicationContext(), "???????????? ??? ?????????", Toast.LENGTH_LONG).show();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment5).commit();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "???????????? ???????????????.", Toast.LENGTH_LONG).show();
//                        }
//                        return true;
                }
                return false;
            }
        });
    }

    public void UIinit(){
        matching_f = new Matching();
        service_f = new Service();
        mypage_f = new Mypage();
        boardMatching_f = new BoardMatching();
        mypageMatchManage_f = new MypageMatchManage();
        boardNotice_f = new BoardNotice();
        boardQnA_f = new BoardQnA();
        schedule_f = new Schedule();

    }

    public void onTabSelected(int position, Object item) { // fragment?????? ????????? ????????? ????????? ?????? ????????? ?????? ??????, ?????? ????????? ?????? ??? selected item??? ???????????????  onnavigationItemSelected??? ?????? ?????? ???????????? ??????
        bundle = new Bundle();
        if (position == AppConstants.FRAGMENT_MATCH) {
            bottomNavigation.setSelectedItemId(R.id.tab1);
        } else if (position == AppConstants.FRAGMENT_SERVICE) {
            bottomNavigation.setSelectedItemId(R.id.tab2);
        } else if (position == AppConstants.FRAGMENT_MYPAGE) {
            bottomNavigation.setSelectedItemId(R.id.tab3);
        } else if (position == AppConstants.FRAGMENT_BOARD_MATCH){
            BMatchDTO BMatchDTO = (BMatchDTO)item;
            bundle.putSerializable("item", BMatchDTO);
            boardMatching_f.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, boardMatching_f).addToBackStack(null).commit();
        }
        else if (position == AppConstants.FRAGMENT_BOARD_NOTICE){
            NoticeDTO noticeDTO = (NoticeDTO)item;
            bundle.putSerializable("item", noticeDTO);
            boardNotice_f.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, boardNotice_f).addToBackStack(null).commit();
        }
        else if (position == AppConstants.FRAGMENT_BOARD_QNA){
            QnADTO qnaDTO = (QnADTO) item;
            bundle.putSerializable("item", qnaDTO);
            boardQnA_f.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_main, boardQnA_f).addToBackStack(null).commit();
        }
        else if (position == AppConstants.FRAGMENT_CALENDAR){
            mypage_f.getChildFragmentManager().beginTransaction().replace(R.id.container_mypage, schedule_f).commit();
        }
        else if (position == AppConstants.FRAGMENT_MATCHINGMANGER){
            mypage_f.getChildFragmentManager().beginTransaction().replace(R.id.container_mypage, mypageMatchManage_f).commit();
        }
    }
////////////////////////////////////////////////////////////////////////////????????? ???????????? ?????????
    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL("http://175.205.193.91:13580/resources/certifi/"+profile_file_name);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is,null,options);
            handler.sendEmptyMessage(0);
            is.close();
            conn.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////// ?????? ??????
    public void toolbarSetting(Toolbar toolbar) {
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_tab:
                ServerSend("LoginState",null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    ///////////////////////////////////////Danger permission request///////////////////////////////////////
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int i, String[] permissions) {
//        Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGranted(int i, String[] permissions) {
//        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_SHORT).show();
    }
    ///////////////////////////////////////fragment ????????? ??????///////////////////////////////////////
    public interface onKeyBackPressedListener {
        void onBackKey();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    private long lastTimeBackPressed;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }
    public Stack<onKeyBackPressedListener> mFragmentBackStack = new Stack<>();

    @Override public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for(Fragment fragment : fragmentList){
            if(fragment instanceof onKeyBackPressedListener){
                ((onKeyBackPressedListener)fragment).onBackKey();
                return;
            }
        }
        //??? ??? ????????? ?????? ??????
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this,"'??????' ????????? ??? ??? ??? ????????? ???????????????.",Toast.LENGTH_SHORT).show();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////// ????????????

    @Override
    public void ServerSend(String cmd, Map<String,String> params) {
        String url =AppConstants.URL;
        if(cmd.equals("LoginState")){
            url +="rest/check";
            MyApplication.send(AppConstants.LOGINSTATE, Request.Method.GET,url,params,this);
        }else  if(cmd.equals("loginState_fragment3")){
            url +="rest/check";
            MyApplication.send(AppConstants.LOGINSTATEFRAGMENT3, Request.Method.GET,url,params,this);
        }else  if(cmd.equals("logout")){
            url +="rest/logout";
            MyApplication.send(AppConstants.LOGOUT, Request.Method.GET,url,params,this);
        }
    }

    @Override
    public void processResponse(int requestCode, int responseCode, String response) {
        if(responseCode==200){
            if(requestCode == AppConstants.LOGINSTATE){
                if(response.equals("1")){
                    onClickLogoutDialogHandler();
                }else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            }else if(requestCode == AppConstants.LOGINSTATEFRAGMENT3){
                if(response.equals("1")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_main, mypage_f).addToBackStack(null).commit();
                }else{
                    Toast.makeText(getApplicationContext(),"???????????? ???????????????.",Toast.LENGTH_SHORT).show();
                }
            }else if(requestCode == AppConstants.LOGOUT){
                if(response.equals("1")){
                    Toast.makeText(getApplicationContext(),"???????????? ???????????????.",Toast.LENGTH_SHORT).show();
                    onTabSelected(AppConstants.FRAGMENT_MATCH,null);
                }else{
                    Toast.makeText(getApplicationContext(),"????????? ????????? ???????????????.",Toast.LENGTH_SHORT).show();
                }
            }else{
                System.out.println("unknown request code :" + requestCode);
            }
        }else{
            System.out.println("failure request code :" + requestCode);
        }
    }
    public void onClickLogoutDialogHandler(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("???????????? ???????????????????");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                ServerSend("logout",null);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}