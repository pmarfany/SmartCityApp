package ptin.smartcity.smartcityapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class DataStorage {

    private static final String PREFS_NAME = "SmartApp_Prefs";
    private static SharedPreferences settings;

    public DataStorage(AppCompatActivity act) {
        // We create a "SharedPreferences object"
        settings = act.getSharedPreferences(PREFS_NAME, 0);
    }

    public static void putString(String key, String val) {
        // We create an editor
        SharedPreferences.Editor editor = settings.edit();

        // We edit the value
        editor.putString(key, val);

        // Commit the edits!
        editor.commit();
    }

    public static String getData(String key) {
        // We return the string (or an empty one if does not exist)
        return settings.getString(key, "");
    }

    public static boolean has(String key) { return settings.contains(key); }

}
