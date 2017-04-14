package ptin.smartcity.smartcityapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    // Obtenim els camps de la pantalla
    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    // Ejecutar el crear la pantalla
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        // SignUp Button: Comportament
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signup(); }
        });

        // Login Button: Comportament
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
    }

    // Funció per a registrar-se
    public void signup() {
        // Comprovar que els camps siguin correctes
        if ( !validate() ) {
            onSignupFailed();
            return;
        }

        // Desactivem el botó de SignUp
        _signupButton.setEnabled(false);

        // Obrim una pantalla de creació de compte
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.LoginTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage( getString(R.string.signUp_creatingAccount) );
        progressDialog.show();

        // Obtenim els valors
        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        // TEMPORAL: després de 3 segons, autenticar correcte!
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    // Disable going back to the MainActivity
    @Override
    public void onBackPressed() {}

    // Registre correcte
    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    // Error de registre
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.loginFailed), Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    // Funció de validació dels camps
    public boolean validate() {
        boolean valid = true;

        // Obtenir valors
        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // NAME: Comprovar que el camp no estigui buit.
        if (name.isEmpty()) {
            _nameText.setError( getString(R.string.ERROR_nameField) );
            valid = false;
        } else _nameText.setError(null);

        // ADDRESS: Comprovar que el camp no estigui buit.
        if (address.isEmpty()) {
            _addressText.setError( getString(R.string.ERROR_addressField) );
            valid = false;
        } else _addressText.setError(null);

        // EMAIL: Comprovar que el camp no estigui buit i que sigui un correu electrònic
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError( getString(R.string.ERROR_emailField) );
            valid = false;
        } else _emailText.setError(null);

        // MOBILE: Comprovar que el camp no estigui buit i que sigui un mòbil
        if (mobile.isEmpty() || !Patterns.PHONE.matcher(mobile).matches()) {
            _mobileText.setError( getString(R.string.ERROR_phoneNumberField) );
            valid = false;
        } else _mobileText.setError(null);

        // PASSWORD: Comprovar que el camp no estigui buit i que compleixi amb els requisits mínims de longitud
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError( getString(R.string.ERROR_passwordField) );
            valid = false;
        } else _passwordText.setError(null);

        // REPASSWORD: Comprovar que el camp no estigui buit i que compleixi amb els requisits mínims de longitud
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError( getString(R.string.ERROR_passwordsNotMatching) );
            valid = false;
        } else _reEnterPasswordText.setError(null);

        return valid;
    }
}