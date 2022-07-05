package com.anubhav.retrofit;

import org.json.JSONException;

public interface WebServiceListener {
    void onWebServiceActionComplete(String result, String url) throws JSONException;

    void onWebServiceError(String result, String url);
}