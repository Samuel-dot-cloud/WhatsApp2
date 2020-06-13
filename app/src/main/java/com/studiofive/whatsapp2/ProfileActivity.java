package com.studiofive.whatsapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserId, current_state, currentUser;

    @BindView(R.id.visit_profile_image)
    CircleImageView mProfileImage;
    @BindView(R.id.visit_profile_name)
    TextView mProfileName;
    @BindView(R.id.visit_profile_status)
    TextView mProfileStatus;
    @BindView(R.id.send_message_request_button)
    Button mSendMessageButton;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");

        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        currentUser = mAuth.getCurrentUser().getUid();
        
        retrieveUserInfo();

        current_state = "new";

    }

    private void retrieveUserInfo() {
        mRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))){
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

                    Picasso.get().load(userImage).placeholder(R.drawable.profile1).into(mProfileImage);
                    mProfileName.setText(userName);
                    mProfileStatus.setText(userStatus);

                    manageChatRequests();

                }
                else {
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatus = dataSnapshot.child("status").getValue().toString();

                    mProfileName.setText(userName);
                    mProfileStatus.setText(userStatus);

                    manageChatRequests();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void manageChatRequests() {
        if(!currentUser.equals(receiverUserId)){

        }
        else {
            mSendMessageButton.setVisibility(View.INVISIBLE);
        }
    }
}