package org.tensorflow.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class Homevol extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    public RequestQueue mRequestQue;
    public String URL = "https://fcm.googleapis.com/fcm/send";
    public static String subscriber;
    ImageView imgViewBell;
    static String pushID;

    // Change
    private RecyclerView recyclerView;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    String notificationlastUid;
    private List<UserData> result;
    UserAdapter adapter;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homevol);

        Splashmn.ifVol = true;

        imgViewBell = findViewById(R.id.imageView);
        btnClear = findViewById(R.id.btnClear);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        subscriber = getIntent().getStringExtra("sub");

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("vol");

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Homevol.this);
                alertDialogBuilder.setTitle("Clear Notifications");
                alertDialogBuilder.setMessage("You are sure you want to delete all notifications?");

                alertDialogBuilder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("lastUid", pushID);
                        editor.apply();

                        result.clear();
                        adapter.notifyDataSetChanged();

                        setEmptyNotiMsg();
                    }
                });

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                setEmptyNotiMsg();
            }
        });

//        if (getIntent().hasExtra("category")){
//            Toast.makeText(this, "In Vol Home", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(Homevol.this, NotificationActivity.class);
//            intent.putExtra("sub", getIntent().getStringExtra("sub"));
//            intent.putExtra("category", getIntent().getStringExtra("category"));
//            intent.putExtra("brandID", getIntent().getStringExtra("brandID"));
//            startActivity(intent);
//        }

//        findViewById(R.id.logOutVol).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
////                SharedPreferences.Editor editor = sharedPreferences.edit();
////                editor.clear();
////                editor.apply();
////
////                FirebaseAuth.getInstance().signOut();
////                startActivity(new Intent(Homevol.this, Selectopt.class));
////                finish();
//                startActivity(new Intent(Homevol.this, NotificationActivity.class));
//                finish();
//            }
//        });

        mRef.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        result = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);

        adapter = new UserAdapter(this  ,result);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        getNotification();

        setEmptyNotiMsg();

    }

    private void getNotification() {

        Query notificationRef = mRef.child("notifications");

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        notificationlastUid = sharedPreferences.getString("lastUid","");

        if (notificationlastUid.isEmpty()) {
            notificationRef = notificationRef.orderByKey().limitToLast(1);
        }else {
            notificationRef = notificationRef.orderByKey().startAt(notificationlastUid);
        }

        notificationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String uName = dataSnapshot.child("username").getValue(String.class);
                final Integer newState = dataSnapshot.child("newState").getValue(Integer.class);
                Long timeStamp = dataSnapshot.child("timestamp").getValue(Long.class);
                String body = dataSnapshot.child("msg").getValue(String.class);
                String subID = dataSnapshot.child("subID").getValue(String.class);
                String link=dataSnapshot.child("link").getValue(String.class);
                result.add(0, new UserData(uName, body,link,subID,timeStamp, newState));
                adapter.notifyDataSetChanged();

                setEmptyNotiMsg();

                pushID = dataSnapshot.getKey();

                if (notificationlastUid.isEmpty()){
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("lastUid", pushID);
                    editor.apply();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_currloc:
                Toast.makeText(Homevol.this, "Current Location Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
//                Toast.makeText(this, "Sub : " +subscriber , Toast.LENGTH_SHORT).show();
//                sendNotification();
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Homevol.this, Selectopt.class));
                finish();
                Toast.makeText(Homevol.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_endis:
                Toast.makeText(Homevol.this, "Enable/Disable Selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    private void setEmptyNotiMsg() {
        if (result.isEmpty()) {
            imgViewBell.setVisibility(View.VISIBLE);
        } else {
            imgViewBell.setVisibility(View.INVISIBLE);
        }
    }

//    private void sendNotification(){
//        JSONObject mainObj = new JSONObject();
//        try {
//
//            mainObj.put("to", "/topics/"+subscriber);
//            JSONObject notificationObj = new JSONObject();
//            notificationObj.put("title","any title");
//            notificationObj.put("body", "any body");
//
//            JSONObject extraData = new JSONObject();
//            extraData.put("sub", "vol");
//            extraData.put("brandID", "puma");
//            extraData.put("category","shoes");
//
//            mainObj.put("notification", notificationObj);
//            mainObj.put("data",extraData);
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
//                    mainObj, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//            }
//            ){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String,String> header = new HashMap<>();
//                    header.put("content-type","application/json");
//                    header.put("authorization","key=AIzaSyDcjZz_-JrVxswr0TsQ6_3F3S2e85QiSVE");
//                    return header;
//                }
//            };
//
//            mRequestQue.add(request);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    protected String getRandomString(){
//        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        StringBuilder salt = new StringBuilder();
//        Random random = new Random();
//        while (salt.length() < 6){
//            int index = (int)(random.nextFloat()*SALTCHARS.length());
//            salt.append(SALTCHARS.charAt(index));
//        }
//        String saltStr = salt.toString();
//        return saltStr;
//    }
//
//
}

