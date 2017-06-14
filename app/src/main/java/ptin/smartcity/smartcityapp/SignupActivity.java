package ptin.smartcity.smartcityapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import ptin.smartcity.loginService.SignUpAsync;
import ptin.smartcity.storage.DataChecker;
import ptin.smartcity.storage.DataStorage;

public class SignupActivity extends LoginMasterActivity {

    // Obtenim els camps de la pantalla
    private EditText _nameField;
    private EditText _surnameField;
    private EditText _reEnterPasswordField;

    // Ejecutar el crear la pantalla
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // We set the layout that we are using
        setContentView(R.layout.activity_signup);

        // Load "our" fields
        _nameField = (EditText) findViewById(R.id.input_name);
        _surnameField = (EditText) findViewById(R.id.input_surname);
        _reEnterPasswordField = (EditText) findViewById(R.id.input_reEnterPassword);

        // We call the super class
        super.onCreate(savedInstanceState);
    }

    protected Intent openOtherScreen() {
        return new Intent(getApplicationContext(), LoginActivity.class);
    }

    protected void overridePendingTransition() {
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
    }

    @Override
    public void onSuccess() {
        // Emmagatzem les dades ja entrades
        String name = _nameField.getText().toString();
        String surname = _surnameField.getText().toString();

        DataStorage.putString("Name", name);
        DataStorage.putString("Surname", surname);

        super.onSuccess();
    }

    // Funció per a registrar-se
    public void acceptButtonAction() {
        // Comprovar que els camps siguin correctes
        if ( !validate() ) {
            onFailed();
            return;
        }

        // Desactivem el botó de SignUp
        acceptButton.setEnabled(false);

        // Obtenim els valors
        String name = _nameField.getText().toString();
        String surname = _surnameField.getText().toString();
        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        // Creem una tasca en un altre procés per al login
        SignUpAsync signUpAsync = new SignUpAsync(this);
        signUpAsync.execute(name, surname, email, password);
    }

    // Funció de validació dels camps
    public boolean validate() {
        boolean valid = true;

        DataChecker dataChecker = new DataChecker(this);

        valid = valid && dataChecker.checkField(_nameField, DataChecker.TYPE_TEXT);
        valid = valid && dataChecker.checkField(_surnameField, DataChecker.TYPE_TEXT);
        valid = valid && dataChecker.checkField(_emailField, DataChecker.TYPE_EMAIL);
        valid = valid && dataChecker.checkField(_passwordField, DataChecker.TYPE_PASSWORD);
        valid = valid && dataChecker.checkField(_reEnterPasswordField, DataChecker.TYPE_PASSWORD);

        // Additional check for password matching
        String password = _passwordField.getText().toString();
        String reEnterPassword = _reEnterPasswordField.getText().toString();

        if ( valid && !reEnterPassword.equals(password) ) {
            _reEnterPasswordField.setError( getString(R.string.ERROR_Field) );
            valid = false;
        } else _reEnterPasswordField.setError(null);

        return valid;
    }
}