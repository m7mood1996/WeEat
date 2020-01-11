package com.mahmood_anas.weeat;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private ArrayList<GroupInfo> groupInfos;
    FirebaseDatabase database;
    DatabaseReference ref;
    GroupInfoAdapter groupInfosAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,newGroupActivity.class);
                startActivity(intent);
            }
        });
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        groupInfos = new ArrayList<GroupInfo>();

        groupInfosAdapter = new GroupInfoAdapter(this, groupInfos);

        // Get a reference to the ListView, and attach the adapter to the listView.
        ListView listView = findViewById(R.id.list_view_groups);
        listView.setAdapter(groupInfosAdapter);

        // add Item Click Listener
        listView.setOnItemClickListener(this);
        fetchDataFromFireBase();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        GroupInfo groupInfo = groupInfos.get(position);
        showSimpleAlert(groupInfo.getImageResourceId(), groupInfo.getRestaurantName() , groupInfo.getLocation(),groupInfo.getNumber_of_members(),groupInfo.getTime_added());
    }

    public void showSimpleAlert(String imageResId, String resurantName,String location, String number_of_members, String time_added)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(resurantName);
        alertDialog.setMessage("Ver Number: " +location + "number_of_members:" + number_of_members + "time added:" + time_added + "image" + imageResId);
        alertDialog.setIcon(R.drawable.donut);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }


    public void fetchDataFromFireBase() {

        // get data about email

        // get data about phone num

        ref.child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                System.out.println(snapshot.getValue() + "hello here");
                groupInfos.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String restaurantName = (String) ds.child("restaurantName").getValue();

                    String imageResourceId = (String)ds.child("imageResourceId").getValue();
                    String Time = (String) ds.child("Time").getValue();
                    String location = (String) ds.child("location").getValue();
                    String number = (String) ds.child("numberofmembers").getValue();

                    GroupInfo groupInfo = new GroupInfo(restaurantName,number,imageResourceId,Time,location);
                    groupInfos.add(groupInfo);

                }

                groupInfosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }


}
