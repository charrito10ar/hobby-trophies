package com.marbit.hobbytrophies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.marbit.hobbytrophies.login.SignUpActivity;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Preferences;

public class SplashActivity extends AppCompatActivity {

    protected boolean active = true;
    protected int splashTime = 4000;
    private View layoutLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.layoutLogo = (RelativeLayout) findViewById(R.id.layout_logo_splash);



        Thread splashThread = new Thread(){
            @Override
            public void run(){
                try{
                    int waited = 0;
                    while(active && (waited < splashTime)){
                        sleep(100);
                        if(active){
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e){

                } finally{
                    openApp();
                    interrupt();
                }

            }
        };
        splashThread.start();

    }

    @Override
    public void onResume(){
        super.onResume();
        YoYo.with(Techniques.FadeIn)
                .duration(3000)
                .playOn(layoutLogo);
    }

    private void openApp(){
        if (Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)) {
            Intent intentDashboard = new Intent(getApplicationContext(), MainActivity.class);
            intentDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentDashboard);
        } else {
            Intent intentDashboard = new Intent(getApplicationContext(), SignUpActivity.class);
            intentDashboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentDashboard);
        }

    }
}
