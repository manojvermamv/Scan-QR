package com.anubhav.scanqr.activities;

import static com.anubhav.scanqr.utils.ViewUtils.hideKeyboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.anubhav.scanqr.MainActivity;
import com.anubhav.commonutility.CustomToast;
import com.anubhav.commonutility.customtheme.ThemeUtils;
import com.anubhav.scanqr.R;
import com.anubhav.scanqr.utils.GlobalData;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ViewGroup rootView;
    private ProgressBar progressBar;
    private CustomToast customToast;

    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        GlobalData.setStatusBarFullScreen(this);
        GlobalData.setLightStatusBar(this);

        StartApp();
        resumeApp();
    }

    private void StartApp() {
        context = this;
        customToast = new CustomToast(context);
        rootView = findViewById(R.id.root_view);
        ThemeUtils.setThemeFont(this, (ViewGroup) rootView);

        hideKeyboard(this);
        GlobalData.SetLanguage(context);
    }

    private void resumeApp() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.INVISIBLE);
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