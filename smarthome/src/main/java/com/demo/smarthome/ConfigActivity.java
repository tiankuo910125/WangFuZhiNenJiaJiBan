package com.demo.smarthome;

import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.ui.fragment.IndoorAndOutdoorConfigFragment;
import com.demo.smarthome.ui.fragment.OnFragmentInteractionListener;
import com.demo.smarthome.ui.fragment.TimeBasedConfigFragment;
import com.demo.smarthome.ui.model.ConfigInfo;
import com.google.gson.Gson;
import com.demo.smarthome.ui.adapter.RoomNameHorizontalTabAdapter;

import java.util.ArrayList;

public class ConfigActivity extends BaseSmartHomeActivity implements OnFragmentInteractionListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private String TAG = "ConfigActivity";
    private Context mContext;
    private ConfigInfo mConfigInfo;
    private FragmentManager fragmentManager;
    private Toolbar mToolbar;
    private IndoorAndOutdoorConfigFragment mInsideOutdoorConfig;
    private TimeBasedConfigFragment mTemperatureConfig;
    private TimeBasedConfigFragment mHumidityConfig;
    private TimeBasedConfigFragment mAirqualityConfig;

    private GridView mConfigSelectGrid;
    private RoomNameHorizontalTabAdapter mConfigHorizontalTabAdapter;
    private ArrayList<String> configItem;

    public static final int INSIDE_OUTSIDE_CONFIG=0;
    public static final int TEMPERATURE_CONFIG=1;
    public static final int HUMIDITY_CONFIG=2;
    public static final int AIR_QUALITY_CONFIG=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        try {
            mConfigInfo = gson.fromJson(PreferenceUtil.getString("ConfigInfo", ""), ConfigInfo.class);
        }catch (NullPointerException e){
            e.printStackTrace();
            mConfigInfo = new ConfigInfo();
        }
        if (mConfigInfo == null) mConfigInfo = new ConfigInfo();
        mContext = this;
        setContentView(R.layout.activity_config);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mam.pushOneActivity(ConfigActivity.this);

        configItem = new ArrayList<>();
        configItem.add("回家离家");
        configItem.add("温度分时");
        configItem.add("湿度分时");
        configItem.add("净化分时");

        mConfigSelectGrid = (GridView)findViewById(R.id.config_select_grid);
        mConfigHorizontalTabAdapter = new RoomNameHorizontalTabAdapter(mContext,configItem, null);
        mConfigSelectGrid.setAdapter(mConfigHorizontalTabAdapter);
        int size = configItem.size();
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        float density = dm.density;
        int allWidth = (int) (150 * size * density);
        int itemWidth = (int) (100 * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.FILL_PARENT);
        mConfigSelectGrid.setLayoutParams(params);
        mConfigSelectGrid.setColumnWidth(itemWidth);
        mConfigSelectGrid.setHorizontalSpacing(20);
        mConfigSelectGrid.setStretchMode(GridView.NO_STRETCH);
        mConfigSelectGrid.setNumColumns(size);
        mConfigSelectGrid.setOnItemClickListener(mGridItemClickListener);
        mConfigHorizontalTabAdapter.setCheckedItem(0);

        fragmentManager = getFragmentManager();
        mInsideOutdoorConfig = new IndoorAndOutdoorConfigFragment(mConfigInfo);
        fragmentManager.beginTransaction().add(R.id.content,mInsideOutdoorConfig).commit();
    }

    private AdapterView.OnItemClickListener mGridItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mConfigHorizontalTabAdapter.setCheckedItem(position);
            switch (position){
                case INSIDE_OUTSIDE_CONFIG:{
                    mInsideOutdoorConfig = new IndoorAndOutdoorConfigFragment(mConfigInfo);
                    fragmentManager.beginTransaction().replace(R.id.content, mInsideOutdoorConfig).commit();
                    break;
                }
                case TEMPERATURE_CONFIG:{
                    mTemperatureConfig = new TimeBasedConfigFragment(mConfigInfo,TEMPERATURE_CONFIG);
                    fragmentManager.beginTransaction().replace(R.id.content, mTemperatureConfig).commit();
                    break;
                }
                case HUMIDITY_CONFIG:{
                    mHumidityConfig = new TimeBasedConfigFragment(mConfigInfo,HUMIDITY_CONFIG);
                    fragmentManager.beginTransaction().replace(R.id.content, mHumidityConfig).commit();
                    break;
                }
                case AIR_QUALITY_CONFIG:{
                    mAirqualityConfig = new TimeBasedConfigFragment(mConfigInfo,AIR_QUALITY_CONFIG);
                    fragmentManager.beginTransaction().replace(R.id.content, mAirqualityConfig).commit();
                    break;
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(ConfigActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}