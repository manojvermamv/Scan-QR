package com.anubhav.scanqr.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.anubhav.commonutility.ImageLoading;
import com.anubhav.commonutility.customfont.FontUtils;
import com.anubhav.scanqr.BaseRecyclerAdapter;
import com.anubhav.scanqr.R;
import com.anubhav.scanqr.database.common.InstalledAppsModel;
import com.anubhav.scanqr.interfaces.ItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InstalledAppsAdapter extends BaseRecyclerAdapter<InstalledAppsModel> {

    public InstalledAppsAdapter(Context context, List<InstalledAppsModel> listItems) {
        super(context, listItems, InstalledAppsModel.class);
    }

    @NonNull
    @Override
    protected ViewHolder<InstalledAppsModel> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_apps_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Bundle bundle) {
    }

    @Override
    public List<InstalledAppsModel> getSearchFilterList(List<InstalledAppsModel> originalDataList, String charString) {
        List<InstalledAppsModel> filteredList = new ArrayList<>();
        for (int i = 0; i < originalDataList.size(); i++) {
            InstalledAppsModel model = originalDataList.get(i);
            if (model.getAppName().toLowerCase().contains(charString) ||
                    model.getPkgName().toLowerCase().contains(charString)) {
                filteredList.add(model);
            }
        }
        return filteredList;
    }

    @Override
    public HashMap<String, Object> getPayloadList(InstalledAppsModel oldItem, InstalledAppsModel newItem) {
        return null;
    }

    public class MyViewHolder extends ViewHolder<InstalledAppsModel> {
        public RelativeLayout root;
        public TextView txtTitle, txtDesc;
        public ImageView imgIcon, btnMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            root = bindView(itemView, R.id.root_view);
            txtTitle = bindView(itemView, R.id.title);
            txtDesc = bindView(itemView, R.id.desc);
            imgIcon = bindView(itemView, R.id.iv_app);
            btnMore = bindView(itemView, R.id.more_item);

            FontUtils.setFont(getContext(), root);
        }

        @Override
        protected void performBind(ItemClickListener<InstalledAppsModel> itemClickCallback, @NonNull InstalledAppsModel item) {
            int position = getBindingAdapterPosition();

            txtTitle.setText(item.getAppName());
            txtDesc.setText(item.getPkgName());
            ImageLoading.loadLocalImages(R.drawable.ts_ic_category_apk, imgIcon);

            root.setOnClickListener(view1 -> {
                if (itemClickCallback != null) {
                    itemClickCallback.onItemClick(position, item);
                }
            });
            root.setOnLongClickListener(v -> {
                if (itemClickCallback != null) {
                    itemClickCallback.onItemSelected(position, item);
                    return true;
                }
                return false;
            });

            btnMore.setOnClickListener(v -> {
                if (itemClickCallback != null) {
                    itemClickCallback.onItemSelected(position, item);
                }
            });
        }
    }

}