package com.anubhav.scanqr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anubhav.scanqr.activities.ActivityBrowseGallery;

import com.anubhav.scanqr.databinding.ActQrScanBinding;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class QrScanActivity extends BaseActivity<ActQrScanBinding> implements View.OnClickListener {

    private static final String TAG = QrScanActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent intent = new Intent(context, QrScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.act_qr_scan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GlobalData.setStatusBarFullScreen(this);
        //GlobalData.setLightStatusBar(this);

        onCreateAdminApp();
        onResumeAdminApp();
    }

    private CodeScanner mCodeScanner;

    private void onCreateAdminApp() {
        // action bar initialization
        binding.imgClose.setOnClickListener(view -> finish());

    }

    public void onResumeAdminApp() {
        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customToast.showToast(result.getText());
                    }
                });
            }
        });

        binding.browseQrcode.setOnClickListener(view -> {
            ActivityBrowseGallery.OpenBrowseActivity(context);
            //mCodeScanner = ActivityBrowseProfileImage.imageUri;
            //mCodeScanner.setImageURI(null);
            //mCodeScanner.setImageURI(ActivityBrowseProfileImage.imageUri);
        });
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
        if (ActivityBrowseGallery.imageUri != null) {
            Result scanText = GetBarcodeText(ActivityBrowseGallery.imageUri);
            customToast.showToast(scanText.getText());

            ActivityBrowseGallery.imageUri = null;
        }
        mCodeScanner.startPreview();
    }

    protected Result GetBarcodeText(Uri imgUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imgUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                Log.e(TAG, "uri is not a bitmap," + imgUri.toString());
                return null;
            }
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.recycle();
            bitmap = null;
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();
            try {
                return reader.decode(bBitmap);
            } catch (NotFoundException e) {
                Log.e(TAG, "decode exception", e);
                return null;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "can not open file" + imgUri.toString(), e);
            return null;
        }
    }

}