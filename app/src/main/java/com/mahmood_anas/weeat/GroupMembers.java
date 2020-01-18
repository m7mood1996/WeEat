package com.mahmood_anas.weeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

public class GroupMembers extends AppCompatActivity implements ListView.OnItemClickListener, View.OnClickListener{

    private ArrayList<MembersInfo> membersInfo;
    TextView Group_Name;
    ImageView Group_Image;
    String key, name, number;
    Button Join;
    String url;
    FirebaseDatabase database;
    DatabaseReference ref;
    MembersAdapter membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        Group_Name = findViewById(R.id.grp_name);
        Group_Image = findViewById(R.id.grp_img);
        Join = findViewById(R.id.join_id);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        Group_Name.setText(getIntent().getStringExtra("RES"));
        url = getIntent().getStringExtra("URL");
        //Uri uri = Uri.parse(url);
        //System.out.println("url is \t" + uri);
        new DownloadImageFromInternet().execute(url);



        // Create an ArrayList of MembersInfo objects
        membersInfo = new ArrayList<MembersInfo>();


        // Create an AndroidFlavorAdapter, whose data source is a list of AndroidFlavors.
        // The adapter knows how to create list item views for each item in the list.
        membersAdapter = new MembersAdapter(this, membersInfo);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = findViewById(R.id.list_members);
        listView.setAdapter(membersAdapter);

        // add Item Click Listener
        listView.setOnItemClickListener(this);
        fetchDataFromFireBase();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MembersInfo membersInf = membersInfo.get(position);
        showSimpleAlert(membersInf.getName(), membersInf.getNumber());
    }

    @Override
    public void onClick(View v) {
//        if (view.getId() == R.id.join_grp){
//            System.out.println("I have to enter here !! ");
//            key = getIntent().getStringExtra("KEY");
//            Intent Member_Details = new Intent(this,MemberDetails.class);
//            Member_Details.putExtra("KEY", key);
//            startActivity(Member_Details);
//        }
        switch (v.getId()) {
            case R.id.join_id:
                System.out.println("I have to enter here !! ");
                key = getIntent().getStringExtra("KEY");
                Intent intent = new Intent(GroupMembers.this, MemberDetails.class);
                intent.putExtra("KEY", key);
                startActivity(intent);
        }
    }

    public void showSimpleAlert(String verName, String verNum) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(verName);
        alertDialog.setMessage(" " + verNum);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }

    public void fetchDataFromFireBase() {
        key = getIntent().getStringExtra("KEY");
        // get data about email
        // get data about phone num
        //String k = ref.child("Groups").getPath().toString();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                membersInfo.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!ds.getKey().equals("Time") && !ds.getKey().equals("imageUrl") && !ds.getKey().equals("location") && !ds.getKey().equals("numberofmembers") && !ds.getKey().equals("restaurantName")) {
                        name = (String) ds.child("name").getValue();
                        number = (String) ds.child("phoneNumber").getValue();
                        /*System.out.println("name is \t" + name);
                        System.out.println("num is \t" + number);*/
                        MembersInfo members = new MembersInfo(name, number);
                        membersInfo.add(members);
                    }
                }
                membersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.child("Groups").child(key).addValueEventListener(valueEventListener);
    }




    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {


        public DownloadImageFromInternet() {


        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            Group_Image.setImageBitmap(result);
        }
    }
}
