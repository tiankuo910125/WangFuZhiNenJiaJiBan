package com.demo.smarthome.communication.devicesmanager.gizwits;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by liukun on 2016/5/5.
 */
public class GizwitzCommunicationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
