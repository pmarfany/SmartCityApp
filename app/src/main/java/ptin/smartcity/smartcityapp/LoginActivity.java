package ptin.smartcity.smartcityapp;

import android.os.Bundle;

import android.content.Intent;

import ptin.smartcity.storage.DataChecker;
import ptin.smartcity.loginService.LoginAsync;

public class LoginActivity extends LoginMasterActivity {

    // Ejecutar el crear la pantalla
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // We set the layout that we are using
        setContentView(R.layout.activity_login);

        // We call the super class
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Intent openOtherScreen() {
        return new Intent(getApplicationContext(), SignupActivity.class);
    }

    @Override
    protected void overridePendingTransition() {
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    // Funció per a autenticar-se
    public void acceptButtonAction() {
        // Comprovar que els camps siguin correctes
        if ( !validate() ) {
            onFailed();
            return;
        }

        // Desactivem el botó de SignUp
        acceptButton.setEnabled(false);

        // Obtenim els valors
        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        // Creem una tasca en un altre procés per al login
        LoginAsync loginAsync = new LoginAsync(this);
        loginAsync.execute(email, password);
    }

    // Funció de validació dels camps
    public boolean validate() {
        boolean valid = true;

        DataChecker dataChecker = new DataChecker(this);

        valid = valid && dataChecker.checkField(_emailField, DataChecker.TYPE_EMAIL);
        valid = valid && dataChecker.checkField(_passwordField, DataChecker.TYPE_PASSWORD);

        return valid;
    }
}
