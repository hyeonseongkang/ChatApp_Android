package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemberShipActivity extends AppCompatActivity {

    private static final String TAG = "MemberShipActivity";
    int REQUEST_EXTERNAL_STORAGE_PERMISSON = 1002;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUser = database.getReference("user");

    private List<String> users = new ArrayList<>();

    private EditText membershipId, membershipPw, membershipName, membershipPhone, membershipEmail;
    private ImageView profile, deleteImage;
    private Button joinButton;


    private Bitmap profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        membershipId = (EditText) findViewById(R.id.membershipId);
        membershipPw = (EditText) findViewById(R.id.membershipPw);
        membershipName = (EditText) findViewById(R.id.membershipName);
        membershipPhone = (EditText) findViewById(R.id.membershipPhone);
        membershipEmail = (EditText) findViewById(R.id.membershipEmail);

        profile = (ImageView) findViewById(R.id.profile);
        deleteImage = (ImageView) findViewById(R.id.deleteImage);

        joinButton = (Button) findViewById(R.id.joinButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setId = membershipId.getText().toString();
                String setPw = membershipPw.getText().toString();
                String setName = membershipName.getText().toString();
                String setPhone = membershipPhone.getText().toString();
                String setEmail = membershipEmail.getText().toString();

                if (setId.getBytes().length <= 0 || setPw.getBytes().length <= 0) {
                    Toast.makeText(MemberShipActivity.this, "입력 사항을 확인해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                users.clear();

                myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User userData = dataSnapshot.getValue(User.class);
                            users.add(userData.getId());
                        }

                        if (users.contains(setId)) {
                            Toast.makeText(MemberShipActivity.this, "사용 중인 아이디입니다", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            String imageString = BitmapToString(profileImage);

                            String key = myRefUser.push().getKey();
                            myRefUser.child(key).setValue(new User(key, setId, setPw, imageString, setName, setEmail, setPhone));


                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("id", setId);
                            editor.putString("name", setName);
                            editor.putString("key", key);
                            editor.commit();

                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        if (ContextCompat.checkSelfPermission(MemberShipActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MemberShipActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(MemberShipActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSON);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                profileImage = profileImage(data.getData());
                if (profileImage != null) {
                    System.out.println(profileImage);
                    profile.setImageBitmap(profileImage);
                    if (profileImage != null) {
                        deleteImage.setVisibility(View.GONE);
                    }
                }
            } catch (OutOfMemoryError e) {
                return;
            } catch (Exception e) {
                return;
            }
        }

    }

    public String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[]  b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap profileImage(Uri imgUri){
        String imagePath = getRealPathFromURI(imgUri);
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(imagePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap originaImage = BitmapFactory.decodeFile(imagePath);
        int reduceSize = (int)(originaImage.getHeight()*(1024.0/originaImage.getWidth()));
        Bitmap reduceImage = Bitmap.createScaledBitmap(originaImage, 1024, reduceSize, true);
        Bitmap returnImage = rotate(reduceImage, exifDegree);
        return returnImage;

    }

    public String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap src, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }
}