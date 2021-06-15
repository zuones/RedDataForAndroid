package com.zhjaid.reddata.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.rmondjone.locktableview.LockTableView;
import com.rmondjone.xrecyclerview.ProgressStyle;
import com.rmondjone.xrecyclerview.XRecyclerView;
import com.zhjaid.reddata.BaseActivity;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.pojo.ApiPojo;
import com.zhjaid.reddata.pojo.ResultPojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Headers;

public class ResultHeaderActivity extends BaseActivity {
    private QMUITopBar qmuiTopBar;
    private LinearLayout content;
    private ArrayList<ArrayList<String>> mTableDatas = new ArrayList<>();
    private LockTableView mLockTableView;
    public static ResultPojo resultPojo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultheader);
        qmuiTopBar = findViewById(R.id.qmuiBar);
        content = findViewById(R.id.content);

        qmuiTopBar.setTitle("Result Header");
        qmuiTopBar.setTitleGravity(Gravity.LEFT | Gravity.CENTER);

        updateData();
    }

    /**
     * 加载请求参数
     */
    private void updateData() {
        mTableDatas.clear();
        //表头
        ArrayList<String> mfristData = new ArrayList<String>();
        mfristData.add("key");
        mfristData.add("value");
        mfristData.add("date");
        mTableDatas.add(mfristData);
        try {
            Headers headers = resultPojo.getHeaders();
            Set<String> names = headers.names();
            if (names == null || names.size() <= 0) {
                ArrayList<String> mRowDatas = new ArrayList<String>();
                //数据填充
                mRowDatas.add("暂无Header数据");
                mTableDatas.add(mRowDatas);
                return;
            }
            Iterator<String> iterator = names.iterator();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            while (iterator.hasNext()) {
                ArrayList<String> mRowDatas = new ArrayList<String>();
                //数据填充
                String next = iterator.next();
                mRowDatas.add(next);
                mRowDatas.add(headers.get(next));
                if (headers.getDate(next) != null)
                    mRowDatas.add(format.format(headers.getDate(next)));
                mTableDatas.add(mRowDatas);
            }
        } catch (Exception e) {

        }
        mLockTableView = new LockTableView(this, content, mTableDatas);
        //Log.e("表格加载开始", "当前线程：" + Thread.currentThread());
        mLockTableView.setLockFristColumn(false) //是否锁定第一列
                .setLockFristRow(true) //是否锁定第一行
                .setMaxColumnWidth(350) //列最大宽度
                .setMinColumnWidth(60) //列最小宽度
                .setColumnWidth(2, 200) //设置指定列文本宽度(从0开始计算,宽度单位dp)
                .setMinRowHeight(20)//行最小高度
                .setMaxRowHeight(60)//行最大高度
                .setTextViewSize(16) //单元格字体大小
                .setFristRowBackGroudColor(R.color.table_head)//表头背景色
                .setTableHeadTextColor(R.color.beijin)//表头字体颜色
                .setTableContentTextColor(R.color.border_color)//单元格字体颜色
                .setCellPadding(8)//设置单元格内边距(dp)
                .setNullableString("N/A") //空值替换值
                .setTableViewListener(new LockTableView.OnTableViewListener() {
                    @Override
                    public void onTableViewScrollChange(int x, int y) {
//                        Log.e("滚动值","["+x+"]"+"["+y+"]");
                    }
                })
                .setTableViewRangeListener(new LockTableView.OnTableViewRangeListener() {
                    @Override
                    public void onLeft(HorizontalScrollView view) {
//                        Log.e("滚动边界","滚动到最左边");
                    }

                    @Override
                    public void onRight(HorizontalScrollView view) {
//                        Log.e("滚动边界","滚动到最右边");
                    }
                })//设置横向滚动边界监听
                .setOnLoadingListener(new LockTableView.OnLoadingListener() {
                    @Override
                    public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                        mLockTableView.setTableDatas(mTableDatas);
                        mXRecyclerView.refreshComplete();
                    }

                    @Override
                    public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                        //Log.e("onLoadMore", Thread.currentThread().toString());
                        mXRecyclerView.setNoMore(true);
                    }
                })
                .setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
                    @Override
                    public void onItemClick(View item, int position) {
                        // 从API11开始android推荐使用android.content.ClipboardManager
                        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("result", JSON.toJSONString(mTableDatas.get(position)));
                        // 将文本内容放到系统剪贴板里。
                        cm.setPrimaryClip(clipData);
                        makeText("复制成功");
                    }
                })
                .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                    @Override
                    public void onItemLongClick(View item, int position) {
                        //Log.e("长按事件",position+"");
                    }
                })
                .setOnItemSeletor(R.color.OnItemSeletor)//设置Item被选中颜色
                .show(); //显示表格,此方法必须调用
        mLockTableView.getTableScrollView().setPullRefreshEnabled(false);
        mLockTableView.getTableScrollView().setLoadingMoreEnabled(false);
        mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
    }

}
