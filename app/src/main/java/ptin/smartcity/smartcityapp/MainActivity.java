package ptin.smartcity.smartcityapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   View.OnClickListener {

    // Layout del menú lateral
    private DrawerLayout drawer;

    // Botó de SOS
    private ImageButton _SOSButton;

    // Sel·lecció del servei d'emergencies
    private String EMERGENCY_OPTION = "AMBULANCE";

    // "Accés" pantalla emergències
    Intent emergency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Inicialitzem la pantalla de login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);*/

        // Configurar toolbar de l'aplicació
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Configurar layout principal
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar menú lateral
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Seleccionem opció "Ambulance" per defecte
        navigationView.setCheckedItem(R.id.nav_ambulance);

        // SOSButton: Comportament
        _SOSButton = (ImageButton) findViewById(R.id.btn_sos);
        _SOSButton.setOnClickListener(this);

        // Inicialitzem la pantalla d'emergències
        emergency = new Intent(this, EmergencyActivity.class);
    }

    /* Check IP Address
    public boolean checkIP() {
        // Obtenir valors
        String ip_address = _IPField.getText().toString();

        // EMAIL: Comprovar que el camp no estigui buit i que sigui una adreça IP
        if (ip_address.isEmpty() || !Patterns.IP_ADDRESS.matcher(ip_address).matches()) {
            _IPField.setError("IP Address not valid");
            return false;
        } else {
            _IPField.setError(null);
            return true;
        }
    }*/


    @Override
    public void onBackPressed() {
        // Si el menú lateral està obert, tancar-lo.
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);

            // En cas contrari, tancar l'aplicació.
        else super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_ambulance:
                EMERGENCY_OPTION = "AMBULANCE";
                break;
            case R.id.nav_firefighter:
                EMERGENCY_OPTION = "FIREFIGHTER";
                break;
            case R.id.nav_police:
                EMERGENCY_OPTION = "POLICE";
                break;
            case R.id.nav_settings:
                // Handle the settings action
                break;
        }

        // Tanquem el menú
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        // Desactivem el botó
        _SOSButton.setEnabled(false);

        // TODO: Comportament (localització)

        // Iniciem la comunicació
        //EmergencySocket communicacio = new EmergencySocket( _IPField.getText().toString() );
        //communicacio.sendMessage("SOS - Android App");
        //communicacio.close();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.LoginTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending message");
        progressDialog.show();

        // TEMPORAL: després de 3 segons, Missatge Enviat
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();

                        _SOSButton.setEnabled(true);

                        // Iniciem la pantalla d'emergències
                        emergency.putExtra("EMERGENCY_OPTION", EMERGENCY_OPTION);
                        startActivity(emergency);
                    }
                }, 3000);
    }
}
