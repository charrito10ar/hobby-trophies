package com.marbit.hobbytrophies.interfaces;

import com.marbit.hobbytrophies.model.Meeting;

public interface MeetingDetailView{

    void setDescriptionMeeting(String description);

    void setTimerMeeting(long days, long hours, long minutes, long seconds);

    void openEditMeeting();

    void shareMeetingLink(Meeting meeting, String longMeetingLink);
}
