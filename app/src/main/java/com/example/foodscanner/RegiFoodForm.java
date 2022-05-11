package com.example.foodscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class RegiFoodForm extends AppCompatActivity {

    private static final String TAG = "RegiFoodForm";

    private EditText regiBrand, regiName, regiBarNum, addSubNum, regiDateScan;
    private Button submitBut;
    private DataBaseHelper dbh = new DataBaseHelper(this);
    private AddList addLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi_food_form);

        init();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            regiBarNum.setText(extras.getString("barcodeNum"));
            regiDateScan.setText(extras.getString("dateScanned"));
        }

        ArrayList<NameBrandList> findBarName = dbh.findBarcode(regiBarNum.getText().toString());
        if (!findBarName.isEmpty()) {
            regiBrand.setText(findBarName.get(0).getBrand());
            regiName.setText(findBarName.get(0).getName());
        }

        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addLists = new AddList(regiBrand.getText().toString(),regiName.getText().toString()
                        ,regiDateScan.getText().toString()
                        ,addSubNum.getText().toString()
                        ,regiBarNum.getText().toString());

                if (dbh.addOne(addLists)) {
                    Toast.makeText(RegiFoodForm.this, "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegiFoodForm.this, "Error adding", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(RegiFoodForm.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init() {

        regiBrand = findViewById(R.id.regiBrand);
        regiName = findViewById(R.id.regiName);
        regiBarNum = findViewById(R.id.regiNum);
        addSubNum = findViewById(R.id.addSubNum);
        regiDateScan = findViewById(R.id.regiDateScan);
        submitBut = findViewById(R.id.submitBut);

    }

}