package com.mahmood_anas.weeat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MembersAdapter extends ArrayAdapter<MembersInfo> {

    public MembersAdapter(Activity context, ArrayList<MembersInfo> Members) {

        super(context, 0, Members);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.members_layout, parent, false);


        // Get the {@link AndroidFlavor} object located at this position in the list
        MembersInfo currentMembersInfo = getItem(position);

        // Find the TextView in the list_item2.xmll layout with the ID version_name
        TextView name_member = listItemView.findViewById(R.id.member_name);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        name_member.setText(currentMembersInfo.getName());

        // Find the TextView in the list_item2.xml layout with the ID version_number
        TextView number_member = listItemView.findViewById(R.id.member_num);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        number_member.setText(currentMembersInfo.getNumber());



        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}



