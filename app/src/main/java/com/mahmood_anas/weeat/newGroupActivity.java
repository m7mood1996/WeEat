package com.mahmood_anas.weeat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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
import java.util.List;
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
    EditText numberofmembers;
    private LocationListener locationListener;
    private LocationManager locationManager;
    List<Address> addresses;
    String Address;
    Boolean isInt;
    @RequiresApi(api = Build.VERSION_CODES.M)
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
        numberofmembers = findViewById(R.id.num_of_members);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final Geocoder geocoder =new Geocoder(this);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    addresses= geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                    String addres = addresses.get(0).getAddressLine(0);
                    String area = addresses.get(0).getLocality();


                    Address = addres + ", " + area;
                    System.out.println(Address+ "hello--");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);

            }
        };
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},10);
            return;
        }
        else{
            locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configerButton();
                else
                    finish();
        }
    }

    public void configerButton(){

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
        if (v.getId() == R.id.upload_photo_button)
        {
            getPhotofromphone();
        }
        else if (v.getId() == R.id.new_group_button)
        {
            if (numberofmembers.getText().toString().equals("") || namePart.getText().toString().equals("") || phoneNumber.getText().toString().equals("") || restaurantName.getText().toString().equals(""))
            {
                Toast.makeText(this, "Please fill all the fields !", Toast.LENGTH_SHORT).show();
            }
            else {
                addNewGroupWithPhoto();
            }
        }
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
        mDatabase.child("Time").setValue(Calendar.getInstance().getTime().toString());
        mDatabase.child("location").setValue(Address);
        mDatabase.child("numberofmembers").setValue(numberofmembers.getText().toString());

        Intent service = new Intent(this,SMSNotifiIntintService.class);
        service.putExtra("group_id",mGroupId);

        mGroupId = mDatabase.push().getKey();



        SharedPreferences sp = getSharedPreferences("my_id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("id",mGroupId );
        editor.commit();


        mDatabase.child(mGroupId).child("name").setValue(namePart.getText().toString());
        mDatabase.child(mGroupId).child("phoneNumber").setValue(phoneNumber.getText().toString());

        service.putExtra("group_size",Integer.parseInt( numberofmembers.getText().toString()));

        //sendBroadcast(service.setAction("MyAction"));
        startService(service);


        finish();

    }





}
