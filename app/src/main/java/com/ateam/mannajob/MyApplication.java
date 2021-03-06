package com.ateam.mannajob;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import java.io.IOException;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "JSESSIONID";
    public static RequestQueue requestQueue;
    private SharedPreferences preferences;
    private static MyApplication instance;
    public static MyApplication get(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (requestQueue == null) {

            requestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpURLConnection connection = super.createConnection(url);
                    connection.setInstanceFollowRedirects(false);

                    return connection;
                }
            });
        }
    }
    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }
    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(SESSION_COOKIE, cookie);
                editor.commit();
            }
        }
    }

    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;

    }

    public static interface OnResponseListener {
        public void processResponse(int requestCode, int responseCode, String response);
    }

    public static void send(final int requestCode, final int requestMethod, final String url, final Map<String, String> params, final OnResponseListener listener) {
        StringRequest request = new StringRequest(
                requestMethod,
                url,
                response -> {
                    if (listener != null) {
                        listener.processResponse(requestCode, 200, response);
                    }
                },
                error -> {
                    Log.d(TAG, "Error for " + requestCode + " -> " + error.getMessage());
                    if (listener != null) {
                        listener.processResponse(requestCode, 400, error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            // JSESSIONID ??? ????????? ???????????? ?????????
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<>();
                }

                MyApplication.instance.addSessionCookie(headers);
                return headers;
            }

            // JSESSIONID ??? ???????????? ?????????
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                MyApplication.instance.checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }
        };

        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.requestQueue.add(request);
        Log.d(TAG, "Request sent : " + requestCode);
        Log.d(TAG, "Request url : " + url);
    }

    ////////////////////////////////????????? ?????????/////////////////////////////////////////////
//    private static volatile MyApplication instance = null;

    private static class KakaoSDKAdapter extends KakaoAdapter {
        /**
         * Session Config??? ???????????? default????????? ????????????.
         * ????????? ??????????????? override?????? ???????????? ???.
         * @return Session??? ?????????.
         */
        // ????????? ????????? ????????? ????????? ?????? ???????????? ???????????? ??????.
        public ISessionConfig getSessionConfig() {

            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                    /*???????????? ?????? ????????? ???????????? ??????. AuthType?????? ?????? ??? ?????? ????????? ??????.
                    KAKAO_TALK: ?????????????????? ?????????, KAKAO_STORY: ????????????????????? ?????????, KAKAO_ACCOUNT: ????????? ?????? ?????????,
                    KAKAO_TALK_EXCLUDE_NATIVE_LOGIN: ????????????????????? ?????????+?????? ????????? ???????????? ?????? ??????
                    KAKAO_LOGIN_ALL: ?????? ??????????????? ?????? ??????. ????????????, ?????????????????? ????????????????????? ????????? ??? ????????? ????????? ????????? ????????????, ??? ??? ????????? ????????? ?????? ???????????? ????????????.
                     */

                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                    // SDK ???????????? ???????????? WebView?????? pause??? resume?????? Timer??? ???????????? CPU????????? ????????????.
                    // true ??? ??????????????? webview???????????? ???????????? ????????? ?????? webview??? onPause??? onResume ?????? Timer??? ????????? ????????? ??????.
                    // ???????????? ?????? ??? false??? ????????????.
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                    // ???????????? access token??? refresh token??? ????????? ?????? ????????? ????????? ????????????.
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                    // ?????? ???????????? ?????? Kakao??? ????????? ???????????? ???????????? ?????????, ?????? ???????????? ???????????? ApprovalType.INDIVIDUAL ?????? ???????????? ??????.
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                    // Kakao SDK ?????? ???????????? WebView?????? email ???????????? ???????????? ???????????? ????????? ????????????.
                    // true??? ??????, ???????????? ?????? ????????? ??? email ?????? ????????? ????????? ???????????? ???????????? ????????????.
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return MyApplication.getGlobalApplicationContext();
                }
            };
        }
    }

    public static MyApplication getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }
}
