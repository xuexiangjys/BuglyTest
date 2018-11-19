package com.xuexiang.buglytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * @author xuexiang
 * @since 2018/11/19 下午5:03
 */
public class AppUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_update);

    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_check_update:
                Beta.checkUpgrade();
                break;
            default:
                break;
        }

    }
}
