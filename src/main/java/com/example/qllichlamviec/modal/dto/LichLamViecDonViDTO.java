package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichLamViecDonViDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId donVi;

    private String tieuDe;
    private String noiDung;
    private LocalDateTime thoiGianBD;
    private LocalDateTime thoiGianKT;
    private LocalDateTime thoiGianTao;
    private String diaDiem;
    private String ghiChu;
}
