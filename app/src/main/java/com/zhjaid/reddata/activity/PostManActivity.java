package com.zhjaid.reddata.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhjaid.reddata.BaseActivity;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.adapter.GroupAdapter;
import com.zhjaid.reddata.data.BaseData;
import com.zhjaid.reddata.pojo.GroupPojo;
import com.zhjaid.reddata.utils.DialogListener;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class PostManActivity extends BaseActivity {

    private RecyclerView postman_list;
    private QMUITopBar qmuiBar;
    private QMUIAlphaTextView addButton;

    private BaseData baseData;

    private GroupAdapter groupAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postman);

        initViews();

        initConfig();

        groupAdapter = new GroupAdapter(getActivity(), null);
        postman_list.setAdapter(groupAdapter);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                    }
                });
            }
        }, 0, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(getActivity());
            builder.setTitle("提示").setMessage("是否退出RedData?").addAction("退出", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog dialog, int index) {
                    finish();
                }
            }).addAction("取消", DialogListener.cancelAction).create().show();
            return true;
        }
        return false;
    }

    private void initData() {
        GroupPojo groupPojo = baseData.getGroupPojo();
        if (groupPojo != null) {
            List<GroupPojo.Group> groups = groupPojo.getGroups();
            if (groups == null || groups.size() <= 0) {
                qmuiBar.setSubTitle("当前没有分组");
            } else {
                qmuiBar.setSubTitle("当前分组" + groups.size() + "");
            }
            groupAdapter.updateGroups(groups);
        }
    }

    private void initConfig() {
        baseData = new BaseData(getActivity());

        postman_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        qmuiBar.setTitle("RedData");
        qmuiBar.setTitleGravity(Gravity.LEFT | Gravity.CENTER);

        addButton.setTextColor(Color.BLACK);
        addButton.setText("添加组");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
                builder.setPlaceholder("输入分组名字");
                builder.setTitle("输入分组名");
                builder.addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String name = builder.getEditText().getText().toString();
                        if (name.length() <= 0) {
                            dialog.dismiss();
                            return;
                        }
                        GroupPojo.Group group = new GroupPojo.Group();
                        group.setCount(0);
                        group.setCreateTime(new Date());
                        group.setLabel("");
                        group.setUpdateTime(new Date());
                        group.setName(name);
                        group.setFileName(UUID.randomUUID().toString());
                        baseData.putGroup(group);
                        initData();
                        dialog.dismiss();
                    }
                });
                builder.addAction("取消", DialogListener.cancelAction);
                builder.create().show();
            }
        });
    }

    private void initViews() {
        postman_list = findViewById(R.id.postman_list);
        qmuiBar = findViewById(R.id.qmuiBar);
        addButton = findViewById(R.id.addButton);
    }
}
