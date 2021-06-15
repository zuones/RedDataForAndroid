package com.zhjaid.reddata.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.zhjaid.reddata.BaseActivity;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.data.BaseData;
import com.zhjaid.reddata.pojo.GroupPojo;
import com.zhjaid.reddata.pojo.ResultPojo;
import com.zhjaid.reddata.utils.DialogListener;
import com.zhjaid.reddata.utils.JsonFormatTool;
import com.zhjaid.reddata.view.MarkdownWebView;

public class ResultActivity extends BaseActivity {
    public static ResultPojo result = null;
    private static String resultMsg = "```json\n" +
            "{\n" +
            "    \"code\":500,\n" +
            "    \"message\":\"请求失败\"\n" +
            "}\n" +
            "```\n" +
            "\n";
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        BaseData data = new BaseData(getActivity());
        GroupPojo groupPojo = data.getGroupPojo();
        QMUITopBar qmuiTopBar = findViewById(R.id.resultQmuiTopbar);
        editText = findViewById(R.id.edit);

        Button info = qmuiTopBar.addRightTextButton("详情", R.id.resultinfo);
        Button check = qmuiTopBar.addRightTextButton("切换", R.id.resultcheckid);

        Button header = qmuiTopBar.addLeftTextButton("Header", R.id.resultheader);
        header.setTextColor(Color.parseColor("#00BCD4"));
        info.setTextColor(Color.parseColor("#FF1100"));
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIDialog.AutoResizeDialogBuilder builder = new QMUIDialog.AutoResizeDialogBuilder(getActivity()) {
                    @Override
                    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
                        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_import, null);
                    }
                };
                builder.setTitle("详情");
                builder.addAction("确定", DialogListener.cancelAction);
                QMUIDialog qmuiDialog = builder.create();
                EditText editText = qmuiDialog.findViewById(R.id.importEdit);
                JsonFormatTool formatTool = new JsonFormatTool();
                if (result == null) {
                    String s = formatTool.formatJson(JSON.toJSONString(resultMsg));
                    editText.setText(s);
                } else {
                    if (result.getCode() == 5001) {
                        editText.setText(result.getMessage());
                    } else {
                        String s = formatTool.formatJson(JSON.toJSONString(result));
                        editText.setText(s);
                    }
                }
                qmuiDialog.show();
            }
        });


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultHeaderActivity.resultPojo = result;
                ResultHeaderActivity.start(getActivity(), ResultHeaderActivity.class);
            }
        });

        MarkdownWebView markdownWebView = findViewById(R.id.markdownWebView);

        qmuiTopBar.setTitle("响应结果");

        JsonFormatTool jsonFormatTool = new JsonFormatTool();
        if (result != null) {
            if (result.getCode() != 5001) {
                if (result.getType() != null) {
                    qmuiTopBar.setSubTitle(result.getTypeString());
                }
                markdownWebView.setText(result.getBody());
                String markdown = result.getBody();
                if (result.getTypeString().toLowerCase().contains("xml")) {
                    markdown = "```xml\n" + result.getBody() + "\n```";
                } else if (result.getTypeString().toLowerCase().contains("json")) {
                    markdown = "```json\n" + jsonFormatTool.formatJson(result.getBody()) + "\n```";
                }
                markdownWebView.setText(markdown);
                editText.setText(result.getBody());
            } else {
                markdownWebView.setText(result.getMessage());
                editText.setText(result.getCode() + ":" + result.getMessage());
            }
        } else {
            markdownWebView.setText(jsonFormatTool.formatJson(resultMsg));
            editText.setText(jsonFormatTool.formatJson(resultMsg));
        }


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getVisibility() == View.GONE) {
                    editText.setVisibility(View.VISIBLE);
                    markdownWebView.setVisibility(View.GONE);
                    groupPojo.setShowView("edit");
                } else {
                    editText.setVisibility(View.GONE);
                    markdownWebView.setVisibility(View.VISIBLE);
                    groupPojo.setShowView("web");
                }
                data.setGroupPojo(groupPojo);
            }
        });

        String showView = groupPojo.getShowView();
        if (showView == null || showView.equals("edit")) {
            editText.setVisibility(View.VISIBLE);
            markdownWebView.setVisibility(View.GONE);
        } else {
            editText.setVisibility(View.GONE);
            markdownWebView.setVisibility(View.VISIBLE);
        }
    }
}
