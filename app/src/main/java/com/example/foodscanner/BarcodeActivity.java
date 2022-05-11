package com.example.foodscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.util.ArrayList;

public class BarcodeActivity extends AppCompatActivity {

    private final static String TAG = "CameraActivity";

    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private String barcodeNum;
    private BarcodeList barcodeLists;
    private DataBaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        init();

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        barcodeNum = result.getText();
                        if (getIntent().getStringExtra("from").equals("cameraButton")) {
                            Intent intent = new Intent(BarcodeActivity.this, ScanDateActivity.class);
                            intent.putExtra("barcodeNum", barcodeNum);
                            startActivity(intent);
                        }
                        else if (getIntent().getStringExtra("from").equals("barcodeButton")) {

                            barcodeLists = new BarcodeList(-1,"PlaceHolder","PlaceHolder",barcodeNum);

                            if (dbh.addBarcode(barcodeLists)) {
                                Intent intent = new Intent(BarcodeActivity.this, RegiBarcode.class);
                                intent.putExtra("barcodeNum", barcodeNum);
                                startActivity(intent);
                            } else {
                                Toast.makeText(BarcodeActivity.this, "Barcode Exist!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BarcodeActivity.this,Settings.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void init() {

        scannerView = findViewById(R.id.scanArea);
        dbh = new DataBaseHelper(this);

    }


}