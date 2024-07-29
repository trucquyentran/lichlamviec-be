package com.example.qllichlamviec.util.pojo;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
public class FormatTime {
    public static LocalDateTime roundToMinute(LocalDateTime dateTime) {

        return dateTime.withSecond(0).withNano(0);
    }
    public static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("ICT"));
        Date parsedDate = inputFormat.parse(dateString);
        return parsedDate;
    }

    // Define the output date format
    private static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Method to format a Date object to the desired string format
    public static String formatDateFromDate(Date date) {
        return OUTPUT_FORMAT.format(date);
    }
    public static Date roundToMinuteFromDate(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
