package com.example.qllichlamviec.config.modalmap;

import org.modelmapper.AbstractConverter;

import java.time.LocalDate;
import java.sql.Date;

public class LocalDateConverter extends AbstractConverter<Date, LocalDate> {
    @Override
    protected LocalDate convert(Date source){
        return source.toLocalDate();
    }
}
