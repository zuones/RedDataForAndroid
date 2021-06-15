package com.zhjaid.reddata.adapter.viewholder;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.utils.RequestType;


public class GroupViewHolder extends RecyclerView.ViewHolder {
    private TextView groupTitle;
    private TextView count;
    private TextView apiSubTitle;
    private QMUIAlphaTextView edit, delete;
    private LinearLayout groupRoot;


    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        groupTitle = itemView.findViewById(R.id.groupTitle);
        groupRoot = itemView.findViewById(R.id.groupRoot);
        count = itemView.findViewById(R.id.count);
        apiSubTitle = itemView.findViewById(R.id.apiSubTitle);
        edit = itemView.findViewById(R.id.edit);
        delete = itemView.findViewById(R.id.delete);
    }

    public void setSubTitle(String dt) {
        if (dt == null || dt.length() <= 0)
            dt = "暂无备注";
        apiSubTitle.setText(dt);
    }

    public void setCount(String cut) {
        count.setText(cut);
        count.setTextColor(Color.WHITE);
        BitmapDrawable drawableWithSize =
                QMUIDrawableHelper.createDrawableWithSize(groupTitle.getResources(),
                        70, 70, 50, RequestType.getHashColor(cut));
        count.setBackground(drawableWithSize);
    }

    public void setDeleteOnClickListener(View.OnClickListener clickListener) {
        delete.setOnClickListener(clickListener);
    }

    public void setEditOnClickListener(View.OnClickListener clickListener) {
        edit.setOnClickListener(clickListener);
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        groupRoot.setOnClickListener(clickListener);
    }

    public void setTitle(String title) {
        groupTitle.setText(title);
    }
}
