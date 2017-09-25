package com.demo.smarthome.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.communication.jsonbean.sub.Rooms;
import com.demo.smarthome.ui.adapter.DeviceListAdapter;
import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.R;
import com.demo.smarthome.communication.jsonbean.sub.Devices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liukun on 2016/3/17.
 */
public class OneRoomDeviceView extends RoomControlBaseView {
    private Context mContext;
    private LayoutInflater mInflater;
    private NotScrollListView mDeviceList;
    private DeviceListAdapter mDeviceListAdapter;

    private String mRoomname;
    private ArrayList<Devices> mDevices;
    private ArrayList<String> mDeviceState;

    private TextView mRoomNameTextView;


    public OneRoomDeviceView(Context context, Rooms roomInfo) {
        super(context);
        init(context, roomInfo);
    }

    private void init(final Context context, Rooms roomInfo){
        mContext = context;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.fragment_oneroom_device, this);

        mRoomname = roomInfo.getName();
        mDevices = new ArrayList<>();
        for (int i=0;i<roomInfo.getCategories().size();i++){
            for (int j=0;j<roomInfo.getCategories().get(i).getDevices().size();j++){
                mDevices.add(roomInfo.getCategories().get(i).getDevices().get(j));
            }
        }

        mDeviceState = new ArrayList<>();

        for (int i=0;i<mDevices.size();i++)
        {
            mDeviceState.add("OFF");
        }
        mRoomNameTextView = (TextView)findViewById(R.id.name);
        mRoomNameTextView.setText(mRoomname);

        mDeviceList = (NotScrollListView)findViewById(R.id.device_list);
        mDeviceListAdapter = new DeviceListAdapter(mContext,mRoomname,mDevices,mDeviceState);
        mDeviceList.setAdapter(mDeviceListAdapter);
    }

    public void setUpdateData(final ConcurrentHashMap<String, Object> dataMap) {
        Tasks.executeInBackground(mContext, new BackgroundWork<List<String>>() {
                    @Override
                    public List<String> doInBackground() throws Exception {
                        //TODO:提取数据中的数据型数据点的信息
                        ConcurrentHashMap<String, Object> statuMap = new ConcurrentHashMap<String, Object>();// 设备状态数据
                        String jsonString = dataMap.get("data").toString();
                        JSONObject receive = new JSONObject(jsonString);
                        Iterator actions = receive.keys();
                        while (actions.hasNext()) {
                            String param = actions.next().toString();
                            //   Log.d("revjson", "action=" + action);
                            // 忽略特殊部分
                            if (param.equals("cmd") || param.equals("qos") || param.equals("seq") || param.equals("version")) {
                                continue;
                            }
                            Object value = receive.get(param);
                            statuMap.put(param, value);
                        }
                        //TODO:获取数值
                        for (int i = 0; i < mDevices.size(); i++) {
                            Devices device = mDevices.get(i);
                            if (statuMap.get(device.getExtid()) != null) {
                                mDeviceState.set(i, statuMap.get(device.getExtid()).toString());
                            }
                        }
                        return mDeviceState;
                    }
                }
                , new Completion<List<String>>() {
                    @Override
                    public void onSuccess(Context context, List<String> result) {
                        mDeviceListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Context context, Exception e) {

                    }
                }

        );
    }
}
