package com.example.appamst_lab3_api;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registro_sensor extends AppCompatActivity {

    private RequestQueue mQueue;
    private String token = "";

    public float temp, peso;
    public int humedad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_sensor);
        mQueue = Volley.newRequestQueue(this);

        Intent login = getIntent();
        this.token = (String)login.getExtras().get("token");
    }

    public void subirDatos(View v){
        final EditText tempEtAdd = (EditText) findViewById(R.id.tempEtAdd);
        final EditText pesoEtAdd = (EditText) findViewById(R.id.pesoEtAdd);
        final EditText humedadEtAdd = (EditText) findViewById(R.id.humedadEtAdd);

        temp = Float.parseFloat(tempEtAdd.getText().toString());
        peso = Float.parseFloat(pesoEtAdd.getText().toString());
        humedad = Integer.parseInt(humedadEtAdd.getText().toString());

        agregarDatosSensor(temp, peso, humedad);

    }

    private void agregarDatosSensor(float temp, float peso, int humedad){

        Map<String, Float> paramsFloat = new HashMap();
        paramsFloat.put("temperatura", temp);
        paramsFloat.put("peso", peso);

        JSONObject parametros = new JSONObject(paramsFloat);
        try {
            parametros.put("humedad", humedad);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        String url_addS1 = "https://amstlab.onrender.com/api/sensores";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url_addS1, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            Toast.makeText(registro_sensor.this, "Datos subidos", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(registro_sensor.this).create();
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("Error de Conexion");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
        mQueue.add(request);
    }
}