package ptin.smartcity.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Fernan on 15/05/2017.
 */

public class LocationService implements LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private static double LATITUDE;
    private static double LONGITUDE;

    private static LocationManager locationManager;

    public LocationService(AppCompatActivity activity) {
        // Cridem al constructor superior
        super();

        // Creem un "locationManager"
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        // Comprovem els permisos
        try {
            checkPermissions(activity);

            // Activem l'accés al "servei" de localització
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

            // Inicialitzem el valor de la localització antiga.
            initLocation( locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) );

        } catch ( SecurityException e ) {}
    }

    // Funció de comprovació de permisos
    private void checkPermissions(AppCompatActivity activity) {
        // Check permissions
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Mostrem una pantalla sol·licitant el permís d'ubicació
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // Obtenir latitud
    public static double getLatitude() { return LATITUDE; }

    // Obtenir longitud
    public static double getLongitude() { return LONGITUDE; }

    // Mètode per inicialitzar les coordenades en funció de una localització "loc".
    private void initLocation(Location loc) {
        if (loc != null) {
            this.LATITUDE = loc.getLatitude();
            this.LONGITUDE = loc.getLongitude();
        }
    }

    // Mètode d'actualització de la localització
    @Override
    public void onLocationChanged(Location loc) {
        this.LATITUDE = loc.getLatitude();
        this.LONGITUDE = loc.getLongitude();
    }

    // Mètodes per defecte de la classe "Location Listener".
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}