package com.zhjaid.reddata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.activity.ApisActivity;
import com.zhjaid.reddata.activity.RequestActivity;
import com.zhjaid.reddata.adapter.viewholder.ApiViewHolder;
import com.zhjaid.reddata.data.BaseData;
import com.zhjaid.reddata.pojo.ApiPojo;
import com.zhjaid.reddata.utils.DialogListener;
import com.zhjaid.reddata.utils.RequestType;

import java.util.ArrayList;
import java.util.List;

public class ApisAdapter extends RecyclerView.Adapter {

    public ApisAdapter(Context context, ApiPojo apiPojo) {
        this.context = context;
        if (apiPojo == null) {
            this.apiPojo = new ApiPojo();
        } else {
            this.apiPojo = apiPojo;
        }
        if (this.apiPojo.getApis() == null) {
            this.apiPojo.setApis(new ArrayList<>());
        }
        this.inflater = LayoutInflater.from(context);
    }

    private Context context;

    public ApiPojo getApiPojo() {
        return apiPojo;
    }

    public void setApiPojo(ApiPojo apiPojo) {
        if (apiPojo != null) {
            this.apiPojo = apiPojo;
        }
        if (this.apiPojo != null){
            if (this.apiPojo.getApis() == null) {
                this.apiPojo.setApis(new ArrayList<>());
            }
        }
    }

    private ApiPojo apiPojo;
    private LayoutInflater inflater;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ApiViewHolder(inflater.inflate(R.layout.item_apis, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ApiViewHolder) {
            List<ApiPojo.Api> apis = apiPojo.getApis();
            ApiPojo.Api api = apis.get(position);
            ApiViewHolder viewHolder = (ApiViewHolder) holder;
            viewHolder.setTitle(api.getName());
            if (api.getRequest() != null)
                viewHolder.setType(api.getRequest().getMethod());
            viewHolder.setSubTitle(api.getRequest().getUrl().getRaw());
            viewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestActivity.apiPojo = api;
                    RequestActivity.start(context, RequestActivity.class);
                }
            });
            viewHolder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(context);
                    builder.setTitle("选择操作");
                    builder.addItem("编辑");
                    builder.addItem("查看JSON");
                    builder.addItem("删除");
                    builder.setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            dialog.dismiss();
                            if (position == 0) {
                                showEdit(api);
                            } else if (position == 1) {
                                showJson(api);
                            } else if (position == 2) {
                                showInfo(api);
                            }
                        }
                    });
                    builder.build().show();
                    return true;
                }
            });
        }
    }

    private void showEdit(ApiPojo.Api api) {
        QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle("提示").setPlaceholder(api.getName()).setDefaultText(api.getName())
                .addAction("保存", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String string = builder.getEditText().getText().toString();
                        if (string.length() > 0)
                            api.setName(string);

                        BaseData baseData = new BaseData(context);
                        baseData.updateRequest(ApisActivity.group, api);
                        dialog.dismiss();
                    }
                }).addAction("取消", DialogListener.cancelAction).create().show();
    }

    private void showJson(ApiPojo.Api api) {
        QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(context);
        builder.setTitle("提示").setMessage(JSON.toJSONString(api)).addAction("确定", DialogListener.cancelAction).create().show();
    }

    private void showInfo(ApiPojo.Api api) {
        QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(context);
        builder.setTitle("提示").setMessage("是否删除" + api.getName() + "?").addAction("删除", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                BaseData data = new BaseData(context);
                data.removeRequest(ApisActivity.group, api);
                dialog.dismiss();
            }
        }).addAction("取消", DialogListener.cancelAction).create().show();
    }

    @Override
    public int getItemCount() {
        if (apiPojo == null) {
            return 0;
        }
        List<ApiPojo.Api> apis = apiPojo.getApis();
        if (apis == null) {
            return 0;
        }
        return apis.size();
    }
}
