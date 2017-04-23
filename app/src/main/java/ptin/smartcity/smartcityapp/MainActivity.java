package ptin.smartcity.smartcityapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;


public class MainActivity extends AppCompatActivity {

    // Obtenim els camps de la pantalla
    @Bind(R.id.IP_input) EditText _IPField;
    @Bind(R.id.btn_sos) ImageButton _SOSButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Inicialitzem la pantalla de login
        Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);

        // Inicialitzem la pantalla d'emergències
        final Intent emergency = new Intent(this, EmergencyActivity.class);

        // SignUp Button: Comportament
        _SOSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            _SOSButton.setEnabled(false);

            if ( !checkIP() ) {
                Toast.makeText(getBaseContext(), "IP Address not valid", Toast.LENGTH_SHORT).show();
                _SOSButton.setEnabled(true);
                return;
            }

            // TODO: Comportament (localització)
            EmergencySocket communicacio = new EmergencySocket( _IPField.getText().toString() );
            communicacio.sendMessage("SOS - Android App");
            communicacio.close();

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.LoginTheme_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage( "Sending message" );
            progressDialog.show();

            // TEMPORAL: després de 3 segons, Missatge Enviat
            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();

                        // Iniciem la pantalla d'emergències
                        startActivity(emergency);
                    }
                }, 3000);

            _SOSButton.setEnabled(true);
            }
        });

    }

    // Check IP Address
    public boolean checkIP() {
        // Obtenir valors
        String ip_address = _IPField.getText().toString();

        // EMAIL: Comprovar que el camp no estigui buit i que sigui una adreça IP
        if ( ip_address.isEmpty() || !Patterns.IP_ADDRESS.matcher(ip_address).matches() ) {
            _IPField.setError( "IP Address not valid" );
            return false;
        } else {
            _IPField.setError(null);
            return true;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
