package com.project.debs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SambunganBautMenu extends AppCompatActivity {

    private Button btnTipeTumpu;
    private Button btnFriksi;
    private Button btnKuatRencanaTarik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sambungan_baut_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sambungan Baut");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        btnTipeTumpu = (Button)findViewById(R.id.btnTipeTumpu);
        btnFriksi    = (Button) findViewById(R.id.btnTipeFriksi);
        btnKuatRencanaTarik = (Button) findViewById(R.id.btnKuatRencaTarik);

        btnTipeTumpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(SambunganBautMenu.this,SambunganBautTipeTumpu.class);
                startActivity(a);
                finish();
            }
        });

        btnFriksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(SambunganBautMenu.this,TipeFriksi.class);
                startActivity(a);
                finish();
            }
        });

        btnKuatRencanaTarik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(SambunganBautMenu.this,KuatRencanaTarik.class);
                startActivity(a);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(SambunganBautMenu.this, Debs.class);
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
