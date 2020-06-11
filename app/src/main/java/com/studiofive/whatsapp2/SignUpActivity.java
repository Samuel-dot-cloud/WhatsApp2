package com.studiofive.whatsapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.signUp_button)
    Button mSignUpButton;
    @BindView(R.id.signUp_email)
    EditText mSignUpEmail;
    @BindView(R.id.signUp_password)
    EditText mSignUpPassword;
    @BindView(R.id.already_have_account_link)
    TextView mAlreadyAccount;
    @BindView(R.id.signUp_image)
    ImageView mSignUpImage;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        mAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        String email = mSignUpEmail.getText().toString();
        String password = mSignUpPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter valid details!!", Toast.LENGTH_SHORT).show();
        }
        else {
            mProgressDialog.setTitle("Creating New Account");
            mProgressDialog.setMessage("Please wait while we are creating a new account for you...");
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String uid = mAuth.getCurrentUser().getUid();
                                mReference.child(Constants.USERS_NODE).child(uid).setValue("");
                                sendUserToMainActivity();
                                Toast.makeText(SignUpActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(SignUpActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                            }
                            mProgressDialog.dismiss();
                        }
                    });
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    private void sendUserToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}