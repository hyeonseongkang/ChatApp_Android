package com.example.chatapp.ChatPage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapp.Image;
import com.example.chatapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatListActivity extends Fragment {

    private static final String TAG = "ChatListActivity";

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("photo");

    private View v;

    private ImageView imageView;

    Bitmap bitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_chat_list, container, false);


        /*
        Image image = new Image();
        imageView = (ImageView) v.findViewById(R.id.imageView);

        databaseReference.child("photo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, String.valueOf(snapshot.getValue(String.class)));

                imageView.setImageBitmap(image.StringToBitMap(snapshot.getValue(String.class)));

                //Log.d(TAG,snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */


        return v;
    }
}
