package id.web.eric_suwarno.penilaianlab.aslab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import id.web.eric_suwarno.penilaianlab.R;
import id.web.eric_suwarno.penilaianlab.Utama;
import id.web.eric_suwarno.penilaianlab.aslab.menus.Aktivitas;
import id.web.eric_suwarno.penilaianlab.aslab.menus.Home;
import id.web.eric_suwarno.penilaianlab.aslab.menus.Kelas;
import id.web.eric_suwarno.penilaianlab.aslab.menus.Komponen;
import id.web.eric_suwarno.penilaianlab.aslab.menus.Penilaian;
import id.web.eric_suwarno.penilaianlab.sesi.SesiLogin;

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView navList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Integer id_aslab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_aslab);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        //getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        //Toast.makeText(this, session.toString(), Toast.LENGTH_LONG).show();

        if(getIntent().hasExtra("id_aslab")){
            id_aslab = Integer.parseInt(getIntent().getStringExtra("id_aslab"));
        }else{
            Toast.makeText(this, "Sepertinya, Anda tidak login, silakan login terlebih dahulu.", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, Utama.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navList = (ListView) findViewById(R.id.navlist);
        ArrayList<String> navArray = new ArrayList<String>();
        String[] arr = getResources().getStringArray(R.array.menu_aslab);
        for(int i = 0; i < arr.length; i++){
            navArray.add(arr[i]);
        }

        navList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, navArray);

        navList.setAdapter(arrayAdapter);
        navList.setOnItemClickListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        //actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            throw new AssertionError();
        } else {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            fragmentManager = getSupportFragmentManager();

            loadSelection(0);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void loadSelection(int position) {
        navList.setItemChecked(position, true);

        Bundle args = new Bundle();

        switch(position){
            case 0:
                Home home = new Home();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, home);
                fragmentTransaction.commit();
                break;
            case 1:
                Kelas kelas = new Kelas();
                args = new Bundle();
                args.putInt("id_aslab", id_aslab);
                kelas.setArguments(args);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, kelas);
                fragmentTransaction.commit();
                break;
            case 2:
                Komponen komponen = new Komponen();
                args = new Bundle();
                args.putInt("id_aslab", id_aslab);
                komponen.setArguments(args);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, komponen);
                fragmentTransaction.commit();
                break;
            case 3:
                Aktivitas aktivitas = new Aktivitas();
                args = new Bundle();
                args.putInt("id_aslab", id_aslab);
                aktivitas.setArguments(args);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, aktivitas);
                fragmentTransaction.commit();
                break;
            case 4:
                Penilaian penilaian = new Penilaian();
                args = new Bundle();
                args.putInt("id_aslab", id_aslab);
                penilaian.setArguments(args);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentholder, penilaian);
                fragmentTransaction.commit();
                break;
            case 5:
                new AlertDialog.Builder(this)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadSelection(position);
        drawerLayout.closeDrawer(navList);
    }
}
