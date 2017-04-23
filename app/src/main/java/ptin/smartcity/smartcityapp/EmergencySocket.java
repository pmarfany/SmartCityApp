package ptin.smartcity.smartcityapp;

import android.os.StrictMode;
import android.util.Log;

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

    private final int SERVERPORT = 5000;
    private final String SERVER_IP;

    public EmergencySocket(String server_ip) {
        // Obtenim la IP del servidor introduïda
        this.SERVER_IP = server_ip;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Connectem al servidor
        try {
            this.socket = new Socket(SERVER_IP, SERVERPORT);
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
        } catch (UnknownHostException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void sendMessage(String str) {
        try {
            out.print(str);
            out.flush();
            Log.d(this.getClass().toString(), "Sent Message: " + str);
        } catch (Exception e) { e.printStackTrace(); }
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
