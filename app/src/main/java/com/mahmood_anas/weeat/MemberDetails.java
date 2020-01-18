package com.mahmood_anas.weeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberDetails extends AppCompatActivity implements View.OnClickListener {

    TextView Mem_Name, Mem_Num;
    Button Add_Mem;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        Mem_Name = findViewById(R.id.new_name);
        Mem_Num = findViewById(R.id.new_num);
        Add_Mem = findViewById(R.id.add_member);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_member){
            System.out.println("You Clicked on this button !");
            if (Mem_Name.getText().toString().equals("") || Mem_Num.getText().toString().equals("")){
                Toast.makeText(this,  " Please fill the fields !", Toast.LENGTH_SHORT).show();
            }
            else {
                addMember();
                finish();
            }
        }
    }

    public void addMember() {
    key = getIntent().getStringExtra("KEY");
    DatabaseReference joinGroup = (FirebaseDatabase.getInstance().getReference()).child("Groups").child(key).push();
    joinGroup.child("name").setValue(Mem_Name.getText().toString());
    joinGroup.child("phoneNumber").setValue(Mem_Num.getText().toString());
    }
}
