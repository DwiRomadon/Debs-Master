package com.project.debs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import server.AppController;
import server.Config_URL;

public class SambunganBautTipeTumpu extends AppCompatActivity {

    private View v;

    ProgressDialog pDialog;

    //JSON Array
    private JSONArray result;

    private Button btnHitung;

    private EditText jumlahBidangGeser;
    private EditText jumlhaBautKeseluruhan;
    private EditText jumlahBautSatuGaris;
    private EditText NU;
    private EditText rn1;
    private EditText rn2;
    private EditText rnt;
    private EditText hasilRn1Rn2;
    private EditText nN;
    private EditText baut;
    private EditText batangTarik;

    private Spinner profileSpn;
    private Spinner bajaSpn;
    private Spinner mutuBautSpn;
    private Spinner jenisBautSpn;
    private Spinner diameterBautSpn;

    private ArrayList<String> spnProfile;
    private ArrayList<String> spnBaja;
    private ArrayList<String> spnJenisBaut;
    private ArrayList<String> spnDiameterBaut;
    private ArrayList<String> spnMutuBaut;

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
    double rr;
    double fu;
    double mutuBaut;

    private TextView tampilanTet;


    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


    EditText rntt,rntt2;
    EditText nn, nn2;
    ImageView img,img2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sambungan_baut_tipetumpu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tipe Tumpu");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        profileSpn = (Spinner) findViewById(R.id.spnProfile);
        bajaSpn    = (Spinner) findViewById(R.id.spnMutuBaja);
        mutuBautSpn = (Spinner)findViewById(R.id.spnMutuBaut);
        jenisBautSpn = (Spinner)findViewById(R.id.spnJenisBaut);
        diameterBautSpn = (Spinner)findViewById(R.id.spnDiameterBaut);

        btnHitung = (Button) findViewById(R.id.btnHitung);

        tampilanTet = (TextView) findViewById(R.id.tampilanText);

        jumlahBidangGeser       = (EditText) findViewById(R.id.jmlhBidangGeser);
        jumlhaBautKeseluruhan   = (EditText) findViewById(R.id.jmlhBautKeseluruhan);
        jumlahBautSatuGaris     = (EditText) findViewById(R.id.jmlhBautDalamSatuGaris);
        NU                      = (EditText) findViewById(R.id.nu);
        rn1                     = (EditText) findViewById(R.id.rn1);rn1.setEnabled(false);
        rn2                     = (EditText) findViewById(R.id.rn2);rn2.setEnabled(false);
        rnt                     = (EditText) findViewById(R.id.rnt);rnt.setEnabled(false);
        hasilRn1Rn2             = (EditText) findViewById(R.id.editHasil);hasilRn1Rn2.setEnabled(false);
        nN                      = (EditText) findViewById(R.id.Nn);nN.setEnabled(false);
        baut                    = (EditText) findViewById(R.id.baut);baut.setEnabled(false);
        batangTarik             = (EditText) findViewById(R.id.batangTarik); batangTarik.setEnabled(false);


        rntt = (EditText)findViewById(R.id.txtRnt);rntt.setText("0");rntt.setEnabled(false);
        nn = (EditText)findViewById(R.id.txtNn);nn.setText("0");nn.setEnabled(false);

        rntt2 = (EditText)findViewById(R.id.txtRnt2);rntt2.setText("0");rntt2.setEnabled(false);
        nn2 = (EditText)findViewById(R.id.txtNn2);nn2.setText("0");nn2.setEnabled(false);

        img = (ImageView) findViewById(R.id.ketentuan);
        img2= (ImageView) findViewById(R.id.ketentuan2);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.mutuBaut, R.layout.spinner_item);
        //mutuBautSpn.setAdapter(adapter);

        spnProfile = new ArrayList<String>();
        spnBaja = new ArrayList<String>();
        spnMutuBaut = new ArrayList<String>();
        spnJenisBaut = new ArrayList<String>();
        spnDiameterBaut = new ArrayList<String>();

        getDataProfile();
        select();
        hitung();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(SambunganBautTipeTumpu.this, SambunganBautMenu.class);
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
                            getDataJenisBaut();
                            getDataDiameterBaut();
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
        bajaSpn.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spnBaja));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnBaja);
        bajaSpn.setAdapter(mAdapter);
    }

    private void getDataJenisBaut(){

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
                            getDataJenisBaut(result);
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
                params.put("tag", "getJenisBaut");
                return params;
            }
        };

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getDataJenisBaut(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                spnJenisBaut.add(json.getString("jns_baut"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        jenisBautSpn.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spnJenisBaut));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnJenisBaut);
        jenisBautSpn.setAdapter(mAdapter);
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
                Log.d(String.valueOf(this), "Response: " + response.toString());
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
                Log.d(String.valueOf(getApplicationContext()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        fy       = jObj.getDouble("FY");
                        fu       = jObj.getDouble("Fu");
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
                params.put("tag","selectJenisBajaBalokLentur");
                params.put("jenisbaja", jenisBaja);
                return params;
            }
        };


        strReqq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReqq,tag_string_req);
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

    private void getJeenisBaut(final String jenisBaut){
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
                        rr = jObj.getDouble("Faktor_Keamanan");
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
                params.put("tag","selectJenisBaut");
                params.put("jenisbaut", jenisBaut);
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

        jenisBautSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String baut=jenisBautSpn.getSelectedItem().toString();
                Log.e("Selected item : ",baut);
                getJeenisBaut(baut);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

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

                //String mutuBaut             = mutuBautSpn.getSelectedItem().toString();
                String diameter             = diameterBautSpn.getSelectedItem().toString();
                String jmlhBidangGeser      = jumlahBidangGeser.getText().toString();
                String jmlhBautKeseluruhan  = jumlhaBautKeseluruhan.getText().toString();
                String jmlhBautSatuGaris    = jumlahBautSatuGaris.getText().toString();
                String edNu                 = NU.getText().toString();

                if(diameter.trim().length() > 0 &&  jmlhBidangGeser.trim().length() > 0
                        && jmlhBautKeseluruhan.trim().length() > 0 && jmlhBautSatuGaris.trim().length() > 0 && jmlhBidangGeser.trim().length() > 0
                        && edNu.trim().length() > 0){

                    DecimalFormat dfRn1 = new DecimalFormat("#.##");
                    //hitung rn1
                    double jariJariBaut = Double.parseDouble(diameter) / 2;
                    double ab           = (3.14) * (Math.pow(jariJariBaut,2));
                    double hitRn1        = Double.parseDouble(jmlhBidangGeser) * rr * mutuBaut * ab;
                    double hitRn11       = hitRn1/1000;
                    double fixRn1       = 0.75 * hitRn11;
                    rn1.setText(String.valueOf(dfRn1.format(fixRn1)));

                    //hitung rn2
                    double hitRn2   = 2.4 * Double.parseDouble(diameter) * tf * fu;
                    double hitRn22  = hitRn2/1000;
                    double fixRn2   = 0.75 * hitRn22;
                    rn2.setText(String.valueOf(dfRn1.format(fixRn2)));

                    if(fixRn1<fixRn2){
                        hasilRn1Rn2.setText("BAUT PUTUS");
                    }else if(fixRn1 > fixRn2){
                        hasilRn1Rn2.setText("PLAT SOBEK");
                    }

                    double hitRnt = Double.parseDouble(jmlhBautKeseluruhan) * fixRn1;
                    rnt.setText(String.valueOf(dfRn1.format(hitRnt)));
                    //storeDataToServer(String.valueOf(dfRn1.format(hitRnt)));

                    //hitung nn
                    //hitung leleh
                    double leleh = 0.9 * (aa*100) * fy;
                    double hasilLeleh = leleh/1000;

                    //hitung fraktur
                    double db2 = Double.parseDouble(diameter) + 2;
                    double An = (aa*100) - Double.parseDouble(jmlhBautSatuGaris) * db2 * tf;
                    double Ae = An * 0.9;
                    double fraktur = 0.75 * Ae * fu;
                    double hasilFraktur = fraktur / 1000;

                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new Double(hasilLeleh));
                    arrayList.add(new Double(hasilFraktur));

                    Object fixNn = Collections.min(arrayList);
                    nN.setText(String.valueOf(dfRn1.format(fixNn)));

                    double parsingNn = Double.parseDouble(String.valueOf(fixNn));
                    double pasingNu  = Double.parseDouble(String.valueOf(edNu));

                    rntt.setText(String.valueOf(dfRn1.format(hitRnt)));
                    nn.setText(String.valueOf(dfRn1.format(parsingNn)));

                    if (hitRnt < parsingNn){
                        baut.setText("Sambungan Tak Baik");
                        img.setImageResource(R.drawable.img_kurang);
                    }else if(hitRnt > parsingNn){
                        baut.setText("Sambungan Baik");
                        img.setImageResource(R.drawable.img_lebihdari);
                    }

                    rntt2.setText(String.valueOf(dfRn1.format(hitRnt)));
                    nn2.setText(edNu);
                    if(hitRnt > pasingNu){
                        //kuat
                        batangTarik.setText("Kuat");
                        img2.setImageResource(R.drawable.img_lebihdari);
                        tampilanTet.setText("");
                    }else if(hitRnt < pasingNu){
                        //tidak kuat
                        batangTarik.setText("Tidak Kuat");
                        img2.setImageResource(R.drawable.img_kurang);
                        tampilanTet.setText(R.string.textTipeTumpu);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), " Data Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void storeDataToServer(final String rnt){

        // Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Wait ...");
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

                    if (!error) {
                        //String errorrr = jObj.getString("success");
                        //Toast.makeText(getApplicationContext(),
                                //errorrr, Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(String.valueOf(getApplicationContext()), "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "insertRnt");
                params.put("rnt", rnt);
                return params;
            }

        };

        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}

