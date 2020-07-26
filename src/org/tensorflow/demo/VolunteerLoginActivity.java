package org.tensorflow.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VolunteerLoginActivity extends AppCompatActivity {

    Button volLoginSubmitBtn;
    ImageView volBackBtn, volLoginRegBtn;
    EditText loginVolMobNo;
    FirebaseUser currentUserVolLogin;
    FirebaseAuth firebaseAuthVolLogin;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    String numberVolLogin;
    ArrayList<String> arrayListVolLogin = new ArrayList<>();
    boolean forLoginVol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_login);

        volLoginSubmitBtn = findViewById(R.id.btnLoginvolVolunteer);
        volBackBtn = findViewById(R.id.bckfrmvollogVolunteer);
        volLoginRegBtn = findViewById(R.id.vollogregVolunteer);
        loginVolMobNo = findViewById(R.id.txtPwdvollogVolunteer);
        firebaseAuthVolLogin = FirebaseAuth.getInstance();
        currentUserVolLogin = firebaseAuthVolLogin.getCurrentUser();
        mRef.keepSynced(true);

        mRef.child("volunteer").child("numberList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String myChildValues = dataSnapshot.getKey();
                arrayListVolLogin.add(myChildValues);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        volLoginSubmitBtn.getBackground().setAlpha(128);
        volLoginSubmitBtn.setEnabled(false);

        loginVolMobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtUsername = loginVolMobNo.getText().toString();
                if (txtUsername.isEmpty()){
                    forLoginVol = false;
                    volLoginSubmitBtn.setEnabled(false);
                    volLoginSubmitBtn.getBackground().setAlpha(128);
                }else {
                    forLoginVol = true;
                    volLoginSubmitBtn.setEnabled(true);
                    volLoginSubmitBtn.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        volLoginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VolunteerLoginActivity.this, Volreg.class));
                finish();

            }
        });

        volBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VolunteerLoginActivity.this, Selectopt.class));
                finish();
            }
        });

        Animation myAnim1 = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MybounceInterpolator interpolator = new MybounceInterpolator(0.2, 20);
        myAnim1.setInterpolator(interpolator);
        volLoginSubmitBtn.startAnimation(myAnim1);

        volLoginSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(VolunteerLoginActivity.this, Homevol.class));
//                finish();

                if (forLoginVol) {
                    numberVolLogin = loginVolMobNo.getText().toString().trim();
                    if (numberVolLogin.length() < 10) {
                        loginVolMobNo.setError("Valid Number is required..");
                        loginVolMobNo.requestFocus();
                        return;
                    }
                    numberVolLogin = "+91" + numberVolLogin;
                    if (arrayListVolLogin.contains(numberVolLogin)) {
                        //SAVE INFO.
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pNo", numberVolLogin);
                        editor.putString("type","vol");
                        editor.apply();

                        startActivity(new Intent(VolunteerLoginActivity.this, Homevol.class));
                        finish();

                    } else {
                        Toast.makeText(VolunteerLoginActivity.this, "Mobile Number doesn't match our database", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
