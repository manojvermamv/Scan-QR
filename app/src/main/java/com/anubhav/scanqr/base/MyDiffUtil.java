package com.anubhav.scanqr.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.anubhav.scanqr.database.common.InstalledAppsModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDiffUtil<T> extends DiffUtil.Callback {

    public static String TAG = MyDiffUtil.class.getSimpleName();

    Class<T> tClass;
    List<T> oldData = new ArrayList<>();
    List<T> newData = new ArrayList<>();

    public MyDiffUtil(List<T> oldData, List<T> newData, Class<T> tClass) {
        Log.e(TAG, "MyDiffUtil : " + oldData.size() + " - " + newData.size());
        this.oldData = oldData;
        this.newData = newData;
        this.tClass = tClass;
    }

    public MyDiffUtil(List<T> oldData, List<T> newData, Callback<T> callback, Class<T> tClass) {
        Log.e(TAG, "MyDiffUtil : " + oldData.size() + " - " + newData.size());
        this.oldData = oldData;
        this.newData = newData;
        this.tCallback = callback;
        this.tClass = tClass;
    }

    @Override
    public int getOldListSize() {
        return oldData != null ? oldData.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newData != null ? newData.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Log.e(TAG, String.valueOf("areItemsTheSame -> " + oldItemPosition + newItemPosition));
        T oldItem = oldData.get(oldItemPosition);
        T newItem = newData.get(newItemPosition);
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldData.get(oldItemPosition);
        T newItem = newData.get(newItemPosition);

        int result = 0;
        if (tClass == InstalledAppsModel.class) {
            InstalledAppsModel oldModel = (InstalledAppsModel) oldItem;
            InstalledAppsModel newModel = (InstalledAppsModel) newItem;
            //result = newModel.compareTo(oldModel);

        }

        Log.e(TAG, "areContentsTheSame -> " + result);
        if (result == 0) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Log.e(TAG, "getChangePayload : " + oldItemPosition + " - " + newItemPosition);
        T oldItem = oldData.get(oldItemPosition);
        T newItem = newData.get(newItemPosition);

        if (tCallback != null) {
            HashMap<String, Object> bundleData = tCallback.getPayloadList(oldItem, newItem);
            return getBundle(bundleData);
        }
        return null;
    }

    public Bundle getBundle(HashMap<String, Object> bundleData) {
        Bundle bundle = new Bundle();

        if (bundleData != null && bundleData.size() != 0) {
            for (String key : bundleData.keySet()) {
                Object value = bundleData.get(key);

                if (value instanceof String) {
                    bundle.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    bundle.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    bundle.putBoolean(key, (Boolean) value);
                } else if (value instanceof Serializable) {
                    bundle.putSerializable(key, (Serializable) value);
                } else if (value instanceof Parcelable) {
                    bundle.putParcelable(key, (Parcelable) value);
                }
            }
        }

        if (bundle.size() == 0) {
            return null;
        }
        return bundle;
    }

    /**
     * callbacks for get Bundle to use in getChangePayload()
     */
    Callback<T> tCallback;

    public interface Callback<T> {
        HashMap<String, Object> getPayloadList(T oldItem, T newItem);
    }

    public void setCallback(Callback<T> callback) {
        this.tCallback = callback;
    }

}