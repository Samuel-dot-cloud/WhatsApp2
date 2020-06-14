package com.studiofive.whatsapp2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private String messageReceiverId, messageReceiverName, messageReceiverImage;

    private TextView mLastSeen, mProfileName;
    private CircleImageView mProfileImage;
    private Toolbar mChatToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        messageReceiverId = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("visit_user_name").toString();
        messageReceiverImage = getIntent().getExtras().get("visit_user_image").toString();

        mProfileName.setText(messageReceiverName);
        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.profile1).into(mProfileImage);

        initializeFields();
    }

    private void initializeFields() {
        mProfileName = (TextView) findViewById(R.id.custom_profile_name);
        mProfileImage = (CircleImageView) findViewById(R.id.custom_profile_image);
        mLastSeen = (TextView) findViewById(R.id.custom_user_last_seen);
        mChatToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        //inflating the custom chat layout
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_layout, null);
        actionBar.setCustomView(actionBarView);

    }
}