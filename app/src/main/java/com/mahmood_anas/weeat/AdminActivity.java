package com.mahmood_anas.weeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener {

    private ArrayList<MembersInfo> membersInfo;
    TextView Group_Name;
    ImageView Group_Image;
    String key, name, number;
    Button Done;
    //String url;
    FirebaseDatabase database;
    DatabaseReference ref;
    MembersAdapter membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Group_Name = findViewById(R.id.final_grp_name);
        Group_Image = findViewById(R.id.final_grp_img);
        Done = findViewById(R.id.done);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        membersInfo = new ArrayList<MembersInfo>();
        membersAdapter = new MembersAdapter(this, membersInfo);
        ListView listView = findViewById(R.id.list_members_2);
        listView.setAdapter(membersAdapter);

        SharedPreferences sp = getSharedPreferences("my_id", Context.MODE_PRIVATE);

        key = sp.getString("id", null);
        System.out.println("message in Admin" + key);
        sp.edit().clear().apply();


        // add Item Click Listener
        listView.setOnItemClickListener(this);
        //new DownloadImageFromInternet().execute(url);
        Done.setOnClickListener(this);

        fetchDataFromFireBase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.done:
                ref.child("Groups").child(key).removeValue();
                finish();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void fetchDataFromFireBase() {
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
