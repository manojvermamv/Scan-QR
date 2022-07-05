package com.manoj.github.modularadapter;

import android.content.Context;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.manoj.github.modularadapter.itemmanager.ItemManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 13/08/16
 */
public abstract class ModularAdapter<T> extends RecyclerView.Adapter<ModularAdapter.ViewHolder<? extends T>> {

    public abstract static class ViewHolder<T> extends RecyclerView.ViewHolder {

        private T mCurrentItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public final void bind(T item) {
            mCurrentItem = item;
            performBind(item);
        }

        protected abstract void performBind(@NonNull T item);

        protected void onAttach() {
        }

        protected void onDetach() {
        }

        public final T getCurrentItem() {
            return mCurrentItem;
        }
    }

    public interface ViewHolderFactory<VH extends ViewHolder<?>> {
        VH create(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);
    }

    public static class Builder<T> {

        private final List<ModularAdapterImpl.Module<?, ?>> mModules = new ArrayList<>();

        private final Context mContext;
        private final ItemManager<T> mItemManager;

        public Builder(@NonNull Context context, @NonNull ItemManager<T> itemManager) {
            mContext = context;
            mItemManager = itemManager;
        }

        public <M extends T, VH extends ViewHolder<M>> Builder<T> add(@NonNull Class<M> modelClass, @NonNull ViewHolderFactory<VH> holderFactory) {
            mModules.add(new ModularAdapterImpl.Module<M, VH>(
                    mModules.size(),
                    modelClass,
                    holderFactory
            ));
            return this;
        }

        public ModularAdapter<T> build() {
            return new ModularAdapterImpl<>(mContext, mItemManager, mModules);
        }
    }

    private final ItemManager<T> mItemManager;
    private final LayoutInflater mInflater;
    private final Context context;

    public ModularAdapter(@NonNull Context context, @NonNull ItemManager<T> itemManager) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mItemManager = itemManager;

        itemManager.addChangeSetCallback(changeSet -> changeSet.applyTo(
                this::notifyItemMoved,
                this::notifyItemRangeInserted,
                this::notifyItemRangeRemoved,
                this::notifyItemRangeChanged
        ));
    }

    public Context getContext() {
        return context;
    }

    public ItemManager<T> getItemManager() {
        return mItemManager;
    }

    @Override
    public final ViewHolder<? extends T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(mInflater, parent, viewType);
    }

    @NonNull
    protected abstract ViewHolder<? extends T> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    public final <view extends View> view bindView(View itemView, int id) {
        return itemView.findViewById(id);
    }

    @Override
    public final void onBindViewHolder(ViewHolder<? extends T> holder, int position) {
        final T item = getItem(position);
        ((ViewHolder<T>) holder).bind(item);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder<? extends T> holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttach();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder<? extends T> holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onDetach();
    }

    @Override
    public int getItemCount() {
        return mItemManager.getItemCount();
    }

    @NonNull
    public final T getItem(int position) {
        return mItemManager.getItem(position);
    }

}