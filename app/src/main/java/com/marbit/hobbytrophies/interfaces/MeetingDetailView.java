package com.marbit.hobbytrophies.interfaces;

/**
 * Created by marcelo on 20/03/17.
 */

public interface MeetingDetailView{

    void setDescriptionMeeting(String description);

    void setTimerMeeting(long days, long hours, long minutes, long seconds);

    void openEditMeeting();
}
