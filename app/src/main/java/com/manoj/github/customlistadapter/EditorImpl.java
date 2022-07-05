package com.manoj.github.customlistadapter;

import androidx.annotation.NonNull;

import com.manoj.github.modularadapter.itemmanager.ModifiableItemManager;

import java.util.Collection;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 01/04/2017
 */

class EditorImpl<T extends CustomListAdapter.ViewModel> implements CustomListAdapter.Editor<T> {

    private final ModifiableItemManager.Transaction<T> mTransaction;

    EditorImpl(ModifiableItemManager.Transaction<T> transaction) {
        mTransaction = transaction;
    }

    @Override
    public CustomListAdapter.Editor<T> add(@NonNull T item) {
        mTransaction.add(item);
        return this;
    }

    @Override
    public CustomListAdapter.Editor<T> add(@NonNull Collection<T> items) {
        mTransaction.add(items);
        return this;
    }

    @Override
    public CustomListAdapter.Editor<T> remove(@NonNull T item) {
        mTransaction.remove(item);
        return this;
    }

    @Override
    public CustomListAdapter.Editor<T> remove(@NonNull Collection<T> items) {
        mTransaction.remove(items);
        return this;
    }

    @Override
    public CustomListAdapter.Editor<T> replaceAll(@NonNull Collection<T> items) {
        mTransaction.replaceAll(items);
        return this;
    }

    @Override
    public CustomListAdapter.Editor<T> removeAll() {
        mTransaction.removeAll();
        return this;
    }

    @Override
    public void commit() {
        mTransaction.commit();
    }
}
