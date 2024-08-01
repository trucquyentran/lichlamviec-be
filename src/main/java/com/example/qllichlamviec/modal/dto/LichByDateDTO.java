package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.LichLamViec;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichByDateDTO {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ObjectId id;
    private LocalDate thoiGian;
    private List<LichLamViec> lichLamViecList;
}
