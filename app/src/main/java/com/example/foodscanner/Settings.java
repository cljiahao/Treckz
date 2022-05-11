package com.example.foodscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    private RecyclerView settingsRecView;
    private SettingsAdapter settingsAdapter;
    private ArrayList<SettingList> settingLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        setSettingAdapter();

    }

    public void onBackPressed() {
        Intent intent = new Intent(Settings.this,MainActivity.class);
        startActivity(intent);

    }

    private void init() {

        settingsRecView = findViewById(R.id.settingRecView);

        settingLists.add(new SettingList("Filter",R.drawable.ic_filter));
        settingLists.add(new SettingList("Scan Barcode",R.drawable.ic_qr_code_scanner));
        settingLists.add(new SettingList("Barcode List",R.drawable.ic_barcode));

    }

    private void setSettingAdapter() {

        settingsAdapter = new SettingsAdapter(this);
        settingsRecView.setAdapter(settingsAdapter);
        settingsRecView.setLayoutManager(new LinearLayoutManager(this));
        settingsAdapter.setSettingLists(settingLists);

    }

}