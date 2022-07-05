package com.manoj.github.customlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.manoj.github.modularadapter.ModularAdapter;
import com.manoj.github.modularadapter.itemmanager.ItemManager;
import com.manoj.github.modularadapter.itemmanager.ModifiableItemManager;
import com.manoj.github.modularadapter.itemmanager.sortedlist.SortedListItemManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 13/08/16
 */
public abstract class CustomListAdapter<T extends CustomListAdapter.ViewModel> extends ModularAdapter<T> {

    public interface Callback {
        void onEditStarted();
        void onEditFinished();
    }

    public interface Editor<T extends ViewModel> {
        Editor<T> add(@NonNull T item);
        Editor<T> add(@NonNull Collection<T> items);
        Editor<T> remove(@NonNull T item);
        Editor<T> remove(@NonNull Collection<T> items);
        Editor<T> replaceAll(@NonNull Collection<T> items);
        Editor<T> removeAll();
        void commit();
    }

    public abstract static class ViewHolder<T extends ViewModel> extends ModularAdapter.ViewHolder<T> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    public interface ViewModel extends SortedListItemManager.ViewModel {
    }

    public static class ComparatorBuilder<T extends ViewModel> extends com.manoj.github.modularadapter.itemmanager.sortedlist.ComparatorBuilder<T> {
    }

    public interface ViewHolderFactory<VH extends ViewHolder<?>> {
        VH create(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);
    }

    public static class Builder<T extends ViewModel> {

        private final List<ModularCustomListAdapterImpl.Module<?, ?>> mModules = new ArrayList<>();

        private final Context mContext;
        private final Class<T> mItemClass;
        private final Comparator<T> mComparator;

        public Builder(@NonNull Context context, @NonNull Class<T> itemClass, @NonNull Comparator<T> comparator) {
            mContext = context;
            mItemClass = itemClass;
            mComparator = comparator;
        }

        public <M extends T, VH extends ViewHolder<M>> Builder<T> add(@NonNull Class<M> modelClass, @NonNull ViewHolderFactory<VH> holderFactory) {
            mModules.add(new ModularCustomListAdapterImpl.Module<M, VH>(
                    mModules.size(),
                    modelClass,
                    holderFactory
            ));
            return this;
        }

        public CustomListAdapter<T> build() {
            return new ModularCustomListAdapterImpl<>(mContext, mItemClass, mComparator, mModules);
        }
    }

    private final ItemManager.StateCallback mStateCallback = new ItemManager.StateCallback() {

        @Override
        public void onChangesInProgress() {
            for (Callback callback : mCallbacks) {
                callback.onEditStarted();
            }
        }

        @Override
        public void onChangesFinished() {
            for (Callback callback : mCallbacks) {
                callback.onEditFinished();
            }
        }
    };

    private final List<Callback> mCallbacks = new ArrayList<>();

    public CustomListAdapter(@NonNull Context context, @NonNull Class<T> itemClass, @NonNull Comparator<T> comparator) {
        super(context, new SortedListItemManager<>(itemClass, comparator));
        getItemManager().addStateCallback(mStateCallback);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    protected abstract CustomListAdapter.ViewHolder<? extends T> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    public void addCallback(@NonNull Callback callback) {
        mCallbacks.add(callback);
    }

    public void removeCallback(@NonNull Callback callback) {
        mCallbacks.remove(callback);
    }

    @NonNull
    public final Editor<T> edit() {
        final ModifiableItemManager<T> itemManager = (ModifiableItemManager<T>) getItemManager();
        final ModifiableItemManager.Transaction<T> transaction = itemManager.newTransaction();
        return new EditorImpl<T>(transaction);
    }
}