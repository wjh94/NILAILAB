package id.web.eric_suwarno.penilaianlab.aslab.menus;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.web.eric_suwarno.penilaianlab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Kelas extends Fragment {


    public Kelas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.aslab_kelas, container, false);
    }


}
