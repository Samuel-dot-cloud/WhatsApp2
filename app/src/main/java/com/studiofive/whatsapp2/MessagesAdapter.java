package com.studiofive.whatsapp2;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(position);

        String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();

        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).placeholder(R.drawable.profile1).into(holder.mMessageImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.mReceiverText.setVisibility(View.GONE);
        holder.mMessageImage.setVisibility(View.GONE);
        holder.mSenderText.setVisibility(View.VISIBLE);
        holder.mSenderImageView.setVisibility(View.GONE);
        holder.mReceiverImageView.setVisibility(View.GONE);

        if (fromMessageType.equals("text")) {


            if (fromUserId.equals(messageSenderId)) {
                holder.mSenderText.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.mSenderText.setText(messages.getMessage() + "\n \n" + messages.getTime() + "-" + messages.getDate());
            } else {
                holder.mSenderText.setVisibility(View.GONE);
                holder.mMessageImage.setVisibility(View.VISIBLE);
                holder.mReceiverText.setVisibility(View.VISIBLE);

                holder.mReceiverText.setBackgroundResource(R.drawable.receiver_messages_layout);
                holder.mReceiverText.setText(messages.getMessage() + "\n \n" + messages.getTime() + "-" + messages.getDate());
            }
        }
        else  if (fromMessageType.equals("image")) {
            if (fromUserId.equals(messageSenderId)){
                holder.mSenderImageView.setVisibility(View.VISIBLE);
                holder.mSenderText.setVisibility(View.INVISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.mSenderImageView);
            }
            else {
                holder.mReceiverImageView.setVisibility(View.VISIBLE);
                holder.mMessageImage.setVisibility(View.VISIBLE);
                holder.mSenderText.setVisibility(View.INVISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.mReceiverImageView);
            }
        }
        else if (fromMessageType.equals("PDF") ||  (fromMessageType.equals("Document"))){
            if (fromUserId.equals(messageSenderId)) {
                holder.mSenderText.setVisibility(View.GONE);
                holder.mSenderImageView.setVisibility(View.VISIBLE);
                holder.mSenderImageView.setBackgroundResource(R.drawable.file);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        holder.itemView.getContext().startActivity(intent);

                    }
                });
            }
            else {
                holder.mReceiverImageView.setVisibility(View.VISIBLE);
                holder.mMessageImage.setVisibility(View.VISIBLE);
                holder.mSenderText.setVisibility(View.GONE);
                holder.mReceiverImageView.setBackgroundResource(R.drawable.file);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
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
        @BindView(R.id.message_sender_image_view)
        ImageView mSenderImageView;
        @BindView(R.id.message_receiver_image_view)
        ImageView mReceiverImageView;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
