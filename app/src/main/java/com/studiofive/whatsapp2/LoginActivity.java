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
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private FirebaseUser mFirebaseUser;
    @BindView(R.id.login_button)
    Button mLoginButton;
    @BindView(R.id.login_email)
    EditText mLoginEmail;
    @BindView(R.id.need_new_account)
    TextView mNeedAccount;
    @BindView(R.id.forget_password_link)
    TextView mForgetPassword;
    @BindView(R.id.login_password)
    EditText mLoginPassword;
    @BindView(R.id.login_phone_button)
    Button mPhoneButton;
    @BindView(R.id.login_image)
    ImageView mLoginImage;
    @BindView(R.id.login_using)
    TextView mLoginUsing;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth =FirebaseAuth.getInstance();
        mFirebaseUser =mAuth.getCurrentUser();

        mProgressDialog = new ProgressDialog(this);

        mNeedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToSignUpActivity();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        String email = mLoginEmail.getText().toString();
        String password = mLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter valid details!!", Toast.LENGTH_SHORT).show();
        } else {
            mProgressDialog.setTitle("Signing In");
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "Logged in Successfully!!", Toast.LENGTH_SHORT).show();
                            }else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                            mProgressDialog.dismiss();
                        }
                    });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseUser != null){
            sendUserToMainActivity();
        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void sendUserToSignUpActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}