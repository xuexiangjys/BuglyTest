package com.xuexiang.buglytest;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author xuexiang
 * @since 2018/11/19 上午11:46
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setEnableANRCrashMonitor(true)
                .setEnableNativeCrashMonitor(true)
                .setUploadProcess(true)
                .setRecordUserInfoOnceADay(true);
        /* Bugly SDK初始化
         * 参数1：上下文对象
         * 参数2：APPID，平台注册时得到,注意替换成你的appId
         * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
         */
        CrashReport.initCrashReport(getApplicationContext(), "a9fbdd8435", true, strategy);
    }
}
