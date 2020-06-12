package com.studiofive.whatsapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneLoginActivity extends AppCompatActivity {
    @BindView(R.id.send_verification_code_button)
    Button mVerificationButton;
    @BindView(R.id._verify_button)
    Button mVerifyButton;
    @BindView(R.id.phone_number_input)
    EditText mPhoneInput;
    @BindView(R.id.verification_code_input)
    EditText mVerifyInput;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mPhoneInput.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(PhoneLoginActivity.this, "Please enter your phone number first!!!", Toast.LENGTH_SHORT).show();
                }else {
                    mProgressDialog.setTitle("Phone Verification");
                    mProgressDialog.setMessage("Please wait while phone number is being authenticated...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, PhoneLoginActivity.this, mCallbacks);
                }
            }
        });

        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerificationButton.setVisibility(View.INVISIBLE);
                mPhoneInput.setVisibility(View.INVISIBLE);

               String verificationCode = mVerifyInput.getText().toString();

               if (TextUtils.isEmpty(verificationCode)){
                   Toast.makeText(PhoneLoginActivity.this, "Please provide an input", Toast.LENGTH_SHORT).show();
               }else{
                   mProgressDialog.setTitle("Code Verification");
                   mProgressDialog.setMessage("Please wait while verification code is being authenticated...");
                   mProgressDialog.setCanceledOnTouchOutside(false);
                   mProgressDialog.show();

                   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                   signInWithPhoneAuthCredential(credential);
               }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                mProgressDialog.dismiss();

                Toast.makeText(PhoneLoginActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();

                mVerificationButton.setVisibility(View.VISIBLE);
                mPhoneInput.setVisibility(View.VISIBLE);

                mVerifyButton.setVisibility(View.INVISIBLE);
                mVerifyInput.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                mProgressDialog.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Code sent, please verify...", Toast.LENGTH_SHORT).show();

                mVerificationButton.setVisibility(View.INVISIBLE);
                mPhoneInput.setVisibility(View.INVISIBLE);

                mVerifyButton.setVisibility(View.VISIBLE);
                mVerifyInput.setVisibility(View.VISIBLE);
            }
        };
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, "Congratulations, you are logged in successfully!!", Toast.LENGTH_SHORT).show();
                            sendUserToMainActivity();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this, "Error: " + message , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}