package com.marbit.hobbytrophies.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private String id;
    private String name;
    private String psnId;
    private String email;
    private String country;
    private String avatarUrl;
    private String color;
    private boolean isPsnPlus;
    private int points;
    private int level;
    private int progress;
    private int platinum;
    private int gold;
    private int silver;
    private int bronze;
    private int total;
    private double percentTrophiesCompleted;
    private List<Game> gamesList;
    private int rolMeeting;
    private int trophiesWonInMeeting;

    public User(String psnId){
        this.psnId = psnId;
        this.gamesList = new ArrayList<>();
        this.isPsnPlus = false;
        this.rolMeeting = 1;
    }

    public User(){
        this.gamesList = new ArrayList<>();
        this.isPsnPlus = false;
        this.rolMeeting = 1;
    }

    public User(String psnId, String avatarUrl){
        this.psnId = psnId;
        this.avatarUrl = avatarUrl;
    }

    protected User(Parcel in) {
        id = in.readString();
        psnId = in.readString();
        email = in.readString();
        country = in.readString();
        avatarUrl = in.readString();
        color = in.readString();
        isPsnPlus = in.readByte() != 0;
        points = in.readInt();
        level = in.readInt();
        progress = in.readInt();
        platinum = in.readInt();
        gold = in.readInt();
        silver = in.readInt();
        bronze = in.readInt();
        total = in.readInt();
        percentTrophiesCompleted = in.readDouble();
        gamesList = in.createTypedArrayList(Game.CREATOR);
        rolMeeting = in.readInt();
        trophiesWonInMeeting = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPsnId() {
        return psnId;
    }

    public void setPsnId(String psnId) {
        this.psnId = psnId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isPsnPlus() {
        return isPsnPlus;
    }

    public void setPsnPlus(boolean psnPlus) {
        isPsnPlus = psnPlus;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPlatinum() {
        return platinum;
    }

    public void setPlatinum(int platinum) {
        this.platinum = platinum;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public double getPercentTrophiesCompleted() {
        return percentTrophiesCompleted;
    }

    public void setPercentTrophiesCompleted(double percentTrophiesCompleted) {
        this.percentTrophiesCompleted = percentTrophiesCompleted;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Game> getGamesList() {
        return gamesList;
    }

    public void setGamesList(List<Game> gamesList) {
        this.gamesList = gamesList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(psnId);
        parcel.writeString(email);
        parcel.writeString(country);
        parcel.writeString(avatarUrl);
        parcel.writeString(color);
        parcel.writeByte((byte) (isPsnPlus ? 1 : 0));
        parcel.writeInt(points);
        parcel.writeInt(level);
        parcel.writeInt(progress);
        parcel.writeInt(platinum);
        parcel.writeInt(gold);
        parcel.writeInt(silver);
        parcel.writeInt(bronze);
        parcel.writeInt(total);
        parcel.writeDouble(percentTrophiesCompleted);
        parcel.writeTypedList(gamesList);
        parcel.writeInt(rolMeeting);
        parcel.writeInt(trophiesWonInMeeting);
    }

    public int getRolMeeting() {
        return rolMeeting;
    }

    public void setRolMeeting(int rolMeeting) {
        this.rolMeeting = rolMeeting;
    }

    public int getTrophiesWonInMeeting() {
        return trophiesWonInMeeting;
    }

    public void setTrophiesWonInMeeting(int trophiesWonInMeeting) {
        this.trophiesWonInMeeting = trophiesWonInMeeting;
    }
}
