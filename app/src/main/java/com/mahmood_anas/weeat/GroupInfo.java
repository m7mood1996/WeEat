package com.mahmood_anas.weeat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class GroupInfo {



    private String groupKey;
    private String restaurantName;
    private String location;
    private String number_of_members;
    private String time_added;
    private String imageUrl;

    public Bitmap bitmap;

    public GroupInfo(String name, String number, String imageUrl, String number_of_members, String time_added,String groupKey)
    {
        this.groupKey = groupKey;
        this.restaurantName = name;
        this.location = number;
        this.number_of_members = number_of_members;
        this.time_added = time_added;
        this.imageUrl = imageUrl;
        this.bitmap = null;
        new DownloadImageFromInternet().execute(getImageUrl());
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String resurantName) {
        this.restaurantName = resurantName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber_of_members() {
        return number_of_members;
    }

    public void setNumber_of_members(String number_of_members) {
        this.number_of_members = number_of_members;
    }

    public String getTime_added() {
        return time_added;
    }

    public void setTime_added(String time_added) {
        this.time_added = time_added;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setView(ImageView imageView, GroupInfoAdapter groupInfoAdapter){
        if(this.bitmap !=null)
        imageView.setImageBitmap(this.bitmap);
        else
            imageView.setImageResource(R.drawable.donut);

        groupInfoAdapter.notifyDataSetChanged();

    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
            bitmap =result;
        }

    }

}
