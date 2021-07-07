package com.ateam.mannajob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class JoinAgree extends AppCompatActivity {
    CheckBox agree1;
    CheckBox agree2;
    Button  agreeok_btn;
    String email;
    String name;
    String m_api;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_agree);

        initUi();

        Intent intent = getIntent();
        m_api = intent.getExtras().getString("m_api");
        if(!m_api.equals("x")) {
            id = intent.getExtras().getString("id");
            if (m_api.equals("n")) {
                email = intent.getExtras().getString("email");
            }
            name = intent.getExtras().getString("name");
            Log.d("JoinAgree", m_api);
            Log.d("JoinAgree", name);
            Log.d("JoinAgree", id);
        }
    }

    public void initUi(){
        agree1 = findViewById(R.id.agree1);
        agree2 = findViewById(R.id.agree2);
        agreeok_btn = findViewById(R.id.agreeok_btn);
        agreeok_btn.setOnClickListener(v -> {
            if(!agree1.isChecked()||!agree2.isChecked()) {
                Toast.makeText(this, "약관에 모두 동의하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(getApplicationContext(),Join.class);
                intent.putExtra("m_api", m_api);
                if(!m_api.equals("x")) {
                    intent.putExtra("m_id", id);
                    intent.putExtra("m_name", name);
                    if (m_api.equals("n")) {
                        intent.putExtra("m_email", email);
                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }
}