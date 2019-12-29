package com.xbl.ylmax;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.xbl.ylmax.service.DYService;
import com.xbl.ylmax.utils.NetUtil;
import com.xbl.ylmax.utils.NodeUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permissions;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context context;
    private Button startButton;
    private Timer timer = new Timer();

    public static TextView allMsgTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this.getApplicationContext();
        startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DYService.context == null){
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                }else {
                    PackageManager pm = context.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("com.ss.android.ugc.aweme");
                    startActivity(intent);
                }
            }
        });

        allMsgTV = findViewById(R.id.all_msg);



        APP.runWorkThread(new Runnable() {
            @Override
            public void run() {
                NetUtil.obtainPhoneAndDownloadImg();
            }
        },300);

    }


    public void SetToast(final String message)
    {
        Handler handlerThree=new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable(){
            public void run(){
                Toast.makeText(context ,""+message+"",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void testNet(View view) {
//        NetUtil.obtainPhoneAndDownloadImg();
        boolean res = NodeUtil.isNumericZidai("213");
        Log.d(TAG, "testNet: res = "+res);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        allMsgTV = null;
    }
}
