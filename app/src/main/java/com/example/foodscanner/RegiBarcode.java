package com.example.foodscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegiBarcode extends AppCompatActivity {

    private final static String TAG = "RegiBarcode";

    private EditText barcodeName,barcodeNum,barcodeBrand;
    private Button submitButton;
    private BarcodeList barcodeLists;
    private DataBaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi_barcode);

        init();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barcodeNum.setText(extras.getString("barcodeNum"));
        } else {
            barcodeNum.setText(0);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String brand = barcodeBrand.getText().toString();
                String name = barcodeName.getText().toString();
                String num = barcodeNum.getText().toString();

                barcodeLists = new BarcodeList(-1,brand,name,num);

                Log.d(TAG,barcodeLists.toString());

                if (!brand.isEmpty() && !name.isEmpty() && !num.isEmpty()) {
                    if (dbh.updateBarcode(barcodeLists)) {
                        Toast.makeText(RegiBarcode.this, "Successfully Added Barcode", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegiBarcode.this, "Barcode Exist!", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(RegiBarcode.this,Settings.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegiBarcode.this, "Missing information!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {

        barcodeBrand = findViewById(R.id.barBrand);
        barcodeName = findViewById(R.id.barName);
        barcodeNum = findViewById(R.id.barNum);
        submitButton = findViewById(R.id.submitBarBut);

        dbh = new DataBaseHelper(this);

    }
}