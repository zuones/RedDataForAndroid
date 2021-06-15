package com.zhjaid.reddata.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.activity.ApisActivity;
import com.zhjaid.reddata.activity.RequestActivity;
import com.zhjaid.reddata.adapter.viewholder.GroupViewHolder;
import com.zhjaid.reddata.data.BaseData;
import com.zhjaid.reddata.pojo.GroupPojo;
import com.zhjaid.reddata.utils.DialogListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页的组适配器
 */
public class GroupAdapter extends RecyclerView.Adapter {

    public GroupAdapter(Activity activity, List<GroupPojo.Group> groups) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        if (groups == null)
            this.groups = new ArrayList<>();
        else
            this.groups = groups;
    }

    public void updateGroups(List<GroupPojo.Group> groups) {
        if (groups != null) {
            this.groups.clear();
            this.groups.addAll(groups);
            notifyDataSetChanged();
        }
    }

    public void remove() {
        this.groups.clear();
    }

    private Activity activity;
    private List<GroupPojo.Group> groups;
    private LayoutInflater inflater;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(inflater.inflate(R.layout.item_postman, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            GroupPojo.Group group = groups.get(position);
            groupViewHolder.setTitle(group.getName());
            groupViewHolder.setSubTitle("共有" + group.getCount() + "个API");
            groupViewHolder.setCount(group.getName().substring(0, 1));
            groupViewHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApisActivity.group = group;
                    RequestActivity.group = group;
                    ApisActivity.start(activity, ApisActivity.class);
                }
            });


            groupViewHolder.setDeleteOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(activity);
                    builder.setTitle("提示").setMessage("是否删除" + group.getName() + "?").addAction("删除", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            BaseData data = new BaseData(activity);
                            data.removeGroup(group);
                            dialog.dismiss();
                        }
                    }).addAction("取消", DialogListener.cancelAction).create().show();
                }
            });
            groupViewHolder.setEditOnClickListener(new View.OnClickListener() {
                private QMUIDialog qmuiDialog;

                @Override
                public void onClick(View v) {
                    QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(activity);
                    qmuiDialog = builder.setTitle("提示").setDefaultText(group.getName()).addAction("保存", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            String string = builder.getEditText().getText().toString();
                            if (string.length() > 0) {
                                group.setName(string);
                            }
                            BaseData data = new BaseData(activity);
                            data.updateGroup(group);
                            dialog.dismiss();
                        }
                    }).addAction("取消", DialogListener.cancelAction).create();
                    qmuiDialog.show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
