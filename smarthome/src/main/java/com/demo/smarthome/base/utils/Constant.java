package com.demo.smarthome.base.utils;

import android.content.Context;

import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.R;

public class Constant {

    public static final String SP_GOHOME = "goHome";
    public static final String SP_OUTHOME = "outHome";
    public static final String SP_MONITOR = "monitor";
    public static final String SP_FLG_MONITOR = "flgMonitor";
    public static final String SP_FLG_GOHOME = "flgGoHome";
    public static final String SP_CURTAIN = "sp_curtain";

    public static final int EVENT_BUS_MODIFY_PERSONAL_INFORMATION = 111;//修改个人信息的通知
    public static final int EVENT_BUS_MODIFY_PERSONAL_HOUSE = 112;//修改房屋信息的通知
    public static final int EVENT_BUS_MODIFY_PERSONAL_REMARKS = 113;//修改个人信息的通知
    public static final int EVENT_BUS_MODIFY_PERSONAL_DELETE_IMG_ID = 114;//删除图片


    public static final String ERRORCODE = "errorCode";
    public static final int TIME_OUT = 20 * 1000;

    public static final String WsnApiKey = "3364e8fc314cd065620a9af913b67e00";
    public static final String BaiduMapApiKey = "p08cZcndkmgmzkUDWOowVjC7";
    public static final String BaiduPushApiKey = "p08cZcndkmgmzkUDWOowVjC7";
    public static final String BaiduPushSecretKey = "dwGd7jO4fheKsXQD0iCXXo6pBrmmIbWc";
    public static final String HeWeatherApiKey = "3a5a3c5ebffd4615af798c8ba37e9104";


    public static final Boolean GizwitzSDKDefined = true;

    public static final String SecurityOnOff = "SecurityOnOff";

    public static class Camera {
        public final static String ProductKey = "910fb531-4d0";
        public final static String ProkuctSecret = "exdKev9Akoj4MNzsAHY8";
        //        public final static String UserName = "13911828879";
//        public final static String Password = "Wili0622";
        public final static String UserName = "15947298333";
        public final static String Password = "wfkj2016";
    }

    public static class GizwitzConfigs {

        /**
         * 设备名字符显示长度.
         */
        public static final int DEVICE_NAME_KEEP_LENGTH = 8;

        /**
         * 设定是否为debug版本.
         */
        public static final boolean DEBUG = true;

        /**
         * 设定AppID，参数为机智云官网中查看产品信息得到的AppID.
         */
//        public static final String APPID = "ac78d0136ce3479b9194e8b677ccd24b";
        public static final String APPID = "b0e606d389cf4ca58c39261f83523695";

        //        public static final String AppSecret="52d71329614b4ac19a67b7b0980f994a";
        public static final String AppSecret = "1d7ab2f535bc49dca1461b079caf12e4";
        public static final String UserNameEnd = "wangfu";
        public static final String Password = "12345678wangfu";
        public static final String UID = "GizwitzUID";
        public static final String TOKEN = "GizwitzToken  ";

        /**
         * 指定该app对应设备的product_key，如果设定了过滤，会过滤出该peoduct_key对应的设备.
         */
//        public static final String PRODUCT_KEY = "17de623a165e49be9f7dac340fca1ed0";
        public static final String PRODUCT_KEY = "a726cd225b1a4a3fb563e388f42509e4";
        //        public static final String PRODUCT_SECRET = "235b2b1eabe84fe49be384fd5bcf58b7";
        public static final String PRODUCT_SECRET = "bb942002636942968cfae251813fa5e1";
        /**
         * 日志保存文件名.
         */
        public static final String LOG_FILE_NAME = "BassApp.log";


    }


    public class UserManager {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOGINSTATE = "loginState";

        public class Info {
            public static final String MOBILENUM = "mobileNum";
            public static final String EMAIL = "email";
            public static final String HEADICON = "headIcon";
        }
    }

    public class RouterManager {
        public static final String ROUTERINFO = "routerInfo";
    }

    public class LockScreenManager {
        public static final String GESTUREPASSWORD = "gesturePassword";
        public static final String SETGESTUREPASSWORD = "setGesturePassword";
        public static final String LOCKSCREENSTATE = "lockScreenState";
        public static final String PASSLOCKSCREENCHECK = "passLockScreenCheck";
    }

    public class HouseSystemManager {
        public static final String ENERGYMODE = "energyMode";
        public static final String LOCATION = "location";
        public static final String CITY = "city";
        public static final String ADDRESS = "address";
        public static final String WEATHER = "weather";
        public static final String HOUSEINFO = "houseInfo";
        public static final String DEVICEINFO = "deviceInfo";
        public static final String DEFAULTHOUSE = "defaultHouse";
        public static final String SYSTEMSTATE = "systemState";

        public class Feature {
            public static final String TEMPERATURE = "temperature";
            public static final String HUMIDITY = "humidity";
            public static final String AQI = "aqi";
            public static final String LIGHT = "light";
            public static final String CURTAIN = "curtain";
        }
    }

    public class UserAuthority {
        public static final String TITLE = "user_title";
        public static final String DEVICE = "user_device_control";
        public static final String SECURITY = "user_security_control";
        public static final String LIGHTING = "user_lighting_control";
        public static final String ENVIRONMENT = "user_environment_control";
    }

    public class MessageType {
        public static final String ALERT = "alert";
        public static final String FAULT = "fault";
        public static final String INFO = "info";
    }

    public final static int getWeatherImg(Context context, int weather_code) {
        int ResId = 0;
        switch (weather_code) {
            case 100:
                ResId = R.drawable.weather_sunny_clear;
                break;
            case 101:
            case 102:
            case 103:
            case 104:
                ResId = R.drawable.weather_partly_cloudy;
                break;
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
                ResId = R.drawable.weather_light_breeze;
                break;
            case 205:
            case 206:
            case 207:
                ResId = R.drawable.weather_strong_breeze;
                break;
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
                ResId = R.drawable.weather_hurricane;
                break;
            case 300:
            case 301:
                ResId = R.drawable.weather_shower_rain;
                break;
            case 302:
                ResId = R.drawable.weather_thundershower;
                break;
            case 303:
            case 304:
                ResId = R.drawable.weather_heavy_thunderstorm;
                break;
            case 305:
                ResId = R.drawable.weather_light_rain;
                break;
            case 306:
                ResId = R.drawable.weather_moderate_rain;
                break;
            case 307:
                ResId = R.drawable.weather_sleet;
                break;
            case 308:
            case 309:
            case 310:
            case 311:
            case 312:
            case 313:
                ResId = R.drawable.weather_severe_storm;
                break;
            case 400:
                ResId = R.drawable.weather_light_snow;
                break;
            case 401:
                ResId = R.drawable.weather_moderate_snow;
                break;
            case 402:
                ResId = R.drawable.weather_heavy_snow;
                break;
            case 403:
                ResId = R.drawable.weather_snowstorm;
                break;
            case 404:
            case 405:
                ResId = R.drawable.weather_rain_and_snow;
                break;
            case 406:
                ResId = R.drawable.weather_snow_flurry;
                break;
            case 407:
                ResId = R.drawable.weather_shower_snow;
                break;
            case 500:
            case 501:
            case 502:
                ResId = R.drawable.weather_mist;
                break;
            case 503:
            case 504:
            case 505:
            case 506:
            case 507:
            case 508:
                ResId = R.drawable.weather_sandstorm;
                break;
            case 900:
                ResId = R.drawable.weather_hot;
                break;
            case 901:
                ResId = R.drawable.weather_hot;
                break;
            case 999:
                ResId = R.drawable.weather_unknown;
                break;
        }
        return ResId;
    }

    public class HttpURL {
        public class Accout {
            public static final String httpVertifyCodeURL = "https://api.wangfurobots.com:81/smart/v1/Account/VerifyCode/%s";
            public static final String httpSignUpURL = "https://api.wangfurobots.com:81/smart/v1/Account/SignUp";
            public static final String httpSignInURL = "https://api.wangfurobots.com:81/smart/v1/Account/SignIn";
            public static final String httpPasswordURL = "https://api.wangfurobots.com:81/smart/v1/Account/Password";
            public static final String httpResetCodeURL = "https://api.wangfurobots.com:81/smart/v1/Account/ResetCode/%s";
            public static final String httpResetPasswordURL = "https://api.wangfurobots.com:81/smart/v1/Account/ResetPassword";
            public static final String httpProfileURL = "https://api.wangfurobots.com:81/smart/v1/Account/Profile";
            public static final String httpSettingURL = "https://api.wangfurobots.com:81/smart/v1/Account/Setting";
            public static final String httpRefreshTokenURL = "https://api.wangfurobots.com:81/smart/v1/Account/RefreshToken";
            public static final String httpSignOutURL = "https://api.wangfurobots.com:81/smart/v1/Account/SignOut";
            public static final String httpReportLocationURL = "https://api.wangfurobots.com:81/smart/v1/Account/ReportLocation/";
            public static final String httpQrcodeURL = "https://api.wangfurobots.com:81/smart/v1/Account/Qrcode/%d";
            public static final String httpScanURL = "https://api.wangfurobots.com:81/smart/v1/Account/Scan";
        }

        public class Device {
            public static final String httpDeviceURL = "https://api.wangfurobots.com:81/smart/v1/Device/";
        }

        public class House {
            public static final String httpHouseUserURL = "https://api.wangfurobots.com:81/smart/v1/House/User";
            public static final String httpHouseURL = "https://api.wangfurobots.com:81/smart/v1/House/%d";

            public static final String httpBindingHouse = "https://api.wangfurobots.com:81/smart/v1/Account/Scan";
        }

        public class Policy {
            public static final String httpPolicyURL = "https://api.wangfurobots.com:81/smart/v1/Policy/";
            public static final String httpUserURL = "https://api.wangfurobots.com:81/smart/v1/Policy/User/";
        }

        public class Public {
            public static final String httpFileUploadURL = "https://api.wangfurobots.com:81/smart/v1/Public/FileUpload/";
            public static final String httpMultiUploadURL = "https://api.wangfurobots.com:81/smart/v1/Public/MultiUpload/";
            public static final String httpBannerURL = "https://api.wangfurobots.com:81/smart/v1/Public/Banner/";
            public static final String httpInformationURL = "https://api.wangfurobots.com:81/smart/v1/Public/Information/";
            public static final String httpVersionURL = "https://api.wangfurobots.com:81/smart/v1/Public/Version/";
            public static final String httpCategoryURL = "https://api.wangfurobots.com:81/smart/v1/Public/Category/";
            public static final String httpSubCategoryURL = "https://api.wangfurobots.com:81/smart/v1/Public/SubCategory/";
        }

        public class Room {
            public static final String httpRoomURL = "https://api.wangfurobots.com:81/smart/v1/Room/%d";
        }

        public class ErrorCode {
            public static final int OK = 200;
            public static final int Bad_Request = 400;
            public static final int Unauthorized = 401;
            public static final int Forbidden = 403;
            public static final int Not_Found = 404;
            public static final int Conflict = 409;
            public static final int Device_Conflict = 423;
            public static final int Runtime_Error = 429;
            public static final int Internal_Server_Error = 500;
            public static final int Not_Implemented = 501;
        }
    }

    public static UserProfileBean userProfileBean;
}
