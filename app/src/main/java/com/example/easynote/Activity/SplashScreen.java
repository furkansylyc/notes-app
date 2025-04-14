package com.example.easynote.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.easynote.MainActivity;
import com.example.easynote.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        ProgressBar progressBar = findViewById(R.id.progressBar);
        ImageView logo = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.textView);
        TextView subtitle = findViewById(R.id.textView2);
        ImageView bottomLogo = findViewById(R.id.imageView2);

        ObjectAnimator logoSlideDown = ObjectAnimator.ofFloat(logo, "translationY", -500f, 0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0.5f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.5f, 1.2f, 1f);
        ObjectAnimator fadeInLogo = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.playTogether(logoSlideDown, scaleX, scaleY, fadeInLogo);
        logoAnim.setDuration(1000);

        ObjectAnimator titleSlideUp = ObjectAnimator.ofFloat(title, "translationY", 100f, 0f);
        ObjectAnimator titleFadeIn = ObjectAnimator.ofFloat(title, "alpha", 0f, 1f);
        AnimatorSet titleAnim = new AnimatorSet();
        titleAnim.playTogether(titleSlideUp, titleFadeIn);
        titleAnim.setDuration(800);
        titleAnim.setStartDelay(400);

        ObjectAnimator subtitleSlideUp = ObjectAnimator.ofFloat(subtitle, "translationY", 100f, 0f);
        ObjectAnimator subtitleFadeIn = ObjectAnimator.ofFloat(subtitle, "alpha", 0f, 1f);
        AnimatorSet subtitleAnim = new AnimatorSet();
        subtitleAnim.playTogether(subtitleSlideUp, subtitleFadeIn);
        subtitleAnim.setDuration(800);
        subtitleAnim.setStartDelay(600);

        ObjectAnimator progressScaleX = ObjectAnimator.ofFloat(progressBar, "scaleX", 0.8f, 1f, 0.9f, 1f);
        ObjectAnimator progressScaleY = ObjectAnimator.ofFloat(progressBar, "scaleY", 0.8f, 1f, 0.9f, 1f);
        AnimatorSet progressAnim = new AnimatorSet();
        progressAnim.playTogether(progressScaleX, progressScaleY);
        progressAnim.setDuration(1500);
        progressAnim.setStartDelay(1000);

        ObjectAnimator rotateBottomLogo = ObjectAnimator.ofFloat(bottomLogo, "rotation", 0f, 360f);
        rotateBottomLogo.setDuration(1000);
        rotateBottomLogo.setStartDelay(1200);

        AnimatorSet allAnimations = new AnimatorSet();
        allAnimations.playSequentially(logoAnim, titleAnim, subtitleAnim, progressAnim, rotateBottomLogo);
        allAnimations.start();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }, 2000);
    }
}
