package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.demo.smarthome.R;
import com.demo.smarthome.communication.jsonbean.sub.UserHouses;

import java.util.List;

/**
 * Created by liukun on 2016/3/14.
 */
public class FamilyMemberInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserHouses> mFamilyMember;
    private LayoutInflater mInflater;

    public FamilyMemberInfoAdapter(Context context, List<UserHouses> member) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mFamilyMember = member;
    }

    @Override
    public int getCount() {
        return mFamilyMember.size();
    }

    @Override
    public Object getItem(int position) {
        return mFamilyMember.get(position);
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
            convertView = mInflater.inflate(R.layout.family_member_list_item_adapter, null);
        }

        final EditText username = ((EditText) convertView.findViewById(R.id.user_name));
        username.setText(mFamilyMember.get(position).getUsername());
        username.setFocusable(false);
        username.setEnabled(false);

        ImageView mDeleteBtn = (ImageView)convertView.findViewById(R.id.delete_btn);
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFamilyMember.remove(position);
                notifyDataSetChanged();
                //TODO:保存到数据库或平台
            }
        });

        if (mFamilyMember.get(position).getRole().equals("owner")){
            mDeleteBtn.setClickable(false);
            mDeleteBtn.setBackground(mContext.getResources().getDrawable(R.drawable.ic_owner_small));
        }
        return convertView;
    }
}
