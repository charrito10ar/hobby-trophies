package com.marbit.hobbytrophies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by marcelo on 17/11/16.
 */

public class Game implements Parcelable, Comparable<Game>{
    private String id;
    private String name;
    private String img;
    private String trophiesUrl;
    private String platinumAmount;
    private String goldenAmount;
    private String silverAmount;
    private String bronzeAmount;
    private Double percentComplete;
    private List<String> platform; //"ps3,psp2,ps4"
    private List<Trophy> trophies;
    private int lastUpdate;


    public Game(){
        this.percentComplete = 0d;
    }
    public Game(String id){
        this.id= id;
        this.percentComplete = 0d;
    }

    protected Game(Parcel in) {
        id = in.readString();
        name = in.readString();
        img = in.readString();
        trophiesUrl = in.readString();
        platinumAmount = in.readString();
        goldenAmount = in.readString();
        silverAmount = in.readString();
        bronzeAmount = in.readString();
        platform = in.createStringArrayList();
        trophies = in.createTypedArrayList(Trophy.CREATOR);
        percentComplete = in.readDouble();
        lastUpdate = in.readInt();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTrophiesUrl() {
        return trophiesUrl;
    }

    public void setTrophiesUrl(String trophiesUrl) {
        this.trophiesUrl = trophiesUrl;
    }

    public List<String> getPlatform() {
        return platform;
    }

    public void setPlatform(List<String> platform) {
        this.platform = platform;
    }

    public String getPlatinumAmount() {
        return platinumAmount;
    }

    public void setPlatinumAmount(String platinumAmount) {
        this.platinumAmount = platinumAmount;
    }

    public String getGoldenAmount() {
        return goldenAmount;
    }

    public void setGoldenAmount(String goldenAmount) {
        this.goldenAmount = goldenAmount;
    }

    public String getSilverAmount() {
        return silverAmount;
    }

    public void setSilverAmount(String silverAmount) {
        this.silverAmount = silverAmount;
    }

    public String getBronzeAmount() {
        return bronzeAmount;
    }

    public void setBronzeAmount(String bronzeAmount) {
        this.bronzeAmount = bronzeAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(img);
        parcel.writeString(trophiesUrl);
        parcel.writeString(platinumAmount);
        parcel.writeString(goldenAmount);
        parcel.writeString(silverAmount);
        parcel.writeString(bronzeAmount);
        parcel.writeStringList(platform);
        parcel.writeTypedList(trophies);
        parcel.writeDouble(percentComplete);
        parcel.writeInt(lastUpdate);
    }

    public List<Trophy> getTrophies() {
        return trophies;
    }

    public void setTrophies(List<Trophy> trophies) {
        this.trophies = trophies;
    }

    public Double getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Double percentComplete) {
        this.percentComplete = percentComplete;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public int compareTo(Game game) {
        if (lastUpdate < game.getLastUpdate()) {
            return 1;
        }
        if (lastUpdate > game.lastUpdate) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Game.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Game other = (Game) obj;
        if (this.id.equals(other.id)) {
            return true;
        }
        return false;
    }
}
