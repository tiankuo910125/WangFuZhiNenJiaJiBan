package com.demo.smarthome.camera;

import android.content.Context;
import android.graphics.Bitmap;

import com.arcsoft.closeli.AsyncTask;
import com.arcsoft.closeli.Closeli;
import com.arcsoft.closeli.ErrorDef;
import com.arcsoft.closeli.Log;
import com.arcsoft.closeli.ServerConfig.ServerType;
import com.arcsoft.closeli.esd.CameraSettingParams;
import com.arcsoft.closeli.esd.CameraSettingParams.CameraSettingType;
import com.arcsoft.closeli.esd.CameraSettingResult;
import com.arcsoft.closeli.esd.GetCameraListResult;
import com.arcsoft.closeli.model.CameraInfo;
import com.arcsoft.closeli.model.LoginResult;
import com.arcsoft.closeli.upns.RegisterNotificationServiceResult;
import com.demo.smarthome.base.interf.ActionCallBack;
import com.demo.smarthome.base.task.BackgroundWork;
import com.demo.smarthome.base.task.Completion;
import com.demo.smarthome.base.task.Tasks;
import com.demo.smarthome.base.utils.Constant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CameraListManager {
    private final static String TAG = "CameraManager";

    private static CameraListManager mInstance;

    /**
     * The list of all cameras.
     */
    private ArrayList<CameraInfo> mCameraList;
    private ArrayList<ICameraListListener> mCameraListListenerList;
    private Map<String, Bitmap> cameraThumbnailMap;


    public static synchronized CameraListManager getInstance() {
        if (mInstance == null) {
            mInstance = new CameraListManager();
        }

        return mInstance;
    }

    public static synchronized void clear() {
        if (mInstance != null) {
            mInstance.clearData();
            mInstance = null;
        }
    }

    private CameraListManager() {
        Log.d(TAG, "new camera manager");
        mCameraList = new ArrayList<CameraInfo>();
        cameraThumbnailMap = new LinkedHashMap<String, Bitmap>();
        // initCamera();
    }

    private void initCamera(Context context) {
        Closeli.enableDebugLog(true);
        Closeli.init(context, Constant.Camera.ProductKey, Constant.Camera.ProkuctSecret, ServerType.CMCC);
    }

    public void loginHmCamera(Context context, final ActionCallBack callBack) {
        initCamera(context);
        Tasks.executeInBackground(context, new BackgroundWork<LoginResult>() {
            @Override
            public LoginResult doInBackground() throws Exception {
                return Closeli.login(Constant.Camera.UserName, Constant.Camera.Password);
            }
        }, new Completion<LoginResult>() {
            @Override
            public void onSuccess(Context context, LoginResult result) {
                registerNotificationService(context);
                if (callBack != null) {
                    callBack.callBack(result);
                }
            }

            @Override
            public void onError(Context context, Exception e) {
            }
        });
    }

    public void logoutCamera() {
        Closeli.logout();
    }

    private void registerNotificationService(final Context context) {
        new AsyncTask<Void, Void, RegisterNotificationServiceResult>() {
            @Override
            protected RegisterNotificationServiceResult doInBackground(Void... params) {
                return Closeli.registerNotificationService(context, "34038106032");
            }

            @Override
            protected void onPostExecute(RegisterNotificationServiceResult result) {
                if (result.getCode() != 0) {
                }
            }
        }.execute();
    }

    public synchronized void saveThumbnailBySrcId(String srcId, Bitmap bmp) {
        cameraThumbnailMap.put(srcId, bmp);
    }

    public synchronized Bitmap getThumbnailBySrcId(String srcId) {
        return cameraThumbnailMap.get(srcId);
    }

    private synchronized void clearData() {
        Log.d(TAG, "clear data");
        mCameraList.clear();
        if (mCameraListListenerList != null) {
            mCameraListListenerList.clear();
        }
    }

    public synchronized ArrayList<CameraInfo> getCameraList() {
        return mCameraList;
    }

    /**
     * Get cameras info from server.
     * 
     * @return The list of cameras which is newly added to
     *         {@link CameraListManager#mCameraList}
     */
    public int getCameraListFromServer() {
        Log.d(TAG, "load from server");
        final GetCameraListResult result = Closeli.getCameraList();

        synchronized (CameraListManager.this) {
            if (result.getCode() == ErrorDef.SUCCESS && result.getCameraList() != null) {
                final ArrayList<CameraInfo> newCameraList = new ArrayList<CameraInfo>();
                for (CameraInfo cameraInfo : result.getCameraList()) {
                    final CameraInfo existCameraInfo = getCameraInfo(cameraInfo.getSrcId());
                    if (existCameraInfo != null) {
                        existCameraInfo.parse(cameraInfo);
                    } else {
                        mCameraList.add(cameraInfo);
                        newCameraList.add(cameraInfo);
                    }
                }

                final ArrayList<CameraInfo> overdueCameraList = new ArrayList<CameraInfo>();
                for (CameraInfo cameraInfo : mCameraList) {
                    boolean exist = false;
                    for (CameraInfo cameraInfo2 : result.getCameraList()) {
                        if (cameraInfo.getSrcId().equalsIgnoreCase(cameraInfo2.getSrcId())) {
                            exist = true;
                            break;
                        }

                    }

                    if (!exist) {
                        overdueCameraList.add(cameraInfo);
                    }
                }

                mCameraList.removeAll(overdueCameraList);
                notifyCameraListChanged();

                Log.d(TAG, String.format("Camera list updated, add=[%s], remove=[%s], total=[%s]", newCameraList.size(), overdueCameraList.size(), mCameraList.size()));
            }
        }

        return result.getCode();
    }

    public synchronized CameraInfo getCameraInfo(final String srcId) {
        if (mCameraList == null) {
            Log.w(TAG, "getCameraInfo failed, list is null");
            return null;
        }

        for (CameraInfo cameraInfo : mCameraList) {
            if (cameraInfo.getSrcId().equalsIgnoreCase(srcId)) {
                return cameraInfo;
            }
        }
        return null;
    }

    public String getMacAddress(final CameraInfo cameraInfo) {
        try {
            final String srcId = cameraInfo.getSrcId();
            String macAddr = srcId.substring(srcId.length() - 12, srcId.length()).toUpperCase();
            char[] charArray = macAddr.toCharArray();
            StringBuffer sb = new StringBuffer();
            sb.append(charArray[0]).append(charArray[1]).append(":").append(charArray[2]).append(charArray[3]).append(":").append(charArray[4]).append(charArray[5]).append(":").append(charArray[6])
                    .append(charArray[7]).append(":").append(charArray[8]).append(charArray[9]).append(":").append(charArray[10]).append(charArray[11]);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateSettingToServer(final Context mContext, final CameraSettingType settingType, final CameraSettingParams settingParams, Completion<CameraSettingResult> completion) {
        Tasks.executeInBackground(mContext, new BackgroundWork<CameraSettingResult>() {
            @Override
            public CameraSettingResult doInBackground() throws Exception {
                return Closeli.changeCameraSettings(mContext, settingType, settingParams);
            }
        }, completion);
    }

    public void loadCameraSettings(final Context mContext, final CameraInfo mCameraInfo, Completion<CameraSettingParams> completion) {
        Tasks.executeInBackground(mContext, new BackgroundWork<CameraSettingParams>() {
            @Override
            public CameraSettingParams doInBackground() throws Exception {
                return Closeli.getCameraSettings(mContext, mCameraInfo);
            }
        }, completion);
    }

    public synchronized boolean exist(final String srcId) {
        return getCameraInfo(srcId) != null;
    }

    public synchronized void addCameraInfo(final CameraInfo cameraInfo) {
        final CameraInfo existCameraInfo = getCameraInfo(cameraInfo.getSrcId());
        if (existCameraInfo != null) {
            existCameraInfo.parse(cameraInfo);
            notifyCameraListChanged();
        } else {
            if (mCameraList != null) {
                mCameraList.add(cameraInfo);
                notifyCameraListChanged();
            }
        }
    }

    public synchronized void registerCameraListListener(final ICameraListListener listener) {
        if (mCameraListListenerList == null) {
            mCameraListListenerList = new ArrayList<ICameraListListener>();
        }

        if (!mCameraListListenerList.contains(listener)) {
            mCameraListListenerList.add(listener);
        }
    }

    public synchronized void unregisterCameraListListener(final ICameraListListener listener) {
        if (mCameraListListenerList != null) {
            mCameraListListenerList.remove(listener);
        }
    }

    private synchronized void notifyCameraListChanged() {
        if (mCameraListListenerList != null) {
            for (ICameraListListener listener : mCameraListListenerList) {
                try {
                    if (listener != null) {
                        listener.onChanged(getCameraList());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static interface ICameraListListener {
        /**
         * Invoked when camera list size changed or the content of camera list
         * is changed.
         * 
         * @param latestList
         *            The latest camera list after modified.
         */
        void onChanged(ArrayList<CameraInfo> latestList);
    }
}