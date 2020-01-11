package com.mahmood_anas.weeat;

public class GroupInfo {

    private String restaurantName;
    private String location;
    private String number_of_members;
    private String time_added;
    private String imageResourceId;

    public GroupInfo(String name, String number, String imageResourceId, String number_of_members, String time_added)
    {
        this.restaurantName = name;
        this.location = number;
        this.number_of_members = number_of_members;
        this.time_added = time_added;
        this.imageResourceId = imageResourceId;
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

    public String getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
