package com.example.foodscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton, settingsButton;
    private RecyclerView listRecView;
    private ListAdapter listAdapter;
    private DataBaseHelper dataBaseHelper;

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This is to ask for permission on camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // here, Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }

        dataBaseHelper = new DataBaseHelper(this);

        init();
        setListAdapter();

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                intent.putExtra("from","cameraButton");
                startActivity(intent);
            }

        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
            }
        });

    }

    private void init() {

        listRecView = findViewById(R.id.listRecView);
        cameraButton = findViewById(R.id.cameraButton);
        settingsButton = findViewById(R.id.SettingsButton);

    }

    private void setListAdapter() {

        listAdapter = new ListAdapter(this);
        listRecView.setAdapter(listAdapter);
        listRecView.setLayoutManager(new StaggeredGridLayoutManager(1,1));
        listAdapter.setInfoList(dataBaseHelper.mainfoodlist());

    }

    public void onBackPressed() {
        finishAffinity();
    }

}