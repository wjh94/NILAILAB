package id.web.eric_suwarno.penilaianlab.aslab;

import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import id.web.eric_suwarno.penilaianlab.R;
import id.web.eric_suwarno.penilaianlab.aslab.menus.Home;

public class Main extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_aslab);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawerLayout = (DrawerLayout) findViewById(R.id.layoutDrawerAslab);
        listView = (ListView) findViewById(R.id.navListAslab);

        ArrayList<String> menuNavArray = new ArrayList<>();
        menuNavArray.add("Home");
        menuNavArray.add("Kelas");
        menuNavArray.add("Komponen");
        menuNavArray.add("Aktivitas");
        menuNavArray.add("Penilaian");
        menuNavArray.add("Logout");

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, menuNavArray);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.bukaDrawer, R.string.tutupDrawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        loadSelection(0);
    }

    private void loadSelection(int pos){
        listView.setItemChecked(pos, true);
        switch(pos) {
            case 0:
                Home home = new Home();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentHolderAslab, home);
                fragmentTransaction.commit();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadSelection(position);

        drawerLayout.closeDrawer(listView);
    }
}
