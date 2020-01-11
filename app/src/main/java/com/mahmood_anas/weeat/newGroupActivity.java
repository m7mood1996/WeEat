package com.mahmood_anas.weeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;
import java.util.Calendar;

import static android.os.SystemClock.sleep;

public class newGroupActivity extends AppCompatActivity implements View.OnClickListener {
    Uri filePath;
    ImageView imageChoosen;
    Button upload, create_group;
    StorageReference storageReference;
    EditText restaurantName;
    EditText namePart;
    EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        imageChoosen = findViewById(R.id.imageToUpload);
        upload = findViewById(R.id.upload_photo_button);
        create_group = findViewById(R.id.new_group_button);
        upload.setOnClickListener(this);
        create_group.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        restaurantName = findViewById(R.id.restaurantName_input);
        namePart = findViewById(R.id.part_name);
        phoneNumber = findViewById(R.id.phone_number);

    }




    public void getPhotofromphone(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture") ,71);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 71 && resultCode == RESULT_OK && data !=null && data.getData() != null ){
            filePath = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageChoosen.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();

            }

        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.upload_photo_button){
            getPhotofromphone();
        }
        else if (v.getId() == R.id.new_group_button)
            AddnewMember();
    }


    public void AddnewMember() {
        System.out.println(filePath.toString());


    }

    public void addGrouptofirebase(){
        String mGroupId = FirebaseDatabase.getInstance().getReference().push().getKey();
        //String url = uri.toString();
        DatabaseReference mDatabase = (FirebaseDatabase.getInstance().getReference()).child("Groups").child(mGroupId);
        //mDatabase.child("imageUrl").setValue(url);
        mDatabase.child("restaurantName").setValue(restaurantName.getText().toString());
        mDatabase.child("Time").setValue("number_of_members");
        mDatabase.child("imageResourceId").setValue("number_of_members");
        mDatabase.child("location").setValue("number_of_members");
        mDatabase.child("numberofmembers").setValue("SLA");

        mGroupId = mDatabase.push().getKey();

        mDatabase.child(mGroupId).child("name").setValue("my_name");
        mDatabase.child(mGroupId).child("phoneNumber").setValue("my_phone_number");




    }




}
