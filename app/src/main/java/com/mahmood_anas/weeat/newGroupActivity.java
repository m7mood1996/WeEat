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
            addNewGroupWithPhoto();
    }


    public void addNewGroupWithPhoto() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(newGroupActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(newGroupActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                // Progress Listener for loading
                // percentage on the dialog box
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress );
                    progressDialog.setProgress((int) (progress));
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            addGrouptofirebase(uri);
                        }
                    });

                }
            });

        }
        else{
            addGrouptofirebase(null);
        }
    }



    public void addGrouptofirebase(Uri uri){
        String mGroupId = FirebaseDatabase.getInstance().getReference().push().getKey();
        DatabaseReference mDatabase = (FirebaseDatabase.getInstance().getReference()).child("Groups").child(mGroupId);

        if (uri != null) {
            String url = uri.toString();
            mDatabase.child("imageUrl").setValue(url);
        }

        else{
            mDatabase.child("imageUrl").setValue("null");
        }

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
