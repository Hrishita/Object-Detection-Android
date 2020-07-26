package org.tensorflow.demo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Selectopt extends AppCompatActivity {
    ImageButton myimg1,myimg2;
    private TextToSpeech tts;
    View view;
    Button btn, imgslctlog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectopt);

        view=(View)findViewById(R.id.viewid);
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
                    speak("Hello Welcome to We see . we are here to help you in Navigating and you can also contact volunteer in case you need help. Tap on the screen and  Speak blind to register as blind and speak volunteer to register as volunteer.");

                } else {

                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //speak("ब्लाइंड के रूप में रजिस्टर कर ने के लिए ब्लाइंड बोलिये या वॉलंटियर के रूप में रजिस्टर कर ने के लिए वॉलंटियर बोलिये ।");

                speak("  ");
                listen();

                //  final Handler handler = new Handler();
            /*    handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       listen();
                    }
                }, 8000);*/
            }

        });

        myimg1 = (ImageButton) findViewById(R.id.imgbtnblnd);
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.fadein);
        myimg1.startAnimation(myAnim);

        myimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent beach = new Intent(Selectopt.this, Option_redirect.class);

                startActivity(beach);
               // finish();
                speak("Do you want to login or register ?");

            }
        });

        myimg2 = (ImageButton) findViewById(R.id.imgbtnvol);
        myimg2.startAnimation(myAnim);

        myimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selectopt.this, VolunteerLoginActivity.class));
               finish();
              //  speak("Do you want to login or register ?");
            }
        });
    }

    private void recognition(String text) {
        Log.e("Speech", "" + text);

        String[] speech = text.split(" ");
        if(text.contains("blind"))
        {
            Intent intent = new Intent(Selectopt.this,Option_redirect.class);
            startActivity(intent);
            speak("Do you want to login or register ?");


        }
        else if(text.contains("volunteer")) {
            Intent intent = new Intent(Selectopt.this, VolunteerLoginActivity.class);
            startActivity(intent);
            //  speak("Do you want to login or register ?");
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
            Toast.makeText(Selectopt.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
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

