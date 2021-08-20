package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUser = database.getReference("user");

    private List<User> users = new ArrayList<>();

    private EditText loginId, loginPw;
    private Button loginButton, membershipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginId = (EditText) findViewById(R.id.loginId);
        loginPw = (EditText) findViewById(R.id.loginPw);

        loginButton = (Button) findViewById(R.id.loginButton);
        membershipButton = (Button) findViewById(R.id.membershipButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setId = loginId.getText().toString();
                String setPw = loginPw.getText().toString();


                if (setId.getBytes().length <= 0 || setPw.getBytes().length <= 0) {
                    Toast.makeText(LoginActivity.this, "입력 사항을 확인해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                users.clear();

                myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for(DataSnapshot childrensnapshot: snapshot.getChildren()) {
                            User userData = childrensnapshot.getValue(User.class);
                            users.add(userData);
                        }

                        for(User user : users) {
                            if (setId.equals(user.getId()) && setPw.equals(user.getPw())) {

                                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("id", user.getId());
                                editor.putString("name", user.getName());
                                editor.putString("profile", user.getProfile());
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                                loginId.setText("");
                                loginPw.setText("");
                                return;
                            }
                        }
                        Toast.makeText(LoginActivity.this, "존재하지 않은 회원입니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        });

        membershipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MemberShipActivity.class);
                startActivity(intent);
            }
        });

    }
}