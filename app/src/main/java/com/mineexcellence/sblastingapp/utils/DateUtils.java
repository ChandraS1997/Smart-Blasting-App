package com.mineexcellence.sblastingapp.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateUtils {


    public static final String SINGLE_CHAT_DATE_FORMAT = "dd MMM yy";
    public static final String CHAT_HISTORY_ROW_DATE_FORMAT = "hh:mm aa dd/MM/yy";
    public static final String CONVERSATION_HEADER_DATE_FORMAT = "dd MMMM yyyy";
    public static final String CHAT_DISPLAY_DATE_FORMAT = "hh:mm a";
    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String appDateFormat="dd/MM/yyyy";
    public static final String dateFormat1="yyyy-MM-dd";
    public static final String dateFormat2="dd MMM";
    public static final String appTimeFormat="hh:mm aa";
    public static final String timeFormat1="hh:mm:ss";
    public static final String postDateFormat="dd MMMM";
    public static final String monthdateFormat="MMMM,yyyy";
    public static final String monthYear="MMM yyyy";
    public static long secondsInMilli = 1000;
    public static long minutesInMilli = secondsInMilli * 60;
    public static long hoursInMilli = minutesInMilli * 60;
    public static long daysInMilli = hoursInMilli * 24;

    public static String getDate(Date date, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return formatter.format(date.getTime());
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date = new Date(milliSeconds);
        return formatter.format(date.getTime());
    }



    public static Date getDateFromStringDate(String dateToConvert, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(dateToConvert);
        } catch (Exception ignored) {

        }
        return date;
    }


    public static String parseStringDate(String dateToConvert, String inputDateFormat, String outputDateFormat) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputDateFormat, Locale.getDefault());
        try {
            Date date = inputFormat.parse(dateToConvert);
            return outputFormat.format(date);
        } catch (ParseException | NullPointerException e) {
            // e.printStackTrace();
            return dateToConvert == null ? "" : dateToConvert;
        }
    }

//    public static Date getCurrentDate(){
//        return new Date(System.currentTimeMillis());
//    }


    public static SimpleDateFormat getdateFormat(String format){
        return new SimpleDateFormat(format);
    }

    public static String getCurrentDate() {

        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat(appDateFormat, Locale.getDefault());
            Date currentDate = new Date(System.currentTimeMillis());

            return outputFormat.format(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDate(String format) {

        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.getDefault());
            Date currentDate = new Date(System.currentTimeMillis());

            return outputFormat.format(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentTime() {
        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat(appTimeFormat, Locale.getDefault());
            Date currentDate = new Date(System.currentTimeMillis());

            return outputFormat.format(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static long getTimeInMillis(String date, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat, Locale.getDefault());

        try {
            //Convert to Date
            Date startDate = df.parse(date);
            Calendar c1 = Calendar.getInstance();
            //Change to Calendar Date
            c1.setTime(startDate);
            return c1.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String dateDifference(String today, String lastDate) {

        long diff = 0;

        try {
            //get Time in milli seconds
            long ms1 = getTimeInMillis(today, "yyyyMMdd");
            long ms2 = getTimeInMillis(lastDate, "yyyyMMdd");
            //get difference in milli seconds
            diff = ms2 - ms1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Find number of days by dividing the mili seconds
        int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
        //System.out.println("Number of days difference is: " + diffInDays);

        return "" + diffInDays;
        //To get number of seconds diff/1000
        //To get number of minutes diff/(1000 * 60)
        //To get number of hours diff/(1000 * 60 * 60)

    }



    public static String convertDateFormat(Date dateToConvert, String toDateFormat) {
        SimpleDateFormat outputFormat = new SimpleDateFormat(toDateFormat, Locale.getDefault());
        return outputFormat.format(dateToConvert);
    }


    public static String getDateDifference(String startDate, String endDate, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());

        try {
            Date getStartDate = simpleDateFormat.parse(startDate);
            Date getEndDate = simpleDateFormat.parse(endDate);

            //milliseconds
            long dateDifference = getEndDate.getTime() - getStartDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("dateDifference : " + dateDifference);



            long elapsedDays = dateDifference / daysInMilli;
            if(elapsedDays==1){
                return elapsedDays+" Day";
            }
            else if(elapsedDays>1){
                return elapsedDays+" Days";
            }
            // dateDifference = dateDifference % daysInMilli;

            long elapsedHours = dateDifference / hoursInMilli;

            if(elapsedHours>=1){
                return elapsedHours+" hr";
            }

            //  dateDifference = dateDifference % hoursInMilli;

            long elapsedMinutes = dateDifference / minutesInMilli;
            //  dateDifference = dateDifference % minutesInMilli;
            if(elapsedMinutes>=1){
                return elapsedMinutes+" min";
            }
            long elapsedSeconds = dateDifference / secondsInMilli;

            if(elapsedSeconds>=1){
                return elapsedSeconds+" sec";
            }

            System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
            return "0";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";

    }


    public static String calculateAge(String input_date,String dateFormat) {
        String ageTxt="";
        Calendar c = Calendar.getInstance();
        c.setTime(getDateFromStringDate(input_date,dateFormat));

        Calendar today = Calendar.getInstance();

        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        int currentMonth = today.get(Calendar.MONTH);
        int currentYear = today.get(Calendar.YEAR);

        Date now = new Date(currentYear, currentMonth, currentDay);

        int birthDay = c.get(Calendar.DAY_OF_MONTH);
        int birthMonth = c.get(Calendar.MONTH);
        int birthYear = c.get(Calendar.YEAR);

        Date dob = new Date(birthYear, birthMonth, birthDay);

        if (dob.after(now)) {
            Log.e("error", "Birthday can't in future");
            return ageTxt;
        }
        // days of every month
        int months[] = {31, 28, 31, 30, 31, 30, 31,
                31, 30, 31, 30, 31};

        // if birth date is greater then current birth
        // month then do not count this month and add 30
        // to the date so as to subtract the date and
        // get the remaining days
        if (birthDay > currentDay) {
            currentDay = currentDay + months[birthMonth - 1];
            currentMonth = currentMonth - 1;
        }

        // if birth month exceeds current month, then do
        // not count this year and add 12 to the month so
        // that we can subtract and find out the difference
        if (birthMonth > currentMonth) {
            currentYear = currentYear - 1;
            currentMonth = currentMonth + 12;
        }

        // calculate date, month, year
        int calculated_date = currentDay - birthDay;
        int calculated_month = currentMonth - birthMonth;
        int calculated_year = currentYear - birthYear;
        if(calculated_year>0){
            if(calculated_year>1)
                 ageTxt=calculated_year+" yrs";
            else
                ageTxt=calculated_year+" yr";
        }
        if(calculated_month>0){
            if(calculated_month>1)
            ageTxt=ageTxt+" "+calculated_month+" mons";
            else  ageTxt=ageTxt+" "+calculated_month+" mon";
        }
        if(calculated_month==0 && calculated_year==0 ){
            if(calculated_date>1)
                ageTxt=ageTxt+" "+calculated_date+" days";
            else  ageTxt=ageTxt+" "+calculated_date+" day";
        }
        Log.e("age","Year "+calculated_year+" Months "+calculated_month+" DaY "+calculated_date);
        return ageTxt;
    }


    private void nextBirthday(String input_date,String dateFormat) {
        String ageTxt="";
        Calendar c = Calendar.getInstance();
        c.setTime(getDateFromStringDate(input_date,dateFormat));

        Calendar today = Calendar.getInstance();

        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        int currentMonth = today.get(Calendar.MONTH);
        int currentYear = today.get(Calendar.YEAR);
        Calendar current = Calendar.getInstance();
        current.set(currentYear, currentMonth, currentDay);

        int birthDay = c.get(Calendar.DAY_OF_MONTH);
        int birthMonth = c.get(Calendar.MONTH);
        int birthYear = c.get(Calendar.YEAR);


        Calendar birthday = Calendar.getInstance();
        birthday.set(birthYear, birthMonth, birthDay);

        long difference = birthday.getTimeInMillis() - current.getTimeInMillis();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(difference);
        ageTxt=cal.get(Calendar.MONTH)+" "+cal.get(Calendar.DAY_OF_MONTH);
    }

    public static String getAgeFromDOB(String input_date,String dateFormat){

        Calendar c = Calendar.getInstance();
        c.setTime(getDateFromStringDate(input_date,dateFormat));
        int day = c.get(Calendar.DAY_OF_WEEK);
        int month=c.get(Calendar.MONTH);
        int year=c.get(Calendar.YEAR);
        Log.e("dob",""+day+"-"+month+"-"+year);
        Calendar dob = Calendar.getInstance();

        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1,
                today.get(Calendar.DATE));

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {

            age--;

        }



        return age+" Yrs";
    }

    public static String getCurrentDateCompare(String startTime){
        String getTimeDif = "";
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(startTime);
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes= TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours= TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days= TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(minutes<60) {
                getTimeDif = String.valueOf(minutes);
            } else if(hours<24) {
                getTimeDif = String.valueOf(hours)+":"+minutes;
            } else {
                getTimeDif = String.valueOf(days)+":"+hours+":"+minutes;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getTimeDif;
    }

    public static boolean isYesterday(String date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            Date d = simpleDateFormat.parse(date);
            return android.text.format.DateUtils.isToday(d.getTime() + android.text.format.DateUtils.DAY_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isTomorrow(String date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        try {
            Date d = simpleDateFormat.parse(date);
            return android.text.format.DateUtils.isToday(d.getTime() - android.text.format.DateUtils.DAY_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }



    public static boolean isToday(String date_time, String dateFormat) {
        DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date = null;
        try {
            date = (Date) formatter.parse(date_time);
           // System.out.println("Today is " + date.getTime());
            return android.text.format.DateUtils.isToday(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getPrettyDate(String date_time,String inputFormat,String outputFormat){
        if(isYesterday(date_time,inputFormat)){
            return "Yesterday";
        }
        else if(isToday(date_time,inputFormat)){
           return  "Today";
        }
        else if(isTomorrow(date_time,inputFormat)){
            return "Tomorrow";
        }
        return getFormattedTime(date_time,inputFormat,outputFormat);
    }

    public static boolean areSameDate(String d1, String d2, String dateFormat) {
        try {
            Date date1 =getDateFromStringDate(d1, dateFormat);
            Date date2 = getDateFromStringDate(d2, dateFormat);

            /*return date1.getDate() == date2.getDate() &&
                    date1.getMonth() == date2.getMonth() &&
                    date1.getYear() == date2.getYear();*/

            return date1.equals(date2);
        } catch (Exception e) {
            return false;
        }
    }

    public static Long getDateDifferenceInDays(Date startDate, Date endDate) {
        //milliseconds
        Long different = endDate.getTime() - startDate.getTime();

        Long secondsInMilli = 1000L;
        Long minutesInMilli = secondsInMilli * 60;
        Long hoursInMilli = minutesInMilli * 60;
        Long daysInMilli = hoursInMilli * 24;

        Long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        Long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        Long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        Long elapsedSeconds = different / secondsInMilli;

        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return elapsedDays;
    }

    public static Date getDateFromMilliseconds(Long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        Date date = new Date(milliSeconds);
        return date;
    }

    public static List<String> getMonthList(int limit) {
        List<String> monthsList = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -i);
            monthsList.add(getMonthName(calendar.get(Calendar.MONTH)));
        }
        return monthsList;
    }

    public static String getMonthName(int monthPosition) {
        switch (monthPosition) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Jan";
        }
    }


    public static int getMonthNumber(String monthName) {
        switch (monthName) {
            case "January":
                return 0;
            case "February":
                return 1;
            case "March":
                return 2;
            case "April":
                return 3;
            case "May":
                return 4;
            case "June":
                return 5;
            case "July":
                return 6;
            case "August":
                return 7;
            case "September":
                return 8;
            case "October":
                return 9;
            case "November":
                return 10;
            case "December":
                return 11;
            default:
                return 0;
        }
    }

    public static int getTotalDaysOfMonth(String date){
        Calendar calendar = Calendar.getInstance();
        Date date1=getDateFromStringDate("1"+date,"ddMMMM,yyyy");
        calendar.setTime(date1);
        Log.e("days",calendar.getActualMaximum(Calendar.DATE)+"");
        return calendar.getActualMaximum(Calendar.DATE);
    }

    public static boolean isDateBetweenCurrentDate(final Date start, final Date end, final Date date) {

        try {
            Date min = start;
            Date max = end;

            if (min.after(max)) {
                min = end;
                max = start;
            } else if (max.after(min)) {
                min = start;
                max = end;
            } else if (min.compareTo(max) == 0) {
                min = start;
                max = end;
            }
            return !(date.before(min) || date.after(max));
        }catch (Exception e){
            return true;
        }
    }

    public static String getLastMonthDateFromCurDate(String dateFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    public static String getNextMonthDateFromCurDate(String dateFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, +1);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    public static String getDayAndDate(String format, String date, String applyFormat) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date myDate = sdf.parse(date);
        sdf.applyPattern(applyFormat);
        return sdf.format(myDate);
    }


    public static String getCurrentDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek.toLowerCase();
    }



    public static boolean isDateInRange(final Date min, final Date max, final Date date){
        //return !(date.before(min) || date.after(max));
       // return date.after(min) && date.before(max);
       //return min.compareTo(date) * max.compareTo(date) >= 0;
        return date.compareTo(min) >= 0 && date.compareTo(max) <= 0;
    }

    public static String getTimeWithAddMinute(int minuteForAdd, String givenTime){
        SimpleDateFormat df = new SimpleDateFormat(SERVER_DATE_FORMAT);
        Date d = null;
        try {
            d = df.parse(givenTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        assert d != null;
        cal.setTime(d);
        cal.add(Calendar.MINUTE, minuteForAdd);
        return df.format(cal.getTime());
    }

    public static String convertUtcToLocalTime(String dateStr){
        SimpleDateFormat df = new SimpleDateFormat(SERVER_DATE_FORMAT, Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        String formattedDate="";
        try {
            date = df.parse(dateStr);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("LocalTime",formattedDate);
        return formattedDate;
    }

    public static String convertLocalTimeToUtc(String dateFormat){
        String stringDate = "";
        Date date=getDateFromStringDate(getCurrentDate(SERVER_DATE_FORMAT),dateFormat);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        stringDate = simpleDateFormat.format(date);
        Log.e("UTCTime",stringDate);
        return stringDate;

    }


    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat") DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d)+":00"; // "23:00"

        return x;
    }

    public static String convertTo12Hour(String Time) {

        if(Time.length()==5){
            Time=Time+":00";
        }
        DateFormat f1 = new SimpleDateFormat("hh:mm:ss"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("hh:mm a",Locale.US);
        String x = f2.format(d); // "23:00"

        return x;
    }


    public static Date parseDate(String date, SimpleDateFormat inputParser) {
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    public static String addMinToTime(String myTime,int minsToAdd){
        SimpleDateFormat df = new SimpleDateFormat("mm");
        Date d = null;
        try {
            d = df.parse(myTime);
            df = new SimpleDateFormat("HH:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, minsToAdd);
        return df.format(cal.getTime());
    }


    public static String minuteToTime(int minute) {
        int hour = minute / 60;
        minute %= 60;
//        if(minute==0){
//            return ""+(hour<10 ? "0" + hour : hour);
//        }
//        else {
            return (hour<10 ? "0" + hour : hour)+ ":" + (minute < 10 ? "0" + minute : minute);
       // }
    }

    public  static String getTimeStamp(){
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getTimeStamp2(String datetxt){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = (Date)formatter.parse(datetxt);
            return String.valueOf(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return getTimeStamp();
    }


    public static String getFormattedTime(String dateTxt,String inputFormat,String outputFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(outputFormat, Locale.ENGLISH);
        return sdf.format(getDateFromStringDate(dateTxt,inputFormat));

    }

    public static String formatLogDate(String dateTxt){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat2, Locale.ENGLISH);
        return sdf.format(getDateFromStringDate(dateTxt,SERVER_DATE_FORMAT));
    }



    public static String getFormattedTime3(String dateTxt){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        //  Log.e("date", "formatted string: "+sdf.format(DateUtils.getDateFromStringDate("2021-03-21T15:40:10+05:30","yyyy-MM-dd'T'HH:mm:ssZZZZZ")));
        return sdf.format(getDateFromStringDate(dateTxt,appTimeFormat));

    }

    public static String parseTime(String dateTxt) {
        //String date= String.valueOf(dateTxt+":00:00");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(dateTxt);
            return new SimpleDateFormat(appTimeFormat).format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime) throws ParseException {

        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
        if (initialTime.matches(reg) && finalTime.matches(reg) &&
                getCurrentHour().matches(reg)) {
            boolean valid = false;
            //Start Time
            Log.e("ctime",getCurrentHour());
            //all times are from java.util.Date
            Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);

            //Current Time
            Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(getCurrentHour());
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);

            //End Time
            Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }

            Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) ||
                    actualTime.compareTo(calendar1.getTime()) == 0) &&
                    actualTime.before(calendar2.getTime())) {
                valid = true;
                return valid;
            } else {
                return false;
            }
        }
        return false;
    }


    public static String getCurrentHour(){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return  currentTime;
    }
    public static Calendar parseDateString(String date) {
        SimpleDateFormat BIRTHDAY_FORMAT_PARSER = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        BIRTHDAY_FORMAT_PARSER.setLenient(false);
        try {
            calendar.setTime(BIRTHDAY_FORMAT_PARSER.parse(date));
        } catch (ParseException e) {}
        return calendar;
    }

    public static boolean isValidBirthday(String birthday) {
        Calendar calendar = parseDateString(birthday);
        int year = calendar.get(Calendar.YEAR);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        return year >= 1900 && year < thisYear;
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }


    public static String getDateWeekendMonth(Calendar calendar,int type,int from){
        if(type==0){
            if(from==0){
                calendar.add(Calendar.DATE, 1);
                return getdateFormat(appDateFormat).format(calendar.getTime());
            }
            else if(from==1){
                calendar.add(Calendar.DATE, -1);
                return getdateFormat(appDateFormat).format(calendar.getTime());
            }
            else if(from==2){

                return getdateFormat(appDateFormat).format(calendar.getTime());
            }
        }
        else if(type==1){
            if(from==0){
                return getNextWeek(calendar);
            }
            else if(from==1){
                return getLastWeek(calendar);
            }
            else if(from==2){
                return getCurrentWeek(calendar);

            }
        }
        else if(type==2){
            if(from==0){
                calendar.add(Calendar.MONTH, 1);
                return getdateFormat("MMMM,yyyy").format(calendar.getTime());
            }
            else if(from==1){
                calendar.add(Calendar.MONTH, -1);
                return getdateFormat("MMMM,yyyy").format(calendar.getTime());
            }
            else if(from==2){
                return getdateFormat("MMMM,yyyy").format(calendar.getTime());
            }

        }
        return "Not found";

    }


    public static String getLastThreeMonth(Calendar calendarStart, Calendar calendarEnd,int from){
        String month1="",month2="";
         if(from==1){
            month1=getdateFormat(appDateFormat).format(calendarStart.getTime());//aug
            month2=getdateFormat(appDateFormat).format(calendarEnd.getTime());//oct
        } else {
            month2=getdateFormat(appDateFormat).format(calendarEnd.getTime());//oct
            month1=getdateFormat(appDateFormat).format(calendarStart.getTime());//aug
        }
        //Log.e("Ved : ", calendarStart.getTime()+"/"+calendarEnd.getTime());
        return month1+"/"+month2;
    }

    public static String getLastWeek(Calendar mCalendar) {
        mCalendar.add(Calendar.DAY_OF_YEAR, -13);
        Date mDateMonday = mCalendar.getTime();
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date mDateSunday = mCalendar.getTime();

        String strDateFormat = appDateFormat;
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(mDateSunday);

        return MONDAY + "/" + SUNDAY;
    }

    public static String getCurrentWeek(Calendar mCalendar) {
        Date date = new Date();
        mCalendar.setTime(date);

        // 1 = Sunday, 2 = Monday, etc.
        int day_of_week = mCalendar.get(Calendar.DAY_OF_WEEK);

        int monday_offset;
        if (day_of_week == 1) {
            monday_offset = -6;
        } else
            monday_offset = (2 - day_of_week); // need to minus back
        mCalendar.add(Calendar.DAY_OF_YEAR, monday_offset);

        Date mDateMonday = mCalendar.getTime();

        // return 6 the next days of current day (object cal save current day)
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date mDateSunday = mCalendar.getTime();

        //Get format date
        String strDateFormat =appDateFormat;
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(mDateSunday);

        // Sub String
//        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
//            MONDAY = MONDAY.substring(0, 2);
//        }

        return MONDAY + "/" + SUNDAY;
    }

    public static String getNextWeek(Calendar mCalendar) {
        // Monday
        mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        Date mDateMonday = mCalendar.getTime();

        // Sunday
        mCalendar.add(Calendar.DAY_OF_YEAR, 6);
        Date Week_Sunday_Date = mCalendar.getTime();

        // Date format
        String strDateFormat = appDateFormat;
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

        String MONDAY = sdf.format(mDateMonday);
        String SUNDAY = sdf.format(Week_Sunday_Date);

        // Sub string
//        if ((MONDAY.substring(3, 6)).equals(SUNDAY.substring(3, 6))) {
//            MONDAY = MONDAY.substring(0, 2);
//        }

        return MONDAY + "/" + SUNDAY;
    }




    public static void showTimePicker(TextView textView, Context context) {
        final Calendar myCalendar = Calendar.getInstance();
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalendar.set(Calendar.MINUTE, minute);
                    Date date = null;
                    try {
                        date = DateUtils.getdateFormat("HH:mm").parse(hourOfDay + ":" + minute );
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    textView.setText(DateUtils.getdateFormat(appTimeFormat).format(date));

                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }


    public static LiveData<String> showDatePicker(TextView textView, Context context,String selectedDate,String dateFormat){
        final MutableLiveData<String> data = new MutableLiveData<>();
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        if(!TextUtils.isEmpty(selectedDate)){
            c.setTime(getDateFromStringDate(selectedDate,dateFormat));
        }
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                textView.setText(getdateFormat(appDateFormat).format(calendar.getTime()));
                data.setValue(getdateFormat(appDateFormat).format(calendar.getTime()));
            }
        }, mYear, mMonth, mDay);


        //datePickerDialog.getDatePicker().setMinDate(Objects.requireNonNull(getPreviousDate()).getTime());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
        return data;
    }



    public static String getTimeDuration(String timeFrom,String timeTo){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        Date date1=null,date2 = null;
        try {
            date1 = simpleDateFormat.parse(convertTo24Hour(timeFrom));
            date2 = simpleDateFormat.parse(convertTo24Hour(timeTo));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long difference = date2.getTime() - date1.getTime();
        if(difference<0)
        {
            Date dateMax = null;
            Date dateMin = null;
            try {
                dateMax = simpleDateFormat.parse("24:00:00");
                dateMin = simpleDateFormat.parse("00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            difference=(dateMax.getTime() -date1.getTime() )+(date2.getTime()-dateMin.getTime());
        }
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        int sec = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours) - (1000*60*min)) / (1000);
       // Log.e("log_tag","Hours: "+hours+", Mins: "+min+", Secs: "+sec);
        if(min>0){
            int secTotals=(hours*60*60)+(min*60);
            return String.valueOf(secTotals);
           // return (hours<10?"0"+hours:hours)+":"+(min<10?"0"+min:min);
        }
        else {
            return ""+(hours*60*60);
            //return (hours<10?"0"+hours:hours)+":00";
        }

    }

    public static String getTimeOfDay(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String time = "";

        if(timeOfDay >= 0 && timeOfDay < 12){
            time = "Good Morning, ";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            time = "Good Afternoon, ";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            time = "Good Evening, ";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            time = "Good Night, ";
        }
        return time;
    }

    public static String getDayFromDate(String dateTxt) {
        return getDate(parseDate(dateTxt,new SimpleDateFormat(appDateFormat,Locale.getDefault())),"EEEE").toLowerCase();
    }


    public static String dateFormatForTab1(String dateTxt){
       return getFormattedTime(dateTxt, appDateFormat, "dd MMMM");
    }

    public static String[] dateFormatForTab2(String dateTxt){
        String [] split=dateTxt.split("/");
        return split;
    }


    public static String monthStartEndDate(String dateTxt,int type){
        String [] split=dateTxt.split(",");
        int month=getMonthNumber(split[0]);
        int year= Integer.parseInt(split[1]);
        Calendar c = Calendar.getInstance();
        int day = 1;
        c.set(year, month, day);
        int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(type==0)
        return getdateFormat(appDateFormat).format(c.getTime());
        else
        c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
        return getdateFormat(appDateFormat).format(c.getTime());
    }

    public static String previousDate() {
        DateFormat dateFormat = new SimpleDateFormat(appDateFormat);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(getCurrentDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date previousDate = calendar.getTime();
           return dateFormat.format(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Date getPreviousDate() {
        DateFormat dateFormat = new SimpleDateFormat(appDateFormat);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(getCurrentDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            Date previousDate = calendar.getTime();
            return previousDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String nextDate() {
        DateFormat dateFormat = new SimpleDateFormat(appDateFormat);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(getCurrentDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date previousDate = calendar.getTime();
            return dateFormat.format(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLastDayOfTheMonth(String datetxt) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDateFromStringDate(datetxt,dateFormat1));

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();

        DateFormat sdf = new SimpleDateFormat(dateFormat1);
        System.out.println("Today            : " + sdf.format(getDateFromStringDate(datetxt,dateFormat1)));
        System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
        return sdf.format(lastDayOfMonth);

    }

    public static String addMinuteInCurrentTime(int addMinuteVal, String returnFormat) {
        SimpleDateFormat df = new SimpleDateFormat(returnFormat);
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.add(Calendar.MINUTE, addMinuteVal);
        return df.format(cal.getTime());
    }

    public static String addSecondInCurrentTime(int addSecondVal, String returnFormat) {
        SimpleDateFormat df = new SimpleDateFormat(returnFormat);
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.add(Calendar.SECOND, addSecondVal);
        return df.format(cal.getTime());
    }

    public static String convertSecIntoHourMin(String sec){
        if(TextUtils.isEmpty(sec)){
            return "0h";
        }

        int seconds= Math.round(Integer.parseInt(sec));
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        if(hours==0 && minutes>0){
            return String.valueOf(minutes+" min");
        }
        else if(hours!=0 && minutes>0){
            return String.valueOf(hours+"h "+minutes+" min");
        }
        else {
            return String.valueOf(hours+"h");
        }

    }

    public static String convertSecIntoHourMin2(String sec){
        if(TextUtils.isEmpty(sec)){
            return "0h";
        }

        int seconds= Math.round(Integer.parseInt(sec));
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        if(hours==0 && minutes>0){
            return String.valueOf("."+minutes);
        }
        else if(hours!=0 && minutes>0){
            return String.valueOf(hours+"h "+minutes+"m");
        }
        else {
            return String.valueOf(hours+"h");
        }

    }


    public static String getLastPreviousDate(int days){
        DateFormat format = new SimpleDateFormat(DateUtils.SERVER_DATE_FORMAT, Locale.US);
        Calendar cal = Calendar.getInstance();
//        try {
//            cal.setTime(format.parse(DateUtils.getCurrentDate()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        System.out.println("currentDate: " + format.format(cal.getTime())); // print current month
        cal.add(Calendar.DAY_OF_MONTH, days);
        System.out.println("first date: " + format.format(cal.getTime())); // print first date

        return format.format(cal.getTime());
    }


    public static boolean isFutureDate(String selectedDate){
        Calendar cal = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat(SERVER_DATE_FORMAT, Locale.US);
        Date date1=null,date2=null;
        try {
             date1=format.parse(getCurrentDate(SERVER_DATE_FORMAT));
             date2=format.parse(selectedDate);
             return date1.before(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCurrentMonthFirstLastDate(){
        String dates="";
        DateFormat format = new SimpleDateFormat(appDateFormat, Locale.US);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = 1;
        c.set(year, month, day);
        int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        dates=format.format(c.getTime());
        System.out.println("First Day of month: " + c.getTime());
        c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
        dates=dates+"/"+format.format(c.getTime());
        System.out.println("Last Day of month: " + c.getTime());

        return dates;
    }




    public static boolean isAfterToday(String time)
    {
        Date currentDate=DateUtils.getDateFromStringDate(getCurrentDate(DateUtils.SERVER_DATE_FORMAT),DateUtils.SERVER_DATE_FORMAT);
        Date checkDate=DateUtils.getDateFromStringDate(getCurrentDate()+" "+time,DateUtils.SERVER_DATE_FORMAT);
        return !checkDate.before(currentDate);
    }

    public static boolean checkValidSleepTime(String startTime,String endTime,String dateFormat){
        Date startDate=getDateFromStringDate(startTime,dateFormat);
        Date endDate=getDateFromStringDate(endTime,dateFormat);
        return endDate.after(startDate);
    }

    public static boolean checkCallExpiry(String startTime){
        String getTimeDif = "";
        try
        {

            Date past = getdateFormat(SERVER_DATE_FORMAT).parse(startTime);
            Date now = getDateFromStringDate(getCurrentDate(SERVER_DATE_FORMAT),SERVER_DATE_FORMAT);
            assert past != null;
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes= TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours= TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days= TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(minutes>30) {
              return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(SERVER_DATE_FORMAT);
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                //if(second==0)
                convTime = "Just now";
               // else convTime = second + " sec" + suffix;
            } else if (minute < 60) {
                convTime = minute + " min"+suffix;
            } else if (hour < 24) {
                convTime = hour + " hr"+suffix;
            }
            else if (day < 7) {
                if(day==1)
                convTime = day+" day"+suffix;
                else
                    convTime = day+" days"+suffix;
            }

            else if (day >= 7) {
//                if (day > 360) {
//                    convTime = (day / 360) + " Years " + suffix;
//                } else if (day > 30) {
//                    convTime = (day / 30) + " Months " + suffix;
//                } else {
//                    convTime = (day / 7) + " Week " + suffix;
//                }
             convTime=DateUtils.getPrettyDate(dataDate,DateUtils.SERVER_DATE_FORMAT,DateUtils.postDateFormat);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

    public static String convertTimeIntoHourAndMinute(double val) {
        return convertSecIntoHourMin(String.valueOf((int) val));
    }

}
