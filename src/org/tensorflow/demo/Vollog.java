package org.tensorflow.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Vollog extends AppCompatActivity {

    ImageView imgbckfromvllog,imggotovolreg;
    Button btnvolsub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vollog);

        imgbckfromvllog.findViewById(R.id.bckfrmvollog);
        imggotovolreg.findViewById(R.id.vollogreg);
        btnvolsub.findViewById(R.id.btnLoginvol);

        imggotovolreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Vollog.this, Volreg.class));
                finish();

            }
        });
        imgbckfromvllog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vollog.this, Selectopt.class));
                finish();
            }
        });

        Animation myAnim1 = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MybounceInterpolator interpolator = new MybounceInterpolator(0.2, 20);
        myAnim1.setInterpolator(interpolator);
        btnvolsub.startAnimation(myAnim1);
        btnvolsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Vollog.this, Homevol.class));
                finish();
            }
        });
    }
}
