package ptin.smartcity.smartcityapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EmergencyActivity extends AppCompatActivity {

    @Bind(R.id.loading_dots) ImageView img;
    @Bind(R.id.ambulance) ImageView ambulance;
    @Bind(R.id.emergency_text) TextView emergency_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        ButterKnife.bind(this);

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
