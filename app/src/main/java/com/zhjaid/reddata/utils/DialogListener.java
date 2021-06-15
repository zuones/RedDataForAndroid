package com.zhjaid.reddata.utils;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

public class DialogListener {
    /**
     * 取消显示
     */
    public static final QMUIDialogAction.ActionListener cancelAction = new QMUIDialogAction.ActionListener() {
        @Override
        public void onClick(QMUIDialog dialog, int index) {
            dialog.dismiss();
        }
    };
}
