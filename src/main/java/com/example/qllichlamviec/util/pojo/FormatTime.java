package com.example.qllichlamviec.util.pojo;

import java.time.LocalDateTime;
public class FormatTime {
    public static LocalDateTime roundToMinute(LocalDateTime dateTime) {
        return dateTime.withSecond(0).withNano(0);
    }
}
