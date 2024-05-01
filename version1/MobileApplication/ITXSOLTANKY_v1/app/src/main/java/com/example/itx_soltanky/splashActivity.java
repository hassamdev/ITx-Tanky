package com.example.itx_soltanky;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
public class splashActivity extends AppCompatActivity {
    Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mainHandler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread(){
            public  void run(){
                try{
                    sleep(4000);
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    connect.connect_with_pump(splashActivity.this);

                }
            }
        };
        thread.start();
    }

    public void main_activity(){
        Intent intent = new Intent(splashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    public void error_activity(int err_head,int err_var){
        errorActivity.set_error(err_head,err_var);
        Intent intent = new Intent(splashActivity.this, errorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
        // Finish all activities in the application
        finishAffinity();

    }

    public void tank_edit_activity(){
        Intent intent = new Intent(splashActivity.this, TankEditActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
