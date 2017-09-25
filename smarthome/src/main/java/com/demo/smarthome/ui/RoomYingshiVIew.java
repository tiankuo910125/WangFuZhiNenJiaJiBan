package com.demo.smarthome.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.smarthome.LoginActivity;
import com.demo.smarthome.R;
import com.demo.smarthome.ui.base.RoomControlBaseView;
import com.demo.smarthome.ui.model.RoomInfo;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIPlayer;

import java.util.Calendar;

/**
 * Created by wangdongyang on 2017/4/20.
 */
public class RoomYingshiVIew extends RoomControlBaseView implements EZUIPlayer.EZUIPlayerCallBack {
    private static EZUIPlayer player_ui;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private RoomInfo mRoomInfo;
    private LayoutInflater mInflater;

    private ImageView player_bg;
    private CheckBox camera_onoff;

    private String mUrl;


    public RoomYingshiVIew(final Context context,RoomInfo roominfo,String url) {
        super(context);
        mContext = context;
        mRoomInfo = roominfo;
        mUrl = url;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.view_yingshi, this);

        player_bg = (ImageView) findViewById(R.id.player_bg);
        camera_onoff = (CheckBox) findViewById(R.id.camera_onoff);

        player_ui = (EZUIPlayer) findViewById(R.id.camera_screen_surface);
        player_ui.setBackgroundColor(Color.parseColor("#ff000000"));
        Log.i("TAG", "RoomYingshiVIew: url---" + url);
        player_ui.setCallBack(this);
        player_ui.setUrl(mUrl);
//        player_ui.setPlayParams(mUrl, new EZUIPlayer.EZUIPlayerCallBack() {
//            @Override
//            public void onPlaySuccess() {
//
//            }
//
//            @Override
//            public void onPlayFail(EZUIError ezuiError) {
//                Log.i("TAG", "onPlayFail: " + ezuiError);
//                if (ezuiError.getErrorString().equals(EZUIError.UE_ERROR_INNER_VERIFYCODE_ERROR)){
//
//                }else{
//                    // TODO: 2017/5/12
//                    //未发现录像文件
//                    Toast.makeText(context, "未发现录像文件",Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onVideoSizeChange(int i, int i1) {
//
//            }
//        });
        //player_ui.startPlay();
        camera_onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    player_ui.stopPlay();
                    player_bg.setVisibility(View.VISIBLE);
                }else{
                    player_ui.startPlay();
                    player_bg.setVisibility(View.GONE);

                }
            }
        });

    }

    public RoomYingshiVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomYingshiVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static void setDestory(){
        if(player_ui!=null){
            player_ui.stopPlay();
            player_ui.releasePlayer();
        }
    }

    @Override
    public void onPlaySuccess() {
        Log.d("TAG","onPlaySuccess");
    }

    @Override
    public void onPlayFail(EZUIError ezuiError) {
        Log.d("TAG","onPlayFail");
        // TODO: 2017/2/21 播放失败处理
        if (ezuiError.getErrorString().equals(EZUIError.UE_ERROR_INNER_VERIFYCODE_ERROR)){
            Log.i("RoomYingshiVIew", "onPlayFail: 视频验证码错误，建议重新获取url地址增加验证码");
        }else if(ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_NOT_FOUND_RECORD_FILES)){
            // TODO: 2017/5/12
            //未发现录像文件
            Toast.makeText(mContext,"未发现录像文件",Toast.LENGTH_LONG).show();
        }else if (ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_TRANSF_DEVICE_OFFLINE)) {
            Toast.makeText(mContext,"设备不在线",Toast.LENGTH_SHORT).show();
        }else if (ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_INNER_STREAM_TIMEOUT)) {
            Log.i("RoomYingshiVIew", "onPlayFail: 播放失败，请求连接设备超时，检测设备网路连接是否正常");
        }else if (ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_TRANSF_TERMINAL_BINDING)) {
            //当前账号开启了终端绑定，只允许指定设备登录操作
        }else if (ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_CAMERA_NOT_EXIST)) {
            //通道不存在，设备参数错误，建议重新获取播放地址
        }else if (ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_APPKEY_ERROR)) {
            //通道不存在，设备参数错误，建议重新获取播放地址
        }else if (ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_PLAY_FAIL)) {
            //视频播放失败
            Toast.makeText(mContext,"视频播放失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onVideoSizeChange(int width, int height) {
        Log.d("TAG","onVideoSizeChange  width = "+width+"   height = "+height);
    }

    @Override
    public void onPrepared() {
        Log.d("TAG","onPrepared");
        //播放
        player_ui.startPlay();
    }

    @Override
    public void onPlayTime(Calendar calendar) {
        Log.d("TAG","onPlayTime");
        if (calendar != null) {
            // TODO: 2017/2/16 当前播放时间
            Log.d("TAG","onPlayTime calendar = "+calendar.getTime().toString());
        }
    }

    @Override
    public void onPlayFinish() {
        // TODO: 2017/2/16 播放结束
        Log.d("TAG","onPlayFinish");
    }
}
