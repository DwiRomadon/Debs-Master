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

public class KuatRencanaTarik extends AppCompatActivity {

    private Spinner mutuBautSpn;
    private Spinner diameterBautSpn;

    private EditText koefisienGesek;
    private EditText jmlhBaut;
    private Button btnHitung;
    private EditText nu;
    //JSON Array
    private JSONArray result;

    ProgressDialog pDialog;

    private EditText rn;
    private EditText rnt;
    private EditText hasil;
    private EditText rnt2;

    private ArrayList<String> spnDiameterBaut;
    private ArrayList<String> spnMutuBaut;

    TextView kuat,tdkKuat;

    double mutuBaut;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuat_rencana_tarik);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tahanan Tarik Baut");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        mutuBautSpn = (Spinner)findViewById(R.id.spnMutuBaut);
        diameterBautSpn = (Spinner)findViewById(R.id.spnDiametBaut);
        koefisienGesek = (EditText)findViewById(R.id.koefisienGesek);
        jmlhBaut        = (EditText)findViewById(R.id.jmlhBaut);
        rn              = (EditText)findViewById(R.id.rn);rn.setEnabled(false);
        rnt             = (EditText)findViewById(R.id.rnt);rnt.setEnabled(false);
        hasil           = (EditText) findViewById(R.id.editHasil);hasil.setEnabled(false);
        nu              = (EditText)findViewById(R.id.nu);
        rnt2            = (EditText)findViewById(R.id.rnt2);rnt2.setEnabled(false);

        kuat            = (TextView) findViewById(R.id.txtKuat);kuat.setVisibility(View.INVISIBLE);
        tdkKuat         = (TextView) findViewById(R.id.txtTdkKuat) ;tdkKuat.setVisibility(View.INVISIBLE);

        btnHitung     = (Button)findViewById(R.id.btnHitung);

        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.mutuBaut, R.layout.spinner_item);
        //mutuBautSpn.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        spnDiameterBaut = new ArrayList<String>();
        spnMutuBaut     = new ArrayList<String>();

        getDataDiameterBaut();
        hitung();
        select();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(KuatRencanaTarik.this, SambunganBautMenu.class);
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

    private void getDataDiameterBaut(){

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
                            getDataDiameterBaut(result);
                            getDataMutuBaut();
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

    private void getDataDiameterBaut(JSONArray j){
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

    private void getDataMutuBaut(){

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
                            getDataMutuBaut(result);
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
                params.put("tag", "getAllMutuBaut");
                return params;
            }
        };

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getDataMutuBaut(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                spnMutuBaut.add(json.getString("Jenis_Baut"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        mutuBautSpn.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spnMutuBaut));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnMutuBaut);
        mutuBautSpn.setAdapter(mAdapter);
    }

    private void getMutuBaut(final String jenisBaut){
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
                        mutuBaut       = jObj.getDouble("Kekuatan");
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
                params.put("tag","selectMutuBaut");
                params.put("jenisbaut", jenisBaut);
                return params;
            }
        };


        strReqq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReqq,tag_string_req);
    }

    private void select(){

        mutuBautSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String baut=mutuBautSpn.getSelectedItem().toString();
                Log.e("Selected item : ",baut);
                getMutuBaut(baut);
                //jumlahBidangGeser.setText(String.valueOf(baut));
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

                //String fub                  = mutuBautSpn.getSelectedItem().toString();
                String diameter             = diameterBautSpn.getSelectedItem().toString();
                String n1                   = jmlhBaut.getText().toString();
                String nuu                  = nu.getText().toString();

                if(diameter.trim().length() > 0 && n1.trim().length() > 0 && nuu.trim().length() > 0){
                    DecimalFormat df = new DecimalFormat("#.##");

                    double jariJariBaut = Double.parseDouble(diameter) / 2;
                    double ab           = (3.14) * (Math.pow(jariJariBaut,2));
                    double ketRn        = 0.75 *(mutuBaut * 0.75 * ab);
                    double fixRn        = ketRn / 1000;
                    rn.setText(String.valueOf(df.format(fixRn)));

                    double ketRnt = Double.parseDouble(n1) * fixRn;
                    //double fixRnt = ketRnt / 10;
                    rnt.setText(String.valueOf(df.format(ketRnt)));
                    rnt2.setText(String.valueOf(df.format(ketRnt)));

                    if (ketRnt > Double.parseDouble(nuu)){
                        //hasil.setText("Kuat");
                        kuat.setVisibility(View.VISIBLE);
                        tdkKuat.setVisibility(View.INVISIBLE);
                    }else if(ketRnt < Double.parseDouble(nuu)){
                        //hasil.setText("Tidak Kuat");
                        tdkKuat.setVisibility(View.VISIBLE);
                        kuat.setVisibility(View.INVISIBLE);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Data tidak boleh kosong !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}