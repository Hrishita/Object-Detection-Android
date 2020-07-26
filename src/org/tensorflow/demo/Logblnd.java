package org.tensorflow.demo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.Locale;

public class Logblnd extends AppCompatActivity {
    EditText loginBlindMobNo;
    Button loginBlindbtn;
    FirebaseUser currentUser;
    Handler handler;
    private TextToSpeech tts;

    FirebaseAuth firebaseAuth;
    boolean flag=false;
    ImageView imglogreg;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    String number;
    View log_view;

    ArrayList<String> arrayList = new ArrayList<>();
    boolean forLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logblnd);
        handler=new Handler();
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }, 3000);*/
        log_view=(View)findViewById(R.id.login_tap_view);
     log_view.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             listen();
         }
     });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("hin","IND"));

                    tts.setSpeechRate(0.9f);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    tts.setPitch(0.9f);

                } else {

                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });
        imglogreg=findViewById(R.id.blndlogreg);
        //imgbcklogblnd=findViewById(R.id.bckfromlogblnd);
        loginBlindMobNo = findViewById(R.id.txtPwd);
        loginBlindbtn = findViewById(R.id.btnLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        mRef.keepSynced(true);

        mRef.child("users").child("numberList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String myChildValues = dataSnapshot.getKey();
                arrayList.add(myChildValues);
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
        loginBlindbtn.getBackground().setAlpha(128);
        loginBlindbtn.setEnabled(false);

        loginBlindMobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtUsername = loginBlindMobNo.getText().toString();
                if (txtUsername.isEmpty()){
                    forLogin = false;
                    loginBlindbtn.setEnabled(false);
                    loginBlindbtn.getBackground().setAlpha(128);
                }else {
                    forLogin = true;
                    loginBlindbtn.setEnabled(true);
                    loginBlindbtn.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Animation myAnim1 = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MybounceInterpolator interpolator = new MybounceInterpolator(0.2, 20);
        myAnim1.setInterpolator(interpolator);
        loginBlindbtn.startAnimation(myAnim1);
        loginBlindbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forLogin) {
                    number = loginBlindMobNo.getText().toString().trim();
                    if (number.length() < 10) {
                        loginBlindMobNo.setError("Valid Number is required..");
                        speak("Enter valid number again");
                        loginBlindMobNo.requestFocus();
                        return;
                    }
                    number = "+91" + number;
                    if (arrayList.contains(number)) {
                        //SAVE INFO.
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("pNo", number);
                        editor.apply();

                        startActivity(new Intent(Logblnd.this, Homescreen.class));
                        speak("Login Successful."+"Hey "+Homescreen.username +" . Explore the we see services .Tap on the screen and speak 1 to navigate , speak 2 to search objects around you . speak 3 to ask for help from volunteer . speak 4 to read text . speak 5 to logout");


                        // finish();

                    } else {
                        Toast.makeText(Logblnd.this, "Mobile Number doesn't match our database", Toast.LENGTH_SHORT).show();
                 speak("Mobile number doesn't match our database");
                    }
                }
            }
        });

        imglogreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Logblnd.this, Blindreg.class));
               // finish();
            }
        });

        /*imgbcklogblnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Logblnd.this, Selectopt.class));
                finish();
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*SharedPreferences sharedPreferences = getSharedPreferences("backSpeech", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();*/

    }
    private void recognition(String text) {
       /* final int[] count = {0};
if (text.contains("register")||text.contains("registered")||text.contains("registration"))
{
    flag=false;
    Intent intent=new Intent(Logblnd.this,Blindreg.class);
    startActivity(intent);
    speak("To  get yourself registered answer few questions. what is your username.");
}
else if (text.contains("login")||text.contains("loggedin")||text.contains("logined"))
{
    flag=true;
    count[0]++;
    if(count[0]==1) {
        speak("Enter your mobile number");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                listen();

            }
        }, 3000);
        count[0]++;
    }
    else if(count[0]==2)
    {
        loginBlindMobNo.setEnabled(true);
        loginBlindMobNo.setText(text);
    }
   // Toast.makeText(this, ""+count[0], Toast.LENGTH_SHORT).show();



    if(loginBlindMobNo.isActivated()||loginBlindMobNo.isFocused()||loginBlindMobNo.isEnabled()){
        if (forLogin) {
            number = loginBlindMobNo.getText().toString().trim();
            if (number.length() < 10) {
                loginBlindMobNo.setError("Valid Number is required..");
                loginBlindMobNo.requestFocus();
                return;
            }
            number = "+91" + number;
            if (arrayList.contains(number)) {
                //SAVE INFO.
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pNo", number);
                editor.apply();

                startActivity(new Intent(Logblnd.this, Homescreen.class));
                finish();

            } else {
                Toast.makeText(Logblnd.this, "Mobile Number doesn't match our database", Toast.LENGTH_SHORT).show();
            }
        }
    }
    }*/
        loginBlindMobNo.setEnabled(true);
        loginBlindMobNo.setText(text.replaceAll("\\s",""));

      //  loginBlindMobNo.setText(text);
    // Toast.makeText(this, ""+count[0], Toast.LENGTH_SHORT).show();
    if(loginBlindMobNo.isActivated()||loginBlindMobNo.isFocused()||loginBlindMobNo.isEnabled()){
        if (forLogin) {
            number = loginBlindMobNo.getText().toString().trim();
            if (number.length() < 10) {
                loginBlindMobNo.setError("Valid Number is required..");
                speak("Enter valid number again");
                loginBlindMobNo.requestFocus();
                return;
            }
            number = "+91" + number;
            if (arrayList.contains(number)) {
                //SAVE INFO.
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pNo", number);

                editor.apply();
                startActivity(new Intent(Logblnd.this, Homescreen.class));

                speak("Login Successful."+"Hey "+Homescreen.username +" . Explore the we see services .Tap on the screen and speak 1 to navigate , speak 2 to search objects around you . speak 3 to ask for help from volunteer . speak 4 to read text . speak 5 to logout");

                // finish();

            } else {
                speak("Mobile Number doesn't match our database");
                Toast.makeText(Logblnd.this, "Mobile Number doesn't match our database", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }
    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(Logblnd.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
