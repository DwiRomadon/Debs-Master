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

public class LenturBalok extends AppCompatActivity  implements Spinner.OnItemSelectedListener,  View.OnClickListener{

    private static final String TAG = LenturBalok.class.getSimpleName();
    private EditText txtMU;
    private EditText txtMn;
    private EditText txtMomenNominal;
    private EditText txtMomenMaximum;
    private EditText txtA;
    private EditText txtB;

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

    private TextView txtAman;
    private TextView txtTidakAman;

    private Button btnHitungPelat;
    private Button btnHitung;
    ProgressDialog pDialog;

    Object nu;

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
    double x1;
    double x2;
    double sx;
    double ry;
    int mn = 0;
    double ketMn = 0;

    double calculate  = 0;
    double calculate2 = 0;
    double calculate3 = 0;
    double calculate4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lentur_balok);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Lentur Balok");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        txtMomenNominal = (EditText)findViewById(R.id.momenNominal); txtMomenNominal.setText("0"); txtMomenNominal.setEnabled(false);
        txtMU           = (EditText)findViewById(R.id.txtMu); txtMU.setText("0");txtMU.setEnabled(false);
        txtMn           = (EditText)findViewById(R.id.txtMn); txtMn.setText("0");txtMn.setEnabled(false);
        txtMomenMaximum = (EditText)findViewById(R.id.momenMaximum);
        txtMomenMaximum.setEnabled(false);

        bebanMati   = (EditText) findViewById(R.id.bebanMati);
        bebanHidup  = (EditText) findViewById(R.id.bebanHidup);
        bebanAtap   = (EditText) findViewById(R.id.bebanAtap);
        bebanHujan  = (EditText) findViewById(R.id.bebanHujan);
        bebanAngin  = (EditText) findViewById(R.id.bebanAngin);
        bebanGempa  = (EditText) findViewById(R.id.bebanGempa);
        jarakSokongLateral = (EditText) findViewById(R.id.jarakSokongLateral);

        bebanMati.setEnabled(false);
        bebanHidup.setEnabled(false);
        bebanAtap.setEnabled(false);
        bebanHujan.setEnabled(false);
        bebanAngin.setEnabled(false);
        bebanGempa.setEnabled(false);
        jarakSokongLateral.setEnabled(false);

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

        btnHitungPelat = (Button)findViewById(R.id.hitungPelatSayap);
        btnHitung           = (Button)findViewById(R.id.btnHitung);btnHitung.setEnabled(false);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        spnProfile = new ArrayList<String>();
        spnBaja = new ArrayList<String>();

        profileSpn.setOnItemSelectedListener(this);

        getDataProfile();
        select();
        hitung();
        //hitungPelatBaja();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(LenturBalok.this, BalokLentur.class);
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
        profileSpn.setAdapter(new ArrayAdapter<String>(LenturBalok.this, android.R.layout.simple_spinner_dropdown_item, spnProfile));
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
        bajaSpn.setAdapter(new ArrayAdapter<String>(LenturBalok.this, android.R.layout.simple_spinner_dropdown_item, spnBaja));
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
                        bf       = jObj.getDouble("Bf");
                        tf       = jObj.getDouble("Tf");
                        h2       = jObj.getDouble("H2");
                        tw       = jObj.getDouble("Tw");
                        zx       = jObj.getDouble("Zx");
                        x1       = jObj.getDouble("X1");
                        x2       = jObj.getDouble("X2");
                        sx       = jObj.getDouble("SX");
                        ry       = jObj.getDouble("R");
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
        btnHitungPelat.setOnClickListener(new View.OnClickListener() {
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
                        //txtMomenMaximum.setEnabled(true);
                        btnHitung.setEnabled(true);
                        bebanMati.setEnabled(true);
                        bebanHidup.setEnabled(true);
                        bebanAtap.setEnabled(true);
                        bebanHujan.setEnabled(true);
                        bebanAngin.setEnabled(true);
                        bebanGempa.setEnabled(true);
                        jarakSokongLateral.setEnabled(true);
                    }else {
                        Toast.makeText(getApplication(), "Tidak kompak", Toast.LENGTH_LONG).show();
                        //txtMomenMaximum.setEnabled(false);
                        btnHitung.setEnabled(false);
                        btnHitung.setEnabled(false);
                        bebanMati.setEnabled(false);
                        bebanHidup.setEnabled(false);
                        bebanAtap.setEnabled(false);
                        bebanHujan.setEnabled(false);
                        bebanAngin.setEnabled(false);
                        bebanGempa.setEnabled(false);
                        jarakSokongLateral.setEnabled(false);
                    }

                }else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String momen;
                //momen = new Double(txtMomenMaximum.getText().toString());

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
                momen = txtMomenMaximum.getText().toString();
                lJarakSokongLateral = jarakSokongLateral.getText().toString();

                ArrayList arrayList = new ArrayList();

                if (lJarakSokongLateral.trim().length() > 0 && dlBebanMati.trim().length() > 0 && llBebanHidup.trim().length() > 0 && laBebanAtap.trim().length() > 0
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

                    ArrayList mnKecil = new ArrayList();

                    double muu = 0.125 * Double.parseDouble(String.valueOf(nu)) * Math.pow(Double.parseDouble(lJarakSokongLateral)/1000,2);

                    //tekuk lokal
                    mn = (int) (fy * zx);
                    //tekuk lateral
                    DecimalFormat df = new DecimalFormat("#.##");
                    double mr = (sx * 1000) * (fy-70);
                    double mp = zx * fy;
                    double lp = 1.76 * (ry*10) * (Math.sqrt(200000/fy));
                    double fl = fy - 70;
                    double lr = (ry*10) *  (x1/fl)   *((Math.sqrt(1+(Math.sqrt(1 + x2 * Math.pow(fl,2))))));
                    double mnLateral = 1 * (mr + (mp- mr) * ((lr - Double.parseDouble(lJarakSokongLateral))/(lr-lp)));

                    //DecimalFormat df = new DecimalFormat("#.##");

                    mnKecil.add(new Double(mn));
                    mnKecil.add(new Double(mnLateral));

                    Object fixMN = Collections.min(mnKecil);

                    //Toast.makeText(getApplicationContext(), String.valueOf(mn), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), String.valueOf(mnLateral), Toast.LENGTH_LONG).show();


                    if(mn<mnLateral){
                        Toast.makeText(getApplicationContext(), "Tekuk Lokal", Toast.LENGTH_LONG).show();
                    }else if(mnLateral<mn){
                        Toast.makeText(getApplicationContext(), "Tekuk Lateral", Toast.LENGTH_LONG).show();
                    }

                    txtMomenMaximum.setText(String.valueOf(muu));
                    double mnn = Double.parseDouble(String.valueOf(fixMN)) / 1000000;
                    txtMomenNominal.setText(String.valueOf(df.format(mnn)));

                    ImageView img = (ImageView)findViewById(R.id.ketentuan);

                    ketMn = 0.9 * Double.parseDouble(String.valueOf(fixMN));
                    double hasilKetMN = ketMn/1000000;


                    txtMn.setText(String.valueOf(df.format(hasilKetMN)));
                    //double hasilMomenU = Double.parseDouble(String.valueOf(nu))/1000000;
                    txtMU.setText(String.valueOf(df.format(muu)));
                    double parsingNu = Double.parseDouble(String.valueOf(muu));

                    if(parsingNu<hasilKetMN){
                        //Toast.makeText(getApplicationContext(), "Aman", Toast.LENGTH_LONG).show();
                        txtAman.setVisibility(View.VISIBLE);
                        txtTidakAman.setVisibility(View.INVISIBLE);
                        img.setImageResource(R.drawable.img_kurang);
                    }else {
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
