package com.project.debs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class InfoUmum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_umum);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Info Umum");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        Button btnBalokLentur = (Button) findViewById(R.id.btnBalokLentur);
        btnBalokLentur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(InfoUmum.this, InfoBalokLentur.class);
                startActivity(a);
                finish();
            }
        });

        Button btnBatangtekan = (Button) findViewById(R.id.btnBatangTekan);
        btnBatangtekan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(InfoUmum.this, InfoBatangTekan.class);
                startActivity(a);
                finish();
            }
        });

        Button btnSambunganBaut = (Button) findViewById(R.id.btnSambunganBaut);
        btnSambunganBaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(InfoUmum.this, InfoSambunganBaut.class);
                startActivity(a);
                finish();
            }
        });

        Button btnBatangTarik = (Button) findViewById(R.id.btnBatangTarik);
        btnBatangTarik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(InfoUmum.this, InfoBatangTarik.class);
                startActivity(a);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(InfoUmum.this, Debs.class);
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
}
