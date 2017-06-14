package ptin.smartcity.loginService;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import ptin.smartcity.storage.DataStorage;

public class LoginSocket {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private int SERVER_PORT = 10001;
    private String SERVER_IP = "192.168.1.40";

    private static final String STATUS_LOGIN = "Login";
    private static final String STATUS_SIGNUP = "SignUp";

    public LoginSocket() throws IOException {
        // Obtenim la IP i el port del servidor
        if ( !DataStorage.has("ipAddress") || !DataStorage.has("port") ) {
            throw new IOException("There is no socket data available!");
        }

        this.SERVER_IP = DataStorage.getData("ipAddress");
        this.SERVER_PORT = Integer.parseInt( DataStorage.getData("port") );

        this.socket = new Socket();
        try {
            // Connectem al servidor
            this.socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), 10000);

            // Creem els buffers de dades.
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
        } catch (SocketException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Tanquem el socket
    public void close() {
        try {
            this.out.flush();
            this.out.close();
            this.socket.close();
        } catch ( IOException e ) { e.printStackTrace(); }
    }

    /**
     * Operation functions
     */
    public boolean login(String email, String password) {
        // We create a JSONObject
        JSONObject jsonObject = new JSONObject();

        // We add all the values
        try {
            jsonObject.put("Status", STATUS_LOGIN);
            jsonObject.put("Email", email);
            jsonObject.put("Password", password);
        } catch ( JSONException e ) { e.printStackTrace(); }

        try {
            sendMessage(jsonObject);
        } catch (Exception e) {
            Log.e("LoginSocket", "Cannot send message: " + jsonObject.toString() );
            return false;
        }

        // We wait for an "OK" or "ERROR".
        return receiveMessage();
    }

    public boolean register(String name, String surname, String email, String password) {
        // We create a JSONObject
        JSONObject jsonObject = new JSONObject();

        // We add all the values
        try {
            jsonObject.put("Status", STATUS_SIGNUP);
            jsonObject.put("Name", name);
            jsonObject.put("Surname", surname);
            jsonObject.put("Email", email);
            jsonObject.put("Password", password);
        } catch ( JSONException e ) { e.printStackTrace(); }

        // We send the message
        try {
            sendMessage(jsonObject);
        } catch (Exception e) {
            Log.e("LoginSocket", "Cannot send message: " + jsonObject.toString() );
            return false;
        }

        // We wait for an "OK" or "ERROR".
        return receiveMessage();
    }

    /**
     * Common functions
     */
    private void sendMessage(JSONObject jsonObject) throws Exception {
        // We send the data
        try {
            this.out.print( jsonObject.toString() );
            this.out.flush();
            Log.d("LoginSocket", "Sent Message: " + jsonObject.toString() );

        } catch (Exception e) { throw e; }
    }

    private boolean receiveMessage() {
        // Get message
        String incomingMessage;

        try {
            while (true) {
                incomingMessage = in.readLine();
                if (incomingMessage != null) break;
            }

            Log.d("LoginSocket", "Received Message: " + incomingMessage);

        } catch (IOException e) { return false; }

        // Decode JSON
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(incomingMessage);

            // Check message
            if ( jsonObject.getString("Status").compareTo("OK") == 0 ) return true;
            else return false;

        } catch (JSONException e) { return false; }
    }
}
