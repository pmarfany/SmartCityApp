package ptin.smartcity.storage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.TextView;

import ptin.smartcity.smartcityapp.R;

/**
 * Created by PauMarfany on 14/6/17.
 */

public class DataChecker {

    // Personal data
    public final static int TYPE_TEXT = 0;
    public final static int TYPE_GENDER = 1;
    public final static int TYPE_DATE = 2;
    public final static int TYPE_PHONE = 3;

    // Login data
    public final static int TYPE_EMAIL = 4;
    public final static int TYPE_PASSWORD = 5;

    // Server data
    public final static int TYPE_IPADDRESS = 6;

    private Context mContext;

    public DataChecker(AppCompatActivity act) { mContext = act.getApplicationContext(); }

    public boolean checkField(TextView field, int type) {
        // We get the text from the field
        String data = field.getText().toString();

        // We get the "state" of the field
        Boolean state = false;
        switch (type) {
            case TYPE_TEXT:
                state = !data.isEmpty();
                break;

            case TYPE_EMAIL:
                state = !data.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(data).matches();
                break;

            case TYPE_GENDER:
                state = data.compareTo( mContext.getString(R.string.config_gender) ) != 0;
                break;

            case TYPE_DATE:
                state = data.compareTo( mContext.getString(R.string.config_Birthday) ) != 0;
                break;

            case TYPE_PHONE:
                state = !data.isEmpty() && Patterns.PHONE.matcher(data).matches();
                break;

            case TYPE_PASSWORD:
                state = !data.isEmpty() && data.length() >= 6;
                break;

            case TYPE_IPADDRESS:
                state = !data.isEmpty() && Patterns.IP_ADDRESS.matcher(data).matches();
                break;
        }

        // Check field and notify error!
        if ( !state ) field.setError( mContext.getString(R.string.ERROR_Field) );
        else field.setError(null);

        // Return "state"
        return state;
    }

}
