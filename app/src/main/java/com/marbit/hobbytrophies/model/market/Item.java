package com.marbit.hobbytrophies.model.market;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Parcelable{
    public static final int ON_SALE = 0;
    public static final int SOLD = 1;

    private String id;
    private String userId;
    private String userName;
    private String title;
    private String gameId;
    private int consoleId;
    private String description;
    private double price;
    private int itemCategory;
    private ArrayList<File> images;
    private int imageAmount;
    private boolean isDigital;
    private boolean isBarter;
    private Object date;
    private int status;

    public Item(String userId, String userName, String description, int itemCategory, String price, boolean isDigital, boolean isBarter) {
        this.userId = userId;
        this.userName = userName;
        this.description = description;
        this.itemCategory = itemCategory;
        this.price = Double.valueOf(price);
        this.images = new ArrayList<>();
        this.isDigital = isDigital;
        this.isBarter = isBarter;
        this.date = ServerValue.TIMESTAMP;
    }

    public Item(){
        this.date = ServerValue.TIMESTAMP;
    }

    protected Item(Parcel in) {
        id = in.readString();
        userName = in.readString();
        userId = in.readString();
        title = in.readString();
        gameId = in.readString();
        consoleId = in.readInt();
        description = in.readString();
        price = in.readDouble();
        itemCategory = in.readInt();
        images = (ArrayList<File>) in.readSerializable();
        isDigital = in.readByte() != 0;
        isBarter = in.readByte() != 0;
        imageAmount = in.readInt();
        date = in.readValue(Object.class.getClassLoader());
        status = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Exclude
    public List<File> getImages() {
        return images;
    }

    @Exclude
    public void setImages(ArrayList<File> images) {
        this.images = images;
    }

    public int getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public boolean isDigital() {
        return isDigital;
    }

    public void setDigital(boolean digital) {
        isDigital = digital;
    }

    public boolean isBarter() {
        return isBarter;
    }

    public void setBarter(boolean barter) {
        isBarter = barter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userName);
        dest.writeString(userId);
        dest.writeString(title);
        dest.writeString(gameId);
        dest.writeInt(consoleId);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeInt(itemCategory);
        dest.writeSerializable((Serializable) images);
        dest.writeByte((byte) (isDigital ? 1 : 0));
        dest.writeByte((byte) (isBarter ? 1 : 0));
        dest.writeInt(imageAmount);
        dest.writeValue(date);
        dest.writeInt(status);
    }

    public int getImageAmount() {
        return imageAmount;
    }

    public void setImageAmount(int imageAmount) {
        this.imageAmount = imageAmount;
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

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;
        if (v instanceof Item){
            Item ptr = (Item) v;
            retVal = ptr.id.equals(this.id);
        }
        return retVal;
    }

    public String getUserName() {
        if(userName != null)
            return userName;
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}