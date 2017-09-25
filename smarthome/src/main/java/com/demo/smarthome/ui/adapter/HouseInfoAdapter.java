package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.communication.jsonbean.sub.Rooms;
import com.demo.smarthome.ui.ModifyActivity;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;

/**
 * Created by liukun on 2016/3/14.
 */
public class HouseInfoAdapter extends BaseAdapter {

    private KProgressHUD kProgressHUD;
    private Gson gson = new Gson();
    private String houseName;

    private Context mContext;
    private List<Rooms> mHouseItem;
    private LayoutInflater mInflater;

    public HouseInfoAdapter(Context context, List<Rooms> houseData, String houseName) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mHouseItem = houseData;
        kProgressHUD = new KProgressHUD(mContext);
        this.houseName = houseName;
    }

    @Override
    public int getCount() {
        return mHouseItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mHouseItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.room_remark_list_adapter, null);
        }

        final TextView remarkedit = ((TextView) convertView.findViewById(R.id.room_name));

        String str = mHouseItem.get(position).getName();
        String[] sourceStrArray = str.split("--");
        if (sourceStrArray != null && sourceStrArray.length == 2) {
            remarkedit.setText(sourceStrArray[1]);
        } else {
            remarkedit.setText("室内");
        }

//        remarkedit.setText(mHouseItem.get(position).getName());
//        remarkedit.setFocusable(false);
//        remarkedit.setEnabled(false);
        final ImageView mEditBtn = (ImageView) convertView.findViewById(R.id.edit_btn);
//        final ImageView mConfirmBtn = (ImageView) convertView.findViewById(R.id.confirm_btn);

        convertView.findViewById(R.id.room_name_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ModifyActivity.class);
                intent.putExtra("HOUSENAME", houseName);
                intent.putExtra("TAG", 2);
                intent.putExtra("ID", mHouseItem.get(position).getId() + "");
                mContext.startActivity(intent);
            }
        });
//        mEditBtn.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
////                remarkedit.setEnabled(true);
////                remarkedit.setFocusable(true);
////                remarkedit.setFocusableInTouchMode(true);
////                remarkedit.requestFocus();
////                remarkedit.requestFocusFromTouch();
////                mEditBtn.setVisibility(View.GONE);
////                mConfirmBtn.setVisibility(View.VISIBLE);
//                Intent intent = new Intent();
//                intent.setClass(mContext, ModifyActivity.class);
//                intent.putExtra("HOUSENAME",houseName);
//                intent.putExtra("TAG",2);
//                intent.putExtra("ID",mHouseItem.get(position).getId()+"");
//                mContext.startActivity(intent);
//
//            }
//        });

//        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
//                remarkedit.setFocusable(false);
//                remarkedit.setEnabled(false);
//                mEditBtn.setVisibility(View.VISIBLE);
//                mConfirmBtn.setVisibility(View.GONE);
//                if (remarkedit.getText().toString().equals("")) {
//                    AppUtils.show(mContext, "输入不能为空");
//                } else {
//                    getManagementDataHouse(2, mHouseItem.get(position).getId(), remarkedit.getText().toString());
//                }
//
//
////                mHouseItem.get(position).setName(remarkedit.getText().toString());
////                //TODO:保存到服务器
////                Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
////                    @Override
////                    public String doInBackground() throws Exception {
////                        return HouseUtilsImpl.getInstance().updateRoomInfo(remarkedit.getText().toString(), mHouseItem.get(position).getId());
////                    }
////                }, new Completion<String>() {
////                    @Override
////                    public void onSuccess(Context context, String result) {
////                        if (result != null) {
////                            ResultBean r = GsonTools.getBean(result, ResultBean.class);
////                            //TODO: 验证  如果成功继续，如果失败重新登录
////                            if (r.getCode() == Constant.HttpURL.ErrorCode.OK) {
////                                Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
////                            } else {
////                                Toast.makeText(mContext, "修改失败：" + r.getMessage(), Toast.LENGTH_SHORT).show();
////                            }
////                        } else {
////                            Toast.makeText(mContext, "服务器无响应", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////
////                    @Override
////                    public void onError(Context context, Exception e) {
////
////                    }
////                });
//
//            }
//        });

        return convertView;
    }


    /**
     * tag  1--房子  2-－房间
     *
     * @param tag
     */
//    private void getManagementDataHouse(final int tag, final int id, final String name) {
//
//        kProgressHUD.show();
//        final Modify modify = new Modify();
//        modify.setName(name);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(HttpUtils.HOSTUTIL_HTTPS)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(RetrofitAPIManager.getOkHttpClient(mContext))
//                .build();
//
//        HttpInterface httpInterface = retrofit.create(HttpInterface.class);
//
//        String urlEnd = "";
//        switch (tag) {
//            case 1:
//                urlEnd = "smart/v1/House/";
//                break;
//            case 2:
//                urlEnd = "smart/v1/Room/";
//                break;
//        }
//
//        Call<ResultBean> call = httpInterface.getModifyHouse(urlEnd + id + "?accessToken=" + PreferenceUtil.getString("accessToken"), modify);
//        call.enqueue(new Callback<ResultBean>() {
//            @Override
//            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
//                System.out.println("-------bbbbbbbbbb--aaaaaaaaa-----" + response.body().toString());
//                kProgressHUD.dismiss();
//                ResultBean resultBean = response.body();
//                if (resultBean.getCode() == 200) {
//                    AppUtils.show(mContext, "修改成功");
//                    if (Constant.userProfileBean == null) {
//                        Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
//                    }
//
//                    switch (tag) {
//                        case 1:
//                            for (int i = 0; i < Constant.userProfileBean.getHouses().size(); i++) {
//
//                                if (Constant.userProfileBean.getHouses().get(i).getId() == id) {
//                                    Constant.userProfileBean.getHouses().get(i).setName(modify.getName());
//                                    break;
//                                }
//                            }
//                            break;
//                        case 2:
//                            outer:
//                            for (int i = 0; i < Constant.userProfileBean.getHouses().size(); i++) {
//                                inner:
//                                for (int j = 0; i < Constant.userProfileBean.getHouses().get(i).getRooms().size(); j++) {
//                                    if (Constant.userProfileBean.getHouses().get(i).getRooms().get(j).getId() == Integer.valueOf(id)) {
//                                        Constant.userProfileBean.getHouses().get(i).getRooms().get(j).setName(modify.getName());
//                                        break outer;
//                                    }
//                                }
//
//                            }
//                            break;
//
//                    }
//
//                    PreferenceUtil.putString("user_profile", gson.toJson(Constant.userProfileBean));
////                    switch (tag) {
////                        case 1:
////                            setResult(111, intent);
////                            break;
////                        case 2:
////                            setResult(112, intent);
////                            break;
////                    }
//                    EventBus.getDefault().post(new EventBus_Account(Constant.EVENT_BUS_MODIFY_PERSONAL_HOUSE));
//                } else {
//                    AppUtils.show(mContext, "修改失败");
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ResultBean> call, Throwable t) {
//                kProgressHUD.dismiss();
//                AppUtils.show(mContext, "修改失败");
//                System.out.println("---aaaaa--failure--bbbbb---" + t.getMessage());
//            }
//        });
//
//    }
}
