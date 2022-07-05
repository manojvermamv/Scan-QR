package com.anubhav.scanqr.base;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.manoj.github.customlistadapter.CustomListAdapter;
import com.anubhav.scanqr.R;

public class NullViewHolder<T extends CustomListAdapter.ViewModel> extends CustomListAdapter.ViewHolder<T> {

    public TextView tvNotFound;
    public LottieAnimationView animationView;

    public NullViewHolder(@NonNull View itemView) {
        super(itemView);
        Log.e("PhoneContactAdapter", "NullViewHolder");
        tvNotFound = itemView.findViewById(R.id.not_found_textView);
        animationView = itemView.findViewById(R.id.not_found_animation);

        tvNotFound.setText(R.string.no_results);
        animationView.setAnimation(R.raw.search_not_found);
        animationView.setOnClickListener(v -> animationView.playAnimation());
    }

    @Override
    protected void performBind(@NonNull T item) {
    }

}