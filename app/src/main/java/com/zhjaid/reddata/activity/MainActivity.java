package com.zhjaid.reddata.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.zhjaid.reddata.BaseActivity;
import com.zhjaid.reddata.R;


public class MainActivity extends BaseActivity {


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean b = checkPermissions();
        if (!b) {
            setPermissonListener(new PermissionListener() {
                @Override
                public void noPermission() {
                    makeText("缺少权限,可能导致使用异常");
                    startPostManActivity();
                }

                @Override
                public void hasPermission() {
                    if (checkPermissions()) {
                        startPostManActivity();
                    } else {
                        makeText("缺少权限,可能导致使用异常");
                        startPostManActivity();
                    }
                }
            });
        } else {
            startPostManActivity();
        }
    }

    private void startPostManActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    MainActivity.start(getActivity(), PostManActivity.class);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
