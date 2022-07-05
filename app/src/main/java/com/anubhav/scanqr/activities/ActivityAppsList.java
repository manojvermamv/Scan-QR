package com.anubhav.scanqr.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import com.anubhav.scanqr.utils.Global;
import com.google.android.material.transition.MaterialContainerTransform;
import com.manoj.github.customlistadapter.CustomListAdapter;
import com.anubhav.scanqr.BaseActivity;
import com.anubhav.scanqr.R;
import com.anubhav.scanqr.adapters.InstalledAppsAdapter;
import com.anubhav.scanqr.database.common.InstalledAppsModel;
import com.anubhav.scanqr.databinding.ActadminAppsListBinding;
import com.anubhav.scanqr.databinding.BsSearchBinding;
import com.anubhav.scanqr.databinding.IncludeActionbarNormalBinding;
import com.anubhav.scanqr.interfaces.ItemClickListener;
import com.anubhav.scanqr.utils.ViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ActivityAppsList extends BaseActivity<ActadminAppsListBinding> implements View.OnClickListener, CustomListAdapter.Callback {

    private String actTitle = "";
    private ViewGroup rootView;
    private IncludeActionbarNormalBinding layActionBar;
    private static final String TAG = ActivityAppsList.class.getSimpleName();

    private String mQuery = "";
    private InstalledAppsAdapter appsAdapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.actadmin_apps_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBusEventEnabled(true);
        if (getIntent().hasExtra("actTitle")) {
            actTitle = getIntent().getStringExtra("actTitle");
        }

        //setWaveAnimation(binding.layMultiwave.waveHeader);
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

    }

    public void onResumeApp() {
        rootView = binding.rootView;
        binding.btnRefresh.setOnClickListener(v -> {
            LoadData();
        });

        loadRecyclerView(null);

        initBottomSearch();
        binding.btnSearch.setOnClickListener(v -> {
            showEndView(binding.btnSearch, binding.layBsSearch.searchRoot);
        });
    }

    @Override
    public void onBackPressed() {
        if (searchBinding.searchRoot.getVisibility() == View.VISIBLE) {
            showStartView(searchBinding.searchRoot, binding.btnSearch);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<InstalledAppsModel> result) {
        //appDatabase.installedAppsDao().insertAppsList(result);
        loadRecyclerView(result);
    }

    @Override
    public void onEditStarted() {
        //progressDialog.show();
    }

    @Override
    public void onEditFinished() {
        //CustomProgress.hide(progressDialog);
    }

    public void LoadData() {

    }

    public void loadRecyclerView(List<InstalledAppsModel> list) {
        if (list == null) list = new ArrayList<>();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        appsAdapter = new InstalledAppsAdapter(context, list);
        binding.recyclerView.setAdapter(appsAdapter);

        appsAdapter.setItemCallback(new ItemClickListener<InstalledAppsModel>() {
            @Override
            public void onItemClick(int position, InstalledAppsModel item) {
                item.prettyPrint();
            }

            @Override
            public void onItemLongClick(int position, InstalledAppsModel item) {

            }

            @Override
            public void onItemSelected(int position, InstalledAppsModel item) {

            }

            @Override
            public void onItemsSelected(List<InstalledAppsModel> selectedItems) {

            }

            @Override
            public void onItemSelectionChanged(int position, InstalledAppsModel item) {

            }
        });
    }

    private BsSearchBinding searchBinding;

    private void initBottomSearch() {
        searchBinding = binding.layBsSearch;

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        searchBinding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchBinding.searchView.setMaxWidth(Integer.MAX_VALUE);
        searchBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mQuery = query;
                onBackPressed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mQuery = query;
                appsAdapter.getFilter().filter(mQuery);
                return true;
            }
        });

        searchBinding.searchView.setQuery(mQuery, true);
        searchBinding.btnCheck.setOnClickListener(v -> onBackPressed());
    }

    private void showEndView(View startView, View endView) {
        // Construct a container transform transition between two views.
        MaterialContainerTransform transition = buildContainerTransform(true);
        transition.setStartView(startView);
        transition.setEndView(endView);

        // Add a single target to stop the container transform from running on both the start and end view.
        transition.addTarget(endView);
        transition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                searchBinding.searchView.requestFocus();
                ViewUtils.showKeyboard(context);
            }
        });

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(rootView, transition);
        startView.setVisibility(View.INVISIBLE);
        endView.setVisibility(View.VISIBLE);
    }

    private void showStartView(View endView, View startView) {
        // Construct a container transform transition between two views.
        MaterialContainerTransform transition = buildContainerTransform(false);
        transition.setStartView(endView);
        transition.setEndView(startView);

        // Add a single target to stop the container transform from running on both the start
        transition.addTarget(startView);
        transition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
            }
        });

        // Trigger the container transform transition.
        TransitionManager.beginDelayedTransition(rootView, transition);
        startView.setVisibility(View.VISIBLE);
        endView.setVisibility(View.INVISIBLE);

        appsAdapter.getFilter().filter(mQuery);
    }

    @NonNull
    private MaterialContainerTransform buildContainerTransform(boolean entering) {
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setTransitionDirection(entering ? MaterialContainerTransform.TRANSITION_DIRECTION_ENTER : MaterialContainerTransform.TRANSITION_DIRECTION_RETURN);
        transform.setDrawingViewId(rootView.getId());
        transform.setDuration(Global.CONTAINER_ANIMATION_DURATION);
        return transform;
    }

}