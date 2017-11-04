package com.marbit.hobbytrophies.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.marbit.hobbytrophies.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by marcelo on 13/01/17.
 */

public class Meeting implements Parcelable {
    private Calendar calendar = Calendar.getInstance();
    private int id;
    private String description;
    private Date date;
    private int type;
    private int reserved;
    private int limitMembers;
    private int state;
    private int duration;
    private Game game;
    private String userOwner;
    private String userOwnerAvatar;
    private List<Trophy> trophyList;
    private List<String> membersList;
    private DateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private boolean full;

    public Meeting(Date date, int type, String description){
        this.date = date;
        this.type = type;
        this.description = description;
        this.calendar.setTime(date);
    }

    public Meeting(){
        this.date = new Date();
    }

    protected Meeting(Parcel in) {
        id = in.readInt();
        description = in.readString();
        type = in.readInt();
        reserved = in.readInt();
        limitMembers = in.readInt();
        state = in.readInt();
        duration = in.readInt();
        game = in.readParcelable(Game.class.getClassLoader());
        trophyList = in.createTypedArrayList(Trophy.CREATOR);
        membersList = in.createStringArrayList();
        userOwner = in.readString();
        userOwnerAvatar = in.readString();
        date = new Date(in.readLong());
        full = in.readByte() != 0;
    }

    public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate(Context context) {
        String[] dayOfWeekArray = context.getResources().getStringArray(R.array.dayOfWeek);
        calendar.setTime(this.date);
        return dayOfWeekArray[calendar.get(Calendar.DAY_OF_WEEK)-1] + " ," + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
        this.calendar.setTime(date);
    }

    public String getTime(){
        String formattedDate = format.format(calendar.getTime());
        return formattedDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLimitMembers() {
        return limitMembers;
    }

    public void setLimitMembers(int limitMembers) {
        this.limitMembers = limitMembers;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Trophy> getTrophyList() {
        return trophyList;
    }

    public void setTrophyList(List<Trophy> trophyList) {
        this.trophyList = trophyList;
    }

    public List<String> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<String> membersList) {
        this.membersList = membersList;
    }

    public int getColor() {
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return R.color.colorAccent;
            case 2:
                return R.color.material_cyan_A700;
            case 3:
                return R.color.material_amber_A700;
            case 4:
                return R.color.material_accentPink_a400;
            case 5:
                return R.color.material_yellow_900;
            case 6:
                return R.color.material_white;
            default:
                return R.color.material_red_800;
        }
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("mid", id);
        result.put("description", description);
        result.put("date", date.getTime());
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(description);
        dest.writeInt(type);
        dest.writeInt(reserved);
        dest.writeInt(limitMembers);
        dest.writeInt(state);
        dest.writeInt(duration);
        dest.writeParcelable(game, flags);
        dest.writeTypedList(trophyList);
        dest.writeStringList(membersList);
        dest.writeString(userOwner);
        dest.writeString(userOwnerAvatar);
        dest.writeLong(date.getTime());
        dest.writeByte((byte) (full ? 1 : 0));
    }

    public String getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(String userOwner) {
        this.userOwner = userOwner;
    }

    public String getUserOwnerAvatar() {
        return userOwnerAvatar;
    }

    public void setUserOwnerAvatar(String userOwnerAvatar) {
        this.userOwnerAvatar = userOwnerAvatar;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean isFull) {
        this.full = isFull;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }
}
