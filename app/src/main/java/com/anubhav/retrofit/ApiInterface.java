package com.anubhav.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    String[] strUrlName = {"", "", ""};
    String[] strUrlText = {"", "", ""};

    String SENDSMS = "";

    String UPDATEFCM = "userDetail";
    String LOGNUSER = "loginAuth";
    String LOGIN = "userAuth";
    String SIGNUP = "registerAuth";
    String CIRCLELIST = "getCircleList";

    @FormUrlEncoded
    @POST(UPDATEFCM)
    Call<String> UpdateFcm(@Field("user_id") String userid,
                           @Field("fcm_id") String fcmid);

    @FormUrlEncoded
    @POST(LOGNUSER)
    Call<String> LoginAttempt(@Field("username") String username,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST(LOGIN)
    Call<String> Login(@Field("username") String username,
                       @Field("password") String password);

    @FormUrlEncoded
    @POST(SIGNUP)
    Call<String> Register(@Field("name") String name,
                          @Field("mobile") String phone,
                          @Field("email") String email,
                          @Field("refercode") String refercode,
                          @Field("password") String password,
                          @Field("transaction_password") String tpassword);

    @GET(CIRCLELIST)
    Call<String> GetCircleList();

}
