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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import server.AppController;
import server.Config_URL;

public class BatangTekan extends AppCompatActivity implements Spinner.OnItemSelectedListener,  View.OnClickListener{

    private static final String TAG = LenturBalok.class.getSimpleName();

    //private Button btnHitungPelat;
    private Button btnHitung;
    ProgressDialog pDialog;

    private EditText bebanMati;
    private EditText bebanHidup;
    private EditText bebanAtap;
    private EditText bebanHujan;
    private EditText bebanAngin;
    private EditText bebanGempa;
    private EditText benTangBaja;

    //JSON Array
    private JSONArray result;

    private Spinner profileSpn;
    private Spinner bajaSpn;
    private Spinner tumpuanSpn;
    private ArrayList<String> spnProfile;
    private ArrayList<String> spnBaja;
    private ArrayList<String> spnTumpuan;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    double bf;
    double tf;
    double h2;
    double tw;
    double fy;
    double zx;
    double ht;
    double r;
    double aa;
    double kc;
    int aw = 0;
    double ketVn = 0;
    double mu = 0;

    double calculate  = 0;
    double calculate2 = 0;
    double calculate3 = 0;
    double calculate4 = 0;

    private EditText txtKetNn;
    private EditText txtNu;

    Object  nu;

    double w;

    String dlBebanMati;
    String llBebanHidup;
    String laBebanAtap;
    String hBebanHujan;
    String wxBebanAngin;
    String eBebanGempa;
    String lbentangBj;

    private TextView kuat,tidakKuat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batang_tekan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Batang Tekan");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        profileSpn = (Spinner) findViewById(R.id.spnProfile);
        bajaSpn    = (Spinner) findViewById(R.id.spnMutuBaja);
        tumpuanSpn = (Spinner) findViewById(R.id.spnTumpuan);

        bebanMati   = (EditText) findViewById(R.id.bebanMati);
        bebanHidup  = (EditText) findViewById(R.id.bebanHidup);
        bebanAtap   = (EditText) findViewById(R.id.bebanAtap);
        bebanHujan  = (EditText) findViewById(R.id.bebanHujan);
        bebanAngin  = (EditText) findViewById(R.id.bebanAngin);
        bebanGempa  = (EditText) findViewById(R.id.bebanGempa);
        benTangBaja = (EditText) findViewById(R.id.bentangBaja);

        txtNu       =  (EditText) findViewById(R.id.txtNu);
        txtKetNn    = (EditText) findViewById(R.id.txtKetNn);

        btnHitung   = (Button)findViewById(R.id.btnHitung);

        kuat        = (TextView)findViewById(R.id.kuat);
        tidakKuat   = (TextView)findViewById(R.id.tidakKuat);
        kuat.setVisibility(View.INVISIBLE);
        tidakKuat.setVisibility(View.INVISIBLE);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        spnProfile = new ArrayList<String>();
        spnBaja = new ArrayList<String>();
        spnTumpuan = new ArrayList<String>();


        profileSpn.setOnItemSelectedListener(this);

        getDataProfile();
        select();

        hitung();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(BatangTekan.this, Debs.class);
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
                            getDataBaja();
                            getDataTumpuan();
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
        profileSpn.setAdapter(new ArrayAdapter<String>(BatangTekan.this, android.R.layout.simple_spinner_dropdown_item, spnProfile));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnProfile);
        profileSpn.setAdapter(mAdapter);
    }

    private void getDataBaja(){

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
                            getDataBaja(result);
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
                params.put("tag", "getBajaBalokLentur");
                return params;
            }
        };

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getDataBaja(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                spnBaja.add(json.getString("Jenis_Baja"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        bajaSpn.setAdapter(new ArrayAdapter<String>(BatangTekan.this, android.R.layout.simple_spinner_dropdown_item, spnBaja));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnBaja);
        bajaSpn.setAdapter(mAdapter);
    }

    private void getDataTumpuan(){

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
                            getDataTumpuan(result);
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
                params.put("tag", "getJenisPeletakan");
                return params;
            }
        };

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getDataTumpuan(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                spnTumpuan.add(json.getString("jns_peletakan"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        tumpuanSpn.setAdapter(new ArrayAdapter<String>(BatangTekan.this, android.R.layout.simple_spinner_dropdown_item, spnTumpuan));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnTumpuan);
        tumpuanSpn.setAdapter(mAdapter);
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
                        ht       = jObj.getDouble("Ht");
                        bf       = jObj.getDouble("Bf");
                        tf       = jObj.getDouble("Tf");
                        h2       = jObj.getDouble("H2");
                        tw       = jObj.getDouble("Tw");
                        zx       = jObj.getDouble("Zx");
                        r        = jObj.getDouble("R");
                        aa        = jObj.getDouble("A");
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
                params.put("tag","selectDimensionalBalokLentur");
                params.put("dimensional", dimensi);
                return params;
            }
        };


        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);
    }

    private void getJenisBaja(final String jenisBaja){
        //Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();
        StringRequest strReqq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(getApplication()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        fy       = jObj.getDouble("FY");
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
                params.put("tag","selectJenisBajaBalokLentur");
                params.put("jenisbaja", jenisBaja);
                return params;
            }
        };


        strReqq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReqq,tag_string_req);
    }

    private void getTumpuan(final String tumpuan){
        //Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();
        StringRequest strReqq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(getApplication()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        kc       = jObj.getDouble("Kc");
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
                params.put("tag","selectJenisPeletakan");
                params.put("jenisPeletakan", tumpuan);
                return params;
            }
        };


        strReqq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReqq,tag_string_req);
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

        bajaSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String baja=bajaSpn.getSelectedItem().toString();
                Log.e("Selected item : ",baja);
                getJenisBaja(baja);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        tumpuanSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String tumpuan=tumpuanSpn.getSelectedItem().toString();
                Log.e("Selected item : ",tumpuan);
                getTumpuan(tumpuan);
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
                double a = 0;
                double b = 0;
                double c = 0;
                double d = 0;
                double e = 0;
                double f = 0;
                double g = 0;
                double h = 0;
                //ganti
                double i = 0;
                double jjj = 0;
                double k = 0;
                double l = 0;
                double m = 0;
                double n = 0;
                double o = 0;

                dlBebanMati  = bebanMati.getText().toString();
                llBebanHidup = bebanHidup.getText().toString();
                laBebanAtap  = bebanAtap.getText().toString();
                hBebanHujan  = bebanHujan.getText().toString();
                wxBebanAngin = bebanAngin.getText().toString();
                eBebanGempa  = bebanGempa.getText().toString();
                lbentangBj = benTangBaja.getText().toString();
                llBebanHidup = bebanHidup.getText().toString();

                ArrayList arrayList = new ArrayList();

                if(dlBebanMati.trim().length() > 0 && llBebanHidup.trim().length() > 0 && laBebanAtap.trim().length() > 0
                        && hBebanHujan.trim().length() > 0 && wxBebanAngin.trim().length() > 0 && eBebanGempa.trim().length() > 0){

                    a = Double.parseDouble(dlBebanMati)  * 1.4;
                    b = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(llBebanHidup) * 1.6) + (Double.parseDouble(laBebanAtap) * 0.5);
                    i = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(llBebanHidup) * 1.6) + (Double.parseDouble(hBebanHujan) * 0.5);
                    c = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(laBebanAtap)  * 1.6) + (Double.parseDouble(llBebanHidup) * 1);
                    k = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(hBebanHujan)  * 1.6) + (Double.parseDouble(llBebanHidup) * 1);
                    f = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(laBebanAtap)  * 1.6) + (Double.parseDouble(wxBebanAngin) * 0.8);
                    jjj = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(hBebanHujan)  * 1.6) + (Double.parseDouble(wxBebanAngin) * 0.8);
                    d = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(wxBebanAngin) * 1.3) + (Double.parseDouble(llBebanHidup) * 1 + (Double.parseDouble(laBebanAtap) * 0.5));
                    l = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(wxBebanAngin) * 1.3) + (Double.parseDouble(llBebanHidup) * 1 + (Double.parseDouble(hBebanHujan) * 0.5));
                    e = (Double.parseDouble(dlBebanMati) * 1.2) + (Double.parseDouble(eBebanGempa)  * 1.0) + (Double.parseDouble(llBebanHidup) * 1);
                    m = (Double.parseDouble(dlBebanMati) * 1.2) - (Double.parseDouble(eBebanGempa)  * 1.0) + (Double.parseDouble(llBebanHidup) * 1);
                    //f = (Double.parseDouble(dlBebanMati) * 1.2) - (Double.parseDouble(eBebanGempa)  * 1.0) - (Double.parseDouble(llBebanHidup) * 1);
                    g = (Double.parseDouble(dlBebanMati) * 0.9) + (Double.parseDouble(wxBebanAngin) * 1.3);
                    n = (Double.parseDouble(dlBebanMati) * 0.9) + (Double.parseDouble(eBebanGempa) * 1.0);
                    h = (Double.parseDouble(dlBebanMati) * 0.9) - (Double.parseDouble(wxBebanAngin) * 1.3);
                    o = (Double.parseDouble(dlBebanMati) * 0.9) - (Double.parseDouble(eBebanGempa) * 1.0);


                    arrayList.add(new Double(a));
                    arrayList.add(new Double(b));
                    arrayList.add(new Double(c));
                    arrayList.add(new Double(d));
                    arrayList.add(new Double(e));
                    arrayList.add(new Double(f));
                    arrayList.add(new Double(g));
                    arrayList.add(new Double(h));
                    arrayList.add(new Double(i));
                    arrayList.add(new Double(jjj));
                    arrayList.add(new Double(k));
                    arrayList.add(new Double(l));
                    arrayList.add(new Double(m));
                    arrayList.add(new Double(n));
                    arrayList.add(new Double(o));

                    nu = Collections.max(arrayList);

                    //Toast.makeText(getApplicationContext(), String.valueOf(nu), Toast.LENGTH_LONG).show();

                    DecimalFormat df = new DecimalFormat("#.###");


                    double hitLk          = kc * Double.parseDouble(lbentangBj);
                    int elastisitas       = 200000;
                    double lamdaC       = (1/3.1415) * hitLk / (r * 10) * (Math.sqrt(fy/elastisitas));

                    //menentukan panjang kolom w
                    //double hitLamdaC = 0.67 * lamdaC;

                    double lamdaKuadrat     = Math.pow(lamdaC,2);

                    if( lamdaC <=0.25){
                        w = 1;
                    }
                    if(0.25 < lamdaC && lamdaC < 1.2){
                        w = 1.43/(1.6 - 0.67 * lamdaC);
                    }
                    if(lamdaC >= 1.2) {
                        w = 1.25 * lamdaKuadrat;
                    }

                    //
                    //double nn = ;
                    double ketNn = 0.85 *(aa * 100) * (fy / (w)) ;
                    //Toast.makeText(getApplicationContext(),"ag =" +String.valueOf(ketNn),Toast.LENGTH_LONG).show();
                    DecimalFormat dformat = new DecimalFormat("#.####");
                    double ketNnn = ketNn / 1000;

                    double parsingNu = Double.parseDouble(String.valueOf(nu));

                    txtKetNn.setText(String.valueOf(Math.round(ketNnn)));
                    txtNu.setText(String.valueOf(parsingNu));

                    ImageView img = (ImageView) findViewById(R.id.ketentuan);

                    if(ketNnn > parsingNu){
                        //Toast.makeText(getApplicationContext(),"Kuat",Toast.LENGTH_LONG).show();
                        kuat.setVisibility(View.VISIBLE);
                        tidakKuat.setVisibility(View.INVISIBLE);
                        img.setImageResource(R.drawable.img_lebihdari);
                    }else {
                        //Toast.makeText(getApplicationContext(),"Tidak Kuat",Toast.LENGTH_LONG).show();
                        kuat.setVisibility(View.INVISIBLE);
                        tidakKuat.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.img_kurang);
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"Data Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}