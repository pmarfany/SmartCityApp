package ptin.smartcity.smartcityapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import ptin.smartcity.storage.DataChecker;
import ptin.smartcity.storage.DataStorage;
import ptin.smartcity.services.DatePickerFragment;

public class ConfigActivity extends AppCompatActivity {

    // Camps de dades personals
    private EditText nameField;
    private EditText surnameField;
    private Button genderButton;
    private Button birthdayButton;
    private EditText phoneNumberField;
    private EditText commentsField;

    // Camps de dades del servidor
    private EditText ipAddressField;
    private EditText portField;
    private EditText MQTTipAddressField;
    private EditText MQTTportField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Create toolbar
        createToolbar();

        // Initilize variables
        initVariables();

        // Set variables values
        setValues();
    }

    // Creem la barra superior
    private void createToolbar() {
        // Creem la barra superior
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Afegim el botó d'enrradera
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        // Configurem la barra superior
        setSupportActionBar(toolbar);
    }

    // Iniciem les variables "d'Android"
    private void initVariables() {
        // Camps de dades personals
        nameField = (EditText) findViewById(R.id.config_Name);
        surnameField = (EditText) findViewById(R.id.config_Surname);
        genderButton = (Button) findViewById(R.id.config_gender);
        birthdayButton = (Button) findViewById(R.id.config_birthday);
        phoneNumberField = (EditText) findViewById(R.id.config_phoneNumber);
        commentsField = (EditText) findViewById(R.id.config_comments);

        // Camps de dades del servidor
        ipAddressField = (EditText) findViewById(R.id.settings_ipAddress);
        portField = (EditText) findViewById(R.id.settings_port);

        MQTTipAddressField = (EditText) findViewById(R.id.settings_MQTTipAddress);
        MQTTportField = (EditText) findViewById(R.id.settings_MQTTport);
    }

    // Posem els valors anteriorment guardats en els seus camps corresponents
    private void setValues() {
        // Camps de dades personals
        nameField.setText( DataStorage.getData("Name") );
        surnameField.setText( DataStorage.getData("Surname") );

        if ( DataStorage.has("Gender") ) {
            genderButton.setText(DataStorage.getData("Gender"));
            genderButton.setTextColor( ContextCompat.getColor(ConfigActivity.this, R.color.black) );
        }
        if ( DataStorage.has("Birthday") ) {
            birthdayButton.setText(DataStorage.getData("Birthday"));
            birthdayButton.setTextColor( ContextCompat.getColor(ConfigActivity.this, R.color.black) );
        }

        phoneNumberField.setText( DataStorage.getData("PhoneNumber") );
        commentsField.setText( DataStorage.getData("Comments") );

        // Camps de dades del servidor
        ipAddressField.setText( DataStorage.getData("ipAddress") );
        portField.setText( DataStorage.getData("port") );

        MQTTipAddressField.setText( DataStorage.getData("MQTTipAddress") );
        MQTTportField.setText( DataStorage.getData("MQTTport") );
    }

    /**
     * Funcionalitat de la Pantalla
     */

    // Mostra el diàleg de selecció de data
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment) newFragment).setActivity(this);

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Mostra el diàleg de selecció de gènere
    public void showGenderDialog(View v) {
        // Creem el sub-menú
        PopupMenu popup = new PopupMenu(this, genderButton, Gravity.RIGHT);

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_gender, popup.getMenu());

        // Registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                genderButton.setText( item.getTitle() );
                genderButton.setTextColor( ContextCompat.getColor(ConfigActivity.this, R.color.black) );
                return true;
            }
        });

        popup.show(); //showing popup menu

    }

    /**
     * Menús
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_done:
                // Comprovem que els camps estigui correctament omplerts
                if ( checkFields() ) {
                    // Guardar dades
                    DataStorage.putString("Name", nameField.getText().toString());
                    DataStorage.putString("Surname", surnameField.getText().toString());
                    DataStorage.putString("Gender", genderButton.getText().toString());
                    DataStorage.putString("Birthday", birthdayButton.getText().toString());
                    DataStorage.putString("PhoneNumber", phoneNumberField.getText().toString());
                    DataStorage.putString("Comments", commentsField.getText().toString());

                    DataStorage.putString("ipAddress", ipAddressField.getText().toString());
                    DataStorage.putString("port", portField.getText().toString());

                    DataStorage.putString("MQTTipAddress", MQTTipAddressField.getText().toString());
                    DataStorage.putString("MQTTport", MQTTportField.getText().toString());

                    // Tanquem l'activitat
                    this.finish();
                }
                return true;

            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkFields() {
        boolean valid = true;

        DataChecker dataChecker = new DataChecker(this);

        // We check all the fields and we keep a reminder
        valid = valid && dataChecker.checkField(nameField, DataChecker.TYPE_TEXT);
        valid = valid && dataChecker.checkField(surnameField, DataChecker.TYPE_TEXT);
        valid = valid && dataChecker.checkField(genderButton, DataChecker.TYPE_GENDER);
        valid = valid && dataChecker.checkField(birthdayButton, DataChecker.TYPE_DATE);
        valid = valid && dataChecker.checkField(phoneNumberField, DataChecker.TYPE_PHONE);

        valid = valid && dataChecker.checkField(ipAddressField, DataChecker.TYPE_IPADDRESS);
        valid = valid && dataChecker.checkField(portField, DataChecker.TYPE_TEXT);

        valid = valid && dataChecker.checkField(MQTTipAddressField, DataChecker.TYPE_IPADDRESS);
        valid = valid && dataChecker.checkField(MQTTportField, DataChecker.TYPE_TEXT);

        return valid;
    }
}
