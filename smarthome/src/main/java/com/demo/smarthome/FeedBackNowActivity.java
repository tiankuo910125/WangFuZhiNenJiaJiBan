package com.demo.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.HeadPortrait;
import com.demo.smarthome.communication.jsonbean.ginseng.FeedBackGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.ImgUrl;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.EventBus_Account;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.OutHeadPortraits;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.myinterface.HttpInterface;
import com.demo.smarthome.ui.adapter.PhotoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 反馈页面
 * Created by wangdongyang on 16/8/24.
 */
public class FeedBackNowActivity extends Activity implements OnClickListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private KProgressHUD kProgressHUD;

    private ImageView head_back;
    private TextView head_title;
    private ImageView head_img;
    private TextView head_submit;

    private PhotoAdapter adapter;
    private ArrayList<String> list;

    private EditText feed_back_edit;//输入框
    private GridView noScrollgridview;//展示图片的gridview


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mam.pushOneActivity(FeedBackNowActivity.this);
        kProgressHUD = new KProgressHUD(FeedBackNowActivity.this);
        EventBus.getDefault().register(this);
        init();
    }

    public void init() {
        head_back = (ImageView) this.findViewById(R.id.head_back);
        head_title = (TextView) this.findViewById(R.id.head_title);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        head_submit = (TextView) this.findViewById(R.id.head_submit);

        feed_back_edit = (EditText) this.findViewById(R.id.feed_back_edit);
        noScrollgridview = (GridView) this.findViewById(R.id.noScrollgridview);


        head_title.setText("建议反馈");
        head_img.setVisibility(View.GONE);
        head_submit.setVisibility(View.VISIBLE);
        head_submit.setOnClickListener(this);
        head_back.setOnClickListener(this);


        feed_back_edit.addTextChangedListener(new EditChangedListener());

        list = new ArrayList<>();
        adapter = new PhotoAdapter(this, list);
        noScrollgridview.setAdapter(adapter);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.size() < 9 && position == list.size()) {
                    PhotoPicker.builder()
                            .setPhotoCount(9 - list.size())
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .start(FeedBackNowActivity.this, PhotoPicker.REQUEST_CODE);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(FeedBackNowActivity.this, BabyDetailImageGalleryActivity.class);
                    intent.putStringArrayListExtra("imgUrls", list);
                    intent.putExtra("index", position);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoon_in_out, 0);
                }
            }
        });
    }


    private void getUpLoadImg() {


        kProgressHUD.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file[" + i + "]", file.getName(), requestFile);
            parts.add(body);
        }

        // 创建 RequestBody，用于封装 请求RequestBody
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);


        Call<OutHeadPortraits> call = httpInterface.uploadImages("smart/v1/Public/MultiUpload?accessToken=" + PreferenceUtil.getString("accessToken"), parts);

        call.enqueue(new Callback<OutHeadPortraits>() {
            @Override
            public void onResponse(Call<OutHeadPortraits> call, Response<OutHeadPortraits> response) {

                kProgressHUD.dismiss();
                OutHeadPortraits outHeadPortraits = response.body();

                if (outHeadPortraits != null && outHeadPortraits.getCode() == 200) {
                    List<String> imgs = new ArrayList<>();
                    for (HeadPortrait headPortrait : outHeadPortraits.getResult()) {
                        imgs.add(headPortrait.getUrl());
                    }
                    FeedBackGinseng feedBackGinseng = new FeedBackGinseng();
                    feedBackGinseng.setContent(feed_back_edit.getText().toString());
                    List<ImgUrl> imgUrls = new ArrayList<ImgUrl>();
                    for(String img:imgs){
                        ImgUrl imgUrl = new ImgUrl();
                        imgUrl.setUrl(img);
                        imgUrls.add(imgUrl);
                    }

                    feedBackGinseng.setAttaches(imgUrls);
                    getFeedBack(feedBackGinseng);
//                    String[] imgs = new String[list.size()];
//                    for(int i = 0 ; i<outHeadPortraits.getResult().size();i++){
//                        imgs[i] = outHeadPortraits.getResult().get(i).getUrl();
//                    }
//                    for (HeadPortrait headPortrait : outHeadPortraits.getResult()) {
//                        imgs.add(headPortrait.getUrl());
//                    }
//                    FeedBackGinseng feedBackGinseng = new FeedBackGinseng();
//                    feedBackGinseng.setContent(feed_back_edit.getText().toString());
//                    feedBackGinseng.setAttaches(imgs);
                    getFeedBack(feedBackGinseng);
                } else {
                    kProgressHUD.dismiss();
                    AppUtils.show(FeedBackNowActivity.this, "反馈失败");
                }
            }

            @Override
            public void onFailure(Call<OutHeadPortraits> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(FeedBackNowActivity.this, "反馈失败");
            }
        });
    }


    private void getFeedBack(FeedBackGinseng feedBackGinseng) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();
        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<ResultBean> call = httpInterface.getFeedBack("smart/v1/Account/Advise?accessToken=" + PreferenceUtil.getString("accessToken"), feedBackGinseng);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {

                kProgressHUD.dismiss();
                ResultBean resultBean = response.body();
                if (resultBean != null && resultBean.getCode() == 200) {
                    AppUtils.show(FeedBackNowActivity.this, "反馈成功");
                    finish();
                } else {
                    AppUtils.show(FeedBackNowActivity.this, "反馈失败");
                }
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(FeedBackNowActivity.this, "反馈失败");
            }
        });
    }


    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 200;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getEventBus(EventBus_Account message) {
        int tag = message.tag;
        int number = message.number;
        String address = message.address;
        if (tag == Constant.EVENT_BUS_MODIFY_PERSONAL_DELETE_IMG_ID) {
            list.remove(address);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back://返回
                this.finish();
                break;
            case R.id.head_submit://提交
                if (feed_back_edit.getText().toString().trim() == null || feed_back_edit.getText().toString().trim().equals("")) {
                    AppUtils.show(FeedBackNowActivity.this, "反馈信息不能为空");
                } else {
                    if (list.size() > 0) {
                        getUpLoadImg();
                    } else {
                        FeedBackGinseng feedBackGinseng = new FeedBackGinseng();
                        feedBackGinseng.setContent(feed_back_edit.getText().toString());
                        feedBackGinseng.setAttaches(new ArrayList<ImgUrl>());
                        getFeedBack(feedBackGinseng);
                    }
                }



                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            list.addAll(photos);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mam.popOneActivity(FeedBackNowActivity.this);
    }
}
