package aprivate.oo.gizwitopenapi;


import com.google.gson.JsonObject;

import aprivate.oo.gizwitopenapi.requestbody.BindBody;
import aprivate.oo.gizwitopenapi.requestbody.LoginBody;
import aprivate.oo.gizwitopenapi.requestbody.RegistBody;
import aprivate.oo.gizwitopenapi.response.BindingList;
import aprivate.oo.gizwitopenapi.response.DeviceData;
import aprivate.oo.gizwitopenapi.response.LoginResponse;
import aprivate.oo.gizwitopenapi.response.RegistResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by zhuxiaolong on 2017/8/31.
 */

public interface OpenApi {

    @POST("app/login")
    Call<LoginResponse> loginServer(@Body LoginBody body);

    @POST("app/users")
    Call<RegistResponse> registServer(@Body RegistBody body);

    @POST("app/bind_mac")
    Call<BindingList.DevicesBean> bindServer(@Body BindBody body);

    @GET
    Call<BindingList> getDeviceList(@Url String url);

    @GET("/app/devdata/{did}/latest")
    Call<DeviceData> getDeviceData(@Path("did") String did);

    @POST("/app/control/{did}")
    Call<JsonObject> contralDevice(@Path("did") String did, @Body String body);

    @GET("/app/devices/{did}/raw_data?type={type}")
    Call<JsonObject> getCmdHistory(@Path("did") String did, @Query("type") String type,@Query("start_time")String st,
                                   @Query("end_time")String et,@Query("skip")String skip,@Query("limit")String limit);
}
