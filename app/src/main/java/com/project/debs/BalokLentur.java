package com.project.debs;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class BalokLentur extends AppCompatActivity {

    private Button btnLenturBalok;
    private Button btnBalokGeser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balok_lentur);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Balok Lentur");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        btnLenturBalok = (Button)findViewById(R.id.btnLenturBalok);
        btnBalokGeser  = (Button)findViewById(R.id.btnBalokGeser);

        btnLenturBalok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(BalokLentur.this, LenturBalok.class);
                startActivity(a);
                finish();
            }
        });

        btnBalokGeser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(BalokLentur.this, BalokGeser.class);
                startActivity(a);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(BalokLentur.this, Debs.class);
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
