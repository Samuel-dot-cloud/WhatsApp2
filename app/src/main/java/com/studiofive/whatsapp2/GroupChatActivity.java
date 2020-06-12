package com.studiofive.whatsapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupChatActivity extends AppCompatActivity {
    @BindView(R.id.send_message_button)
    ImageButton mImageButton;
    @BindView(R.id.group_chat_text_display)
    TextView mTextView;
    @BindView(R.id.input_group_message)
    EditText mGroupInput;
    @BindView(R.id.my_scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.group_chat_bar_layout)
    Toolbar mToolBar;

    private String currentGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.bind(this);

        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupChatActivity.this, currentGroupName, Toast.LENGTH_SHORT).show();

        initializeFields();
    }

    private void initializeFields() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Group Name");
    }
}