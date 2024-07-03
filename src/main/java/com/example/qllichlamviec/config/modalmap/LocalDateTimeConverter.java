package com.example.qllichlamviec.config.modalmap;

import org.modelmapper.AbstractConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeConverter extends AbstractConverter<Timestamp, LocalDateTime>  {
    @Override
    protected LocalDateTime convert(Timestamp source) {
        return source == null ? null : source.toLocalDateTime();
    }
}