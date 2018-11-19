package com.xuexiang.buglytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_crash_report:
                startActivity(new Intent(this, CrashReportActivity.class));
                break;
            case R.id.btn_update:
                startActivity(new Intent(this, AppUpdateActivity.class));
                break;
            case R.id.btn_hotfix:
                startActivity(new Intent(this, HotFixActivity.class));
                break;
            default:
                break;
        }
    }
}
