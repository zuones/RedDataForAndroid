package com.zhjaid.reddata;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends QMUIActivity {
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final int PERMISSON_REQUESTCODE = 512;

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        QMUISwipeBackActivityManager.init(getApplication());
    }

    public void showToast(String content) {
        QMUITipDialog.Builder builder = new QMUITipDialog.Builder(getActivity());
        builder.setTipWord(content);
        builder.setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO);
        builder.create().show();
    }

    public void makeText(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
    }

    public Activity getActivity() {
        return this;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        try {
            for (String perm : permissions) {
                Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                        String.class);
                if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                        || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                    needRequestPermissonList.add(perm);
                }
            }
        } catch (Throwable e) {

        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private PermissionListener mPermissionListener;


    /**
     * 返回true 已经开启了需要的权限 false 未开启权限
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            List<String> needRequestPermissonList = findDeniedPermissions(permissions);
            if (null != needRequestPermissonList
                    && needRequestPermissonList.size() > 0) {
                try {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class, int.class});
                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                    return false;
                } catch (Exception e) {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public interface PermissionListener {
        void noPermission();

        void hasPermission();
    }

    /**
     * 设置权限监听
     *
     * @param permissonListener
     */
    public void setPermissonListener(PermissionListener permissonListener) {
        this.mPermissionListener = permissonListener;
    }


    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                mPermissionListener.noPermission();
            } else {
                mPermissionListener.hasPermission();
            }
        }
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog() {


        QMUIDialog.MessageDialogBuilder dialogBuilder = new QMUIDialog.MessageDialogBuilder(getActivity());

        dialogBuilder.setMessage("当前应用缺少必要权限。请点击设置，开启所有权限");
        dialogBuilder.setTitle("提示");
        dialogBuilder.addAction("设置", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                startAppSettings();
            }
        });
        dialogBuilder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                makeText("拒绝权限可能会导致使用异常");
            }
        });

    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
