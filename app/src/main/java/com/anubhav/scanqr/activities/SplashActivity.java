package com.anubhav.scanqr.activities;

import static com.anubhav.scanqr.utils.ViewUtils.hideKeyboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.anubhav.scanqr.MainActivity;
import com.anubhav.commonutility.CustomToast;
import com.anubhav.commonutility.customtheme.ThemeUtils;
import com.anubhav.scanqr.R;
import com.anubhav.scanqr.databinding.ActSplashBinding;
import com.anubhav.scanqr.utils.GlobalData;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private CustomToast customToast;
    private ActSplashBinding binding;

    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_splash);
        GlobalData.setStatusBarFullScreen(this);
        GlobalData.setLightStatusBar(this);

        StartApp();
        resumeApp();
    }

    private void StartApp() {
        context = this;
        customToast = new CustomToast(context);
        ThemeUtils.setThemeFont(this, (ViewGroup) binding.rootView);

        hideKeyboard(this);
        GlobalData.SetLanguage(context);
    }

    private void resumeApp() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.tvName.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));

        new Handler().postDelayed(() -> {
            binding.progressbar.setVisibility(View.INVISIBLE);
            MainActivity.start(context);
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}