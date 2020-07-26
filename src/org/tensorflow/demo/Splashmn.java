package org.tensorflow.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splashmn extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;
    public static boolean ifVol = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv=(TextView)findViewById(R.id.splastv);
        iv=(ImageView)findViewById(R.id.splasiv);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.setAnimation(myanim);
        tv.setAnimation(myanim);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("pNo","");
        String type = sharedPreferences.getString("type", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (name.isEmpty()){
                    startActivity(new Intent(Splashmn.this, Selectopt.class));
                    finish();
                } else {
                    if (!type.equals("vol")) {
                        startActivity(new Intent(Splashmn.this, Homescreen.class));
                        finish();
                    }else {
                        startActivity(new Intent(Splashmn.this, Homevol.class));
                        finish();
                    }
                }
            }
        }, 1000);
    }
}
