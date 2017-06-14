package ptin.smartcity.smartcityapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class EmergencyActivity extends AppCompatActivity {

    private ImageView img;
    private ImageView ambulance;
    private TextView emergency_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        img = (ImageView) findViewById(R.id.loading_dots);
        ambulance = (ImageView) findViewById(R.id.ambulance);
        emergency_text = (TextView) findViewById(R.id.emergency_text);

        // Set text
        setEmergencyText();

        // Shaking car
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
        ambulance.startAnimation(shake);

        // Loading dots
        img.setBackgroundResource(R.drawable.loading_dots_animation);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();
    }

    public void setEmergencyText() {
        // Posem el text en funció del servei
        String option = getIntent().getStringExtra("EMERGENCY_OPTION");

        switch (option) {
            case "AMBULANCE":
                emergency_text.setText(R.string.ambulance_message);
                break;
            case "FIREFIGHTER":
                emergency_text.setText(R.string.firefighter_message);
                break;
            case "POLICE":
                emergency_text.setText(R.string.police_message);
                break;
        }

    }
}
