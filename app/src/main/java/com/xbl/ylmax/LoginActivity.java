package com.xbl.ylmax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xbl.ylmax.utils.NetUtil;
import com.xbl.ylmax.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {


    private EditText deviceET;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        deviceET = findViewById(R.id.activity_login_device_id);
        submit = findViewById(R.id.activity_login_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String device =  deviceET.getText().toString();
               if (NetUtil.deviceId.equals(device)){
                   startActivity(new Intent(LoginActivity.this,MainActivity.class));
               }else {
                   ToastUtils.showToast("设备号错误！");
               }
            }
        });

    }







}
