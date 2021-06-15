package com.zhjaid.reddata.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.zhjaid.reddata.BaseActivity;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.adapter.ApisAdapter;
import com.zhjaid.reddata.data.BaseData;
import com.zhjaid.reddata.pojo.ApiPojo;
import com.zhjaid.reddata.pojo.GroupPojo;
import com.zhjaid.reddata.pojo.ImportPojo;
import com.zhjaid.reddata.utils.DialogListener;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ApisActivity extends BaseActivity {

    public static GroupPojo.Group group;

    private RecyclerView apis_list;
    private QMUITopBar qmuiBar;
    private QMUIAlphaTextView addButton;
    private BaseData baseData;

    private ApisAdapter apisAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apis);
        initViews();


//
        baseData = new BaseData(getActivity());



        apisAdapter = new ApisAdapter(getActivity(), baseData.getApis(group));
        apis_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        apis_list.setAdapter(apisAdapter);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            initData();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        }, 2000, 1500);
        try {
            initData();
        } catch (Exception e) {
        }
    }

    private void initData() {
        qmuiBar.setTitle(group.getName());
        if (baseData.getApis(group) != null) {
            List<ApiPojo.Api> apis = baseData.getApis(group).getApis();
            if (apis != null) {
                qmuiBar.setSubTitle("共有" + apis.size() + "条");
                group.setCount(apis.size());
                baseData.updateGroup(group);
            }
        } else {
            qmuiBar.setSubTitle("暂无数据");
        }
        qmuiBar.setTitleGravity(Gravity.LEFT | Gravity.CENTER);
        addButton.setTextColor(Color.BLACK);
        apisAdapter.setApiPojo(baseData.getApis(group));
        apisAdapter.notifyDataSetChanged();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportActivity.group = group;
                ImportActivity.start(getActivity(), ImportActivity.class);
            }
        });

        findViewById(R.id.importBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiPojo pojoapi = baseData.getApis(group);
                if (pojoapi == null) {
                    makeText("没有API");
                    return;
                }
                List<ApiPojo.Api> apis = baseData.getApis(group).getApis();
                if (apis == null || apis.size() <= 0) {
                    makeText("没有API");
                    return;
                }
                QMUIDialog.AutoResizeDialogBuilder builder = new QMUIDialog.AutoResizeDialogBuilder(getActivity()) {
                    @Override
                    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
                        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_import, null);
                    }
                };
                builder.setTitle("导出JSON");
                builder.addAction("确定", DialogListener.cancelAction);
                QMUIDialog qmuiDialog = builder.create();
                EditText editText = qmuiDialog.findViewById(R.id.importEdit);

                ImportPojo pojo = new ImportPojo();
                ImportPojo.Info info = new ImportPojo.Info();
                info.set_postman_id(UUID.randomUUID().toString());
                info.setName(group.getName());
                info.setSchema("http://www.zhjaid.com");
                info.setInfo("本数据由RedData生成");
                info.setTime(new Date());
                pojo.setInfo(info);
                pojo.setItem(apis);
                //JsonFormatTool tool = new JsonFormatTool();
                editText.setText(JSON.toJSONString(pojo));
                qmuiDialog.show();
            }
        });
    }

    private void initViews() {
        apis_list = findViewById(R.id.apis_list);
        qmuiBar = findViewById(R.id.qmuiBar);
        addButton = findViewById(R.id.addButton);
    }
}
