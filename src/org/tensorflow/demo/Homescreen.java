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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Homescreen extends AppCompatActivity {
    CardView c1,c2,c3,c4;
    FirebaseUser currentUser;
    Handler handler;
    View view_tap;
    private TextToSpeech tts;
    FirebaseAuth firebaseAuth;
    TextView blindName;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    static String randomString, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
    view_tap=(View)findViewById(R.id.view_tap_home_screen);
view_tap.setOnClickListener(new View.OnClickListener() {
        @Override
    public void onClick(View view) {
        listen();
    }
});

        Splashmn.ifVol = false;
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
        randomString = getRandomString();

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic(randomString);

//        if (getIntent().hasExtra("category")){
//            Toast.makeText(this, "In blind Home", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Homescreen.this, NotificationActivity.class);
//            intent.putExtra("sub", getIntent().getStringExtra("sub"));
//            intent.putExtra("category", getIntent().getStringExtra("category"));
//            intent.putExtra("brandID", getIntent().getStringExtra("brandID"));
//            startActivity(intent);
//        }

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        mRef.keepSynced(true);
        blindName = findViewById(R.id.blindName);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("pNo","");

        mRef.child("users").child("numberList").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue().toString();
                blindName.setText("Hey " + username);
                speak("Hey "+username +" . Explore the we see services .Tap on the screen and speak 1 to navigate , speak 2 to search objects around you . speak 3 to ask for help from volunteer . speak 4 to read text . speak 5 to logout");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       /* findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(Homescreen.this, Selectopt.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);

                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Homescreen.this, Selectopt.class));
                finish();
            }
        });*/


        c1 = findViewById(R.id.crdmap);
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.blink);
        c1.startAnimation(myAnim);
        c1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent beach = new Intent(Homescreen.this, MapsActivity.class);
               // startActivity(beach);
            }
        });
        c2=findViewById(R.id.crdsrch);
        c2.startAnimation(myAnim);

        c3=findViewById(R.id.crdvdo);
        c3.startAnimation(myAnim);
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    sendNotification();
             //   speak("message sent");
              //  Toast.makeText(Homescreen.this, "Message Sent", Toast.LENGTH_SHORT).show();
           //     Toast.makeText(Homescreen.this, "String : " + randomString, Toast.LENGTH_SHORT).show();
            }
        });

        c4 = findViewById(R.id.crdocr);
        c4.startAnimation(myAnim);
        c4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //    Intent beach1 = new  Intent(Homescreen.this, Textrec.class);
                //Toast.makeText(Homescreen.this, "Intent init", Toast.LENGTH_SHORT).show();
                //startActivity(beach1);
             //   startActivity(new Intent(Homescreen.this, Textrec.class));
            }
        });
    }
    private void sendNotification(){
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", "/topics/"+"vol");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title",username + " wants your help !!!");
            notificationObj.put("body", "Hello! I need help. Can you please help me out?");

            JSONObject extraData = new JSONObject();
            extraData.put("sub",randomString);
            extraData.put("brandID", "puma");
            extraData.put("category","shoes");

            Map<String, Object> dataMap = new HashMap<>();
//          dataMap.put("userID",currentUser.getUid());
            dataMap.put("username", username);
            dataMap.put("msg", "Hello! I need help. Can you please help me out?");
            dataMap.put("newState",0);
            String uri="http://maps.google.com/maps?saddr="+MapsActivity.latitude+","+MapsActivity.longitude;
           dataMap.put("link",uri);
            dataMap.put("timestamp", System.currentTimeMillis()/1000L);
            dataMap.put("subID", randomString);

            mRef.child("notifications").push().setValue(dataMap);

            mainObj.put("notification", notificationObj);
            mainObj.put("data",extraData);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyDcjZz_-JrVxswr0TsQ6_3F3S2e85QiSVE");
                    return header;
                }
            };
             mRequestQue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    protected String getRandomString(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 6){
            int index = (int)(random.nextFloat()*SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
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
    private void recognition(String text){
if(text.contains("1")||text.contains("one"))
{
    Intent intent=new Intent(Homescreen.this,MapsActivity.class);
    startActivity(intent);
}
else if(text.contains("2")||text.contains("two")||text.contains("tu")||text.contains("too"))
{

}
else if(text.contains("3")||text.contains("three")||text.contains("tree"))
{
   speak("Message sent");
    sendNotification();
    Toast.makeText(Homescreen.this, "Message Sent", Toast.LENGTH_SHORT).show();
    Toast.makeText(Homescreen.this, "String : " + randomString, Toast.LENGTH_SHORT).show();
}
else if(text.contains("4")||text.contains("four")||text.contains("for"))
{
    Intent intent=new Intent(Homescreen.this,Textrec.class);
    startActivity(intent);
}
else if(text.contains("5")||text.contains("five")||text.contains("pie")||text.contains("phii")||text.contains("fii"))
{

    SharedPreferences preferences =getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.clear();
    editor.apply();
    editor.commit();
  //  finish();
             FirebaseAuth.getInstance().signOut();
               startActivity(new Intent(Homescreen.this, Option_redirect.class));
           // finish();

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
            Toast.makeText(Homescreen.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
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
