package com.lilliemountain.urbancrab.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lilliemountain.urbancrab.R;
import com.lilliemountain.urbancrab.model.Profile;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText phone_no,otp;
    String phone;
    Button verify;
    private FirebaseAuth mAuth;
    String sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone_no=findViewById(R.id.phone_no);
        mAuth=FirebaseAuth.getInstance();
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(phone_no.getText()))
                {
                    if (phone_no.getText().toString().length()==10) {
                        phone=phone_no.getText().toString();
                        //TODO CALL FIREBASE AUTH API
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91"+phone,
                                60,
                                TimeUnit.SECONDS,
                                LoginActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                         sms=phoneAuthCredential.getSmsCode();
                                         mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                             @Override
                                             public void onComplete(@NonNull Task<AuthResult> task) {

                                                 Log.e("onVerComp: ",sms );
                                                 final Dialog dialog=new Dialog(LoginActivity.this);
                                                 dialog.setContentView(R.layout.dialog_sms);
                                                 otp=dialog.findViewById(R.id.otp);
                                                 verify=dialog.findViewById(R.id.verify);
                                                 verify.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if(otp.getText().toString().equals(sms)){
                                                             FirebaseDatabase database=FirebaseDatabase.getInstance();
                                                             DatabaseReference reference=database.getReference("users");
                                                             DatabaseReference refChild=reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                             refChild.addValueEventListener(new ValueEventListener() {
                                                                 @Override
                                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                     Profile profile=dataSnapshot.getValue(Profile.class);
                                                                     if (profile==null) {
                                                                         startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                                                                         dialog.dismiss();
                                                                     }
                                                                     else {
                                                                         startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                                                         dialog.dismiss();

                                                                     }
                                                                 }

                                                                 @Override
                                                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                 }
                                                             });

                                                         }
                                                         else {
                                                             otp.setError("Invalid OTP");
                                                             otp.getText().clear();
                                                         }
                                                     }
                                                 });
                                                 dialog.show();
                                                 getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                                             }
                                         });
                                    }

                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        Log.e( "onVerificationFailed: ",e.getMessage() );
                                    }
                                });
                    }
                    else {
                        phone_no.setError("Enter your 10 digit mobile no.");
                    }
                }
                else {
                    phone_no.setError("Enter your 10 digit mobile no.");
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference=database.getReference("users");
            DatabaseReference refChild=reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            refChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Profile profile=dataSnapshot.getValue(Profile.class);
                    if (profile==null) {
                        startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
