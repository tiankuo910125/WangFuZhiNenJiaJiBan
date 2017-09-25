package com.demo.smarthome.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.closeli.AsyncTask;
import com.arcsoft.closeli.fullrelay.FullRelayProxy;
import com.arcsoft.closeli.model.CameraInfo;
import com.arcsoft.closeli.model.CameraMessageInfo;
import com.arcsoft.closeli.p2p.OnCameraMessageListener;
import com.arcsoft.closeli.p2p.P2pManager;
import com.arcsoft.closeli.player.IVideoPlayer;
import com.arcsoft.closeli.player.LivePreviewVideoPlayer;
import com.demo.smarthome.base.utils.SystemUtils;
import com.demo.smarthome.ui.base.CircularProgress;
import com.demo.smarthome.R;
import com.demo.smarthome.camera.CameraListManager;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomControlCameraView extends RoomControlBaseView implements SurfaceHolder.Callback, IVideoPlayer.IPlaybackCallback {
    private Context mContext;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private CheckBox mCarmeraOnOffBtn;


    private CameraInfo mCameraInfo;
    private IVideoPlayer mVideoPlayer;
    private LivePreviewVideoPlayer.GetVideoPlayerTask mGetVideoPlayerTask;
    private DisplayMetrics mDisplayMetrics;

    private FullRelayProxy mFullRelayProxy;
    private UpdateTimeTask mUpdateTimeTask;

    private boolean mIsShown = false;

    private SurfaceView mSurfaceView;
    private CircularProgress mLoading;
    private TextView mMessage;

    private MediaRecorder mMediaRecorder;

    private final static int START_LIVE_PREVIEW=100;


    public RoomControlCameraView(Context context, RoomInfo roominfo) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.room_control_camera_view, this);
        mCarmeraOnOffBtn = (CheckBox)findViewById(R.id.camera_onoff);
        mCarmeraOnOffBtn.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                if(!initView()){
                    mCarmeraOnOffBtn.setChecked(false);
                    return;
                }
                mSurfaceView.setBackground(null);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mIsShown = true;

                        if (mCameraInfo.isOnline()) {
                            whenCameraOnline();
                        } else {
                            whenCameraOffline();
                        }

                    }
                });
            }else {
                if (mIsShown) {
                    mIsShown = false;
                    stopLivePreview();
                }
                mSurfaceView.setBackgroundResource(R.drawable.room_environment_desk_bg);
                mMessage.setVisibility(View.GONE);
                mLoading.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onDestroy() {
        if (mIsShown) {
            mIsShown = false;
            stopLivePreview();
            mSurfaceView.setBackgroundResource(R.drawable.room_environment_desk_bg);
            mMessage.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
        }

        P2pManager.getInstance().removeMessageListener(mCameraMessageListener);
    }



    private void startLivePreview() {
        showBufferLoading();
        if (mGetVideoPlayerTask != null) {
            mGetVideoPlayerTask.cancel();
            mGetVideoPlayerTask = null;
        }

        mGetVideoPlayerTask = new LivePreviewVideoPlayer.GetVideoPlayerTask( this, new IVideoPlayer.IVideoPlayerCallback() {
            @Override
            public void onGetPlayerInstance(final IVideoPlayer player) {
                mHandler.postAtFrontOfQueue(new Runnable() {
                    @Override
                    public void run() {
                        if (player != null) {
                            mVideoPlayer = player;
                            try {
                                mVideoPlayer.init();
                                if (mSurfaceView.getHolder().getSurface().isValid()) {
                                    mVideoPlayer.setSurface(mSurfaceView.getHolder());
                                }

                                mFullRelayProxy = new FullRelayProxy(mContext, mCameraInfo);
                                mFullRelayProxy.setCallback(new FullRelayProxy.FullRelayProxyCallback() {
                                    @Override
                                    public void onMessage(int type, int value) {
                                        if (type == FullRelayProxy.FullRelayProxyCallback.MessageType_LowDownstream) {
                                            showShortToast("Client low downstream.");
                                        } else if (type == FullRelayProxy.FullRelayProxyCallback.MessageType_LowUpstream) {
                                            showShortToast("Camera low upstream.");
                                        } else if (type == FullRelayProxy.FullRelayProxyCallback.MessageType_SDCard_Playback_Busy) {
                                            showShortToast("Some client is playing recorded video on sdcard");
                                        } else if (type == FullRelayProxy.FullRelayProxyCallback.MessageType_Timeline_Starttime) {
                                            showShortToast("Camera delete some old data on sdcard");
                                        } else if (type == FullRelayProxy.FullRelayProxyCallback.MessageType_SDCard_Plug) {
                                            if (value == 0)
                                                showShortToast("SDCard is Pluged out");
                                            else if (value == 1)
                                                showShortToast("SDCard is Pluged in.");
                                        }
                                    }

                                    @Override
                                    public void onAudioTalkStatusChanged(int i) {
                                    }
                                });
                                ((LivePreviewVideoPlayer) mVideoPlayer).setFullRelayProxy(mFullRelayProxy);
                                mVideoPlayer.setUrl(mContext.getApplicationContext(), mFullRelayProxy.formatPlayUrl());
                            } catch (Exception e) {
                                hideBufferLoading();
                                e.printStackTrace();
                            }
                        } else {
                            hideBufferLoading();
                            // Log.w("getVideoPlayer failed: player == "
                            // + player);
                        }
                    }
                });
            }
        });
        // Log.d("getVideoPlayerTask start");
        mGetVideoPlayerTask.start();
    }

    private void stopLivePreview() {
        stopUpdateTime();
        if (mVideoPlayer != null) {
            mVideoPlayer.close();
            mVideoPlayer = null;
        }
    }


    private void startUpdateTime() {
        // Make sure run on the UI thread.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                stopUpdateTime();
                mUpdateTimeTask = new UpdateTimeTask();
                mUpdateTimeTask.execute();
            }
        });
    }

    private void stopUpdateTime() {
        if (mUpdateTimeTask != null) {
            mUpdateTimeTask.cancel(true);
            mUpdateTimeTask = null;
        }
    }

    private void resetVideoSize() {
        if (mVideoPlayer == null) {
            return;
        }

        final int screenWidth = mSurfaceView.getWidth();
        // final int screenHeight = mDisplayMetrics.heightPixels;

        final int videoOriginalWidth = mVideoPlayer.getResolutionWidth();
        final int videoOriginalHeight = mVideoPlayer.getResolutionHeight();

        final float ratio = ((float) screenWidth) / ((float) videoOriginalWidth);
        final int newVideoWidth = (int) (ratio * videoOriginalWidth);
        final int newVideoHeight = (int) (ratio * videoOriginalHeight);

        final MarginLayoutParams params = (MarginLayoutParams) mSurfaceView.getLayoutParams();
        params.width = newVideoWidth;
        params.height = newVideoHeight;
        mSurfaceView.setLayoutParams(params);
        mSurfaceView.getHolder().setFixedSize(params.width, params.height);
        mVideoPlayer.setDisplayRect(0, 0, params.width, params.height);
    }

    private void showBufferLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    private void hideBufferLoading() {
        mLoading.setVisibility(View.GONE);
    }

    private void whenCameraOffline() {// TODO
        mMessage.setVisibility(View.VISIBLE);
        // setControllEnable(false);
        hideBufferLoading();
        stopLivePreview();
    }

    private void whenCameraOnline() {
        mMessage.setVisibility(View.GONE);
        startLivePreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mVideoPlayer != null) {
                    if (mSurfaceView.getHolder().getSurface().isValid()) {
                        mVideoPlayer.setSurface(mSurfaceView.getHolder());
                    }
                }
            }
        }, 500);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mVideoPlayer != null) {
            mVideoPlayer.setSurface(null);
        }
    }

    /*
     * @Override public void onPlayerVideoSize(IXPlayer player) { }
     */

    @Override
    public void onPlayerPrepared(IVideoPlayer player) {
        player.start();
    }

    /*
     * @Override public void onPlayerTrackEnd(IXPlayer player) { }
     */

    @Override
    public void onPlayerStatusChanged(IVideoPlayer player, int newStatus) {
    }

    @Override
    public void onPlayerStatusChanged(IVideoPlayer player, int newStatus, int code) {
        if (newStatus == IVideoPlayer.Player_Status_Error) {
            stopLivePreview();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(5000);
                    if (!mIsShown) {
                        return;
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startLivePreview();
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onPlayerBuffering(IVideoPlayer player, boolean bBuffering) {
        if (bBuffering) {
            showBufferLoading();
        } else {
            if (player.isRendering()) {
                hideBufferLoading();
            }
        }
    }

    @Override
    public void onPlayerRendering(IVideoPlayer player, boolean start) {
        if (start ) {
            hideBufferLoading();
            resetVideoSize();
            startUpdateTime();
        }
    }

    private Handler mHandler = new Handler();

    private OnCameraMessageListener mCameraMessageListener = new OnCameraMessageListener() {
        @Override
        public void onCameraOnline(String srcId) {
            if (mCameraInfo.getSrcId().equalsIgnoreCase(srcId)) {
                whenCameraOnline();
            }
        }

        @Override
        public void onCameraOffline(String srcId) {
            if (mCameraInfo.getSrcId().equalsIgnoreCase(srcId)) {
                whenCameraOffline();
            }
        }

        @Override
        public void onCameraMessage(MessageType type, Object data) {
            if (type == MessageType.CameraMessage) {
                if (CameraMessageInfo.class.isInstance(data)) {
                    final CameraMessageInfo messageInfo = (CameraMessageInfo) data;
                    if (mCameraInfo.getSrcId().equalsIgnoreCase(messageInfo.getSrcId())) {
                        if (messageInfo.getType() == CameraMessageInfo.MessageType_LowCameraUpstream) {
                            showShortToast("Camera low upstream");
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onPlayerMagicZoomStatusChanged(boolean on) {
    }

    @Override
    public void onPlayerTrackEnd(IVideoPlayer player) {
    }

    @Override
    public void onPlayerVideoSizeChanged(IVideoPlayer player, int width, int height) {
    }

    private class UpdateTimeTask extends AsyncTask<Void, String, Void> {
        private DateFormat mDateFormat;
        private SimpleDateFormat mTimeFormat;

        @Override
        protected void onPreExecute() {
            mDateFormat = SystemUtils.getDateFormat(mContext.getApplicationContext());
            mTimeFormat = SystemUtils.getTimeFormat();
            mTimeFormat.setTimeZone(mCameraInfo.getTimeZone(mContext.getApplicationContext()));
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!isCancelled() && mVideoPlayer != null) {
                final long time = ((LivePreviewVideoPlayer) mVideoPlayer).getCameraRealTime();
                if (time > 0) {
                    final String currentTime = String.format("%s %s", mDateFormat.format(new Date(time)), mTimeFormat.format(new Date(time)));
                    publishProgress(new String[] { currentTime });
                }
                SystemClock.sleep(1000);
            }
            return null;
        }
    }


    protected boolean initView() {
        P2pManager.getInstance().addMessageListener(mCameraMessageListener);

        mCameraInfo = CameraListManager.getInstance().getCameraInfo(mRoomInfo.camera_device.get(0).value.toString());
        if (mCameraInfo == null) {
            showShortToast(mContext.getString(R.string.wh_hm_camera_offline));
            return false;
        }
        // isActionBarMenuVisible(true);
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();

        mSurfaceView = (SurfaceView) findViewById(R.id.camera_screen_surface);
        final SurfaceHolder sh_player = mSurfaceView.getHolder();
        sh_player.addCallback(this);
        // sh_player.setSizeFromLayout();
        sh_player.setFormat(PixelFormat.RGBA_8888);
        sh_player.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        sh_player.setKeepScreenOn(true);

        mLoading = $(R.id.loading);
        mMessage = $(R.id.message);
        return true;
    }

    public void showShortToast(final String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(final String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }


    protected String getStringId(int resId) {
        return getResources().getString(resId);
    }

    public <T extends View> T $(int resid) {
        return (T) findViewById(resid);
    }

    public <T extends View> T $(View mView, int resId) {
        return (T) mView.findViewById(resId);
    }

}
