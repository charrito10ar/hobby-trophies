package com.marbit.hobbytrophies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trophy implements Parcelable{
    private int id;
    private String title;
    private String description;
    private String type;
    private String img;
    private String guide;
    private String imgGuideUrl;
    private String youtubeUrl;
    private String iconsGuide;
    private String iconGuideExtra;
    private String idDlc;
    private String dateGet;
    private boolean selected;


    public Trophy(){
        this.idDlc = "null";
    }

    protected Trophy(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        type = in.readString();
        img = in.readString();
        guide = in.readString();
        imgGuideUrl = in.readString();
        youtubeUrl = in.readString();
        iconsGuide = in.readString();
        iconGuideExtra = in.readString();
        idDlc = in.readString();
        dateGet = in.readString();
    }

    public static final Creator<Trophy> CREATOR = new Creator<Trophy>() {
        @Override
        public Trophy createFromParcel(Parcel in) {
            return new Trophy(in);
        }

        @Override
        public Trophy[] newArray(int size) {
            return new Trophy[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(type);
        parcel.writeString(img);
        parcel.writeString(guide);
        parcel.writeString(imgGuideUrl);
        parcel.writeString(youtubeUrl);
        parcel.writeString(iconsGuide);
        parcel.writeString(iconGuideExtra);
        parcel.writeString(idDlc);
        parcel.writeString(dateGet);
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getImgGuideUrl() {
        return imgGuideUrl;
    }

    public void setImgGuideUrl(String imgGuideUrl) {
        this.imgGuideUrl = imgGuideUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getIconsGuide() {
        return iconsGuide;
    }

    public void setIconsGuide(String iconsGuide) {
        this.iconsGuide = iconsGuide;
    }

    public String getIconGuideExtra() {
        return iconGuideExtra;
    }

    public void setIconGuideExtra(String iconGuideExtra) {
        this.iconGuideExtra = iconGuideExtra;
    }

    public String getIdDlc() {
        return idDlc;
    }

    public void setIdDlc(String idDlc) {
        this.idDlc= idDlc;
    }

    public String getDateGet() {
        return dateGet;
    }

    public void setDateGet(String dateGet) {
        this.dateGet = dateGet;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selectd){
        this.selected = selectd;
    }

}
