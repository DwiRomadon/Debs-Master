package com.project.debs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

public class Debs extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    private ImageButton btnBatangTarik;
    private ImageButton btnBalokLentur;
    private ImageButton btnBatangTekan;
    private ImageButton btnSambaut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debs);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer menu
        drawerMenu();

        btnBatangTarik = (ImageButton) findViewById(R.id.btnImgTarik);
        btnBalokLentur = (ImageButton) findViewById(R.id.btnImgBlkLentur);
        btnBatangTekan = (ImageButton) findViewById(R.id.btnImgBatangTekan);
        btnSambaut     = (ImageButton) findViewById(R.id.btnImgSamBaut);

        //Batang tarik
        btnBatangTarik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Debs.this, BatangTarik.class);
                startActivity(a);
                finish();
            }
        });

        //Balok Lentur
        btnBalokLentur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Debs.this, BalokLentur.class);
                startActivity(a);
                finish();
            }
        });

        //Batang Tekan
        btnBatangTekan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Debs.this, BatangTekan.class);
                startActivity(a);
                finish();
            }
        });

        //Sambungan baut
        btnSambaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(Debs.this, SambunganBautMenu.class);
                startActivity(a);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.debs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
            return true;
        //}

        //return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Intent a = new Intent(Debs.this, InfoProfile.class);
            startActivity(a);
            finish();
        } else if (id == R.id.nav_info_umum) {
            Intent a = new Intent(Debs.this, InfoUmum.class);
            startActivity(a);
            finish();
        } else if (id == R.id.nav_help) {
            Intent a = new Intent(Debs.this, Help.class);
            startActivity(a);
            finish();
        } else if (id == R.id.nav_about) {
            Intent a = new Intent(Debs.this, About.class);
            startActivity(a);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void drawerMenu(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}
