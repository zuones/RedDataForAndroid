package com.zhjaid.reddata.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.zhjaid.reddata.BaseActivity;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.data.BaseData;
import com.zhjaid.reddata.pojo.ApiPojo;
import com.zhjaid.reddata.pojo.GroupPojo;
import com.zhjaid.reddata.utils.DialogListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImportActivity extends BaseActivity {
    private QMUITopBar qmuiTopbar;
    private EditText apiName;
    private EditText apiAddress;
    private QMUIRoundButton inputBtn;

    private BaseData baseData;
    public static GroupPojo.Group group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        initViews();

        initData();
    }

    private void initData() {
        baseData = new BaseData(getActivity());
        qmuiTopbar.setTitle("新增API");

        inputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = apiName.getText().toString();
                String address = apiAddress.getText().toString();
                if (name.equals("") || address.equals("")) {
                    makeText("数据不完整");
                } else {
                    ApiPojo.Api api = new ApiPojo.Api();
                    ApiPojo.Api.Request request = new ApiPojo.Api.Request();
                    ApiPojo.Api.Request.Url url = new ApiPojo.Api.Request.Url();
                    url.setRaw(address);
                    request.setUrl(url);
                    api.setRequest(request);
                    api.setName(name);
                    api.setApiId(UUID.randomUUID().toString());
                    boolean b = baseData.putRequest(group, api);
                    if (b) {
                        makeText("添加成功");
                        finish();
                    } else {
                        makeText("添加失败");
                    }
                }
            }
        });

        findViewById(R.id.importBtn).setOnClickListener(new View.OnClickListener() {
            private QMUIDialog qmuiDialog;

            @Override
            public void onClick(View v) {
                QMUIDialog.AutoResizeDialogBuilder builder = new QMUIDialog.AutoResizeDialogBuilder(getActivity()) {
                    @Override
                    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
                        return LayoutInflater.from(getActivity()).inflate(R.layout.dialog_import, null);
                    }
                };
                builder.setTitle("导入API");
                builder.addAction("导入", new QMUIDialogAction.ActionListener() {

                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        EditText editText = qmuiDialog.findViewById(R.id.importEdit);
                        String string = editText.getText().toString();
                        if (string.length() <= 0) {
                            makeText("需输入内容");
                        } else {
                            try {
                                JSONObject jsonObject = JSON.parseObject(string);
                                JSONArray array = jsonObject.getJSONArray("item");
                                for (int i = 0; i < array.size(); i++) {
                                    ApiPojo.Api apipojo = new ApiPojo.Api();
                                    apipojo.setApiId(UUID.randomUUID().toString());
                                    JSONObject api = array.getJSONObject(i);
                                    //名字
                                    String name = api.getString("name");
                                    apipojo.setName(name);
                                    JSONObject request = api.getJSONObject("request");
                                    apipojo.setRequest(new ApiPojo.Api.Request());
                                    //请求方式
                                    String method = request.getString("method");
                                    apipojo.getRequest().setMethod(method);
                                    JSONObject url = request.getJSONObject("url");
                                    apipojo.getRequest().setUrl(new ApiPojo.Api.Request.Url());

                                    if (apipojo.getRequest().getHeader() == null) {
                                        apipojo.getRequest().setHeader(new ArrayList<>());
                                    }
                                    //请求头
                                    apipojo.getRequest().getHeader().add(new ApiPojo.Api.Request.Header("User-Agent", "RedData/1.0", "text"));
                                    apipojo.getRequest().getHeader().add(new ApiPojo.Api.Request.Header("Accept", "*/*", "text"));
                                    apipojo.getRequest().getHeader().add(new ApiPojo.Api.Request.Header("Accept-Encoding", "gzip, deflate, br", "text"));
                                    apipojo.getRequest().getHeader().add(new ApiPojo.Api.Request.Header("Connection", "keep-alive", "text"));
                                    //请求地址
                                    String raw = url.getString("raw");
                                    apipojo.getRequest().getUrl().setRaw(raw);
                                    JSONArray query = url.getJSONArray("query");
                                    List<ApiPojo.Api.Request.Url.Query> queryList = new ArrayList<>();
                                    for (int j = 0; j < query.size(); j++) {
                                        JSONObject qj = query.getJSONObject(j);
                                        queryList.add(new ApiPojo.Api.Request.Url.Query(qj.getString("key"), qj.getString("value"), qj.getString("description")));
                                    }
                                    apipojo.getRequest().getUrl().setQuery(queryList);
                                    baseData.putRequest(group, apipojo);
                                }
                                makeText("成功导入" + array.size() + "条数据");
                                dialog.dismiss();
                            } catch (Exception e) {
                                makeText("JSON 格式有误");
                            }
                        }
                    }
                });
                builder.addAction("取消", DialogListener.cancelAction);
                qmuiDialog = builder.create();
                qmuiDialog.show();
            }
        });
    }

    private void initViews() {
        qmuiTopbar = findViewById(R.id.qmuiTopbar);
        apiName = findViewById(R.id.apiName);
        apiAddress = findViewById(R.id.apiAddress);
        inputBtn = findViewById(R.id.inputBtn);
    }
}
