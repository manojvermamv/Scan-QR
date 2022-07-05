package com.manoj.github.modularadapter.itemmanager;

import androidx.annotation.NonNull;

import java.util.Comparator;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 01/04/2017
 */
public interface ItemManager<T> {

    interface ChangeSetCallback {
        void onChangeSetAvailable(ChangeSet changeSet);
    }

    interface StateCallback {
        void onChangesInProgress();
        void onChangesFinished();
    }

    T getItem(int position);
    int getItemCount();

    void setComparator(Comparator<T> comparator);

    void addChangeSetCallback(ChangeSetCallback callback);
    void removeChangeSetCallback(ChangeSetCallback callback);

    void addStateCallback(StateCallback callback);
    void removeStateCallback(StateCallback callback);
}
