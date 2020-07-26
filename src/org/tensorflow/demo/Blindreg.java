package org.tensorflow.demo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class  Blindreg extends AppCompatActivity {
    private TextToSpeech tts;
    static  int count=0;
    Handler handler;
    private EditText inputPhoneNumber;
    Button Register, SignInLinkBtn;
    ImageView imggotooptblndreg;
    EditText inputUsername, inputAge, inputCity, inputInstitutionName;
    boolean forUsername, forPhoneNo, forAge, forCity, forInstitutionName;
    Boolean EditTextStatus, checkPassword;
    String strUsername, strPhoneNo, strAge, strCity, strInstitutionName;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindreg);
        handler = new Handler();
//        imggotooptblndreg.findViewById(R.id.blndreggotoopt);
//        imggotooptblndreg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Blindreg.this, Selectopt.class));
//                finish();
//            }
//        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                } else {

                    Log.e("TTS", "Initialisation Failed!");
                }
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }, 4000);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        mRef.keepSynced(true);

        inputUsername = findViewById(R.id.register_username);
        inputAge = findViewById(R.id.age);
        inputCity = findViewById(R.id.city);
        inputInstitutionName = findViewById(R.id.institution_name);
        inputPhoneNumber = findViewById(R.id.phone_no);

        Register = (Button)findViewById(R.id.btnblndRegister);
        Register.setEnabled(false);
        Register.getBackground().setAlpha(128);

        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MybounceInterpolator interpolator = new MybounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        Register.startAnimation(myAnim);

        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtUsername = inputUsername.getText().toString();
                if (txtUsername.isEmpty()){
                    forUsername = false;
                    Register.setEnabled(false);
                    Register.getBackground().setAlpha(128);
                }else {
                    forUsername = true;
                }

                if (forPhoneNo && forCity && forUsername && forInstitutionName && forAge){
                    Register.setEnabled(true);
                    Register.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtPhoneNumber = inputPhoneNumber.getText().toString();
                if (txtPhoneNumber.isEmpty()){
                    forPhoneNo = false;
                    Register.setEnabled(false);
                    Register.getBackground().setAlpha(128);
                }else {
                    forPhoneNo = true;
                }

                if (forPhoneNo && forCity && forUsername && forInstitutionName && forAge){
                    Register.setEnabled(true);
                    Register.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtAge = inputAge.getText().toString();
                if (txtAge.isEmpty()){
                    forAge = false;
                    Register.setEnabled(false);
                    Register.getBackground().setAlpha(128);
                }else {
                    forAge = true;
                }

                if (forPhoneNo && forCity && forUsername && forInstitutionName && forAge){
                    Register.setEnabled(true);
                    Register.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtCity = inputCity.getText().toString();
                if (txtCity.isEmpty()){
                    forCity = false;
                    Register.setEnabled(false);
                    Register.getBackground().setAlpha(128);
                }else {
                    forCity = true;
                }

                if (forPhoneNo && forCity && forUsername && forInstitutionName && forAge){
                    Register.setEnabled(true);
                    Register.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
            });
                inputInstitutionName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txtInstitutionName = inputInstitutionName.getText().toString();
                if (txtInstitutionName.isEmpty()){
                    forInstitutionName = false;
                    Register.setEnabled(false);
                    Register.getBackground().setAlpha(128);
                }else {
                    forInstitutionName = true;
                }

                if (forPhoneNo && forCity && forUsername && forInstitutionName && forAge){
                    Register.setEnabled(true);
                    Register.getBackground().setAlpha(255);
                }
            }

        @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.btnblndRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling method to check EditText is empty or not
                isValid();

                if(EditTextStatus) {
                    String number = inputPhoneNumber.getText().toString().trim();
                    if (number.length() != 10) {
                        inputPhoneNumber.setError("Valid Number is required..");
                       speak("valid number is required");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                listen();
                            }
                        }, 2000);

                        inputPhoneNumber.requestFocus();
                        return;
                    }
                    String phoneNumber = "+91" + number;
                    Intent intent = new Intent(Blindreg.this, Controlblnd.class);
                    intent.putExtra("phonenumber", phoneNumber);
                    intent.putExtra("username", strUsername);
                    intent.putExtra("age", strAge);
                    intent.putExtra("city", strCity);
                    intent.putExtra("iname", strInstitutionName);
                    startActivity(intent);
                }
            }
        });
    }




    public void isValid(){
        strUsername = inputUsername.getText().toString().trim();
        strPhoneNo = inputPhoneNumber.getText().toString().trim();
        strAge = inputAge.getText().toString().trim();
        strCity = inputCity.getText().toString().trim();
        strInstitutionName = inputInstitutionName.getText().toString().trim();

        if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strPhoneNo) || TextUtils.isEmpty(strAge)
                || TextUtils.isEmpty(strCity)|| TextUtils.isEmpty(strInstitutionName)){
            EditTextStatus = false;
        }
        else {
            EditTextStatus = true;
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
            Toast.makeText(Blindreg.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
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
    private void recognition(String text) {
        if (count==0)
        {
            inputUsername.setText(text);
            count++;
            speak("Enter Mobile number");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listen();
                }
            }, 2000);
        }
        else if(count==1){
            inputPhoneNumber.setText(text.replaceAll("\\s",""));

/*boolean match =pattern();*/
/*if (match){*/
   count++;
   speak("Enter Your Age");
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            listen();
        }
    }, 2000);
/*}*/
/*else
{
 */
/*
inputPhoneNumber.setText(text.replaceAll("\\s",""));
count++;
*/
/*
speak("Enter age");

    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            listen();
        }
    }, 2000);
*/
}

/*}*/else if (count==2)
        {
            inputAge.setText(text);
            count++;

            speak("Enter City");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listen();
                }
            }, 2000);
        }
        else if(count==3){
            inputCity.setText(text);
            count++;
            speak("Enter your Institution name");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listen();
                }
            }, 2000);
        }
        else if(count==4) {
            inputInstitutionName.setText(text);
            count++;
            recognition(inputPhoneNumber.getText().toString());
        }
        else if(count==5)
        {
            isValid();

            if(EditTextStatus) {

              //  String number = inputPhoneNumber.getText().toString().trim();
                //Toast.makeText(this, "toast1", Toast.LENGTH_LONG).show();
                inputPhoneNumber.setText(text.replaceAll("\\s",""));

                if(inputPhoneNumber.getText().toString().length() != 10) {
               //     Toast.makeText(this, ""+inputPhoneNumber.getText().toString().length(), Toast.LENGTH_SHORT).show();
                    ///inputPhoneNumber.setText(text.replaceAll("\\s",""));

                    //inputPhoneNumber.setError("Valid Number is required..");
                   speak("Enter valid phone number");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listen();
                        }
                    }, 2000);
                    //inputPhoneNumber.setText(text.replaceAll("\\s",""));
                    inputPhoneNumber.requestFocus();

                    return;
                }
                String phoneNumber = "+91" + inputPhoneNumber.getText().toString().trim();
                Intent intent = new Intent(Blindreg.this, Controlblnd.class);
                intent.putExtra("phonenumber", phoneNumber);
                intent.putExtra("username", strUsername);
                intent.putExtra("age", strAge);
                intent.putExtra("city", strCity);
                intent.putExtra("iname", strInstitutionName);
                startActivity(intent);
            }
        }
        }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    count=0;
    }


/*
    boolean pattern()
    {


        boolean flag=false;
        String MobilePattern = "[0-9]{10}";
        if(inputPhoneNumber.getText().toString().matches(MobilePattern)) {

flag = true;
        } else if(!inputPhoneNumber.getText().toString().matches(MobilePattern)) {

            speak("Enter Valid number");
flag = false;
    }
return flag;
    }*/
}
