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

public class Cntrlvol extends AppCompatActivity {

    Button btnVolVerify;

    private String verificationIdVol;
    private FirebaseAuth mAuthVol;
    private ProgressBar progressBarVol;
    private EditText editTextVol;
    TextView phonenumberTextVol;
    FirebaseUser currentUserVol;
    FirebaseAuth firebaseAuthVol;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cntrlvol);

        btnVolVerify=findViewById(R.id.cntrlcondvol);
        Animation myAnim1 = AnimationUtils.loadAnimation(this,R.anim.bounce);
        MybounceInterpolator interpolator = new MybounceInterpolator(0.2, 20);
        myAnim1.setInterpolator(interpolator);
        btnVolVerify.startAnimation(myAnim1);

        mAuthVol = FirebaseAuth.getInstance();
        progressBarVol = findViewById(R.id.progressBarVol);
        editTextVol = findViewById(R.id.verificationEditTextVolunteer);
        phonenumberTextVol = findViewById(R.id.phonenumberTextVolunteer);

        firebaseAuthVol = FirebaseAuth.getInstance();
        currentUserVol = firebaseAuthVol.getCurrentUser();
        mRef.keepSynced(true);

        String phonenumberVol = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumberVol);

        phonenumberTextVol.setText(phonenumberVol);

        btnVolVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Cntrlvol.this, Homevol.class));
                finish();
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationIdVol, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        final String pNo = getIntent().getStringExtra("phonenumber");
        final String uname = getIntent().getStringExtra("username");
        final String email = getIntent().getStringExtra("email");
        final String city = getIntent().getStringExtra("city");
        final String type = "vol";
        mAuthVol.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBarVol.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){
                    FirebaseUser user = task.getResult().getUser();
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("username", uname);
                    dataMap.put("email", email);
                    dataMap.put("city", city);
                    dataMap.put("phoneNumber", pNo);

                    //SAVE INFO.
                    SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pNo", "Volunteer");
                    editor.putString("type",type);
                    editor.apply();

                    mRef.child("volunteer").child(user.getUid()).setValue(dataMap);
                    mRef.child("volunteer").child("numberList").child(pNo).setValue(uname);
//                        Toast.makeText(VerifyPhoneActivity.this, "UID : " + user.getUid(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Cntrlvol.this, Homevol.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Cntrlvol.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        progressBarVol.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationIdVol = s;
            progressBarVol.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                editTextVol.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Cntrlvol.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}
