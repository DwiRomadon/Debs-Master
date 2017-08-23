package com.project.debs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

public class BlokGeser extends AppCompatActivity {

    Intent intent;
    String Tw;
    String Aa;
    String Fu;
    String Fy;

    private EditText fu;
    private EditText fy;
    private EditText bidangGeser;
    private EditText bidangTarik;
    private EditText diameter;
    private EditText agvv;
    private EditText agtt;
    private EditText anvv;
    private EditText antt;
    private EditText hasil;
    private EditText hasilDiameter;

    private Button btnHitung;

    double diamet;
    String diam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blok_geser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Blok Geser");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        intent = getIntent();
        Tw = intent.getStringExtra("tw");
        Aa = intent.getStringExtra("aa");
        Fu = intent.getStringExtra("fu");
        Fy = intent.getStringExtra("fy");

        fu = (EditText) findViewById(R.id.fu);
        fy = (EditText) findViewById(R.id.fy);
        agtt = (EditText) findViewById(R.id.agt);
        anvv = (EditText) findViewById(R.id.anv);
        agvv = (EditText) findViewById(R.id.agv);
        antt = (EditText) findViewById(R.id.ant);
        hasil = (EditText) findViewById(R.id.hasil);
        hasilDiameter = (EditText) findViewById(R.id.hasilDiameter);

        btnHitung = (Button) findViewById(R.id.btnHitung);

        bidangGeser = (EditText) findViewById(R.id.bidangGeser);
        bidangTarik = (EditText) findViewById(R.id.bidangTarik);
        diameter    = (EditText) findViewById(R.id.diameter);

        getDiameter();
        hitung();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(BlokGeser.this, BatangTarik.class);
        startActivity(a);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDiameter(){
        //diamet = Double.parseDouble(diam) + 2;
        //hasilDiameter.setText(String.valueOf(diamet));
        /*if(diam.trim().length() > 0){
            diameter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    diamet = Double.parseDouble(diam) + 2;
                    hasilDiameter.setText(String.valueOf(diamet));
                }
            });
        }*/

        diameter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                // Check some Validations and Conditions here

                // Convert function goes here if all validationand conditions checked
                //
                DecimalFormat df = new DecimalFormat("#");
                if(!diameter.getText().toString().equals("")){
                    diam = diameter.getText().toString();
                    diamet = Double.parseDouble(diam) + 2;
                    hasilDiameter.setText(String.valueOf(df.format(diamet)));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void hitung(){
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DecimalFormat df = new DecimalFormat("#");
                double pasingFu = Double.parseDouble(Fu);
                double parsingFy= Double.parseDouble(Fy);
                fu.setText(String.valueOf(df.format(pasingFu)));
                fy.setText(String.valueOf(df.format(parsingFy)));
                String bdgGeser = bidangGeser.getText().toString();
                String bdgTarik = bidangTarik.getText().toString();
                diam     = hasilDiameter.getText().toString();

                if(bdgGeser.trim().length() > 0 && bdgTarik.trim().length() > 0 && diam.trim().length() > 0){
                    double agv =  Double.parseDouble(bdgGeser) * Double.parseDouble(Tw);
                    double agt = Double.parseDouble(bdgTarik)  * Double.parseDouble(Tw);
                    double anv = agv - 3.5 * Double.parseDouble(diam)  * Double.parseDouble(Tw);
                    double ant = agt - 0.5 * Double.parseDouble(diam)  * Double.parseDouble(Tw);

                    agvv.setText(String.valueOf(df.format(agv)));
                    agtt.setText(String.valueOf(df.format(agt)));
                    anvv.setText(String.valueOf(df.format(anv)));
                    antt.setText(String.valueOf(df.format(ant)));

                    double geserLeleh   = Double.parseDouble(Fu) * ant;
                    double tarikFraktur = 0.6 * Double.parseDouble(Fu) * anv;

                    double nn1           = 0.6 * Double.parseDouble(Fy) * agv + Double.parseDouble(Fu) * ant;
                    double ketNn;

                    if(geserLeleh > tarikFraktur){
                        ketNn = 0.75 * nn1;
                        hasil.setText(String.valueOf(df.format(ketNn)));
                    }else {
                        //double geserFraktur = Double.parseDouble(Fu) * ant;
                        //double tarikLeleh   = 0.6 * Double.parseDouble(Fu) * anv;
                        double nn2          = 0.6 * Double.parseDouble(Fy) * anv + Double.parseDouble(Fu) * agt;
                        ketNn = 0.75 * nn2;
                        hasil.setText(String.valueOf(df.format(ketNn)));
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Data Tidak Boloh Kosong !", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}