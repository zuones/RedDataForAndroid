package com.zhjaid.reddata.adapter.viewholder;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.utils.RequestType;


public class ApiViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView;
    private TextView subTitleView;
    private TextView apiType;
    private QMUIAlphaTextView delete;
    private LinearLayout apiRoot;

    public ApiViewHolder(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.apiTitle);
        subTitleView = itemView.findViewById(R.id.apiSubTitle);
        apiRoot = itemView.findViewById(R.id.apiRoot);
        apiType = itemView.findViewById(R.id.apiType);
        delete = itemView.findViewById(R.id.delete);
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        apiRoot.setOnClickListener(clickListener);
    }

    public void setOnLongClickListener(View.OnLongClickListener l) {
        apiRoot.setOnLongClickListener(l);
    }

    public void setDeleteOnClickListener(View.OnClickListener clickListener) {
        delete.setOnClickListener(clickListener);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setType(String type) {
        if (type == null || type.length() <= 0)
            type = "GET";
        apiType.setText(type);

        BitmapDrawable drawableWithSize =
                QMUIDrawableHelper.createDrawableWithSize(apiType.getResources(),
                        40, 20, 25, RequestType.getColor(type));
        apiType.setBackground(drawableWithSize);
        apiType.setTextColor(Color.WHITE);
    }

    public void setSubTitle(String subTitle) {
        subTitleView.setText(subTitle);
    }
}
