package com.studiofive.whatsapp2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        Toast.makeText(ProfileActivity.this, "User id: " + receiverUserId, Toast.LENGTH_SHORT).show();

    }
}