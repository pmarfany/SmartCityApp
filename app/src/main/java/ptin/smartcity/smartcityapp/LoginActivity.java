package ptin.smartcity.smartcityapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    // Obtenim els camps de la pantalla
    @Bind(R.id.input_email) EditText _emailField;
    @Bind(R.id.input_password) EditText _passwordField;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    // Ejecutar el crear la pantalla
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Login Button: Comportament
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { login(); }
        });

        // SignUp Button: Comportament
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start "SignupActivity"
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
            }
        });
        
    }

    // Funció per a autenticar-se
    public void login() {
        // Comprovar que els camps siguin correctes
        if ( !validate() ) {
            onLoginFailed();
            return;
        }

        // Desactivem el botó de Login
        _loginButton.setEnabled(false);

        // Obrim una pantalla de procés d'autenticació
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.LoginTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage( getString(R.string.login_authenticating) );
        progressDialog.show();

        // Obtenim els valors
        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        // TODO: Implement your own authentication logic here.

        // TEMPORAL: després de 3 segons, autenticar correcte!
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    // Executem en retornar de la pantalla de registre
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Si és la pantalla de registre
        if (requestCode == REQUEST_SIGNUP) {
            // Ens hem registrat correctament
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here

                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    // Disable going back to the MainActivity
    @Override
    public void onBackPressed() {}

    // Autenticació correcta
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        this.finish();
    }

    // Error d'autenticació
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.loginFailed), Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    // Funció de validació dels camps
    public boolean validate() {
        boolean valid = true;

        // Obtenir valors
        String email = _emailField.getText().toString();
        String password = _passwordField.getText().toString();

        // EMAIL: Comprovar que el camp no estigui buit i que sigui un correu electrònic
        if ( email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
            _emailField.setError( getString(R.string.ERROR_emailField) );
            valid = false;
        } else _emailField.setError(null);

        // PASSWORD: Comprovar que el camp no estigui buit.
        if ( password.isEmpty() ) {
            _passwordField.setError( getString(R.string.ERROR_passwordField) );
            valid = false;
        } else _passwordField.setError(null);

        return valid;
    }
}
