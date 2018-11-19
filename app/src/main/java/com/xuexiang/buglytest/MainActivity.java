package com.xuexiang.buglytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tencent.bugly.crashreport.CrashReport;

public class MainActivity extends AppCompatActivity {
    NativeApi nativeApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nativeApi = new NativeApi();
    }

    public void onClick(View view) {
        switch(view.getId()) {
            // 点击测试Java Crash
            case R.id.btnTestJavaCrash:
//                CrashReport.testJavaCrash();
                nativeApi.testJavaCrash();
                break;
            // 点击测试ANR Crash
            case R.id.btnTestANRCrash:
                CrashReport.testANRCrash();
                break;
            // 点击测试Native Crash
            case R.id.btnTestNativeCrash:
//                CrashReport.testNativeCrash();
                nativeApi.testNativeCrash();
                break;
            case R.id.btnTestException:
                try {
                    int s = 1000 / 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    CrashReport.postCatchedException(e);
                }
                break;
            default:
                break;
        }
    }
}
