package com.demo.smarthome.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.EventBus_Account;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.myinterface.DeleteUserInterface;
import com.demo.smarthome.myinterface.HttpInterface;
import com.demo.smarthome.ui.adapter.UserManagementAdapter;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangdongyang on 17/2/7.
 */
public class UserManagementActivity extends Activity implements OnClickListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private Gson gson = new Gson();
    private KProgressHUD kProgressHUD;

    private int deleteId;
    private int deleteUid;

    private ImageView head_back;
    private TextView head_title;
    private ImageView head_img;

    private ListView user_management_listview;
    private UserManagementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);
        mam.pushOneActivity(UserManagementActivity.this);
        EventBus.getDefault().register(this);
        kProgressHUD = new KProgressHUD(this);
        init();
    }

    private void init() {
        head_back = (ImageView) this.findViewById(R.id.head_back);
        head_title = (TextView) this.findViewById(R.id.head_title);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        user_management_listview = (ListView) this.findViewById(R.id.user_management_listview);

        head_back.setOnClickListener(this);
        head_title.setText("人员管理");
        head_img.setVisibility(View.GONE);
        Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
        if (Constant.userProfileBean == null || Constant.userProfileBean.getHouses() == null) {
            AppUtils.show(UserManagementActivity.this, "暂无数据，请退出重试");
        } else {

            adapter = new UserManagementAdapter(UserManagementActivity.this, Constant.userProfileBean.getHouses(), new DeleteUserInterface() {
                @Override
                public void getDeleteUser(int id, int uid) {
                    deleteId = id;
                    deleteUid = uid;
                    Intent intent = new Intent();
                    intent.setClass(UserManagementActivity.this, MyDialogActivity.class);
                    startActivityForResult(intent, 1005);
                }
            });
            user_management_listview.setAdapter(adapter);
        }
    }


    /**
     */
    private void deleteUser() {

        kProgressHUD.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        String urlEnd = "smart/v1/House/User/" + deleteId + "/" + deleteUid;

        Call<ResultBean> call = httpInterface.deleteUser(urlEnd + "?accessToken=" + PreferenceUtil.getString("accessToken"));

        call.enqueue(new Callback<ResultBean>() {
                         @Override
                         public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                             kProgressHUD.dismiss();
                             ResultBean resultBean = response.body();
                             if (resultBean.getCode() == 200) {
                                 if (Constant.userProfileBean == null) {
                                     Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                                 }
                                 outer:
                                 for (int i = 0; i < Constant.userProfileBean.getHouses().size(); i++) {

                                     if (Constant.userProfileBean.getHouses().get(i).getId() == deleteId) {
                                         inner:
                                         for (int j = 0; j < Constant.userProfileBean.getHouses().get(i).getUserHouses().size(); j++) {
                                             if (Constant.userProfileBean.getHouses().get(i).getUserHouses().get(j).getUser_id() == deleteUid) {
                                                 Constant.userProfileBean.getHouses().get(i).getUserHouses().remove(Constant.userProfileBean.getHouses().get(i).getUserHouses().get(j));
                                                 AppUtils.show(UserManagementActivity.this, "删除成功");
                                                 break outer;
                                             }

                                         }
                                     }
                                 }
                                 PreferenceUtil.putString("user_profile", gson.toJson(Constant.userProfileBean));
                                 adapter.notifyDataSetChanged();
                             } else {
                                 AppUtils.show(UserManagementActivity.this, "删除失败");
                             }


                         }

                         @Override
                         public void onFailure(Call<ResultBean> call, Throwable t) {
                             kProgressHUD.dismiss();
                             AppUtils.show(UserManagementActivity.this, "删除失败");
                         }
                     }

        );

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.head_back:
                this.finish();
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getEventBus(EventBus_Account message) {
        int tag = message.tag;

        if (tag == Constant.EVENT_BUS_MODIFY_PERSONAL_REMARKS) {
//            System.out.println("----------999999--------"+Constants.outHouse.getResult().getHouses().get(0).getUserHouses().toString());
//            adapter = new UserManagementAdapter(UserManagementActivity.this, Constants.outHouse.getResult().getHouses());
//            user_management_listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mam.popOneActivity(UserManagementActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 121:
                int tag = data.getIntExtra("TAG_DELETE", -1);
                switch (tag) {
                    case 0:
                        deleteUser();
                        break;
                    case 1:
                        break;
                }
                break;
        }
    }


}
