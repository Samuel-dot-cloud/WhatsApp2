package com.studiofive.whatsapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mNeedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToSignUpActivity();
            }
        });
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