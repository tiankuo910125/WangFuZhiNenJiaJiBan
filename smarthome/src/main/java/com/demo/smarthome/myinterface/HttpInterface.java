package com.demo.smarthome.myinterface;


import com.demo.smarthome.communication.jsonbean.OutAccessToken;
import com.demo.smarthome.communication.jsonbean.OutHeadPortraits;
import com.demo.smarthome.communication.jsonbean.OutModel_QRCode;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.ginseng.AccountChangeGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.BindCodeGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.FeedBackGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.ForgotPasswordGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.LogListGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.LoginGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.Modify;
import com.demo.smarthome.communication.jsonbean.ginseng.RegisteredGinseng;
import com.demo.smarthome.communication.jsonbean.ginseng.ResetPasswordGinseng;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by wangdongyang on 16/11/17.
 */
public interface HttpInterface {

//    @GET("spot/serchCity")
//    Call<JsonObject> getDemo();
//
//    @GET("weather?")
//    Call<JsonObject> getWeather(@Query("city") String city, @Query("key") String key);
//
//    @FormUrlEncoded
//    @POST("smart/v1/Account/SignIn")
//    Call<JsonObject> LgoinDemo(@Field("mobile") String mobile, @Field("password") String password);
//
//
    /**
     * 获取验证码
     * @param mobile
     * @return
     */
//    @FormUrlEncoded
    @POST("smart/v1/Account/VerifyCode/{mobile}")
    Call<ResultBean> getCode(@Path("mobile") String mobile);


    /**
     * 修改密码时候获取验证码
     * @param mobile
     * @return
     */
//    @FormUrlEncoded
    @POST("smart/v1/Account/ResetCode/{mobile}")
    Call<ResultBean> getCodeForgotPassword(@Path("mobile") String mobile);


    /**
     * 注册
     * @param body
     * @return
     */
    @POST("smart/v1/Account/SignUp")
    Call<ResultBean> getRegistered(@Body RegisteredGinseng body);


    /**
     * 获取accesstoken
     * @return
     */
    @FormUrlEncoded
    @POST("api/lapp/token/get")
    Call<OutAccessToken> getAccessToken(@Field("appKey") String appKey, @Field("appSecret") String appSecret);
//
    /**
     * 登录
     * @return
     */
    @POST("smart/v1/Account/SignIn")
    Call<ResultBean> getLogIn(@Body LoginGinseng body);
//
//    /**
//     * 获取个人信息
//     * @param accessToken
//     * @return
//     */
//    @GET("smart/v1/Account/Profile")
//    Call<JsonObject> getUserInformation(@Query("Authorization") String accessToken);
//
    /**
     * 绑定二维码
     * @return
     */
    @POST
    Call<ResultBean> getBindCode(@Url String url, @Body BindCodeGinseng body);

    /**
     * 修改密码
     * @return
     */
    @PUT
    Call<ResultBean> getForgotPassword(@Url String url, @Body ForgotPasswordGinseng body);

    /**
     * 忘记密码
     * @return
     */
    @POST("smart/v1/Account/ResetPassword")
    Call<ResultBean> getResetPassword(@Body ResetPasswordGinseng body);



    @Multipart
    @POST
    Call<ResultBean> uploadImage(@Url String url, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<OutHeadPortraits> uploadImages(@Url String url, @Part() List<MultipartBody.Part> parts);

    /**
     * @return
     * 更新房屋信息
     */
    @PUT
    Call<ResultBean> getModifyHouse(@Url String url, @Body Modify body);


    /**
     * @return
     * 更新用户备注
     */
    @PUT
    Call<ResultBean> getModifyUser(@Url String url, @Body Modify body);


    @PUT
    Call<ResultBean> getAccountChange(@Url String url, @Body AccountChangeGinseng body);


    /**
     * 注销
     * @return
     */
    @POST
    Call<ResultBean> getSignOut(@Url String url);

    /**
     * 意见反馈
     * @return
     */
    @POST
    Call<ResultBean> getFeedBack(@Url String url, @Body FeedBackGinseng body);


    /**
     * 获取设备日志列表
     * @param url
     * @param body
     * @return
     */
    @POST
    Call<ResultBean> getLogList(@Url String url, @Body LogListGinseng body);

    /**
     * 获取历史纪录列表
     * @param url
     * @param body
     * @return
     */
    @POST
    Call<ResultBean> getHistoryLogList(@Url String url, @Body LogListGinseng body);



    /**
     * @return
     * 获取房屋二维码
     */
    @GET
    Call<OutModel_QRCode> getQRCode(@Url String url);

    /**
     * @return
     * 重置房屋二维码
     */
    @PUT
    Call<OutModel_QRCode> upDateQRCode(@Url String url);


    /**
     * @return
     * 删除用户
     */
    @DELETE
    Call<ResultBean> deleteUser(@Url String url);

}
