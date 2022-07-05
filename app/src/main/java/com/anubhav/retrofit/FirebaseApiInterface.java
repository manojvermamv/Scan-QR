package com.anubhav.retrofit;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FirebaseApiInterface {

    String SEND_FCM_PUSH_NOTIFICATION = "send";

    @FormUrlEncoded
    @POST(SEND_FCM_PUSH_NOTIFICATION)
    Call<JSONObject> sendFcmPush(@Field("to") String userToken,
                                 @Field("notification") String notification,
                                 @Field("data") String data_payload);

}
