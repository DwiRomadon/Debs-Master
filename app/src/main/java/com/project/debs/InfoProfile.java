package com.project.debs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import server.AppController;
import server.Config_URL;

public class InfoProfile extends AppCompatActivity implements Spinner.OnItemSelectedListener,  View.OnClickListener{

    double   ht,   bf, tw,  tf,  rr,  h1,   h2,   aa,   uw,   geomatricalIx,geomatricalIy, radiusIx, radiusIy, sx,    sy,  zx,    x1,   x2;

    EditText edHt,edBf,edTw,edTf,edRr,edH1, edH2, edAa, edUw,  edGIx,        edGIy,         edRIx,    edRIy,   edSx,  edSy, edZx, edX1, edX2;

    private Spinner profileSpn;
    private ArrayList<String> spnProfile;
    ProgressDialog pDialog;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    //JSON Array
    private JSONArray result;

    DecimalFormat df = new DecimalFormat("#.######");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Info Profile");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        edHt    = (EditText) findViewById(R.id.ht);
        edBf    = (EditText) findViewById(R.id.bf);
        edTw    = (EditText) findViewById(R.id.tw);
        edTf    = (EditText) findViewById(R.id.tf);
        edRr    = (EditText) findViewById(R.id.r);
        edH1    = (EditText) findViewById(R.id.h1);
        edH2    = (EditText) findViewById(R.id.h2);
        edAa    = (EditText) findViewById(R.id.a);
        edUw    = (EditText) findViewById(R.id.unitweight);
        edGIx   = (EditText) findViewById(R.id.ix);
        edGIy   = (EditText) findViewById(R.id.iy);
        edRIx   = (EditText) findViewById(R.id.ixRadius);
        edRIy   = (EditText) findViewById(R.id.iyRadius);
        edSx    = (EditText) findViewById(R.id.sx);
        edSy    = (EditText) findViewById(R.id.sy);
        edZx    = (EditText) findViewById(R.id.zx);
        edX1    = (EditText) findViewById(R.id.x1);
        edX2    = (EditText) findViewById(R.id.x2);

        profileSpn = (Spinner) findViewById(R.id.spnProfile);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        spnProfile = new ArrayList<String>();
        profileSpn.setOnItemSelectedListener(this);

        getDataProfile();
        select();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(InfoProfile.this, Debs.class);
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void getDataProfile(){

        String tag_string_req = "req_";
        pDialog.setMessage("Loading.....");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config_URL.base_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("result");

                            //Calling method getStudents to get the students from the JSON Array
                            getDataProfile(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(String.valueOf(getApplicationContext()), "Login Error : " + error.getMessage());
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getProfile");
                return params;
            }
        };

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getDataProfile(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                spnProfile.add(json.getString("Dimensional"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        profileSpn.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spnProfile));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnProfile);
        profileSpn.setAdapter(mAdapter);
    }

    /*
      Function Get Code
    */
    public void getNilaiProfile(final String dimensi){
        //Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(getApplication()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        bf              = jObj.getDouble("Bf");edBf.setText(String.valueOf(bf));
                        tf              = jObj.getDouble("Tf");edTf.setText(String.valueOf(tf));
                        h2              = jObj.getDouble("H2");edH2.setText(String.valueOf(h2));
                        tw              = jObj.getDouble("Tw");edTw.setText(String.valueOf(tw));
                        zx              = jObj.getDouble("Zx");edZx.setText(String.valueOf(zx));
                        x1              = jObj.getDouble("X1");edX1.setText(String.valueOf(x1));
                        x2              = jObj.getDouble("X2");edX2.setText(String.valueOf(df.format(x2)));
                        sx              = jObj.getDouble("SX");edSx.setText(String.valueOf(sx));
                        rr              = jObj.getDouble("R");edRr.setText(String.valueOf(rr));
                        ht              = jObj.getDouble("Ht");edHt.setText(String.valueOf(ht));
                        h1              = jObj.getDouble("H1");edH1.setText(String.valueOf(h1));
                        aa              = jObj.getDouble("A");edAa.setText(String.valueOf(aa));
                        uw              = jObj.getDouble("Unit_Weight");edUw.setText(String.valueOf(uw));
                        geomatricalIx   = jObj.getDouble("Geomatrical_IX");edGIx.setText(String.valueOf(geomatricalIx));
                        geomatricalIy   = jObj.getDouble("Geomatrical_IY");edGIy.setText(String.valueOf(geomatricalIy));
                        radiusIx        = jObj.getDouble("Radius_IX");edRIx.setText(String.valueOf(radiusIx));
                        radiusIy        = jObj.getDouble("Radius_IY");edRIy.setText(String.valueOf(radiusIy));
                        sy              = jObj.getDouble("SY");edSy.setText(String.valueOf(sy));


                    }else {
                        String error_msg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error_msg, Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Login Error : " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","selectInfoProfil");
                params.put("dimensional", dimensi);
                return params;
            }
        };


        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }

    private void select(){

        profileSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                // TODO Auto-generated method stub
                String profile=profileSpn.getSelectedItem().toString();
                Log.e("Selected item : ",profile);
                getNilaiProfile(profile);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

}
