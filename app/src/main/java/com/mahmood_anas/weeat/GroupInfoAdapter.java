package com.mahmood_anas.weeat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;


public class GroupInfoAdapter extends ArrayAdapter<GroupInfo> {



    public GroupInfoAdapter(Activity context, ArrayList<GroupInfo> groupInfos) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, groupInfos);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.group_list_view, parent, false);


        // Get the {@link AndroidFlavor} object located at this position in the list
        GroupInfo currentGroupInfo = getItem(position);

        // Find the TextView in the group_list_view.xml layout with the ID restaurant_name
        TextView nameTextView = listItemView.findViewById(R.id.restaurant_name);
        // Get the version name from the current GroupInfo object and
        // set this text on the name TextView
        nameTextView.setText(currentGroupInfo.getRestaurantName());

        // Find the TextView in the group_list_view.xml layout with the ID location
        TextView locationTextView = listItemView.findViewById(R.id.location);
        // Get the location from the current GroupInfo object and
        // set this text on the location TextView
        locationTextView.setText(currentGroupInfo.getLocation());

        // Find the TextView in the group_list_view.xml layout with the ID number_of_members
        TextView numOfMembersTextView = listItemView.findViewById(R.id.number_of_members);
        // Get the number of members from the current GroupInfo object and
        // set this text on TextView
        numOfMembersTextView.setText(currentGroupInfo.getNumber_of_members());

        // Find the TextView in the group_list_view.xml layout with the ID time_added
        TextView time_addedTextView = listItemView.findViewById(R.id.time_added);
        // Get the time added from the current GroupInfo object and
        // set this text on TextView
        time_addedTextView.setText(currentGroupInfo.getTime_added());


        // Find the ImageView in the group_list_view.xml layout with the ID list_item_icon
        ImageView iconView = listItemView.findViewById(R.id.list_item_icon);
        // Get the image resource ID from the current GroupInfo object and
        // set the image to iconView
        System.out.println(currentGroupInfo.getImageUrl() + "hello");

        //iconView.setImageBitmap(currentGroupInfo.bitmap);

        currentGroupInfo.setView(iconView,this);
        this.notifyDataSetChanged();
        //iconView.setImageResource(R.drawable.donut);


            //////Have lsader


        // Return the whole list item layout (containing 4 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }



}
