package com.demo.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.base.utils.view_util.XListView;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.LogList;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.ginseng.LogListGinseng;
import com.demo.smarthome.myinterface.HttpInterface;
import com.demo.smarthome.ui.adapter.NewDeviceListAdapter;
import com.demo.smarthome.ui.adapter.NewJournalAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 日志详情
 * Created by wangdongyang on 17/2/13.
 */
public class HistoryJournalActivity extends Activity implements OnClickListener, XListView.IXListViewListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private Gson gson = new Gson();

    private KProgressHUD kProgressHUD;
    private boolean refreshTag = false;

    private ImageView head_back;
    private TextView head_title;
    private ImageView head_img;
    //    private LRecyclerView journal_list;
    private NewJournalAdapter adapter;
    private List<LogList> logs;

//    private LRecyclerViewAdapter lRecyclerViewAdapter;

    private int page;
    private String product_key;
    private String title;

    private boolean scrollTag;


    private NewDeviceListAdapter newAdapter;
    private XListView journal_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        mam.pushOneActivity(HistoryJournalActivity.this);
        page = 0;
        scrollTag = true;
        refreshTag = false;
        kProgressHUD = new KProgressHUD(this);
        init();
    }

    private void init() {
        logs = new ArrayList<>();
        title = "历史纪录";
        product_key = PreferenceUtil.getString("product_key");

        head_back = (ImageView) this.findViewById(R.id.head_back);
        head_title = (TextView) this.findViewById(R.id.head_title);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        journal_list = (XListView) this.findViewById(R.id.journal_list);
//        journal_layout.addView(journal_list);

        head_title.setText(title);
        head_img.setVisibility(View.GONE);
        head_back.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newAdapter = new NewDeviceListAdapter(this, logs);
        journal_list.setAdapter(newAdapter);
        journal_list.setXListViewListener(this);
        //add a HeaderView

        scrollTag = true;
        kProgressHUD.show();
        getData(0);

//        journal_list.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                scrollTag = true;
//                getData(0);
//            }
//        });
//
//        journal_list.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                scrollTag = false;
//                getData(page + 1);
//            }
//        });

//        journal_list.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                scrollTag = true;
//                getData(0);
//            }
//
//            @Override
//            public void onLoadMore() {
//                scrollTag = false;
//                getData(page + 1);
//
//            }
//        });


    }


    private void getData(int pageTag) {
        refreshTag = true;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();
        LogListGinseng logListGinseng = new LogListGinseng();
        logListGinseng.setPage(pageTag);
        logListGinseng.setProduct_key(product_key);

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<ResultBean> call = httpInterface.getHistoryLogList("smart/v1/Event/EventLog?accessToken=" + PreferenceUtil.getString("accessToken"), logListGinseng);
        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                if (resultBean != null && resultBean.getCode() == 200) {
                    if (scrollTag) {
                        page = 0;
                        logs.clear();
                    } else {
                        page++;
                    }

                    ArrayList<LogList> logList = gson.fromJson(gson.toJson(resultBean.getResult()), new TypeToken<ArrayList<LogList>>() {
                    }.getType());
                    logs.addAll(logList);
//                    adapter.notifyDataSetChanged();
                    newAdapter.notifyDataSetChanged();
                } else {
                    AppUtils.show(HistoryJournalActivity.this, resultBean.getMessage());
                }
                onLoad();
                refreshTag = false;
                if(kProgressHUD!=null && kProgressHUD.isShowing()){
                    kProgressHUD.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                AppUtils.show(HistoryJournalActivity.this, "加载数据失败");
                onLoad();
                refreshTag = false;
                if(kProgressHUD!=null && kProgressHUD.isShowing()){
                    kProgressHUD.dismiss();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(HistoryJournalActivity.this);
    }

    @Override
    public void onRefresh() {
        scrollTag = true;
        journal_list.stopLoadMore();
        getData(0);
    }

    @Override
    public void onLoadMore() {
        if(!refreshTag){
            scrollTag = false;
            journal_list.stopRefresh();
            getData(page + 1);
        }
    }


    private void onLoad() {
        journal_list.stopRefresh();
        journal_list.stopLoadMore();
        journal_list.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
