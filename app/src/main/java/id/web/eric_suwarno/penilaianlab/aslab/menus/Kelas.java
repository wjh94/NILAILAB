package id.web.eric_suwarno.penilaianlab.aslab.menus;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;

import id.web.eric_suwarno.penilaianlab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Kelas extends Fragment {

    private Spinner cbxKelas;
    private final String URL_DAFTAR_KELAS = "http://nilai.eric-suwarno.web.id/kelas/aslab/";
    private final String URL_QUERY_KELAS = "http://nilai.eric-suwarno.web.id/kelas/view/";
    private Button btnPeriksaKelas;
    private RequestQueue requestQueue;
    private StringRequest request;
    private Integer id_aslab;
    private ArrayList<Integer> id_kelas = new ArrayList<>();
    private ArrayList<String> nama_kelas = new ArrayList<>();
    private TextView lblNamaMatkul, lblKomMatkul, lblJumlahPeserta,
            lblNamaAslab, lblNamaDosen, lblPeriode;
    private TableLayout tblDaftarMhs;
    Resources r;

    public Kelas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.aslab_kelas, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getBaseContext());
        cbxKelas = (Spinner) v.findViewById(R.id.cbxDaftarKelas_KelasAslab);
        btnPeriksaKelas = (Button) v.findViewById(R.id.btnPeriksaInfoKelas_Aslab);
        lblNamaMatkul = (TextView) v.findViewById(R.id.lblNamaMatkul_Aslab);
        lblKomMatkul = (TextView) v.findViewById(R.id.lblKomMatkul_Aslab);
        lblNamaDosen = (TextView) v.findViewById(R.id.lblNamaDosen_Aslab);
        lblNamaAslab = (TextView) v.findViewById(R.id.lblNamaAslab_Aslab);
        lblJumlahPeserta = (TextView) v.findViewById(R.id.lblJlhMhs_Aslab);
        lblPeriode = (TextView) v.findViewById(R.id.lblTglMasukSelesai_Aslab);
        tblDaftarMhs = (TableLayout) v.findViewById(R.id.tblDaftarMhs_Aslab);
        r = getActivity().getResources();

        Toast.makeText(getActivity().getBaseContext(), "Sedang memuat data....", Toast.LENGTH_SHORT)
                .show();

        try{
            request = new StringRequest(Request.Method.GET, URL_DAFTAR_KELAS + id_aslab, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject response = new JSONObject(s);
                        JSONArray kelas = response.getJSONArray("kelas");
                        for(int i = 0; i < kelas.length(); i++){
                            id_kelas.add(Integer.parseInt(kelas.getJSONObject(i).getString("id")));
                            nama_kelas.add(kelas.getJSONObject(i).getString("nama_matkul") + " - " + kelas.getJSONObject(i).getString("kom_matkul"));
                        }

                        String[] classList = new String[nama_kelas.size()];
                        nama_kelas.toArray(classList);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, classList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cbxKelas.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast
                                .makeText(getActivity().getBaseContext(), "Maaf, terjadi kesalahan parsing, silakan coba lagi.", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity().getBaseContext(), "Data tidak dapat dimuat, silakan coba lagi dengan berpindah menu.",Toast.LENGTH_LONG).show();
                }
            });

            requestQueue.add(request);
        } catch (Exception e) {
            Toast.makeText(getActivity().getBaseContext(), "Data tidak dapat dimuat, silakan coba lagi.", Toast.LENGTH_LONG).show();
        }

        btnPeriksaKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cbxKelas.getAdapter().getCount() != 0){
                    Toast.makeText(getActivity().getBaseContext(),
                            "Sedang memuat data untuk kelas " + nama_kelas.get(cbxKelas.getSelectedItemPosition()),
                            Toast.LENGTH_SHORT)
                            .show();

                    Integer idKelasTerpilih = id_kelas.get(cbxKelas.getSelectedItemPosition());
                    try {
                        request = new StringRequest(Request.Method.GET, URL_QUERY_KELAS + idKelasTerpilih, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject response = new JSONObject(s);
                                    JSONObject kelas = response.getJSONObject("kelas");
                                    JSONObject aslab = response.getJSONObject("aslab");
                                    JSONArray mhs = response.getJSONArray("mhs");

                                    lblNamaMatkul.setText(kelas.getString("nama_matkul"));
                                    lblKomMatkul.setText(kelas.getString("kom_matkul"));
                                    lblNamaAslab.setText(aslab.getString("nama_lengkap") + " (" + aslab.getString("nomor_induk") + ")");
                                    lblNamaDosen.setText(kelas.getString("nama_lengkap_dosen"));
                                    lblJumlahPeserta.setText(mhs.length() + " orang");
                                    lblPeriode.setText(kelas.getString("tgl_mulai") + " s/d " + kelas.getString("tgl_selesai"));
                                    tblDaftarMhs.removeAllViews();
                                    addHeader();
                                    for(int i = 0; i < mhs.length(); i++) {
                                        TableRow rowMhs = new TableRow(getActivity().getBaseContext());
                                        TextView lblNim = new TextView(getActivity().getBaseContext()),
                                                lblNama = new TextView(getActivity().getBaseContext());

                                        lblNim.setText(mhs.getJSONObject(i).getString("nomor_induk"));
                                        lblNama.setText(mhs.getJSONObject(i).getString("nama_lengkap"));

                                        Float pixel1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics());
                                        Float pixel2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, r.getDisplayMetrics());

                                        lblNim.setGravity(Gravity.CENTER_HORIZONTAL);
                                        lblNama.setGravity(Gravity.CENTER_HORIZONTAL);
                                        TableRow.LayoutParams param = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                                        lblNama.setLayoutParams(param);
                                        lblNim.setLayoutParams(param);
                                        lblNim.setTextColor(Color.BLACK);
                                        lblNama.setTextColor(Color.BLACK);
                                        lblNim.setWidth(Math.round(pixel1));
                                        lblNama.setWidth(Math.round(pixel2));

                                        rowMhs.addView(lblNim);
                                        rowMhs.addView(lblNama);
                                        tblDaftarMhs.addView(rowMhs);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity().getBaseContext(), "Terjadi kesalahan dalam memuat data, silakan coba lagi.", Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });

                        requestQueue.add(request);
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getBaseContext(),
                                "Terjadi kesalahan dalam memuat data, silakan coba lagi.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }else{
                    Toast
                            .makeText(getActivity().getBaseContext(),
                                    "Tidak ada kelas yang terdata, atau Anda tidak sedang bertanggungjawab pada sebuah kelas, silakan coba lagi.",
                                    Toast.LENGTH_LONG)
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

    private void addHeader(){
        TableRow rowHeader = new TableRow(getActivity().getBaseContext());
        TextView lblNim = new TextView(getActivity().getBaseContext()),
                lblNama = new TextView(getActivity().getBaseContext());

        lblNim.setText("NIM");
        lblNama.setText("Nama");

        Float pixel1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, r.getDisplayMetrics());
        Float pixel2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, r.getDisplayMetrics());

        lblNim.setGravity(Gravity.CENTER_HORIZONTAL);
        lblNama.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow.LayoutParams param = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        lblNama.setLayoutParams(param);
        lblNim.setLayoutParams(param);
        lblNim.setWidth(Math.round(pixel1));
        lblNama.setWidth(Math.round(pixel2));
        lblNim.setTextColor(Color.BLACK);
        lblNama.setTextColor(Color.BLACK);

        rowHeader.addView(lblNim);
        rowHeader.addView(lblNama);
        tblDaftarMhs.addView(rowHeader);
    }
}
