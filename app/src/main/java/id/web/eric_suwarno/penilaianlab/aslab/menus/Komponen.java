package id.web.eric_suwarno.penilaianlab.aslab.menus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.web.eric_suwarno.penilaianlab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Komponen extends Fragment {


    public Komponen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.aslab_komponen, container, false);
    }


}
