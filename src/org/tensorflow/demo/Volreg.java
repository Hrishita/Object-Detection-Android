package org.tensorflow.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Volreg extends AppCompatActivity {

    Button btnvolregVolunteerReg;
    ImageView imggtooptVolunteerReg;
    private EditText inputPhoneNumberVolReg;
    Button RegisterVolReg;
    EditText inputUsernameVolReg, inputCityVolReg, inputEmailVolReg;
    boolean forUsernameVolReg, forPhoneNoVolReg, forEmailVolReg, forCityVolReg;
    Boolean EditTextStatusVolReg, checkPasswordVolReg;
    String strUsernameVolReg, strPhoneNoVolReg, strEmailVolReg, strCityVolReg;
    FirebaseUser currentUserVolReg;
    FirebaseAuth firebaseAuthVolReg;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volreg);

        firebaseAuthVolReg = FirebaseAuth.getInstance();
        currentUserVolReg = firebaseAuthVolReg.getCurrentUser();
        mRef.keepSynced(true);

        inputUsernameVolReg = findViewById(R.id.inputUserVolReg);
        inputEmailVolReg = findViewById(R.id.inputEmailVolReg);
        inputPhoneNumberVolReg = findViewById(R.id.inputphoneNoVolReg);
        inputCityVolReg = findViewById(R.id.inputCityVolReg);

        btnvolregVolunteerReg=findViewById(R.id.volregbtnVolunteerReg);
        btnvolregVolunteerReg.setEnabled(false);
        btnvolregVolunteerReg.getBackground().setAlpha(128);

        Animation myAnim1 = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MybounceInterpolator interpolator = new MybounceInterpolator(0.2, 20);
        myAnim1.setInterpolator(interpolator);
        btnvolregVolunteerReg.startAnimation(myAnim1);

        inputUsernameVolReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtUsername = inputUsernameVolReg.getText().toString();
                if (txtUsername.isEmpty()){
                    forUsernameVolReg = false;
                    btnvolregVolunteerReg.setEnabled(false);
                    btnvolregVolunteerReg.getBackground().setAlpha(128);
                }else {
                    forUsernameVolReg = true;
                }

                if (forPhoneNoVolReg && forCityVolReg && forUsernameVolReg && forEmailVolReg){
                    btnvolregVolunteerReg.setEnabled(true);
                    btnvolregVolunteerReg.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        inputEmailVolReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtUsername = inputEmailVolReg.getText().toString();
                if (txtUsername.isEmpty()){
                    forEmailVolReg = false;
                    btnvolregVolunteerReg.setEnabled(false);
                    btnvolregVolunteerReg.getBackground().setAlpha(128);
                }else {
                    forEmailVolReg = true;
                }

                if (forPhoneNoVolReg && forCityVolReg && forUsernameVolReg && forEmailVolReg){
                    btnvolregVolunteerReg.setEnabled(true);
                    btnvolregVolunteerReg.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        inputPhoneNumberVolReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtUsername = inputPhoneNumberVolReg.getText().toString();
                if (txtUsername.isEmpty()){
                    forPhoneNoVolReg = false;
                    btnvolregVolunteerReg.setEnabled(false);
                    btnvolregVolunteerReg.getBackground().setAlpha(128);
                }else {
                    forPhoneNoVolReg = true;
                }

                if (forPhoneNoVolReg && forCityVolReg && forUsernameVolReg && forEmailVolReg){
                    btnvolregVolunteerReg.setEnabled(true);
                    btnvolregVolunteerReg.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        inputCityVolReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtUsername = inputCityVolReg.getText().toString();
                if (txtUsername.isEmpty()){
                    forCityVolReg = false;
                    btnvolregVolunteerReg.setEnabled(false);
                    btnvolregVolunteerReg.getBackground().setAlpha(128);
                }else {
                    forCityVolReg = true;
                }

                if (forPhoneNoVolReg && forCityVolReg && forUsernameVolReg && forEmailVolReg){
                    btnvolregVolunteerReg.setEnabled(true);
                    btnvolregVolunteerReg.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnvolregVolunteerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Volreg.this, Cntrlvol.class));
//                finish();

                isValid();
                String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(EditTextStatusVolReg) {
                    String numberVol = inputPhoneNumberVolReg.getText().toString().trim();
                    if (numberVol.length() != 10) {
                        inputPhoneNumberVolReg.setError("Valid Number is required..");
                        inputPhoneNumberVolReg.requestFocus();
                        return;
                    }
                    if (!inputEmailVolReg.getText().toString().trim().matches(emailPattern)){
                        inputEmailVolReg.setError("Valid Email is required...");
                        inputEmailVolReg.requestFocus();
                        return;
                    }
                    String phoneNumber = "+91" + numberVol;
                    Intent intent = new Intent(Volreg.this, Cntrlvol.class);
                    intent.putExtra("phonenumber", phoneNumber);
                    intent.putExtra("username", strUsernameVolReg);
                    intent.putExtra("email", strEmailVolReg);
                    intent.putExtra("city", strCityVolReg);
                    startActivity(intent);
                    finish();
                }
            }
        });

        imggtooptVolunteerReg=findViewById(R.id.gotooptfrmvolregVolunteerReg);
        imggtooptVolunteerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Volreg.this, Selectopt.class));
                finish();
            }
        });
    }
    public void isValid(){
        strUsernameVolReg = inputUsernameVolReg.getText().toString().trim();
        strPhoneNoVolReg = inputPhoneNumberVolReg.getText().toString().trim();
        strEmailVolReg = inputEmailVolReg.getText().toString().trim();
        strCityVolReg = inputCityVolReg.getText().toString().trim();

        if (TextUtils.isEmpty(strUsernameVolReg) || TextUtils.isEmpty(strPhoneNoVolReg) || TextUtils.isEmpty(strEmailVolReg)
                || TextUtils.isEmpty(strCityVolReg)){
            EditTextStatusVolReg = false;
        }
        else {
            EditTextStatusVolReg = true;
        }
    }
}
