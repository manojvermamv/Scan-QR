package com.manoj.github.customlistadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * Created with Android Studio<br>
 * User: Xaver<br>
 * Date: 27/03/2017
 */
class ModularCustomListAdapterImpl<T extends CustomListAdapter.ViewModel> extends CustomListAdapter<T> {

    static class Module<M extends ViewModel, VH extends ViewHolder<M>> {

        private final int mViewType;
        private final Class<M> mItemClass;
        private final ViewHolderFactory<VH> mHolderFactory;

        Module(int viewType, Class<M> itemClass, ViewHolderFactory<VH> holderFactory) {
            mViewType = viewType;
            mItemClass = itemClass;
            mHolderFactory = holderFactory;
        }
    }

    private final List<Module<?, ?>> mModules;

    ModularCustomListAdapterImpl(Context context, Class<T> itemClass, Comparator<T> comparator, List<Module<?, ?>> modules) {
        super(context, itemClass, comparator);
        mModules = modules;
    }

    @NonNull
    @Override
    protected CustomListAdapter.ViewHolder<? extends T> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        for (Module<?, ?> module : mModules) {
            if (module.mViewType == viewType) {
                return (ViewHolder<? extends T>) module.mHolderFactory.create(inflater, parent);
            }
        }

        throw new IllegalStateException("No mapping for " + viewType + " exists.");
    }

    @Override
    public int getItemViewType(int position) {
        final T item = getItem(position);
        final Class<?> itemClass = item.getClass();
        for (Module<?, ?> module : mModules) {
            if (module.mItemClass.isAssignableFrom(itemClass)) {
                return module.mViewType;
            }
        }

        throw new IllegalStateException("No mapping for " + itemClass + " exists.");
    }

    @Override
    public int getItemCount() {
        return getItemManager().getItemCount();
    }

}
