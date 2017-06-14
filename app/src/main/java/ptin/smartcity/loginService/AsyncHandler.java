package ptin.smartcity.loginService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import ptin.smartcity.smartcityapp.LoginActivity;
import ptin.smartcity.smartcityapp.LoginMasterActivity;
import ptin.smartcity.smartcityapp.R;

/**
 * Created by PauMarfany on 14/6/17.
 */

public abstract class AsyncHandler extends AsyncTask<String, Boolean, Boolean> {

    private ProgressDialog progressDialog;
    private LoginMasterActivity activity;
    private Context mContext;

    public AsyncHandler(LoginMasterActivity act) {
        super();
        activity = act;
        mContext = act.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        // Creem el diàleg
        progressDialog = new ProgressDialog(activity, R.style.LoginTheme_Dialog);
        progressDialog.setMessage( mContext.getString(R.string.login_authenticating) );

        // Propietats
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);

        // Cancelar
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override public void onCancel(DialogInterface dialog) {
                AsyncHandler.this.cancel(true);
            }
        });

        // Run
        progressDialog.show();
    }

    @Override
    protected abstract Boolean doInBackground(String... params);

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
