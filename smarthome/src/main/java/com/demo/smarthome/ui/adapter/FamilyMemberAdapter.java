package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.qrcode.CreateQRActivity;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.R;

import java.util.List;

/**
 * Created by liukun on 2016/3/17.
 */
public class FamilyMemberAdapter extends BaseAdapter {
    private Context mContext;
    private List<Houses>  mHouseList;
    private LayoutInflater mInflater;
    private boolean mEditable=false;

    public FamilyMemberAdapter(Context context, List<Houses> house) {
        super();
        mContext = context;
        mHouseList = house;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mHouseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHouseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.family_member_list_adapter, null);
        }
        ImageView add_user_btn = (ImageView)convertView.findViewById(R.id.add_member_btn);
        add_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateQRActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                intent.putExtra("houseID",mHouseList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        //TODO:从读取房屋信息
        Houses houseData = mHouseList.get(position);
        //更新UI
        TextView houseName = (TextView)convertView.findViewById(R.id.house_name);
        houseName.setText(houseData.getName());
        TextView houseAddress = (TextView)convertView.findViewById(R.id.house_address);
        houseAddress.setText(houseData.getAddress());

        //TODO:从服务器读取对应的家庭成员
        NotScrollListView mMemberList = (NotScrollListView)convertView.findViewById(R.id.member_list);
        FamilyMemberInfoAdapter mFamilyMemberInfoAdapter = new FamilyMemberInfoAdapter(mContext,houseData.getUserHouses());
        mMemberList.setAdapter(mFamilyMemberInfoAdapter);
        return convertView;
    }

}
