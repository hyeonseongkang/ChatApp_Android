package com.example.chatapp.ChatPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.FriendPage.FriendActivityAdapter;
import com.example.chatapp.Image;
import com.example.chatapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private DatabaseReference myRefUser = FirebaseDatabase.getInstance().getReference("user");
    private DatabaseReference myRefChat = FirebaseDatabase.getInstance().getReference("chat");

    private RecyclerView chatActivityRecyclerView;
    private ChatActivityAdapter chatActivityAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Chat> chatDataList = new ArrayList<>();

    private Image image = new Image();

    private String myId, userId, chatListKey, userKey, getUserName;

    private ImageView userProfile;
    private TextView userName;
    private EditText message;
    private AppCompatImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        myId = intent.getStringExtra("myId");
        userId = intent.getStringExtra("userId");
        userKey = intent.getStringExtra("userKey");
        getUserName = intent.getStringExtra("userName");

        Log.d(TAG, myId);
        Log.d(TAG, userId);

//        myId = "강현성";
//        userId = "hyeonseongkang";
//        getUserName = "";

        chatActivityRecyclerView = (RecyclerView) findViewById(R.id.chatActivityRecyclerView);
        layoutManager = new LinearLayoutManager(ChatActivity.this);
        chatActivityRecyclerView.setLayoutManager(layoutManager);
        chatActivityAdapter = new ChatActivityAdapter(chatDataList, myId);
        chatActivityRecyclerView.setAdapter(chatActivityAdapter);

        userProfile = (ImageView) findViewById(R.id.userProfile);
        userName = (TextView) findViewById(R.id.userName);
        message = (EditText) findViewById(R.id.message);
        sendButton = (AppCompatImageButton) findViewById(R.id.sendButton);

        userName.setText(getUserName);

        myRefUser.child(userKey).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userProfileString = snapshot.getValue(String.class);
                //
                userProfile.setImageBitmap(image.StringToBitMap(userProfileString));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setMessage = message.getText().toString();
                if (setMessage.getBytes().length <= 0) {
                    return;
                }

               /* SimpleDateFormat format = new SimpleDateFormat("HH:mm");

                Calendar time = Calendar.getInstance();

                String formatTime = format.format(time.getTime());

                //myRefChat.child("-MhbihzYFtqe5EqGQPUk").child("chatlist").push().setValue(new Chat("강현성", setMessage, formatTime));
                myRefChat.child(chatListKey).child("chatlist").push().setValue(new Chat(myId, setMessage, formatTime));*/

                writeMessage(setMessage);
                message.setText("");
            }
        });

/*
        chatDataList.clear();
        myRefChat.child("-MhbihzYFtqe5EqGQPUk").child("chatlist").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat chatData = snapshot.getValue(Chat.class);
                chatDataList.add(chatData);
                chatActivityRecyclerView.scrollToPosition(chatDataList.size() - 1);
                chatActivityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   */


        myRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    /*
                    chat
                        - -MhbVQNxovMgATd-NKes
                            -chatList
                            -myId : 123 -> dataSnapshot.child("myId").getValue(String.value)
                            -userId : 123123 -> dataSnapshot.child("userId").getValue(String.value)

                       - -MhbYGrJyCsFpx-0-4qI
                     */


                    String user1 = dataSnapshot.child("user1").getValue(String.class);
                    String user2 = dataSnapshot.child("user2").getValue(String.class);

                    System.out.println(user1 + "    " + myId + "    " + user2 + "     " + userId);
                    if (user1.equals(myId) && user2.equals(userId) || user1.equals(userId) && user2.equals(myId)) {
                        chatListKey = dataSnapshot.getKey();
                        Log.d(TAG, chatListKey);
                        count++;
                    }

                }

                if (count == 0) {
                    Map<String , String> map = new HashMap<>();
                    map.put("user1", myId);
                    map.put("user2", userId);
                    chatListKey = myRefChat.push().getKey();
                    myRefChat.child(chatListKey).setValue(map);
                }

                chatDataList.clear();
                myRefChat.child(chatListKey).child("chatlist").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Chat chatData = snapshot.getValue(Chat.class);
                        chatDataList.add(chatData);
                        chatActivityRecyclerView.scrollToPosition(chatDataList.size() - 1);
                        chatActivityAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void writeMessage(String message) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        Calendar time = Calendar.getInstance();

        String formatTime = format.format(time.getTime());

        //myRefChat.child("-MhbihzYFtqe5EqGQPUk").child("chatlist").push().setValue(new Chat("강현성", setMessage, formatTime));
        myRefChat.child(chatListKey).child("chatlist").push().setValue(new Chat(myId, message, formatTime));
    }
}