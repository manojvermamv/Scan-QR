package com.anubhav.scanqr.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.anubhav.commonutility.MyCache;
import com.manoj.github.customlistadapter.CustomListAdapter;
import com.anubhav.scanqr.BaseActivity;
import com.anubhav.scanqr.R;
import com.anubhav.scanqr.databinding.ActadminNewBinding;
import com.anubhav.scanqr.databinding.IncludeActionbarNormalBinding;

public class ActivityNew extends BaseActivity<ActadminNewBinding> implements View.OnClickListener, CustomListAdapter.Callback {

    private String actTitle = "New Activity";
    private IncludeActionbarNormalBinding layActionBar;
    private static final String TAG = ActivityNew.class.getSimpleName();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.actadmin_new;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("actTitle")) {
            actTitle = getIntent().getStringExtra("actTitle");
        }

//        setWaveAnimation(binding.layMultiwave.waveHeader);
        initActionBarMain();
        onResumeApp();

    }

    public void initActionBarMain() {
        // action bar initialization
        layActionBar = binding.layActionbar;

        layActionBar.setActionBarTitle(actTitle);
        layActionBar.imgBack.setOnClickListener(view -> {
            hideKeyboard();
            finish();
        });

        layActionBar.setMenuVisible(true);
        layActionBar.btnMenu.setOnClickListener(view -> {
        });

        Uri clientProfileUri = MyCache.getInstance().getUriByFileName("IMG_CLIENT_DEVICE_PROFILE", context);
        //layActionBar.imgBack.setImageURI(clientProfileUri);

    }

    public void onResumeApp() {

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditFinished() {

    }

}