package ptin.smartcity.smartcityapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EmergencyActivity extends AppCompatActivity {

    @Bind(R.id.loading_dots) ImageView img;
    @Bind(R.id.ambulance) ImageView ambulance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        ButterKnife.bind(this);

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
}
