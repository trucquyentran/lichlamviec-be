package com.example.qllichlamviec.modal.dto;

import com.example.qllichlamviec.util.DonVi;
import com.example.qllichlamviec.util.LichLamViec;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongKeDSLichByDateDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private LocalDate thoiGian;
    @DocumentReference(lazy = false)
    private DonViDTO donVi;
    private List<LichLamViec> lichLamViecList;
}
