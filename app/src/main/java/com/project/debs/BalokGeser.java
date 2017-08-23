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

import server.AppController;
import server.Config_URL;

public class BalokGeser extends AppCompatActivity implements Spinner.OnItemSelectedListener,  View.OnClickListener{

    private static final String TAG = LenturBalok.class.getSimpleName();
    private EditText txtVU;
    private EditText txtVN;
    private EditText txtGeserNominal;
    private EditText txtGeserMaximum;
    private EditText txtA;
    private EditText txtB;

    private TextView txtAman;
    private TextView txtTidakAman;

    //private Button btnHitungPelat;
    private Button btnHitung;
    ProgressDialog pDialog;

    private EditText bebanMati;
    private EditText bebanHidup;
    private EditText bebanAtap;
    private EditText bebanHujan;
    private EditText bebanAngin;
    private EditText bebanGempa;
    private EditText jarakSokongLateral;

    String dlBebanMati;
    String llBebanHidup;
    String laBebanAtap;
    String hBebanHujan;
    String wxBebanAngin;
    String eBebanGempa;
    String lJarakSokongLateral;

    //JSON Array
    private JSONArray result;

    private Spinner profileSpn;
    private Spinner bajaSpn;
    private ArrayList<String> spnProfile;
    private ArrayList<String> spnBaja;

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
    int aw = 0;
    double ketVn = 0;
    double mu = 0;

    double calculate  = 0;
    double calculate2 = 0;
    double calculate3 = 0;
    double calculate4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balok_geser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Balok Geser");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        txtGeserNominal = (EditText)findViewById(R.id.momenNominal); txtGeserNominal.setText("0"); txtGeserNominal.setEnabled(false);
        txtVU           = (EditText)findViewById(R.id.txtMu); txtVU.setText("0");txtVU.setEnabled(false);
        txtVN           = (EditText)findViewById(R.id.txtMn); txtVN.setText("0");txtVN.setEnabled(false);
        txtGeserMaximum = (EditText)findViewById(R.id.momenMaximum);txtGeserMaximum.setText("0");txtGeserMaximum.setEnabled(false);
        //txtMomenMaximum.setEnabled(false);

        bebanMati   = (EditText) findViewById(R.id.bebanMati);
        bebanHidup  = (EditText) findViewById(R.id.bebanHidup);
        bebanAtap   = (EditText) findViewById(R.id.bebanAtap);
        bebanHujan  = (EditText) findViewById(R.id.bebanHujan);
        bebanAngin  = (EditText) findViewById(R.id.bebanAngin);
        bebanGempa  = (EditText) findViewById(R.id.bebanGempa);
        jarakSokongLateral = (EditText) findViewById(R.id.jarakSokongLateral);

        txtA            = (EditText) findViewById(R.id.a);
        txtB            = (EditText) findViewById(R.id.b);
        txtA.setVisibility(View.INVISIBLE);
        txtB.setVisibility(View.INVISIBLE);

        txtAman      = (TextView)findViewById(R.id.txtAman);
        txtTidakAman = (TextView)findViewById(R.id.txtTdkAman);
        txtAman.setVisibility(View.INVISIBLE);
        txtTidakAman.setVisibility(View.INVISIBLE);

        profileSpn = (Spinner) findViewById(R.id.spnProfile);
        bajaSpn    = (Spinner) findViewById(R.id.spnMutuBaja);

        //btnHitungPelat = (Button)findViewById(R.id.hitungPelatSayap);
        btnHitung           = (Button)findViewById(R.id.btnHitung);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        spnProfile = new ArrayList<String>();
        spnBaja = new ArrayList<String>();

        profileSpn.setOnItemSelectedListener(this);

        getDataProfile();
        select();
        hitung();
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

    @Override
    public void onBackPressed() {
        Intent a = new Intent(BalokGeser.this, BalokLentur.class);
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
                params.put("tag", "getProfilIwfHwf");
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
        profileSpn.setAdapter(new ArrayAdapter<String>(BalokGeser.this, android.R.layout.simple_spinner_dropdown_item, spnProfile));
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
        bajaSpn.setAdapter(new ArrayAdapter<String>(BalokGeser.this, android.R.layout.simple_spinner_dropdown_item, spnBaja));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spnBaja);
        bajaSpn.setAdapter(mAdapter);
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
    }


    private void hitung(){
        /*btnHitungPelat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = profileSpn.getSelectedItem().toString();
                String b = bajaSpn.getSelectedItem().toString();
                if(!a.isEmpty() && !b.isEmpty()){
                    getNilaiProfile(a);
                    getJenisBaja(b);
                    double x    = 2*tf;
                    calculate   = bf/x;
                    calculate2  = 170/Math.sqrt(fy);
                    calculate3  = h2/tw;
                    calculate4  = 1680/Math.sqrt(fy);

                    if (calculate<calculate2 && calculate3<calculate4){
                        Toast.makeText(getApplication(), "Kompak", Toast.LENGTH_LONG).show();
                        txtMomenMaximum.setEnabled(true);
                        btnHitung.setEnabled(true);
                    }else {
                        Toast.makeText(getApplication(), "Tidak kompak", Toast.LENGTH_LONG).show();
                        txtMomenMaximum.setEnabled(false);
                        btnHitung.setEnabled(false);
                    }

                }else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });*/

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String momen;
                double vn = 0;
                double a = 0;
                double b = 0;
                double c = 0;
                double d = 0;
                double e = 0;
                double f = 0;
                double g = 0;
                double h = 0;
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
                //momen = txtMomenMaximum.getText().toString();
                lJarakSokongLateral = jarakSokongLateral.getText().toString();

                //momen = new Double(txtMomenMaximum.getText().toString());
                //momen = txtGeserMaximum.getText().toString();

                ArrayList arrayList = new ArrayList();
                if(lJarakSokongLateral.trim().length() > 0 && dlBebanMati.trim().length() > 0 && llBebanHidup.trim().length() > 0 && laBebanAtap.trim().length() > 0
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


                    Object nu = Collections.max(arrayList);
                    DecimalFormat df = new DecimalFormat("#.##");

                    txtGeserMaximum.setText(String.valueOf(df.format(nu)));
                    double vuu = 0.5 * Double.parseDouble(String.valueOf(nu)) * (Double.parseDouble(lJarakSokongLateral)/1000);

                    aw = (int) (tw * ht);
                    vn = (0.6 * fy * aw) / 1000;
                    txtGeserNominal.setText(String.valueOf(df.format(vn)));

                    //double hasilVu = Double.parseDouble(momen);
                    txtVU.setText(String.valueOf(df.format(vuu)));

                    ketVn =  0.9 * vn;
                    txtVN.setText(String.valueOf(df.format(ketVn)));

                    ImageView img = (ImageView)findViewById(R.id.ketentuan);
                    if(vuu<ketVn){
                        //Toast.makeText(getApplicationContext(), "Aman", Toast.LENGTH_LONG).show();
                        txtAman.setVisibility(View.VISIBLE);
                        txtTidakAman.setVisibility(View.INVISIBLE);
                        img.setImageResource(R.drawable.img_kurang);
                    }else if(vuu>ketVn){
                        //Toast.makeText(getApplicationContext(), "Tidak Aman", Toast.LENGTH_LONG).show();
                        txtTidakAman.setVisibility(View.VISIBLE);
                        txtAman.setVisibility(View.INVISIBLE);
                        img.setImageResource(R.drawable.img_lebihdari);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Data tidak boleh kosong !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}