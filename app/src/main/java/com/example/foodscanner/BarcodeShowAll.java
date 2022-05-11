package com.example.foodscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class BarcodeShowAll extends AppCompatActivity {

    private RecyclerView barcodeRecView;
    private BarcodeAdapter barcodeAdapter;
    private DataBaseHelper dataBH = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_show_all);

        init();

        setBarcodeAdapter();
    }

    private void init() {

        barcodeRecView = findViewById(R.id.barcodeRecView);

    }

    private void setBarcodeAdapter() {

        barcodeAdapter = new BarcodeAdapter(this);
        barcodeRecView.setAdapter(barcodeAdapter);
        barcodeRecView.setLayoutManager(new LinearLayoutManager(this));
        barcodeAdapter.setBarcodeLists(dataBH.barcodeLists());

    }

}