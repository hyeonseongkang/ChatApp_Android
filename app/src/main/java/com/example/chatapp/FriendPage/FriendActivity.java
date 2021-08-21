package com.example.chatapp.FriendPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ChatPage.ChatActivity;
import com.example.chatapp.Image;
import com.example.chatapp.R;
import com.example.chatapp.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends Fragment {

    private static final String TAG = "FriendActivity";

    private View v;

    private DatabaseReference myRefUser = FirebaseDatabase.getInstance().getReference("user");

    private List<User> userList = new ArrayList<>();
    private Image image = new Image();

    private RecyclerView friendActivityRecyclerView;
    private FriendActivityAdapter friendActivityAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBar progressBar;

    private ImageView profile;
    private TextView name, friendCount;

    private String myName;
    private String myId;
    private String myProfile;
    private String key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_friend, container, false);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        myName = sharedPreferences.getString("name", "");
        key = sharedPreferences.getString("key", "");

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();


        friendActivityRecyclerView = (RecyclerView) v.findViewById(R.id.friendActivityRecyclerView);
        friendActivityRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        friendActivityRecyclerView.setLayoutManager(layoutManager);
        friendActivityAdapter = new FriendActivityAdapter(userList);
        friendActivityRecyclerView.setAdapter(friendActivityAdapter);

        friendActivityAdapter.setOnItemClickListener(new FriendActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("myId", myId);
                intent.putExtra("userId",userList.get(position).getId());
                intent.putExtra("userKey", userList.get(position).getKey());
                intent.putExtra("userName", userList.get(position).getName());
                startActivity(intent);
            }
        });

        name = (TextView) v.findViewById(R.id.name);
        friendCount = (TextView) v.findViewById(R.id.friendCount);
        profile = (ImageView) v.findViewById(R.id.profile);

        name.setText(myName);

        myRefUser.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                myProfile = user.getProfile();
                profile.setImageBitmap(image.StringToBitMap(myProfile));
                myId = user.getId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!myName.equals(user.getName())) {
                        userList.add(user);
                        friendActivityAdapter.notifyDataSetChanged();
                    }
                }
                friendCount.setText("친구 " + userList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }
}
