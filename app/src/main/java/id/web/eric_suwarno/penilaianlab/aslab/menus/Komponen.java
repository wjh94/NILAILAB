package id.web.eric_suwarno.penilaianlab.aslab.menus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.web.eric_suwarno.penilaianlab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Komponen extends Fragment {
    private final String URL_DAFTAR_KELAS = "http://nilai.eric-suwarno.web.id/kelas/aslab/",
        URL_QUERY_KOMPONEN_KELAS = "http://nilai.eric-suwarno.web.id/komponen/kelas/";
    private Integer id_aslab;
    private StringRequest request;
    private RequestQueue requestQueue;
    private ListView listKomponen;
    private Button btnPeriksa;
    private Spinner cbxKelas;
    private ArrayList<String> nama_kelas = new ArrayList<>();
    private ArrayList<Integer> id_kelas = new ArrayList<>();

    public Komponen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.aslab_komponen, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getBaseContext());

        cbxKelas = (Spinner) v.findViewById(R.id.cbxDaftarKelas_KomponenAslab);
        listKomponen = (ListView) v.findViewById(R.id.listKomponenKelas_Aslab);
        btnPeriksa = (Button) v.findViewById(R.id.btnPeriksaKomponenKelas_Aslab);

        try {
            request = new StringRequest(Request.Method.GET, URL_DAFTAR_KELAS + id_aslab, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject response = new JSONObject(s);
                        JSONArray kelas = response.getJSONArray("kelas");
                        for(int i = 0; i < kelas.length(); i++) {
                            id_kelas.add(Integer.parseInt(kelas.getJSONObject(i).getString("id")));
                            nama_kelas.add(kelas.getJSONObject(i).getString("nama_maktul") + " - " + kelas.getJSONObject(i).getString("kom_matkul"));
                        }

                        String[] classList = new String[nama_kelas.size()];
                        nama_kelas.toArray(classList);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, classList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cbxKelas.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(
                                getActivity().getBaseContext(),
                                "Maaf, terjadi kesalahan pada pemuatan data, silakan ulangi kembali dengan berpindah tab.",
                                Toast.LENGTH_SHORT
                        )
                                .show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(
                            getActivity().getBaseContext(),
                            "Maaf, terjadi kesalahan pada pemuatan data, silakan ulangi kembali dengan berpindah tab.",
                            Toast.LENGTH_SHORT
                    )
                            .show();
                }
            });

            requestQueue.add(request);
        } catch (Exception e) {
            Toast.makeText(
                    getActivity().getBaseContext(),
                    "Maaf, terjadi kesalahan pada pemuatan data, silakan ulangi kembali dengan berpindah tab.",
                    Toast.LENGTH_SHORT
            )
                    .show();
        }

        btnPeriksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbxKelas.getAdapter().getCount() != 0) {
                    try {
                        request = new StringRequest(Request.Method.GET, URL_QUERY_KOMPONEN_KELAS + id_kelas.get(cbxKelas.getSelectedItemPosition()), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(
                                        getActivity().getBaseContext(),
                                        "Maaf, terjadi kesalahan pada pemuatan data, silakan ulangi kembali dengan berpindah tab.",
                                        Toast.LENGTH_SHORT
                                )
                                        .show();
                            }
                        });

                        requestQueue.add(request);
                    } catch (Exception e) {
                        Toast.makeText(
                                getActivity().getBaseContext(),
                                "Maaf, terjadi kesalahan pada pemuatan data, silakan ulangi kembali dengan berpindah tab.",
                                Toast.LENGTH_SHORT
                        )
                                .show();
                    }
                } else {
                    Toast
                            .makeText(
                                    getActivity().getBaseContext(),
                                    "Maaf, data kelas belum dimuat, Anda belum dapat menggunakan tombol ini.",
                                    Toast.LENGTH_LONG
                            )
                            .show();
                }
            }
        });

        return v;
    }

    @Override
    public void setArguments(Bundle args) {
        id_aslab = args.getInt("id_aslab");
        super.setArguments(args);
    }
}
