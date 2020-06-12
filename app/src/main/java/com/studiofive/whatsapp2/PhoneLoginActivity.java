package com.studiofive.whatsapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        ButterKnife.bind(this);

        mVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVerificationButton.setVisibility(View.INVISIBLE);
                mPhoneInput.setVisibility(View.INVISIBLE);

                mVerifyButton.setVisibility(View.VISIBLE);
                mVerifyInput.setVisibility(View.VISIBLE);
            }
        });
    }
}