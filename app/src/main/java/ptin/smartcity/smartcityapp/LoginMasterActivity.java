package ptin.smartcity.smartcityapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ptin.smartcity.storage.DataStorage;

/**
 * Created by PauMarfany on 14/6/17.
 */

public abstract class LoginMasterActivity extends AppCompatActivity {

    protected Button acceptButton;
    protected TextView otherScreenLink;

    protected EditText _emailField;
    protected EditText _passwordField;

    // Ejecutar el crear la pantalla
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load "basic" fields
        acceptButton = (Button) findViewById(R.id.btn_accept);
        otherScreenLink = (TextView) findViewById(R.id.otherScreenLink);
        _emailField = (EditText) findViewById(R.id.input_email);
        _passwordField = (EditText) findViewById(R.id.input_password);

        // SignUp Button: Comportament
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { acceptButtonAction(); }
        });

        // Login Button: Comportament
        otherScreenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = openOtherScreen();
                finish();
                startActivity(intent);
                overridePendingTransition();
            }
        });
    }

    protected abstract Intent openOtherScreen();

    protected abstract void overridePendingTransition();

    public abstract void acceptButtonAction();

    // Disable going back to the MainActivity
    @Override
    public void onBackPressed() { }

    // Autenticació correcta
    public void onSuccess() {
        acceptButton.setEnabled(true);

        String email = _emailField.getText().toString();
        String password = _emailField.getText().toString();

        DataStorage.putString("account_email", email);
        DataStorage.putString("account_password", password);

        this.finish();
    }

    // Error de registre
    public void onFailed() {
        Snackbar.make(findViewById(R.id.loginScrollView), getString(R.string.loginFailed), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        acceptButton.setEnabled(true);
    }

    // Validació dels camps
    public abstract boolean validate();

}
