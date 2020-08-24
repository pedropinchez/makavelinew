package com.maxbets.makaveli;

public class Profile {
    private String username;
     private String age;
    private   String date;
     private   String timestamp;
    private String imageUrl;

    public Profile() {
    }

    public Profile(String username, String age, String date, String timestamp, String imageUrl) {
        this.username = username;
        this.age = age;
        this.date = date;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public  String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public  String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public  String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public  String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}