package com.studiofive.whatsapp2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    @BindView(R.id.decline_message_request_button)
    Button mDeclineMessageButton;

    private DatabaseReference mRef, mChatRef, mContactsRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mChatRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        mContactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

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
        mChatRef.child(currentUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(receiverUserId)){
                            String request_type = dataSnapshot.child(receiverUserId).child("request_type").getValue().toString();

                            if (request_type.equals("sent")){
                                current_state = "request_sent";
                                mSendMessageButton.setText("Cancel Chat Request");

                            }else if (request_type.equals("received")){
                                current_state = "request_received";
                                mSendMessageButton.setText("Accept Chat Request");
                                mDeclineMessageButton.setVisibility(View.VISIBLE);
                                mDeclineMessageButton.setEnabled(true);

                                mDeclineMessageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cancelChatRequest();
                                    }
                                });

                            }
                        }
                        else{
                            mContactsRef.child(currentUser).child(receiverUserId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(receiverUserId)){
                                                current_state = "friends";
                                                mSendMessageButton.setText("Remove Contact");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if(!currentUser.equals(receiverUserId)){
            mSendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSendMessageButton.setEnabled(false);
                    if (current_state.equals("new")){
                        sendChatRequest();
                    }
                    if (current_state.equals("request_sent")){
                        cancelChatRequest();
                    }
                    if (current_state.equals("request_received")){
                       acceptChatRequest();
                    }
                }
            });
        }
        else {
            mSendMessageButton.setVisibility(View.INVISIBLE);
        }
    }

    private void sendChatRequest() {
        mChatRef.child(currentUser).child(receiverUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mChatRef.child(receiverUserId).child(currentUser)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mSendMessageButton.setEnabled(true);
                                                current_state = "request_sent";
                                                mSendMessageButton.setText("Cancel Chat Request");
                                            }

                                        }
                                    });
                        }

                    }
                });
    }

    private void cancelChatRequest() {
        mChatRef.child(currentUser).child(receiverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mChatRef.child(receiverUserId).child(currentUser)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mSendMessageButton.setEnabled(true);
                                            current_state = "new";
                                            mSendMessageButton.setText("Send Chat Request");

                                            mDeclineMessageButton.setVisibility(View.INVISIBLE);
                                            mDeclineMessageButton.setEnabled(false);
                                        }
                                    });
                        }
                    }
                });
    }

    private void acceptChatRequest() {
        mContactsRef.child(currentUser).child(receiverUserId)
                .child("Contacts").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mContactsRef.child(receiverUserId).child(currentUser)
                                    .child("Contacts").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mChatRef.child(currentUser).child(receiverUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    mChatRef.child(receiverUserId).child(currentUser)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    mSendMessageButton.setEnabled(true);
                                                                                    current_state = "friends";
                                                                                    mSendMessageButton.setText("Remove Contact");

                                                                                    mDeclineMessageButton.setVisibility(View.INVISIBLE);
                                                                                    mDeclineMessageButton.setEnabled(false);
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}