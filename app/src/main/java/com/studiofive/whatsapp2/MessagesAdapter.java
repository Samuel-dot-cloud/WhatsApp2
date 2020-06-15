package com.studiofive.whatsapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private List<Messages> userMessagesList;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    public MessagesAdapter(List<Messages> userMessagesList){
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_messages_layout, parent, false);
      mAuth = FirebaseAuth.getInstance();
      return new MessageViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();

        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")){
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile1).into(holder.mMessageImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (fromMessageType.equals("text")){
            holder.mReceiverText.setVisibility(View.INVISIBLE);
            holder.mMessageImage.setVisibility(View.INVISIBLE);
        }
        if (fromUserId.equals(messageSenderId)){
            holder.mSenderText.setBackgroundResource(R.drawable.sender_messages_layout);
            holder.mSenderText.setText(messages.getMessage());
        }else{
            holder.mSenderText.setVisibility(View.INVISIBLE);
            holder.mMessageImage.setVisibility(View.VISIBLE);
            holder.mReceiverText.setVisibility(View.VISIBLE);

            holder.mReceiverText.setBackgroundResource(R.drawable.receiver_messages_layout);
            holder.mReceiverText.setText(messages.getMessage());
        }
    }



    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.sender_message_text)
        TextView mSenderText;
        @BindView(R.id.receiver_message_text)
        TextView mReceiverText;
        @BindView(R.id.message_profile_image)
        CircleImageView mMessageImage;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
