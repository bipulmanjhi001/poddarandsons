package poddarandsons.com.pref;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import poddarandsons.com.main.Splashscreen;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "Bubblespref";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_PHONE = "phone";
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.apply();
    }

    public void userDrawer(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PHONE, user.getPhone());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null) != null && sharedPreferences.getString(KEY_PHONE, null)!= null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString(KEY_TOKEN, null),sharedPreferences.getString(KEY_PHONE, null));
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, Splashscreen.class));
    }
}
