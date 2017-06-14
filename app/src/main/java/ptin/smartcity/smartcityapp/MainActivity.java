package ptin.smartcity.smartcityapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import ptin.smartcity.mqttService.MQTTAsyncHandler;
import ptin.smartcity.storage.DataStorage;
import ptin.smartcity.services.LocationService;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private DrawerLayout drawer;
    private ImageButton _SOSButton;
    public static String EMERGENCY_OPTION = "AMBULANCE";
    private Intent emergency;
    private DataStorage dataStorage;
    private LocationService locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialitzem el sistema d'emmagatzematge
        dataStorage = new DataStorage(this);

        // Inicialitzem la pantalla de login
        if ( !DataStorage.has("account_email") || !DataStorage.has("account_password") ) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

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

        // Iniciem el sistema de localització
        locationListener = new LocationService(this);
    }

    @Override
    public void onBackPressed() {
        // Si el menú lateral està obert, tancar-lo.
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);

        // En cas contrari, tancar l'aplicació.
        else super.onBackPressed();
    }

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
                // Iniciem la pantalla de configuració
                Intent settings = new Intent(this, ConfigActivity.class);
                startActivity(settings);
                break;
            case R.id.nav_signOut:
                // Eliminem totes les dades i sortim
                DataStorage.clearAll();
                this.finish();
            break;
        }

        // Tanquem el menú
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(final View v) {
        // Desactivem el botó
        _SOSButton.setEnabled(false);

        // Creem una tasca en un altre procés per al login
        MQTTAsyncHandler mqttAsyncHandler = new MQTTAsyncHandler(this);
        mqttAsyncHandler.execute();

    }

    public void onSuccess() {
        _SOSButton.setEnabled(true);

        // Iniciem la pantalla d'emergències
        emergency.putExtra("EMERGENCY_OPTION", EMERGENCY_OPTION);
        startActivity(emergency);
    }

    public void onFailed() {
        // Mostrem alerta d'error
        sendErrorAlert(findViewById(R.id.drawer_layout));

        // Activem el botó
        _SOSButton.setEnabled(true);
    }

    private void sendErrorAlert(View v) {
        // Alert!
        Snackbar.make(v, R.string.ERROR_sending_message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // Si el permís s'ha concedit, PERFECTE!!
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}

                // Sinó, tanquem l'app
                else this.finishAndRemoveTask();
            }
        }
    }
}
