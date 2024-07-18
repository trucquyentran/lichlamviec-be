package com.example.qllichlamviec.modal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichLamViecDTO {
    private LocalDateTime thoiGianBD;
    private  LocalDateTime thoiGianKT;
    private LocalDateTime thoiGianTao;
    private String diaDiem;
    private String ghiChu;
    private String noiDung;
    private String tieuDe;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId taiKhoan;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId donVi;
}
