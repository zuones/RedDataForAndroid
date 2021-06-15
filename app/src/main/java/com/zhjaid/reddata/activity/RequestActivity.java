package com.zhjaid.reddata.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.textview.QMUILinkTextView;
import com.rmondjone.locktableview.DisplayUtil;
import com.rmondjone.locktableview.LockTableView;
import com.rmondjone.xrecyclerview.ProgressStyle;
import com.rmondjone.xrecyclerview.XRecyclerView;
import com.zhjaid.reddata.BaseActivity;
import com.zhjaid.reddata.R;
import com.zhjaid.reddata.data.BaseData;
import com.zhjaid.reddata.pojo.ApiPojo;
import com.zhjaid.reddata.pojo.GroupPojo;
import com.zhjaid.reddata.pojo.ResultPojo;
import com.zhjaid.reddata.utils.DialogListener;
import com.zhjaid.reddata.utils.HttpUtils;
import com.zhjaid.reddata.utils.JsonFormatTool;
import com.zhjaid.reddata.utils.MarkdownEntity;
import com.zhjaid.reddata.utils.RequestType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestActivity extends BaseActivity {
    private LinearLayout contentView, headerView;
    private EditText apisApi;
    private ArrayList<ArrayList<String>> mTableDatas = new ArrayList<>();
    private ArrayList<ArrayList<String>> mHeaderDatas = new ArrayList<>();
    private LockTableView mLockTableView, mLockHeaderTableView;
    private QMUITopBar qmuiTopbar;
    private TextView requestMethod, parmter;
    private QMUIRoundButton selectMed, sendData, saveData;
    public static GroupPojo.Group group;
    public static ApiPojo.Api apiPojo;
    private BaseData baseData;
    private QMUILinkTextView qlink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        initViews();
        initDisplayOpinion();
        updateData();
        loadHeaderData();
        checkMethod();

        ColorStateList colorStateList = getResources().getColorStateList(R.color.text_color_state_list);
        qlink.setText("RedData旨为大家提供一个手机端完整接口测试的工具\n一、软件如有意见或者建议请联系2372315936\n二、联系邮箱zhjaid@163.com\n三、更多请关注http://www.zhjaid.com", TextView.BufferType.SPANNABLE);
        qlink.setLinkTextColor(colorStateList);
        qlink.setOnLinkClickListener(new QMUILinkTextView.OnLinkClickListener() {
            @Override
            public void onTelLinkClick(String phoneNumber) {
                try {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + phoneNumber;
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    makeText("唤醒QQ失败，QQ号已复制");
                    copy(phoneNumber);
                }
            }

            @Override
            public void onMailLinkClick(String mailAddress) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822"); // 真机上使用这行
                    i.putExtra(Intent.EXTRA_EMAIL,
                            new String[]{mailAddress});
                    i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
                    i.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
                    startActivity(Intent.createChooser(i,
                            "选择邮箱应用"));
                } catch (Exception e) {
                    makeText("唤醒邮件应用失败，邮箱已复制");
                    copy(mailAddress);
                }

            }

            @Override
            public void onWebUrlLinkClick(String url) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(Intent.createChooser(intent,
                            "选择浏览器应用"));
                } catch (Exception e) {
                    makeText("唤醒浏览器失败，地址已复制");
                    copy(url);
                }
            }
        });
    }

    public void copy(String str) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("result", str);
        // 将文本内容放到系统剪贴板里。
        cm.setPrimaryClip(clipData);
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }

    private void checkMethod() {
        String string = requestMethod.getText().toString();
        requestMethod.setTextColor(RequestType.getColor(string));
        if (string.toUpperCase().equals("GET") || string.toUpperCase().equals("HEAD")) {
            contentView.setVisibility(View.GONE);
            parmter.setVisibility(View.GONE);
        } else {
            parmter.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载请求头
     */
    private void loadHeaderData() {
        mHeaderDatas.clear();
        //表头
        ArrayList<String> mfristData = new ArrayList<String>();
        mfristData.add("key");
        mfristData.add("value");
        mfristData.add("description");
        mHeaderDatas.add(mfristData);
        try {
            List<ApiPojo.Api.Request.Header> headers = apiPojo.getRequest().getHeader();
            for (ApiPojo.Api.Request.Header header : headers) {
                ArrayList<String> mRowDatas = new ArrayList<String>();
                //数据填充
                mRowDatas.add(header.getKey());
                mRowDatas.add(header.getValue());
                mRowDatas.add(header.getDescription());
                mHeaderDatas.add(mRowDatas);
            }
        } catch (Exception e) {

        } finally {
            ArrayList<String> mRowDatas = new ArrayList<String>();
            //数据填充
            mRowDatas.add("点击添加");
            mHeaderDatas.add(mRowDatas);
        }
        mLockHeaderTableView = new LockTableView(this, headerView, mHeaderDatas);
        //Log.e("表格加载开始", "当前线程：" + Thread.currentThread());
        mLockHeaderTableView.setLockFristColumn(false) //是否锁定第一列
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
                        mLockHeaderTableView.setTableDatas(mTableDatas);
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
                        if (position >= mHeaderDatas.size() - 1) {
                            showAddRequest(true);
                        } else {
                            showUpdateRequest(position, true);
                        }
                    }
                })
                .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                    @Override
                    public void onItemLongClick(View item, int position) {
                        //Log.e("长按事件",position+"");
                        if (position < mHeaderDatas.size() - 1 && position > 0) {
                            QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(getActivity());
                            builder.setTitle("提示").setMessage("是否删除选中头信息?").addAction("删除", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    mHeaderDatas.remove(position);
                                    mLockHeaderTableView.setTableDatas(mHeaderDatas);
                                    dialog.dismiss();
                                }
                            }).addAction("取消", DialogListener.cancelAction).create().show();
                        }
                    }
                })
                .setOnItemSeletor(R.color.OnItemSeletor)//设置Item被选中颜色
                .show(); //显示表格,此方法必须调用
        mLockHeaderTableView.getTableScrollView().setPullRefreshEnabled(false);
        mLockHeaderTableView.getTableScrollView().setLoadingMoreEnabled(false);
        mLockHeaderTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
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
        mfristData.add("description");
        mTableDatas.add(mfristData);
        try {
            List<ApiPojo.Api.Request.Url.Query> query = apiPojo.getRequest().getUrl().getQuery();
            for (ApiPojo.Api.Request.Url.Query query1 : query) {
                ArrayList<String> mRowDatas = new ArrayList<String>();
                //数据填充
                mRowDatas.add(query1.getKey());
                mRowDatas.add(query1.getValue());
                mRowDatas.add(query1.getDescription());
                mTableDatas.add(mRowDatas);
            }
        } catch (Exception e) {

        } finally {
            ArrayList<String> mRowDatas = new ArrayList<String>();
            //数据填充
            mRowDatas.add("点击添加");
            mTableDatas.add(mRowDatas);
        }
        mLockTableView = new LockTableView(this, contentView, mTableDatas);
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
                        if (position >= mTableDatas.size() - 1) {
                            showAddRequest(false);
                        } else {
                            showUpdateRequest(position, false);
                        }
                    }
                })
                .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                    @Override
                    public void onItemLongClick(View item, int position) {
                        //Log.e("长按事件",position+"");
                        if (position < mTableDatas.size() - 1 && position > 0) {
                            QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(getActivity());
                            builder.setTitle("提示").setMessage("是否删除选中参数?").addAction("删除", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    mTableDatas.remove(position);
                                    mLockTableView.setTableDatas(mTableDatas);
                                    dialog.dismiss();
                                }
                            }).addAction("取消", DialogListener.cancelAction).create().show();
                        }
                    }
                })
                .setOnItemSeletor(R.color.OnItemSeletor)//设置Item被选中颜色
                .show(); //显示表格,此方法必须调用
        mLockTableView.getTableScrollView().setPullRefreshEnabled(false);
        mLockTableView.getTableScrollView().setLoadingMoreEnabled(false);
        mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
    }

    private EditText inputKey, inputValue, inputDescription;

    private void showUpdateRequest(int position, boolean isHeader) {
        QMUIDialog.AutoResizeDialogBuilder customDialogBuilder =
                new QMUIDialog.AutoResizeDialogBuilder(getActivity()) {
                    @Override
                    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
                        return LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
                    }
                };
        final ArrayList<String> mRowDatas = new ArrayList<>();
        if (isHeader) {
            mRowDatas.addAll(mHeaderDatas.get(position));
            customDialogBuilder.setTitle("修改请求头");
        } else {
            mRowDatas.addAll(mTableDatas.get(position));
            customDialogBuilder.setTitle("修改参数");
        }
        customDialogBuilder.addAction("取消", DialogListener.cancelAction);
        customDialogBuilder.addAction("修改", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                if (inputKey.getText().toString().equals("") || inputValue.getText().toString().equals("")) {
                    makeText("key 和 value为必填项");
                } else {
                    mRowDatas.clear();
                    //数据填充
                    mRowDatas.add(inputKey.getText().toString());
                    mRowDatas.add(inputValue.getText().toString());
                    mRowDatas.add(inputDescription.getText().toString());
                    if (isHeader) {
                        mHeaderDatas.remove(position);
                        mHeaderDatas.add(position, mRowDatas);
                        mLockHeaderTableView.setTableDatas(mHeaderDatas);
                    } else {
                        mTableDatas.remove(position);
                        mTableDatas.add(position, mRowDatas);
                        mLockTableView.setTableDatas(mTableDatas);
                    }
                    dialog.dismiss();
                }
            }
        });
        qmuiDialog = customDialogBuilder.create();

        inputKey = qmuiDialog.findViewById(R.id.inputKey);
        inputValue = qmuiDialog.findViewById(R.id.inputValue);
        inputDescription = qmuiDialog.findViewById(R.id.inputDescription);
        if (isHeader) {
            if (mRowDatas.size() > 0) {
                inputKey.setText(mRowDatas.get(0) == null ? "" : mRowDatas.get(0));
                inputValue.setText(mRowDatas.get(1) == null ? "" : mRowDatas.get(1));
                inputDescription.setText(mRowDatas.get(2) == null ? "" : mRowDatas.get(2));
            }
        } else {
            if (mRowDatas.size() > 0) {
                inputKey.setText(mRowDatas.get(0) == null ? "" : mRowDatas.get(0));
                inputValue.setText(mRowDatas.get(1) == null ? "" : mRowDatas.get(1));
                inputDescription.setText(mRowDatas.get(2) == null ? "" : mRowDatas.get(2));
            }
        }

        qmuiDialog.show();
    }

    QMUIDialog qmuiDialog = null;

    private void showAddRequest(boolean isHeader) {
        QMUIDialog.AutoResizeDialogBuilder customDialogBuilder =
                new QMUIDialog.AutoResizeDialogBuilder(getActivity()) {
                    @Override
                    public View onBuildContent(@NonNull QMUIDialog dialog, @NonNull Context context) {
                        return LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
                    }
                };
        customDialogBuilder.setTitle("添加");
        customDialogBuilder.addAction("取消", DialogListener.cancelAction);
        customDialogBuilder.addAction("确定", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                EditText inputKey = qmuiDialog.findViewById(R.id.inputKey);
                EditText inputValue = qmuiDialog.findViewById(R.id.inputValue);
                EditText inputDescription = qmuiDialog.findViewById(R.id.inputDescription);
                if (inputKey.getText().toString().equals("") || inputValue.getText().toString().equals("")) {
                    makeText("key 和 value为必填项");
                } else {
                    ArrayList<String> mRowDatas = new ArrayList<String>();
                    //数据填充
                    mRowDatas.add(inputKey.getText().toString());
                    mRowDatas.add(inputValue.getText().toString());
                    mRowDatas.add(inputDescription.getText().toString());
                    if (isHeader) {
                        //请求头
                        mHeaderDatas.add(mHeaderDatas.size() - 1, mRowDatas);
                        mLockHeaderTableView.setTableDatas(mHeaderDatas);
                    } else {
                        //请求参数
                        mTableDatas.add(mTableDatas.size() - 1, mRowDatas);
                        mLockTableView.setTableDatas(mTableDatas);
                    }
                    dialog.dismiss();
                }
            }
        });
        qmuiDialog = customDialogBuilder.create();
        qmuiDialog.show();
    }

    /**
     * 删除参数
     */
    public void showDelete() {

    }

    private void initViews() {
        contentView = findViewById(R.id.contentView);
        headerView = findViewById(R.id.headerView);
        apisApi = findViewById(R.id.apisApi);
        qmuiTopbar = findViewById(R.id.qmuiTopbar);
        selectMed = findViewById(R.id.selectMed);
        requestMethod = findViewById(R.id.requestMethod);
        parmter = findViewById(R.id.parmter);
        qlink = findViewById(R.id.qlink);

        qmuiTopbar.setTitle("接口测试");

        baseData = new BaseData(getActivity());

        if (apiPojo.getRequest() != null) {
            String method = apiPojo.getRequest().getMethod();
            if (method != null) {
                requestMethod.setText(method);
            }
        }

        if (apiPojo != null && apiPojo.getRequest() != null && apiPojo.getRequest().getUrl() != null && apiPojo.getRequest().getUrl().getRaw() != null)
            apisApi.setText(apiPojo.getRequest().getUrl().getRaw());
        if (apiPojo != null && apiPojo.getName() != null)
            qmuiTopbar.setTitle(apiPojo.getName());
        selectMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(getActivity());
                builder.setTitle("请求方式");
                builder.addItem("GET");
                builder.addItem("POST");
                builder.addItem("PUT");
                builder.addItem("PATCH");
                builder.addItem("DELETE");
                builder.addItem("HEAD");
                builder.setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        requestMethod.setText(tag);
                        requestMethod.setTextColor(RequestType.getColor(tag));
                        checkMethod();
                    }
                });
                builder.build().show();
            }
        });
        requestMethod.setTextColor(RequestType.getColor(apiPojo.getRequest().getMethod()));

        sendData = findViewById(R.id.sendData);
        saveData = findViewById(R.id.saveData);
        sendData.setText("发送请求");
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData.setText("正在发送");
                sendData.setEnabled(false);
                try {
                    //请求地址
                    String raw = apisApi.getText().toString();
                    //请求参数
                    List<HttpUtils.Parameter> parameters = new ArrayList<>();
                    for (int i = 0; i < mTableDatas.size(); i++) {
                        if (i > 0 && i < mTableDatas.size() - 1) {
                            ArrayList<String> strings = mTableDatas.get(i);
                            parameters.add(new HttpUtils.Parameter(strings.get(0), strings.get(1)));
                        }
                    }
                    //请求方式
                    String method = requestMethod.getText().toString();
                    new Thread(new Runnable() {
                        String markdown = null;

                        @SneakyThrows
                        @Override
                        public void run() {
                            ResultPojo pojo = HttpUtils.sendData(raw, parameters, getHeaders(), method);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    result(pojo);
                                }
                            });

                        }
                    }).start();
                } catch (Exception e) {
                    ResultPojo pojo = new ResultPojo();
                    pojo.setBody(e.getMessage());
                    pojo.setMessage(e.getMessage());
                    pojo.setCode(5001);
                    result(pojo);
                }
            }
        });


        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveConfig();
            }
        });
    }


    private List<ApiPojo.Api.Request.Header> getHeaders() {
        List<ApiPojo.Api.Request.Header> headers = new ArrayList<>();
        for (int i = 0; i < mHeaderDatas.size(); i++) {
            if (i > 0 && i < mHeaderDatas.size() - 1) {
                ArrayList<String> strings = mHeaderDatas.get(i);
                headers.add(new ApiPojo.Api.Request.Header(strings.get(0), strings.get(1), strings.get(2)));
            }
        }
        return headers;
    }

    private List<ApiPojo.Api.Request.Url.Query> getQuerys() {
        List<ApiPojo.Api.Request.Url.Query> query = new ArrayList<>();
        for (int i = 0; i < mTableDatas.size(); i++) {
            if (i > 0 && i < mTableDatas.size() - 1) {
                ArrayList<String> strings = mTableDatas.get(i);
                query.add(new ApiPojo.Api.Request.Url.Query(strings.get(0), strings.get(1), strings.get(2)));
            }
        }
        return query;
    }

    private void saveConfig() {
        if (apiPojo == null) {
            apiPojo = new ApiPojo.Api();
        }
        if (apiPojo.getRequest() == null) {
            apiPojo.setRequest(new ApiPojo.Api.Request());
        }
        if (apiPojo.getRequest().getUrl() == null) {
            apiPojo.getRequest().setUrl(new ApiPojo.Api.Request.Url());
        }


        apiPojo.getRequest().setHeader(getHeaders());
        apiPojo.getRequest().setMethod(requestMethod.getText().toString());
        apiPojo.getRequest().getUrl().setQuery(getQuerys());
        apiPojo.getRequest().getUrl().setRaw(apisApi.getText().toString());
        boolean b = baseData.updateRequest(group, apiPojo);
    }

    private void result(ResultPojo pojo) {
        sendData.setText("发送请求");
        sendData.setEnabled(true);
        ResultActivity.result = pojo;
        ResultActivity.start(getActivity(), ResultActivity.class);
    }
}
