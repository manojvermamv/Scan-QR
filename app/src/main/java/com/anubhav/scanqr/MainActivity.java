package com.anubhav.scanqr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

import com.anubhav.commonutility.CustomToast;
import com.anubhav.commonutility.customfont.FontUtils;
import com.anubhav.scanqr.databinding.ActMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.manoj.github.permissions.PermissionHandler;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends BaseActivity<ActMainBinding> implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Bitmap qrBitmap;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).finishAffinity();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.act_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GlobalData.setStatusBarFullScreen(this);
        //GlobalData.setLightStatusBar(this);

        onCreateAdminApp();
        onResumeAdminApp();
    }

    private void onCreateAdminApp() {
        // action bar initialization
        initActionBarMain();

        binding.navView.setNavigationItemSelectedListener(this);
    }

    public void initActionBarMain() {
        binding.layActionbar.imgMenu.setOnClickListener(view -> {
            binding.rootView.openDrawer(GravityCompat.START);
        });

        binding.layActionbar.imgScanqr.setOnClickListener(v -> {
            QrScanActivity.start(context);
        });
    }

    public void onResumeAdminApp() {
        //UpdatePasswordActivity.startActivity(MainActivity.this, UpdatePasswordActivity.EXTRA_SET_NEW_PASSWORD, 121);

        View navigationHeader = binding.navView.getHeaderView(0);
        FontUtils.setFont(context, (ViewGroup) navigationHeader);

        binding.edtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.tilEdtext.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.btnGenerate.setSelected(true);
        binding.btnShare.setSelected(true);

        binding.btnGenerate.setOnClickListener(v -> GenerateQrProcess());
        binding.btnShare.setOnClickListener(v -> ShareQrProcess());

        SetQrToImageView(null);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_other_apps) {

        } else if (id == R.id.nav_rate_app) {

        } else if (id == R.id.nav_about) {

        }
        binding.rootView.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.rootView.isDrawerOpen(GravityCompat.START)) {
            binding.rootView.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121) {
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * qr generate process by user
     */
    private void GenerateQrProcess() {
        if (TextUtils.isEmpty(binding.edtext.getText())) {
            binding.tilEdtext.setError("Please enter a value");

        } else {
            String inputValue = binding.edtext.getText().toString().trim();

            checkPermissions(new PermissionHandler() {
                @Override
                public void onGranted(ArrayList<String> grantedPermissions) {
                    GenerateQr(grantedPermissions, inputValue);
                }

                @Override
                public void onDenied(Context context, ArrayList<String> deniedPermissions, ArrayList<String> grantedPermissions) {
                    GenerateQr(grantedPermissions, inputValue);
                }
            });
        }
    }

    private void GenerateQr(ArrayList<String> grantedPermissions, String inputValue) {
        if (grantedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && grantedPermissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
            QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, getSmallerDimen());
            qrgEncoder.setColorBlack(Color.BLACK);
            qrgEncoder.setColorWhite(Color.TRANSPARENT);
            try {
                // Getting QR-Code as Bitmap
                Bitmap bitmap = qrgEncoder.getBitmap();

                // Setting Bitmap to ImageView
                SetQrToImageView(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                SetQrToImageView(null);
            }
        }
    }

    private void SetQrToImageView(Bitmap bitmap) {
        qrBitmap = bitmap;
        if (qrBitmap == null) {
            binding.qrImage.setVisibility(View.GONE);
            binding.qrText.setVisibility(View.VISIBLE);

        } else {
            binding.qrText.setVisibility(View.GONE);
            binding.qrImage.setVisibility(View.VISIBLE);
            binding.qrImage.setImageBitmap(qrBitmap);
        }
    }

    /**
     * qr share process by user
     */
    private void ShareQrProcess() {
        if (qrBitmap == null) {
            customToast.showToast("First generate qr to share!", CustomToast.ToastyError);
            return;
        }

        // Save with location, value, bitmap returned and type of Image(JPG/PNG).
        QRGSaver qrgSaver = new QRGSaver();
        //qrgSaver.save(savePath, edtValue.getText().toString().trim(), qrBitmap, QRGContents.ImageType.IMAGE_JPEG);
    }


    private int getSmallerDimen() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;
        return dimen;
    }

}