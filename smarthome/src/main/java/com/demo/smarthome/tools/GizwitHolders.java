package com.demo.smarthome.tools;

import android.util.Log;

import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.communication.jsonbean.sub.Devices;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.communication.jsonbean.sub.Rooms;
import com.demo.smarthome.ui.adapter.AdjustLampItemAdapter;
import com.demo.smarthome.ui.adapter.CurtainItemAdapter;
import com.demo.smarthome.ui.adapter.WindowItemAdapter;
import com.demo.smarthome.ui.model.RoomInfo;
import com.gizwits.gizwifisdk.api.GizWifiDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import aprivate.oo.gizwitopenapi.response.BindingList;

/**
 * Created by zhuxiaolong on 2017/8/15.
 * 这个代码真的是没有办法 看的我想死了。。。
 * <p>
 * 这个类是为了  登录接口返回的巨大返回值而进行的优化
 * <p>
 * 第一阶段优化
 * <p>
 * <p>
 * 1、获取到的登录返回值 包含多有 绑定的房屋信息  房屋较多时 内存占用比较大
 * 保存到 sp 中 需要时读取
 * 2、获取的机智云绑定设备 也是比较多
 * <p>
 * 第二阶段优化
 * 1、对返回值进行数据分割 并采用合理的数据结构
 * 分为 house 列表  以 productkey 为 key  house name 为 value
 * 2、机智云数据的分割
 * 以 productkey 为 key   did 为 value
 */

public class GizwitHolders {

    /*策略设备节点的 ID */
    public static final int EVENT_ID_ONEKEY_TEMPERATURE = 1550;
    public static final int EVENT_ID_ONEKEY_HUMIDITY = 1560;
    public static final int EVENT_ID_ONEKEY_WINDOW_CURTAIN = 1490;
    public static final int EVENT_ID_ONEKEY_CLEANER = 1500;
    public static final int EVENT_ID_ONEKEY_OUTHOME = 1390;
    public static final int EVENT_ID_ONEKEY_SECURITY = 1400;
    public static final int EVENT_ID_ONEKEY_GOHOME = 1380;
    public static final int EVENT_ID_ONEKEY_LIGHT = 1480;

    /*温度控制相关*/
    public static final int DEVICE_TEM_SWITCH = 1530;//温度开关
    public static final int DEVICE_TEM_MODE = 1440;//温度模式  制冷 制热
    public static final int DEVICE_TEM_SET_VALUE = 1120;//设定值
    public static final int DEVICE_TEM_SENSOR = 1110;//当前检测值

    /*湿度控制相关*/
    public static final int DEVICE_HUM_SWITCH = 1540;//湿度开关
    public static final int DEVICE_HUM_SET_VALUE = 1140;//设定值
    public static final int DEVICE_HUM_SENSOR = 1130;//当前检测值

    private ExecutorService workThread = Executors.newSingleThreadExecutor();
    private static GizwitHolders instance;
    private ConfigFile configFile;

    public GizwitHolders() {
        configFile = ConfigFile.getInstance();
    }

    public static GizwitHolders getInstance() {
        if (instance == null) {
            instance = new GizwitHolders();
        }
        return instance;
    }

    private List<GizWifiDevice> deivceList;
    private ArrayList<Houses> userHouseList;
    private UserProfileBean userProfileBean;
    private String productKey;
    private String did;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    private BindingList bindingList;

    public BindingList getBindingList() {
        return bindingList;
    }

    public void setBindingList(BindingList bindingList) {
        this.bindingList = bindingList;
    }

    public String getDid() {
        for (BindingList.DevicesBean bean : bindingList.getDevices()) {
            if (bean.getProduct_key().equals(productKey)) {
                return bean.getDid();
            }
        }
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    /* 一键策略*/
    private RoomInfo.DeviceInfo akeyAirDevice;
    private RoomInfo.DeviceInfo akeyWindowDevice;
    private RoomInfo.DeviceInfo akeyTemDevice;
    private RoomInfo.DeviceInfo akeyHumDevice;
    private RoomInfo.DeviceInfo akeySecDevice;
    private RoomInfo.DeviceInfo akeyHomeDevice;
    private RoomInfo.DeviceInfo akeyLeaveDevice;
    private RoomInfo.DeviceInfo akeyLightDevice;

    public RoomInfo.DeviceInfo getAkeyLightDevice() {
        return akeyLightDevice;
    }

    public void setAkeyLightDevice(RoomInfo.DeviceInfo akeyLightDevice) {
        this.akeyLightDevice = akeyLightDevice;
    }

    public RoomInfo.DeviceInfo getAkeyWindowDevice() {
        return akeyWindowDevice;
    }

    public void setAkeyWindowDevice(RoomInfo.DeviceInfo akeyWindowDevice) {
        this.akeyWindowDevice = akeyWindowDevice;
    }

    public RoomInfo.DeviceInfo getAkeySecDevice() {
        return akeySecDevice;
    }

    public void setAkeySecDevice(RoomInfo.DeviceInfo akeySecDevice) {
        this.akeySecDevice = akeySecDevice;
    }

    public RoomInfo.DeviceInfo getAkeyAirDevice() {
        return akeyAirDevice;
    }

    public void setAkeyAirDevice(RoomInfo.DeviceInfo akeyAirDevice) {
        this.akeyAirDevice = akeyAirDevice;
    }

    public RoomInfo.DeviceInfo getAkeyTemDevice() {
        return akeyTemDevice;
    }

    public void setAkeyTemDevice(RoomInfo.DeviceInfo akeyTemDevice) {
        this.akeyTemDevice = akeyTemDevice;
    }

    public RoomInfo.DeviceInfo getAkeyHumDevice() {
        return akeyHumDevice;
    }

    public void setAkeyHumDevice(RoomInfo.DeviceInfo akeyHumDevice) {
        this.akeyHumDevice = akeyHumDevice;
    }

    public RoomInfo.DeviceInfo getAkeyHomeDevice() {
        return akeyHomeDevice;
    }

    public void setAkeyHomeDevice(RoomInfo.DeviceInfo akeyHomeDevice) {
        this.akeyHomeDevice = akeyHomeDevice;
    }

    public RoomInfo.DeviceInfo getAkeyLeaveDevice() {
        return akeyLeaveDevice;
    }

    public void setAkeyLeaveDevice(RoomInfo.DeviceInfo akeyLeaveDevice) {
        this.akeyLeaveDevice = akeyLeaveDevice;
    }

    public UserProfileBean getUserProfileBean() {
        return userProfileBean;
    }

    public void setUserProfileBean(UserProfileBean userProfileBean) {
        this.userProfileBean = userProfileBean;
    }

    public List<GizWifiDevice> getDeivceList() {
        return deivceList;
    }

    public void setDeivceList(List<GizWifiDevice> deivceList) {
        this.deivceList = deivceList;
    }

    public ArrayList<Houses> getUserHouseList() {
        return userHouseList;
    }

    public void setUserHouseList(ArrayList<Houses> userHouseList) {
        this.userHouseList = userHouseList;
    }

    private final String TAG = "DATAHODER";


    public List<RoomInfo> roomInfoList;

    /***
     * 这里进行了 根据服务器返回的数据信息 生成 房屋结构的 roomInfo
     * 这里的 roominfo 是单个房屋 一个智能网关
     *
     * */
    public void setListInfo(List<RoomInfo> mRoomInfo, List<RoomInfo.DeviceInfo> mAlertDevice,
                            Map<String, Long> mAlertRecord, final UserProfileBean userProfileBean,
                            final RoomInfoInitCallback callback) {
        mRoomInfo = new ArrayList<>();
        mAlertDevice = new ArrayList<>();
        mAlertRecord = new HashMap<>();
        final List<RoomInfo> finalMRoomInfo = mRoomInfo;
        final List<RoomInfo.DeviceInfo> finalMAlertDevice = mAlertDevice;
        new Thread(new Runnable() {
            @Override
            public void run() {

                //TODO:获取房间信息
                for (int i = 0; i < userProfileBean.getHouses().size(); i++) {
                    if (userProfileBean.getHouses().get(i).getId() == PreferenceUtil.getInt("default_house")) {
                        setProductKey(userProfileBean.getHouses().get(i).getGateways().get(0).getExtid());
                        for (int j = 0; j < userProfileBean.getHouses().get(i).getRooms().size(); j++) {
                            RoomInfo temp = new RoomInfo();
                            Rooms room = userProfileBean.getHouses().get(i).getRooms().get(j);
                            temp.roomName = room.getName();
                            Log.i(TAG, "initRoomInfo:  get categories  " + room.getName());
                            for (int k = 0; k < room.getCategories().size(); k++) {
                                //temperature
                                Log.i(TAG, "initRoomInfo: " + room.getCategories().get(k).getName());
                                if (room.getCategories().get(k).getName().equals("temperature")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  temperature");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.temperature_o_device.add(device);
                                    }
                                    Log.i(TAG, "initRoomInfo: get temperature : " + temp.temperature_o_device.size());
                                }
                                //humidity
                                if (room.getCategories().get(k).getName().equals("humidity")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  humidity");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.humidity_o_device.add(device);
                                    }
                                    Log.i(TAG, "initRoomInfo: humidity " + temp.humidity_o_device.size());
                                }
                                //pm25
                                if (room.getCategories().get(k).getName().equals("pm25")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  pm25");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.pm25_o_device.add(device);
                                    }
                                    Log.i(TAG, "initRoomInfo:  pm25 " + temp.pm25_o_device.size());
                                }

                                //co2
                                if (room.getCategories().get(k).getName().equals("co2")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  co2");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.co2_o_device.add(device);
                                    }
                                }
                                //hcho
                                if (room.getCategories().get(k).getName().equals("HCHO")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  hcho");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.hcho_o_device.add(device);
                                    }
                                }
                                //voc
                                if (room.getCategories().get(k).getName().equals("voc")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  voc");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.voc_o_device.add(device);
                                    }
                                }
                                //airdevice
                                if (room.getCategories().get(k).getId() == 1470) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1470");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        Log.i(TAG, "initRoomInfo: name-->" + device.name);
                                        temp.airdevice.add(device);
                                    }
                                    Log.i(TAG, "initRoomInfo: categoriese  airdevice " + temp.airdevice.size());
                                }

                                //---------------------------------------------------------------------------------------------------------
                                //pressure
                                if (room.getCategories().get(k).getName().equals("pressure")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  pressure");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.press_o_device.add(device);
                                    }
                                }
                                //temperature_input
                                if (room.getCategories().get(k).getName().equals("temperature_input")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  temperatureinput");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.temperature_i_device.add(device);
                                    }
                                }
                                //humidity_input
                                if (room.getCategories().get(k).getName().equals("humidity_input")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  humidityinput");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.humidity_i_device.add(device);
                                    }
                                }
                                //pm25_input
                                if (room.getCategories().get(k).getName().equals("pm25_input")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  pm25 input");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.pm25_i_device.add(device);
                                    }
                                }
                                //co2_input
                                if (room.getCategories().get(k).getName().equals("co2_input")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  co2 input");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.co2_i_device.add(device);
                                    }
                                }
                                //voc_input
                                if (room.getCategories().get(k).getName().equals("voc_input")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  voc input ");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.voc_i_device.add(device);
                                    }
                                }
                                //HCHO_input
                                if (room.getCategories().get(k).getName().equals("HCHO_input")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  hcho input");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = 0;
                                        temp.hcho_i_device.add(device);
                                    }
                                }
                                //window
                                if (room.getCategories().get(k).getName().equals("window")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  window");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.descrip = devices.get(m).getDescription();
                                        device.value = WindowItemAdapter.WINDOW_PAUSE;
                                        temp.window_device.add(device);
                                    }
                                }
                                //lighting
                                if (room.getCategories().get(k).getName().equals("lamp")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  lamp");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = false;
                                        temp.lighting_device.add(device);
                                    }
                                }
                                //curtain
                                if (room.getCategories().get(k).getName().equals("curtain")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  curtain");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = CurtainItemAdapter.CURTAIN_PAUSE;
                                        device.descrip = devices.get(m).getDescription();
                                        temp.curtain_device.add(device);
                                    }
                                }
                                //adjust_lamp
                                if (room.getCategories().get(k).getName().equals("adjust_lamp")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  adjust lamp");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.value = AdjustLampItemAdapter.ADJUST_LAMP_OFF;
                                        temp.adjust_lighting_device.add(device);
                                    }
                                }
                                //security
                                if (room.getCategories().get(k).getId() == 1280) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1280");
//                        if (room.getCategories().get(k).getName().equals("camera")) {
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.feature = devices.get(m).getFeature();
                                        temp.camera_device.add(device);
                                    }
                                }
                                if (room.getCategories().get(k).getName().equals("alert")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  alert");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.alert_device.add(device);
                                        finalMAlertDevice.add(device);
                                    }
                                }
                                //water
                                if (room.getCategories().get(k).getName().equals("water_control")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  water control");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.water_control_device.add(device);
                                    }
                                }
                                if (room.getCategories().get(k).getName().equals("water_purifier")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  water purifier");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.water_purifier_control_device.add(device);
                                    }
                                }
                                if (room.getCategories().get(k).getName().equals("gas_control")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  gas control");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.gas_control_device.add(device);
                                    }
                                }
                                if (room.getCategories().get(k).getName().equals("energy_control")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  energy control");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = 0;
                                        temp.energy_control_device.add(device);
                                    }
                                }
                                if (room.getCategories().get(k).getName().equals("heater_control")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  heater control");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.heater_control_device.add(device);
                                    }
                                }
                                //fan
                                if (room.getCategories().get(k).getName().equals("fan")) {
                                    Log.i(TAG, "initRoomInfo:  get categories  fan" + room.getCategories().get(k).getName());
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.fan_device.add(device);
                                        Log.i(TAG, "init Room 风扇 " + device.tag);
                                    }
                                }

                                if (room.getCategories().get(k).getId() == 1450) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1450");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.electronicPurifier_device.add(device);
                                    }
                                }


                                if (room.getCategories().get(k).getId() == 1370) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1370");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.conditioner_device.add(device);
                                    }
                                    Log.i(TAG, "initRoomInfo: categoriese  1370 " + temp.conditioner_device.size());
                                }

                                if (room.getCategories().get(k).getId() == 1440) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1440");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.conditioner_pattern_device.add(device);
                                    }
                                    Log.i(TAG, "initRoomInfo: categoriese  1440 " + temp.conditioner_pattern_device.size());
                                }

                        /*温度开关设备*/
                                if (room.getCategories().get(k).getId() == DEVICE_TEM_SWITCH) {
                                    Log.i(TAG, "温度开关 categories id : " + DEVICE_TEM_SWITCH);
                                    Log.i(TAG, "温度开关 categories size : " + room.getCategories().size());
                                    Log.i(TAG, "温度开关 categories name : " + room.getCategories().get(k).getName());
                                    Log.i(TAG, "温度开关 categories device size : " + room.getCategories().get(k).getDevices().size());
                                    Log.i(TAG, "温度开关 categories id : " + DEVICE_TEM_SWITCH);
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.temperature_switch.add(device);
                                        Log.i(TAG, "温度开关 设备添加");
                                        Log.i(TAG, "温度开关设备 ： " + temp.temperature_switch.size());
                                    }

                                }
                        /*湿度开关设备*/
                                if (room.getCategories().get(k).getId() == DEVICE_HUM_SWITCH) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1540");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.humidity_switch.add(device);
                                        Log.i(TAG, "空气净化开关 设备添加");
                                    }
                                    Log.i(TAG, "initRoomInfo: categoriese  140 " + temp.humidity_switch.size());
                                }


                                //房屋的空气净化
                                if (room.getCategories().get(k).getId() == 1520) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1520");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        device.value = false;
                                        temp.roomAirdevice.add(device);
                                    }
                                    Log.i(TAG, "initRoomInfo: categoriese  1520 " + temp.roomAirdevice.size());
                                }
                                if (room.getCategories().get(k).getId() == 1380) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1380");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        PreferenceUtil.putString(Constant.SP_GOHOME, devices.get(m).getExtid());
                                    }
                                }
                                if (room.getCategories().get(k).getId() == 1390) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1390");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        PreferenceUtil.putString(Constant.SP_OUTHOME, devices.get(m).getExtid());
                                    }
                                }
                                if (room.getCategories().get(k).getId() == 1400) {
                                    Log.i(TAG, "initRoomInfo:  get categories  1400");
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        PreferenceUtil.putString(Constant.SP_MONITOR, devices.get(m).getExtid());
                                    }
                                }

                                if (room.getCategories().get(k).getId() == 1300
                                        || room.getCategories().get(k).getId() == 1310
                                        || room.getCategories().get(k).getId() == 1320
                                        || room.getCategories().get(k).getId() == 1330
                                        || room.getCategories().get(k).getId() == 1340) {
                                    List<Devices> devices = room.getCategories().get(k).getDevices();
                                    for (int m = 0; m < devices.size(); m++) {
                                        RoomInfo.DeviceInfo device = new RoomInfo.DeviceInfo();
                                        device.category = devices.get(m).getCategory();
                                        device.name = devices.get(m).getName();
                                        device.tag = devices.get(m).getExtid();
                                        device.room = room.getName();
                                        if (room.getCategories().get(k).getId() == 1330) {
                                            device.value = 0.0;
                                        } else {
                                            device.value = false;
                                        }
                                        temp.water_device.add(device);
                                    }
                                }
                        /*一键布防 */
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_SECURITY) {
                                    Log.i(TAG, "setlist info  ");
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeySecDevice(deviceInfo);
                                    }
                                }
                        /*  GizwitHolders 保存 一键空气净化设备*/
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_CLEANER) {
                                    Log.i(TAG, "setListInfo:  get categories save akey cleaner");
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeyAirDevice(deviceInfo);
                                    }
                                }

                        /*保存 一键温度控制设备*/
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_TEMPERATURE) {
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeyTemDevice(deviceInfo);
                                    }
                                }
                        /*保存 一键湿度控制设备*/
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_HUMIDITY) {
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeyHumDevice(deviceInfo);
                                    }
                                }
                        /*保存 一键窗帘控制设备*/
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_WINDOW_CURTAIN) {
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeyWindowDevice(deviceInfo);
                                    }
                                }

                           /*保存 一键灯光控制设备*/
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_LIGHT) {
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeyLightDevice(deviceInfo);
                                    }
                                }
                        /*保存一键 离家*/
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_OUTHOME) {
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeyLeaveDevice(deviceInfo);//保存一键 离家节点
                                    }
                                }
                                /*保存一键 回家*/
                                if (room.getCategories().get(k).getId() == GizwitHolders.EVENT_ID_ONEKEY_GOHOME) {
                                    for (Devices devices : room.getCategories().get(k).getDevices()) {
                                        RoomInfo.DeviceInfo deviceInfo = new RoomInfo.DeviceInfo();
                                        deviceInfo.category = devices.getCategory();
                                        deviceInfo.name = devices.getName();
                                        deviceInfo.tag = devices.getExtid();
                                        deviceInfo.room = room.getName();
                                        deviceInfo.value = 0;//设置初值
                                        GizwitHolders.getInstance().setAkeyHomeDevice(deviceInfo);//保存一键 回家节点
                                    }
                                }
                            }
                            finalMRoomInfo.add(temp);
                        }
                    }
                }
                roomInfoList = finalMRoomInfo;
                if (callback != null) {
                    callback.onInited(finalMRoomInfo);
                }
            }
        }).start();
    }


    private RoomInfoInitCallback roomInfoInitCallback;

    public void setRoomInfoInitCallback(RoomInfoInitCallback roomInfoInitCallback) {
        this.roomInfoInitCallback = roomInfoInitCallback;
    }

    public interface RoomInfoInitCallback {
        void onInited(List<RoomInfo> roomInfos);
    }

}
