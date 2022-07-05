package com.anubhav.retrofit;

import static com.anubhav.scanqr.utils.Global.ISTESTING;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.anubhav.commonutility.CustomProgress;
import com.anubhav.commonutility.CustomToast;
import com.anubhav.scanqr.MyApp;
import com.anubhav.scanqr.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebService {

    private final Context context;
    private final WebServiceListener listener;

    private String postUrl;
    private String[] imagePath;
    private boolean isShowText = false;
    private boolean isDialogShow = true;

    private LinkedList<String> lstUploadData = new LinkedList<>();
    private CustomProgress progressDialog;

    public static void callWebService(Context svContext, String postUrl, LinkedList<String> lstUploadData, WebServiceListener listener) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, listener, true);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public static void callWebServiceWithoutLoader(Context svContext, String postUrl, LinkedList<String> lstUploadData, WebServiceListener listener) {
        WebService webService = new WebService(svContext, postUrl, lstUploadData, listener, false);
        webService.LoadDataRetrofit(webService.callReturn());
    }

    public WebService(Context context, WebServiceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public WebService(Context context, String postUrl, LinkedList<String> lstUploadData, WebServiceListener listener) {
        this.context = context;
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.listener = listener;
    }

    public WebService(Context context, String postUrl, LinkedList<String> lstUploadData, WebServiceListener listener, boolean isDialogShow) {
        this.context = context;
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.listener = listener;
        this.isDialogShow = isDialogShow;
    }

    public WebService(Context context, String postUrl, LinkedList<String> lstUploadData, WebServiceListener listener, String[] imagePath) {
        this.context = context;
        this.postUrl = postUrl;
        this.lstUploadData = lstUploadData;
        this.imagePath = new String[imagePath.length];
        this.imagePath = imagePath;
        this.listener = listener;
    }

    public void LoadDataRetrofit(Call<String> call) {
        if (ISTESTING) {
            System.out.println("API Call ---> " + postUrl);
            for (int i = 0; i < lstUploadData.size(); i++) {
                System.out.println("\tParams " + i + " - " + lstUploadData.get(i));
            }
        }

        if (isDialogShow) {
            progressDialog = new CustomProgress(context, R.layout.lay_customprogessdialog);
            TextView textView = (TextView) progressDialog.findViewById(R.id.loader_showtext);
            if (isShowText) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(getDialogText(postUrl));
            } else {
                textView.setVisibility(View.GONE);
            }

            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        call.enqueue(webServiceCallback);
    }

    private final Callback<String> webServiceCallback = new Callback<String>() {
        @Override
        public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
            String bodyString = "";
            try {
                if (ISTESTING)
                    System.out.println(response.toString() + "..........webservice response..........");

                int code = response.code();
                //if (response.isSuccessful()) {
                if (code == 200 || code == 300) {
                    bodyString = response.body();
                    try {
                        if (ISTESTING) {
                            System.out.println("API ---> " + postUrl + "\nJSON Response ---> " + bodyString);
                            //FileUtils.storeTextInFile(context, bodyString, postUrl.split("/")[((postUrl.split("/")).length) - 1]);
                        }
                        if (listener != null)
                            listener.onWebServiceActionComplete(bodyString, postUrl);
                    } catch (JSONException e) {
                        if (ISTESTING)
                            CustomToast.showCustomToast(context, "Some error occurred\n" + e.getMessage(), CustomToast.ToastyError);
                    }

                } else {
                    bodyString = response.message();
                    if (listener != null) listener.onWebServiceError(bodyString, postUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hideProgress();
            }
        }

        @Override
        public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
            hideProgress();
            if (ISTESTING) Log.e(MyApp.appName, t.toString());
        }
    };

    public Call<String> callReturn() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        if (postUrl.equalsIgnoreCase(ApiInterface.SENDSMS)) {
            apiService = ApiClient.getSmsClient().create(ApiInterface.class);
        }

        switch (postUrl) {
            case ApiInterface.UPDATEFCM:
                return apiService.UpdateFcm(lstUploadData.get(0), lstUploadData.get(1));
            case ApiInterface.LOGIN:
                return apiService.Login(lstUploadData.get(0), lstUploadData.get(1));
            case ApiInterface.SIGNUP:
                return apiService.Register(lstUploadData.get(0), lstUploadData.get(1), lstUploadData.get(2), lstUploadData.get(3), lstUploadData.get(4), lstUploadData.get(5));
            case ApiInterface.LOGNUSER:
                return apiService.LoginAttempt(lstUploadData.get(0), lstUploadData.get(1));
            case ApiInterface.CIRCLELIST:
                return apiService.GetCircleList();
            default:
                new CustomToast(context).showToast("Please declare url in WebService class");
                break;
        }
        return null;
    }

    private void hideProgress() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public String getDialogText(String strPostUrl) {
        for (int i = 0; i < (ApiInterface.strUrlName).length; i++) {
            if ((ApiInterface.strUrlName)[i].equals(strPostUrl)) {
                return (ApiInterface.strUrlText)[i];
            }
        }
        return "";
    }

    public static String getStrFromObject(JSONObject obj, String name) throws JSONException {
        String strData = "";
        if (obj.has(name)) {
            strData = obj.getString(name);
        }

        if (strData.equals("") || strData.equals("null")) {
            strData = "";
        }
        return strData;
    }


}