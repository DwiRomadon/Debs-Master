package com.project.debs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class TipeFriksi extends AppCompatActivity {

    TextView txtDeskripsi;
    private EditText jumlahBidangGeser;
    private EditText koefisienGesek;
    private EditText jumlahbaut;
    private EditText rn;
    private EditText rnt;
    private EditText rnt2;

    double ketRntFriksi;
    double ketRnTTipeTumpu;
    double gayaTarik;
    private View v;

    private Button btnHitung;

    ProgressDialog pDialog;

    private Spinner diameterBautSpn;

    private ArrayList<String> spnDiameterBaut;

    //JSON Array
    private JSONArray result;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipe_friksi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tipe Friksi");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        txtDeskripsi = (TextView)findViewById(R.id.deskripsi);

        diameterBautSpn = (Spinner) findViewById(R.id.spnDiametBaut);
        jumlahBidangGeser = (EditText) findViewById(R.id.jmlhBidangGeser);
        koefisienGesek    = (EditText) findViewById(R.id.koefisienGesek);
        jumlahbaut        = (EditText) findViewById(R.id.jmlhBaut);
        rn                = (EditText) findViewById(R.id.rn);
        rnt               = (EditText) findViewById(R.id.rnt);
        rnt2              = (EditText) findViewById(R.id.editHasil);

        koefisienGesek.setText("0.35 (SNI 03-1729-2002)");

        btnHitung       = (Button) findViewById(R.id.btnHitung);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        spnDiameterBaut = new ArrayList<String>();

        getDataDiameter();
        select();
        hitung();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(TipeFriksi.this, SambunganBautMenu.class);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void getRntTipeTumpu(){
        //Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();
        StringRequest strReqq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(getApplicationContext()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        ketRnTTipeTumpu = jObj.getDouble("Rnt");
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
                params.put("tag","selectRnt");
                return params;
            }
        };


        strReqq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReqq,tag_string_req);
    }

    private void getDataDiameter(){

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
                            getDataDiameter(result);
                            getRntTipeTumpu();
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
                params.put("tag", "getDiameterBaut");
                return params;
            }
        };

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getDataDiameter(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                spnDiameterBaut.add(json.getString("diameter_baut"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        diameterBautSpn.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spnDiameterBaut));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnDiameterBaut);
        diameterBautSpn.setAdapter(mAdapter);
    }

    public void getData(final String diameter){
        //Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(getApplicationContext()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        gayaTarik       = jObj.getDouble("GayaTarik");
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
                params.put("tag","selectDiameterBaut");
                params.put("diameterBaut", diameter);
                return params;
            }
        };


        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }

    private void select(){

        diameterBautSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                // TODO Auto-generated method stub
                String diameterBaut=diameterBautSpn.getSelectedItem().toString();
                Log.e("Selected item : ",diameterBaut);
                getData(diameterBaut);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void hitung(){

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DecimalFormat df = new DecimalFormat("#.##");
                String m = jumlahBidangGeser.getText().toString();
                String koefisien = koefisienGesek.getText().toString();
                String n1        = jumlahbaut.getText().toString();

                if(m.trim().length() > 0 && koefisien.trim().length() > 0 && n1.trim().length() > 0){

                    double rnn = 1 * (1.13 * Double.parseDouble(m) * 0.35 * gayaTarik);
                    rn.setText(String.valueOf(df.format(rnn)));

                    double ketRnt = Double.parseDouble(n1) * rnn;
                    rnt.setText(String.valueOf(df.format(ketRnt)));
                    double fixRnt = ketRnt / 10;
                    rnt2.setText(String.valueOf(df.format(fixRnt)));

                    txtDeskripsi.setText(Html.fromHtml("Jika Beban "+df.format(ketRnt)+" kN maka FRIKSI selip," +
                            "dan jika beban dibawah " +df.format(ketRnt)+ " kN maka sambungan tidak terjadi SELIP."));
                }else {
                    Toast.makeText(getApplicationContext(), " Data Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
