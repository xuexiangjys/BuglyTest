package com.xuexiang.buglytest;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.Locale;

/**
 * @author xuexiang
 * @since 2018/11/19 上午11:46
 */
public class App extends Application {

    private static final String APP_ID = "a9fbdd8435";

    @Override
    public void onCreate() {
        super.onCreate();

        XUtil.init(this);
        XAOP.init(this);


        initBuglyUpdate();
        initCrashReport();
    }

    private void initBuglyUpdate() {
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;
        // 设置是否检查版本更新
        Beta.autoCheckUpgrade = false;

        setUpgradeListener(); //全量更新监听

        setBetaPatchListener(); //热更新监听

        initBugly();
    }

    private void initBugly() {
        BuglyStrategy strategy = new BuglyStrategy();
        strategy.setEnableANRCrashMonitor(true)
                .setEnableNativeCrashMonitor(true)
                .setUploadProcess(true)
                .setRecordUserInfoOnceADay(true);
        long start = System.currentTimeMillis();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(this, APP_ID, true, strategy);
        long end = System.currentTimeMillis();
        Log.e("init time--->", end - start + "ms");
    }

    private void setBetaPatchListener() {
        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                ToastUtils.toast(patchFileUrl);
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                ToastUtils.toast(String.format(Locale.getDefault(),
                        "%s %d%%",
                        Beta.strNotificationDownloading,
                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
                ToastUtils.toast("补丁下载成功：" + patchFilePath);
            }

            @Override
            public void onDownloadFailure(String msg) {
                ToastUtils.toast(msg);
            }

            @Override
            public void onApplySuccess(String msg) {
                ToastUtils.toast(msg);
            }

            @Override
            public void onApplyFailure(String msg) {
                ToastUtils.toast(msg);
            }

            @Override
            public void onPatchRollback() {
                ToastUtils.toast("onPatchRollback");
            }
        };
    }

    private void setUpgradeListener() {
        /**
         *  全量升级状态回调
         */
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeFailed(boolean b) {
                ToastUtils.toast("升级失败！");

            }

            @Override
            public void onUpgradeSuccess(boolean b) {
                ToastUtils.toast("升级成功！");
            }

            @Override
            public void onUpgradeNoVersion(boolean b) {
                ToastUtils.toast("已是最新版本！");
            }

            @Override
            public void onUpgrading(boolean b) {
                ToastUtils.toast("正在更新中...");
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                Log.e("xuexiang", "更新下载完成!");
            }
        };
    }

    private void initCrashReport() {
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
        CrashReport.initCrashReport(this, APP_ID, true, strategy);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }


}
