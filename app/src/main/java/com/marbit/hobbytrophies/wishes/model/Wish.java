package com.marbit.hobbytrophies.wishes.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wish implements Parcelable{
    private String id;
    private String userId;
    private int itemType;
    private String name;
    private String gameId;
    private int consoleId = -1;
    private boolean barter;
    private boolean digital;
    private int minPrice = 0;
    private int maxPrice = 1000;

    public Wish(){}

    protected Wish(Parcel in) {
        id = in.readString();
        userId = in.readString();
        itemType = in.readInt();
        name = in.readString();
        gameId = in.readString();
        consoleId = in.readInt();
        barter = in.readByte() != 0;
        digital = in.readByte() != 0;
        minPrice = in.readInt();
        maxPrice = in.readInt();
    }

    public static final Creator<Wish> CREATOR = new Creator<Wish>() {
        @Override
        public Wish createFromParcel(Parcel in) {
            return new Wish(in);
        }

        @Override
        public Wish[] newArray(int size) {
            return new Wish[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getConsoleId() {
        return consoleId;
    }

    public void setConsoleId(int consoleId) {
        this.consoleId = consoleId;
    }

    public boolean isBarter() {
        return barter;
    }

    public void setBarter(boolean barter) {
        this.barter = barter;
    }

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeInt(itemType);
        dest.writeString(name);
        dest.writeString(gameId);
        dest.writeInt(consoleId);
        dest.writeByte((byte) (barter ? 1 : 0));
        dest.writeByte((byte) (digital ? 1 : 0));
        dest.writeInt(minPrice);
        dest.writeInt(maxPrice);
    }
}
