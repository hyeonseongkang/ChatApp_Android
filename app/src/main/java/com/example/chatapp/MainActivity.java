package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.chatapp.ChatPage.ChatActivity;
import com.example.chatapp.FriendPage.FriendActivity;
import com.example.chatapp.NewsPage.NewsActivity;
import com.example.chatapp.SettingPage.SettingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FriendActivity friendActivity;
    private ChatActivity chatActivity;
    private NewsActivity newsActivity;
    private SettingActivity settingActivity;

//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = database.getReference("message");
//    private Button button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", null);

        if (userId == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.friendslist:
                        setFrag(0);
                        break;

                    case R.id.chatting:
                        setFrag(1);
                        break;

                    case R.id.sharp:
                        setFrag(2);
                        break;

                    case R.id.setting:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        friendActivity = new FriendActivity();
        chatActivity = new ChatActivity();
        newsActivity = new NewsActivity();
        settingActivity = new SettingActivity();
        setFrag(0);


//        button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myRef.setValue("Hello, World!");
//            }
//        });

    }

    private void setFrag(int n) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch(n) {
            case 0:
                fragmentTransaction.replace(R.id.frameLayout, friendActivity);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.frameLayout, chatActivity);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.frameLayout, newsActivity);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentTransaction.replace(R.id.frameLayout, settingActivity);
                fragmentTransaction.commit();
                break;
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//    }
}