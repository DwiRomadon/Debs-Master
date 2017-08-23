package com.project.debs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Terminator on 13/08/2017.
 */

public class About extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(About.this, Debs.class);
        startActivity(a);
        finish();
    }
}
