package com.marbit.hobbytrophies.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.MeetingDetailInteractor;
import com.marbit.hobbytrophies.interfaces.MeetingDetailView;
import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.model.User;

import java.text.ParseException;
import java.util.List;

/**
 * Created by marcelo on 20/03/17.
 */

public class MeetingDetailPresenter {
    private MeetingDetailInteractor interactor;
    private MeetingDetailView view;
    private Meeting meeting;
    private List<User> playersList;
    private List<Trophy> trophyList;

    public MeetingDetailPresenter(Context context, MeetingDetailView meetingDetailInterface){
        this.interactor = new MeetingDetailInteractor(context, this);
        this.view = meetingDetailInterface;
    }

    public void start(Meeting meeting){
        this.setMeeting(meeting);
        this.interactor.start(meeting);
        this.setDescription();
        try {
            this.initTimer();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initTimer() throws ParseException {
        this.interactor.initTimer();
    }

    private void setDescription() {
        this.view.setDescriptionMeeting(this.meeting.getDescription());
    }

    public void setMeeting(Meeting meeting){
        this.meeting = meeting;
    }


    public void setRemainingTime(long days, long hours,long minutes,long seconds) {
        this.view.setTimerMeeting(days, hours, minutes, seconds);
    }

    public void editGeneralInfoMeeting() {
        this.view.openEditMeeting();
    }
}
