package com.xuexiang.buglytest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.bugly.beta.upgrade.UpgradeListener;
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
//        initCrashReport();
    }

    private void initBuglyUpdate() {
        initBeta();


        setUpgradeListener(); //全量更新监听

        setBetaPatchListener(); //热更新监听

        initBugly();
    }

    private void initBeta() {
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
        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.mipmap.ic_launcher;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher;
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false;

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
                ToastUtils.toast("查询到最新版本！");
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

        /**
         * 自定义Activity参考，通过回调接口来跳转到你自定义的Actiivty中。
         */
        Beta.upgradeListener = new UpgradeListener() {

            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), UpgradeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    ToastUtils.toast("没有更新!");
                }
            }
        };
    }

    /**
     * 初始化Bugly的话就不需要初始化了
     */
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }


}
