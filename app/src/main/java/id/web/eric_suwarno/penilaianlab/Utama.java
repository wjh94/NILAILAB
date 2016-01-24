package id.web.eric_suwarno.penilaianlab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import id.web.eric_suwarno.penilaianlab.aslab.Main;
import id.web.eric_suwarno.penilaianlab.sesi.SesiLogin;

public class Utama extends AppCompatActivity {

    private Button btnLogin;
    private EditText txtUsername, txtPassword;
    private String username, password;
    private RequestQueue requestQueue;
    private StringRequest request;

    private final String URL_LOGIN = "http://nilai.eric-suwarno.web.id/login_android";

    SesiLogin session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utama);

        requestQueue = Volley.newRequestQueue(this);
        session = new SesiLogin(getApplicationContext());

        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        txtUsername.setText("");
        txtPassword.setText("");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();

                if(username.equals("") || password.equals("") || username.isEmpty() || password.isEmpty()) {
                    new AlertDialog.Builder(Utama.this)
                            .setMessage("Nama pengguna atau kata sandi tidak terisi, silakan periksa dan ulangi lagi.")
                            .setTitle("Galat!")
                            .setNeutralButton("Oke!", null)
                            .show();
                } else {
                    final ProgressDialog pDialog = ProgressDialog.show(Utama.this, null, "Sedang melakukan verifikasi, silakan tunggu...", true, false);
                    try {
                        request = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject response = new JSONObject(s);
                                    pDialog.dismiss();

                                    if(response.getString("authorized").equals("true")){
                                        JSONObject userTerdeteksi = response.getJSONObject("user");
                                        session.createUserLoginSession(userTerdeteksi.getString("username"), userTerdeteksi.getString("nama_aslab"), userTerdeteksi.getString("id_aslab"));
                                        if(userTerdeteksi.getString("id_aslab").equals("")) {

                                        }else{
                                            Intent i = new Intent(getApplicationContext(), Main.class);
                                            i.putExtra("id_aslab", userTerdeteksi.getString("id_aslab"));
                                            i.putExtra("nama_aslab", userTerdeteksi.getString("nama_aslab"));
                                            i.putExtra("username", userTerdeteksi.getString("username"));
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                        }
                                    }else if(response.getString("authorized").equals("false")){
                                        new AlertDialog.Builder(Utama.this)
                                                .setMessage("Maaf, nama pengguna dan/atau kata sandi tidak sesuai, silakan coba lagi.")
                                                .setNeutralButton("Oke.", null)
                                                .setTitle("Galat!")
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(Utama.this, "Telah terjadi kesalahan, silakan ulangi lagi.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                pDialog.dismiss();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", username);
                                params.put("password", password);
                                return params;
                            }
                        };

                        requestQueue.add(request);
                    } catch (Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(Utama.this, "Telah terjadi kesalahan, silakan ulangi lagi.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .setTitle("Konfirmasi")
                .show();
    }
}
