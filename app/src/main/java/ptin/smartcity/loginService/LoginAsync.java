package ptin.smartcity.loginService;

import java.io.IOException;

import ptin.smartcity.smartcityapp.LoginMasterActivity;

/**
 * Created by PauMarfany on 14/6/17.
 */
public class LoginAsync extends AsyncHandler {

    public LoginAsync(LoginMasterActivity act) { super(act); }

    @Override
    protected Boolean doInBackground(String... params) {
        // Comunicació amb el server
        LoginSocket loginSocket;
        try {
            // Creem el socket
            loginSocket = new LoginSocket();

            // Enviar missatge
            final boolean response = loginSocket.login(params[0], params[1]);

            // Tancar connexió
            loginSocket.close();

            // Return
            return response;
        } catch (IOException e) { return false; }
    }
}