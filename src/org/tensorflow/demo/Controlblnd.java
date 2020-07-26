package org.tensorflow.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Controlblnd extends AppCompatActivity {

    Button BlndVerify;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    TextView phonenumberText;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlblnd);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        editText = findViewById(R.id.verificationEditText);
        BlndVerify = findViewById(R.id.cntrlcond);
        phonenumberText = findViewById(R.id.phonenumberText);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        mRef.keepSynced(true);

        String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        phonenumberText.setText(phonenumber);

        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MybounceInterpolator interpolator = new MybounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        BlndVerify.startAnimation(myAnim);

        BlndVerify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String code = editText.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6){
                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        final String pNo = getIntent().getStringExtra("phonenumber");
        final String uname = getIntent().getStringExtra("username");
        final String age = getIntent().getStringExtra("age");
        final String city = getIntent().getStringExtra("city");
        final String iname = getIntent().getStringExtra("iname");

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("username", uname);
                    dataMap.put("age", age);
                    dataMap.put("city", city);
                    dataMap.put("phoneNumber", pNo);
                    dataMap.put("institutionName", iname);

                    //SAVE INFO.
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pNo", pNo);
                    editor.putString("type", "blind");
                    editor.apply();

                    mRef.child("users").child(user.getUid()).setValue(dataMap);
                    mRef.child("users").child("numberList").child(pNo).setValue(uname);
//                        Toast.makeText(VerifyPhoneActivity.this, "UID : " + user.getUid(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Controlblnd.this, Homescreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(Controlblnd.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                editText.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Controlblnd.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}
