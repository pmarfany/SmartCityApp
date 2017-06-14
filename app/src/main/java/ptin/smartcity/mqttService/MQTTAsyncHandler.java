package ptin.smartcity.mqttService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.IOException;

import ptin.smartcity.smartcityapp.MainActivity;
import ptin.smartcity.smartcityapp.R;

/**
 * Created by PauMarfany on 14/6/17.
 */

public class MQTTAsyncHandler extends AsyncTask<Object, Boolean, Boolean> {

    private ProgressDialog progressDialog;
    private MainActivity activity;
    private Context mContext;

    public MQTTAsyncHandler(MainActivity act) {
        super();
        activity = act;
        mContext = act.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        // Creem el diàleg
        progressDialog = new ProgressDialog(activity, R.style.LoginTheme_Dialog);
        progressDialog.setMessage( "Sending message" );

        // Propietats
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        // Cancelar
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                MQTTAsyncHandler.this.cancel(true);
            }
        });

        // Run
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        // Comunicació amb el server
        MQTTCommunication mqttCommunication;
        try {
            // Comunicació MQTT
            mqttCommunication = new MQTTCommunication(mContext);

            mqttCommunication.subscribeToTopic();

            mqttCommunication.sendMessage();

            mqttCommunication.disconnect();

            // Return
            return true;
        } catch (IOException e) { return false; }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();

        if ( result ) activity.onSuccess();
        else activity.onFailed();
    }

    @Override
    protected void onCancelled(Boolean aBoolean) { this.onCancelled(); }

    @Override
    protected void onCancelled() {
        progressDialog.dismiss();

        activity.onFailed();
    }

}
