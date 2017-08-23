package com.project.debs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class LuasNeto extends AppCompatActivity {

    Intent intent;
    String Tw;
    String Aa;
    String Fu;
    String Fy;

    private EditText edAg,edD,edD2,edNA,ednNB,edNC,edAnA,edAnB,edAnC,edAeA,edAeB,edAeC,edU1,edU2,edUC,edSB,edSC,edHasil;

    double diamet;
    String diam;
    double ag;
    double hitungAnA;
    double hitungAnB;
    double hitungAnC;
    double hitungAeA;
    double hitungAeB;
    double hitungAeC;
    double cariNilaiU;
    double cariNilaiS;

    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luas_neto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Luas Neto");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        edAg = (EditText) findViewById(R.id.ag);
        edD  = (EditText) findViewById(R.id.diameter);
        edD2  = (EditText) findViewById(R.id.hasilDiameter);
        edNA  = (EditText) findViewById(R.id.nA);
        ednNB = (EditText) findViewById(R.id.nB);
        edNC  = (EditText) findViewById(R.id.nC);
        edAnA = (EditText) findViewById(R.id.anA);
        edAnB = (EditText) findViewById(R.id.anB);
        edAnC = (EditText) findViewById(R.id.anC);
        edAeA = (EditText) findViewById(R.id.aeA);
        edAeB = (EditText) findViewById(R.id.aeB);
        edAeC = (EditText) findViewById(R.id.aeC);
        edU1  = (EditText) findViewById(R.id.UB);
        edUC  = (EditText) findViewById(R.id.UC);
        edSB  = (EditText) findViewById(R.id.SB);
        edSC  = (EditText) findViewById(R.id.SC);
        edHasil = (EditText) findViewById(R.id.hasil);

        intent = getIntent();
        Tw = intent.getStringExtra("tw");
        Aa = intent.getStringExtra("aa");
        Fu = intent.getStringExtra("fu");
        Fy = intent.getStringExtra("fy");

        ag = Double.parseDouble(Aa) * 100;
        edAg.setText(String.valueOf(df.format(ag)));
        edNA.setText("2");
        ednNB.setText("3");
        edNC.setText("2");
        hitung();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(LuasNeto.this, BatangTarik.class);
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

    private void hitung(){
        //Hitung A
        edD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //DecimalFormat df = new DecimalFormat("#");
                String n = edNA.getText().toString();
                if(!edD.getText().toString().equals("")){
                    diam = edD.getText().toString();
                    diamet = Double.parseDouble(diam) + 2;
                    edD2.setText(String.valueOf(df.format(diamet)));

                    hitungAnA = ag - Double.parseDouble(n) * diamet * Double.parseDouble(Tw);
                    edAnA.setText(String.valueOf(df.format(hitungAnA)));

                    hitungAeA = 0.85 * hitungAnA;
                    edAeA.setText(String.valueOf(df.format(hitungAeA)));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Hitung B
        edSB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //DecimalFormat df = new DecimalFormat("#");
                String U = edU1.getText().toString();
                String n = ednNB.getText().toString();
                String nC = edNC.getText().toString();
                String ss = edSB.getText().toString();
                if(!edNA.getText().toString().equals("")){

                    //Nilai B
                    cariNilaiS = Double.parseDouble(ss) * Double.parseDouble(ss);
                    cariNilaiU = 4 * Double.parseDouble(U);

                    hitungAnB  = ag - Double.parseDouble(n) * diamet * Double.parseDouble(Tw) +
                                 ((cariNilaiS/cariNilaiU)*Double.parseDouble(Tw)) + ((cariNilaiS/cariNilaiU)*Double.parseDouble(Tw));
                    edAnB.setText(String.valueOf(df.format(hitungAnB)));

                    hitungAeB  = 0.85 * hitungAnB;
                    edAeB.setText(String.valueOf(df.format(hitungAeB)));

                    hitungAnC  = ag - Double.parseDouble(nC) * diamet * Double.parseDouble(Tw) +
                            ((cariNilaiS/cariNilaiU)*Double.parseDouble(Tw));
                    edAnC.setText(String.valueOf(df.format(hitungAnC)));

                    hitungAeC  = 0.85 * hitungAnC;
                    edAeC.setText(String.valueOf(df.format(hitungAeC)));
                    edUC.setText(U);
                    edSC.setText(ss);

                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new Double(hitungAnA));
                    arrayList.add(new Double(hitungAnB));
                    arrayList.add(new Double(hitungAnC));

                    Object hasil = Collections.min(arrayList);
                    edHasil.setText(String.valueOf(df.format(hasil)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
