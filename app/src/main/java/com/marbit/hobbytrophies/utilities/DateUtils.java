package com.marbit.hobbytrophies.utilities;

import com.marbit.hobbytrophies.fragments.MeetingFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by marcelo on 11/03/17.
 */

public class DateUtils {
    public static final String DATE_FORMAT_DATA_BASE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_UI = "dd-MM-yyyy HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm";
    private final TimeZone currentTimeZone;
    private long millisDifference;
    private SimpleDateFormat dateFormatDataBase;
    private SimpleDateFormat dateFormatUI;
    private Calendar mCalendar = new GregorianCalendar();


    private static DateUtils instance = null;

    public static DateUtils getInstance() {
        if(instance == null) {
            instance = new DateUtils();
        }
        return instance;
    }

    public DateUtils(){
        currentTimeZone = mCalendar.getTimeZone();
        long currentTime = System.currentTimeMillis();
        int currentOffset = currentTimeZone.getOffset(currentTime);
        int dataBaseOffset = TimeZone.getTimeZone("America/Chicago").getOffset(currentTime);
        millisDifference = (currentOffset - dataBaseOffset);
        dateFormatDataBase = new SimpleDateFormat(DATE_FORMAT_DATA_BASE);
        dateFormatUI = new SimpleDateFormat(DATE_FORMAT_UI);
    }

    public String convertToLocalTimeString(String date) throws ParseException {
        long correctDate = dateFormatDataBase.parse(date).getTime() + millisDifference;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(correctDate);
        return this.dateFormatUI.format(calendar.getTime());
    }

    public Date convertToLocalTimeDate(String date) throws ParseException {
        long correctDate = dateFormatDataBase.parse(date).getTime() + millisDifference;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(correctDate);
        return calendar.getTime();
    }

    public Date convertToLocalTimeDate(Date date) throws ParseException {
        long correctDate = date.getTime() + millisDifference;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(correctDate);
        return calendar.getTime();
    }

    public String getDataBaseTime(){
        long correctDate = mCalendar.getTime().getTime() - millisDifference;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(correctDate);
        return this.dateFormatDataBase.format(calendar.getTime());
    }

    /**
     *
     * @param millisDate: Local time.
     * @return
     * @throws ParseException
     */
    public String convertToServerTime(long millisDate) throws ParseException {
        long correctDate = millisDate - millisDifference;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(correctDate);
        return this.dateFormatDataBase.format(calendar.getTime());
    }

    public boolean meetingIsFinish(Date date) throws ParseException {
        if(mCalendar.getTime().after(date)){
            return true;
        }
        return false;
    }

    public long getCountDown(Date date) throws ParseException {
        Calendar mCalendar = Calendar.getInstance();
        long countDownTime = date.getTime() - mCalendar.getTimeInMillis();
        if(countDownTime < 0)
            return 0;
        return countDownTime;
    }

    public boolean isDiferentDay(long dateOne, long dateTwo) {
        Calendar mCalendarOne = Calendar.getInstance();
        mCalendarOne.setTime(new Date(dateOne));
        Calendar mCalendarTwo = Calendar.getInstance();
        mCalendarTwo.setTime(new Date(dateTwo));

        int dayOfYearOne = mCalendarOne.get(Calendar.DAY_OF_YEAR);
        int dayOfYearTwo = mCalendarTwo.get(Calendar.DAY_OF_YEAR);

        if(dayOfYearOne != dayOfYearTwo){
            return true;
        }else {
            return false;
        }
    }
}
