package id.web.eric_suwarno.penilaianlab.sesi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import id.web.eric_suwarno.penilaianlab.Utama;

/**
 * Created by Eric Ng on 2016-01-20.
 */
public class SesiLogin {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "SessionPref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAMA_ASLAB = "namaAslab";
    private static final String KEY_ID_ASLAB = "idAslab";

    public SesiLogin(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String username, String namaAslab, String idAslab) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_NAMA_ASLAB, namaAslab);
        editor.putInt(KEY_ID_ASLAB, ( idAslab.equals("") ? 0 : Integer.parseInt(idAslab) ));

        editor.commit();
    }

    public boolean checkLogin() {
        if(!this.isUserLoggedIn()) {
            Intent i = new Intent(_context, Utama.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

            return true;
        }

        return false;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_ID_ASLAB, String.valueOf(pref.getInt(KEY_ID_ASLAB, 0)));
        user.put(KEY_NAMA_ASLAB, pref.getString(KEY_NAMA_ASLAB, null));

        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, Utama.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
