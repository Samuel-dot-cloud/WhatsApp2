package com.studiofive.whatsapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {
    private View mChatsView;
    private DatabaseReference mChatsRef, mUsersRef;
    private FirebaseAuth mAuth;
    private String currentUser;

    @BindView(R.id.chats_list)
    RecyclerView mChatsList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters


    public ChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatsFragment newInstance(String param1, String param2) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mChatsView = inflater.inflate(R.layout.fragment_chats, container, false);
        ButterKnife.bind(this, mChatsView);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        mChatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUser);
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mChatsList.setLayoutManager(new LinearLayoutManager(getContext()));
         return mChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(mChatsRef, Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, ChatsViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, ChatsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Contacts model) {
                final String users_ids = getRef(position).getKey();
                final String[] getImage = {"default_image"};

                mUsersRef.child(users_ids).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()){
                           if (dataSnapshot.hasChild("image")){
                                 getImage[0] = dataSnapshot.child("image").getValue().toString();

                               Picasso.get().load(getImage[0]).placeholder(R.drawable.profile1).into(holder.mUserProfileImage);
                           }

                           final String getName = dataSnapshot.child("name").getValue().toString();
                           final String getStatus = dataSnapshot.child("status").getValue().toString();

                           holder.mProfileName.setText(getName);
                           holder.mUserStatus.setText("Last Seen:" + "\n" + "Date " + "Time");

                           holder.itemView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Intent intent = new Intent(getContext(), ChatActivity.class);
                                   intent.putExtra("visit_user_id", users_ids);
                                   intent.putExtra("visit_user_name", getName);
                                   intent.putExtra("visit_user_image", getImage[0]);
                                   startActivity(intent);
                               }
                           });
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
                return new ChatsViewHolder(view);
            }
        };

        mChatsList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class ChatsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.user_profile_name)
        TextView mProfileName;
        @BindView(R.id.user_status)
        TextView mUserStatus;
        @BindView(R.id.users_profile_image)
        CircleImageView mUserProfileImage;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}