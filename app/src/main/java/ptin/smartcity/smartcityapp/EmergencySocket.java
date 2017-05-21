package ptin.smartcity.smartcityapp;

import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class EmergencySocket {

    private Socket socket;
    private PrintWriter out;

    private int SERVER_PORT;
    private String SERVER_IP;

    public EmergencySocket() throws IOException {
        // Obtenim la IP i el port del servidor
        if ( !DataStorage.has("ipAddress") || !DataStorage.has("port") ) {
            throw new IOException("There is no socket data available!");
        }

        this.SERVER_IP = DataStorage.getData("ipAddress");
        this.SERVER_PORT = Integer.parseInt( DataStorage.getData("port") );

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Connectem al servidor
        try {
            this.socket = new Socket(SERVER_IP, SERVER_PORT);
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Enviar missatge
    public boolean sendMessage() {

        // Retrive data from filesystem
        String name = DataStorage.getData("Name");
        String surname = DataStorage.getData("Surname");
        String gender = DataStorage.getData("Gender");
        String birthday = DataStorage.getData("Birthday");
        String phoneNumber = DataStorage.getData("PhoneNumber");
        String comments = DataStorage.getData("Comments");

        // Obtenim la localització
        String latitude = String.valueOf( LocationService.getLatitude() );
        String longitude = String.valueOf( LocationService.getLongitude() );

        // We create a JSONObject
        JSONObject jsonObject = new JSONObject();

        // We add all the values
        try {
            jsonObject.put("EmergencyType", MainActivity.EMERGENCY_OPTION);
            jsonObject.put("Name", name);
            jsonObject.put("Surname", surname);
            jsonObject.put("Gender", gender);
            jsonObject.put("Birthday", birthday);
            jsonObject.put("PhoneNumber", phoneNumber);
            jsonObject.put("Comments", comments);
            jsonObject.put("Latitude", latitude);
            jsonObject.put("Longitude", longitude);
        } catch ( JSONException e ) { e.printStackTrace(); }

        try {
            this.out.print( jsonObject.toString() );
            this.out.flush();
            Log.d(this.getClass().toString(), "Sent Message: " + jsonObject.toString() );
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Tanquem el socket
    public void close() {
        try {
            this.out.flush();
            this.out.close();
            this.socket.close();
        } catch ( IOException e ) { e.printStackTrace(); }
    }
}
