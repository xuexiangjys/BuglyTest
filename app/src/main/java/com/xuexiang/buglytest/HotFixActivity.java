package com.xuexiang.buglytest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tencent.bugly.beta.Beta;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;

import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * @author xuexiang
 * @since 2018/11/19 下午3:45
 */
public class HotFixActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GET_PATCH_PACKAGE = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fix:
                choosePatchApk();
                break;
            case R.id.btn_check_update:
                Beta.applyDownloadedPatch();
                break;
            default:
                break;
        }
    }

    @Permission(STORAGE)
    private void choosePatchApk() {
        ActivityUtils.startActivityForResult(this, IntentUtils.getDocumentPickerIntent(IntentUtils.DocumentType.ANY), REQUEST_CODE_GET_PATCH_PACKAGE);
    }

    @Override
    @SuppressLint("MissingPermission")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || requestCode == REQUEST_CODE_GET_PATCH_PACKAGE) {
            if (data != null) {
                String path = PathUtils.getFilePathByUri(data.getData());
                Beta.applyTinkerPatch(getApplicationContext(), path);
//                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
            }
        }
    }
}
