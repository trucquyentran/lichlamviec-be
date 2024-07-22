package com.example.qllichlamviec.util.pojo;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
public class FormatTime {
    public static LocalDateTime roundToMinute(LocalDateTime dateTime) {
        return dateTime.withSecond(0).withNano(0);
    }
}
