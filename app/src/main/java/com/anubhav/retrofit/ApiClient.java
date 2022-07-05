package com.anubhav.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.anubhav.scanqr.utils.Constants;
import com.anubhav.scanqr.utils.Global;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    private static Gson gson = new GsonBuilder().setLenient().create();

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS);

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Global.PRE_URL)
            .client(httpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson));

    // ApiClient for calling custom sms apis
    public static Retrofit getSmsClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Global.PRE_URL_MSG)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }

    // Default ApiClient for calling rest apis
    public static Retrofit getClient() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Global.PRE_URL)
                .client(httpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));

        return retrofitBuilder.build();
    }

    // ApiClient for calling rest apis with interceptors
    public static <S> S createService(Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(loggingInterceptor)) {
            httpClient.addInterceptor(loggingInterceptor);
            retrofitBuilder.client(httpClient.build());
        }
        return retrofitBuilder.build().create(serviceClass);
    }

    // ApiClient for Firebase FCM
    public static FirebaseApiInterface getFirebaseFcmClient() {
        httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Authorization", "key=" + Constants.FIREBASE_SERVER_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();
            return chain.proceed(request);
        });

        retrofitBuilder = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .client(httpClient.build())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));

        return retrofitBuilder.build().create(FirebaseApiInterface.class);
    }

}
