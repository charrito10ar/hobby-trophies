package com.marbit.hobbytrophies.interactors;

import android.content.Context;
import android.os.CountDownTimer;

import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.presenters.MeetingDetailPresenter;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DateUtils;

import java.text.ParseException;

/**
 * Created by marcelo on 20/03/17.
 */

public class MeetingDetailInteractor {

    private Meeting meeting;
    private Context context;
    private MeetingDetailPresenter presenter;
    private CountDownTimer countDownTimer;

    public MeetingDetailInteractor(Context context, MeetingDetailPresenter meetingDetailPresenter) {
        this.context = context;
        this.presenter = meetingDetailPresenter;
    }

    public void start(Meeting meeting) {
        this.meeting = meeting;
    }

    public void initTimer() throws ParseException {
        long timerMilliseconds = DateUtils.getInstance().getCountDown(this.meeting.getDate());

        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timerMilliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                long days = millisUntilFinished / Constants.MS_PER_DIA;
                long restoDias = millisUntilFinished % Constants.MS_PER_DIA;
                long hours = restoDias / Constants.MS_PER_HORA;
                long restoHoras = restoDias % Constants.MS_PER_HORA;
                long minutes = restoHoras / Constants.MS_PER_MINUTO;
                long restoMinutos = restoHoras % Constants.MS_PER_MINUTO;
                long seconds = restoMinutos / Constants.MS_PER_SEGUNDO;
                presenter.setRemainingTime(days, hours, minutes, seconds);
            }

            public void onFinish() {
                presenter.setRemainingTime(0, 0, 0, 0);
            }
        }.start();

    }

    public void editGeneralInfoMeeting() {

    }
}
