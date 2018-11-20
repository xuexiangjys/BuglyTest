package com.xuexiang.buglytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xutil.app.AppUtils;

import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * @author xuexiang
 * @since 2018/11/19 下午5:03
 */
public class AppUpdateActivity extends AppCompatActivity {

    TextView mTvVersion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_update);

        mTvVersion = findViewById(R.id.tv_version);
        mTvVersion.setText("当前版本:" + AppUtils.getAppVersionName());

    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_check_update:
                update();
                break;
            default:
                break;
        }
    }


    /**
     * 版本更新
     */
    @Permission(STORAGE)
    private void update() {
        Beta.checkUpgrade();
    }
}
