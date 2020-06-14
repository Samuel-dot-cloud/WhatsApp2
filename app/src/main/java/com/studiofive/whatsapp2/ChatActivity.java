package com.studiofive.whatsapp2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {
    private String messageReceiverId, messageReceiverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageReceiverId = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("visit_user_name").toString();

        Toast.makeText(ChatActivity.this, messageReceiverId, Toast.LENGTH_SHORT).show();
        Toast.makeText(ChatActivity.this, messageReceiverName, Toast.LENGTH_SHORT).show();
    }
}