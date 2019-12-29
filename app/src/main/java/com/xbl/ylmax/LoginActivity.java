package com.xbl.ylmax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
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
               NetUtil.deviceId =  deviceET.getText().toString();

               NetUtil.checkDeviceId(new NetUtil.DeviceCallBack() {
                   @Override
                   public void dataBack(boolean isSuccess) {
                       if (isSuccess){
                           ToastUtils.showToast("设备号校验成功");
                           startActivity(new Intent(LoginActivity.this,MainActivity.class));
                       }else {
                           ToastUtils.showToast("设备号错误！");
                       }
                   }
               });

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }







}
