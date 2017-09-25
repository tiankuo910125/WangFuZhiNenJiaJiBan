package com.demo.smarthome.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.EventBus_Account;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.communication.jsonbean.ginseng.Modify;
import com.demo.smarthome.myinterface.HttpInterface;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangdongyang on 17/2/7.
 */
public class ModifyActivity extends Activity implements OnClickListener{
    MyActivityManager mam = MyActivityManager.getInstance();
    private KProgressHUD kProgressHUD;
    private Gson gson = new Gson();

    private String houseName;

    private Intent getIntent;
    private int tag;
    private String id;
    private String uid;

    private EditText modify_edit;

    private ImageView head_back;
    private TextView head_title;
    private ImageView head_img;
    private TextView head_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actviity_modify);
        mam.pushOneActivity(ModifyActivity.this);
        kProgressHUD = new KProgressHUD(this);
        init();
    }

    private void init() {
        getIntent = getIntent();
        tag = getIntent.getIntExtra("TAG", -1);
        id = getIntent.getStringExtra("ID");
        uid = getIntent.getStringExtra("UID");
        houseName = getIntent.getStringExtra("HOUSENAME");

        modify_edit = (EditText) this.findViewById(R.id.modify_edit);
        head_back = (ImageView) this.findViewById(R.id.head_back);
        head_title = (TextView) this.findViewById(R.id.head_title);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        head_submit = (TextView) this.findViewById(R.id.head_submit);

        head_title.setText("修改备注名称");
        head_img.setVisibility(View.GONE);
        head_submit.setVisibility(View.VISIBLE);

        head_back.setOnClickListener(this);
        head_submit.setOnClickListener(this);

    }

    /**
     * tag  1--房子  2-－房间
     *
     * @param tag
     */
    private void getManagementDataHouse(final int tag) {

        kProgressHUD.show();
        final Modify modify = new Modify();

        if (tag == 1){
            modify.setName(modify_edit.getText().toString());
        }else if(tag == 2){
            modify.setName(houseName+"--"+modify_edit.getText().toString());
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        String urlEnd = "";
        switch (tag) {
            case 1:
                urlEnd = "smart/v1/House/";
                break;
            case 2:
                urlEnd = "smart/v1/Room/";
                break;
        }

        Call<ResultBean> call = httpInterface.getModifyHouse(urlEnd + id + "?accessToken=" + PreferenceUtil.getString("accessToken"), modify);
        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                System.out.println("-------bbbbbbbbbb--aaaaaaaaa-----" + response.body().toString());
                kProgressHUD.dismiss();
                ResultBean resultBean = response.body();
                if (resultBean.getCode() == 200) {
                    if (Constant.userProfileBean == null) {
                        Constant.userProfileBean = gson.fromJson( PreferenceUtil.getString("user_profile"),UserProfileBean.class);
                    }

                    switch (tag) {
                        case 1:
                            for (int i = 0; i < Constant.userProfileBean.getHouses().size(); i++) {

                                if (Constant.userProfileBean.getHouses().get(i).getId() == Integer.valueOf(id)) {
                                    Constant.userProfileBean.getHouses().get(i).setName(modify.getName());
                                    break;
                                }
                            }
                            break;
                        case 2:
                            outer:
                            for (int i = 0; i < Constant.userProfileBean.getHouses().size(); i++) {
                                inner:
                                for (int j = 0; i < Constant.userProfileBean.getHouses().get(i).getRooms().size(); j++) {
                                    if (Constant.userProfileBean.getHouses().get(i).getRooms().get(j).getId() == Integer.valueOf(id)) {
                                        Constant.userProfileBean.getHouses().get(i).getRooms().get(j).setName(modify.getName());
                                        break outer;
                                    }
                                }

                            }
                            break;

                    }

                    PreferenceUtil.putString("user_profile", gson.toJson(Constant.userProfileBean));
                    Intent intent = new Intent();
                    switch (tag) {
                        case 1:
                            setResult(111, intent);
                            break;
                        case 2:
                            setResult(112, intent);
                            break;
                    }

                    EventBus.getDefault().post(new EventBus_Account(Constant.EVENT_BUS_MODIFY_PERSONAL_HOUSE));
                    getFinish();
                } else {
                    AppUtils.show(ModifyActivity.this, "修改失败");
                }


            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(ModifyActivity.this, "修改失败");
                System.out.println("---aaaaa--failure--bbbbb---" + t.getMessage());
            }
        });

    }


    /**
     */
    private void getManagementDataUser() {

        kProgressHUD.show();
        final Modify modify = new Modify();
        modify.setName(modify_edit.getText().toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        String urlEnd = "smart/v1/House/User/" + id + "/" + uid;

        Call<ResultBean> call = httpInterface.getModifyUser(urlEnd + "?accessToken=" + PreferenceUtil.getString("accessToken"), modify);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                System.out.println("-------bbbbbbbbbb--aaaaaaaaa-----" + response.body().toString());
                kProgressHUD.dismiss();
                ResultBean resultBean = response.body();
                if (resultBean.getCode() == 200) {
                    if (Constant.userProfileBean == null) {
                        Constant.userProfileBean = gson.fromJson( PreferenceUtil.getString("user_profile"),UserProfileBean.class);
                    }

                    outer:
                    for (int i = 0; i <  Constant.userProfileBean.getHouses().size(); i++) {

                        if ( Constant.userProfileBean.getHouses().get(i).getId() == Integer.valueOf(id)) {
                            inner:
                            for (int j = 0; j <  Constant.userProfileBean.getHouses().get(i).getUserHouses().size(); j++) {
                                if( Constant.userProfileBean.getHouses().get(i).getUserHouses().get(j).getUser_id() == Integer.valueOf(uid)){
                                    Constant.userProfileBean.getHouses().get(i).getUserHouses().get(j).setUsername(modify.getName());
                                    AppUtils.show(ModifyActivity.this, "修改成功");
                                    break outer;
                                }

                            }
                        }
                    }


                    PreferenceUtil.putString("user_profile", gson.toJson(Constant.userProfileBean));
                    EventBus.getDefault().post(new EventBus_Account(Constant.EVENT_BUS_MODIFY_PERSONAL_REMARKS));
                    getFinish();
            }else{
                AppUtils.show(ModifyActivity.this, "修改失败");
            }


        }

        @Override
        public void onFailure (Call < ResultBean > call, Throwable t){
            kProgressHUD.dismiss();
            AppUtils.show(ModifyActivity.this, "修改失败");
            System.out.println("---aaaaa--failure--bbbbb---" + t.getMessage());
        }
    }

    );

}


    private void getFinish() {
        this.finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.head_submit:

                String name = modify_edit.getText().toString().trim();
                if (name != null && !name.equals("")) {
                    switch (tag) {
                        case 0:
                            getManagementDataUser();
                            break;
                        case 1:
                            getManagementDataHouse(1);
                            break;
                        case 2:
                            getManagementDataHouse(2);
                            break;
                        case 3://修改自己的用户名
                            Intent intent = new Intent();
                            intent.putExtra("NAME", name);
                            setResult(101, intent);
                            this.finish();
                            break;
                    }
                } else {
                    AppUtils.show(ModifyActivity.this, "输入不能为空");
                }

                break;
            case R.id.head_back:
                this.finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(ModifyActivity.this);
    }
}
