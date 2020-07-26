package org.tensorflow.demo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Option_redirect extends AppCompatActivity {
    private TextToSpeech tts;
    Handler handler;
    View view_touch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_redirect);
        handler=new Handler();
        view_touch=(View)findViewById(R.id.touchPromptLoginReg);
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
    /*    handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }, 3000);*/
       view_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen();
            }
        });
    }
    private void recognition(String text) {
        Log.e("Speech", "" + text);
        if(text.contains("register")||text.contains("registration")||text.contains("registered"))
        {
            Intent intent = new Intent(Option_redirect.this,Blindreg.class);
            startActivity(intent);
            speak("To get yourself registered answer few questions.what is your username? ");        }
        else if(text.contains("login")||text.contains("loggedin")||text.contains("nagin"))
        {
            Intent intent = new Intent(Option_redirect.this,Logblnd.class);
            startActivity(intent);
            speak("Tap on the screen and Enter your phone number. ?");
        }}
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
            Toast.makeText(Option_redirect.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();

    speak("do you want to login or register.");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }, 3000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        speak("do you want to login or register.");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        }, 3000);
    }
}
